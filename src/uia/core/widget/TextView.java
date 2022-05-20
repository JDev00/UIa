package uia.core.widget;

import uia.core.event.Keyboard;
import uia.core.policy.*;
import uia.core.event.State;
import uia.core.View;
import uia.core.event.Mouse;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.geometry.Rect;
import uia.core.event.Event;
import uia.structure.list.Buffer;
import uia.structure.list.BufferChar;
import uia.structure.list.BufferFloat;
import uia.structure.list.BufferInt;
import uia.utils.Utils;

import static java.lang.Math.*;

/**
 * Widget designed for text rendering and text handling.
 * <br>
 * <b>Note that text properties such as: longest line, justification and so on
 * are calculated in render time.</b>
 * <br>
 * Additional features can be easily added with custom {@link Event} implementations.
 * Some of them have been already implemented (by default are not attached to the TextView):
 * <lu>
 * <li>{@link TextView#funShortcut()}</li>
 * <li>{@link TextView#funTxtSelection()} ()}</li>
 * <li>{@link TextView#funTxtSelectedReset()} ()}</li>
 * </lu>
 *
 * @see TextView#createFriendly(Context, float, float, float, float)
 */

// @Bug see scaling and correlation with scroller
public class TextView extends View {

    /**
     * AlignX object is used to align text along x-axis
     */
    public enum AlignX {LEFT, CENTER, RIGHT}

    /**
     * AlignY object is used to align text along y-axis
     */
    public enum AlignY {TOP, CENTER}

    /*
     * Cursor attributes
     */

    private int cLine = 1;
    private float cOffX;

    protected TextCursor cursor;

    /*
     * Text attributes
     */

    protected int index;
    protected int hIndex;

    private int textHeight = 0;
    private int longestLine = 0;
    private float textBound = 0.95f;
    private float lineFactor = 1f;

    private boolean selected = false;
    private boolean singleLine = false;
    private boolean autoJustification = true;
    private boolean requestJustification = false;

    private int measureSize;

    private final BufferFloat measures;

    private Font font;

    private Paint paintText;

    private Paint paintSelected;

    private AlignX alignX = AlignX.CENTER;

    private AlignY alignY = AlignY.TOP;

    private Scroller scroller;

    /*
     * Text buffers
     */

    private final BufferChar dBuffer;
    private final BufferInt dLines;
    protected final BufferChar buffer;
    protected final BufferInt lines;

    public TextView(Context context,
                    float px, float py,
                    float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setFigure(new Rect());
        super.setExpansion(0f, 0f);
        super.addEvent((Mouse) (v, s) -> {
            MotionEvent e = v.getMotionEvent();
            if (e.isMouseWheeling())
                scroller.wheelUpdate(e.getWheelRotation());
        });

        paintText = context.createColor(null, Context.COLOR.BLACK);

        paintSelected = context.createColor(null, 80, 120, 255);

        buffer = new BufferChar(100);

        lines = new BufferInt(10);

        scroller = new WheelScroller(true);

        dBuffer = new BufferChar(100);

        dLines = new BufferInt(1);

        measureSize = 384;

        measures = new BufferFloat(measureSize);

        setFont(context.createFont(null, "Arial", Font.STYLE.PLAIN, 24));
    }

    /**
     * Function used to automatically detect common key shortcuts and act to them
     */

    public Keyboard funShortcut() {
        return (v, s) -> {
            Key e = v.getKey();

            if (s == Keyboard.PRESSED) {

                if (e.codeTextSelectionAll()) {
                    hIndex = 0;
                    index = buffer.size();
                } else if (e.codeTextCopy() && isTextSelected()) {
                    getContext().copyIntoClipboard(String.valueOf(Buffer.getData(buffer), getMinIndex(), getSelectionCount()));
                } else if (e.codeTextSelectionLeft()) {
                    index = max(index - 1, 0);
                } else if (e.codeTextSelectionRight()) {
                    index = min(index + 1, buffer.size());
                }
            }
        };
    }

    /**
     * Function used to automatically select text with cursor
     */

    public Mouse funTxtSelection() {
        return (v, s) -> {
            MotionEvent e = v.getMotionEvent();

            if (e.isMousePressed()) {
                int ax = -map(alignX);

                index = getIndex(
                        (int) (-ax * dx() / 2f),
                        (int) ((isAlignY(AlignY.TOP) ? 0 : (dy() - textHeight) / 2) - scroller.getOffset()),
                        e.getX(), e.getY(), ax);

                if (!selected) {
                    selected = true;
                    hIndex = index;
                }

            } else if (e.isMouseReleased()) {
                selected = false;
            }
        };
    }

    /**
     * Function used to automatically reset text selection when focus is lost
     */

    public State funTxtSelectedReset() {
        return (v, s) -> {
            if (s == State.FOCUS_LOST)
                index = hIndex = 0;
        };
    }

    /**
     * Map into a number the given AlignX object
     *
     * @param alignX a not null {@link AlignX} object
     */

    private int map(AlignX alignX) {
        switch (alignX) {
            case LEFT:
                return 0;
            case CENTER:
                return 1;
            case RIGHT:
                return 2;
            default:
                return -1;
        }
    }

    /*
     *
     * Font
     *
     */

    /**
     * Set the text font
     *
     * @param f a not null {@link Font}
     */

    public void setFont(Font f) {
        if (f != null) {
            font = f;
            updateMeasure();
        }
    }

    /**
     * @return the text font
     */

    public Font getFont() {
        return font;
    }

    /**
     * Set the font size
     *
     * @param size a value {@code > 0}
     */

    public void setFontSize(float size) {
        if (size > 0) {
            font.setSize(size);
            updateMeasure();
        }
    }

    /**
     * Update the measure buffer
     */

    private void updateMeasure() {
        measures.clear();

        for (int i = 0; i < measureSize; i++) {
            measures.add(measures.size(), font.getWidth((char) i));
        }
    }

    /**
     * Resize the measure buffer
     *
     * @param size a value {@code >= 0}
     */

    public void resizeMeasureBuffer(int size) {
        measureSize = Math.max(size, 0);
        updateMeasure();
    }

    /**
     * @return the amount of buffered chars
     */

    public int measureSize() {
        return measureSize;
    }

    /**
     * @return the font size
     */

    public float getFontSize() {
        return font.getSize();
    }

    /**
     * @return the width of the given char
     */

    public float getWidth(char c) {
        return measures.getFloatAt(c);
    }

    /**
     * @return the width of the given array of chars
     */

    public float getWidth(char[] in, int off, int len) {
        float tot = 0;

        for (int i = 0; i < len; i++) {
            tot += measures.getFloatAt(in[off + i]);
        }

        return tot;
    }

    /**
     * @return the width of the given array of bytes
     */

    public float getWidth(byte[] in, int off, int len) {
        float tot = 0;

        for (int i = 0; i < len; i++) {
            tot += measures.getFloatAt((char) in[off + i]);
        }

        return tot;
    }

    /**
     * @return the width of the given string
     */

    public float getWidth(String in) {
        return getWidth(in.toCharArray(), 0, in.length());
    }

    /**
     * @param off the position of the first char
     * @param len the amount of chars to measure
     * @return the text length
     */

    public float getWidth(int off, int len) {
        return getWidth(Buffer.getData(buffer), off, len);
    }

    /*
     *
     * Text options
     *
     */

    /**
     * Set the text Paint
     *
     * @param paint a not null Paint object
     */

    public void setPaintText(Paint paint) {
        if (paint != null)
            paintText = paint;
    }

    /**
     * @return the Paint object
     */

    public Paint getPaintText() {
        return paintText;
    }

    /**
     * Set the text x-alignment
     *
     * @param alignX a not null {@link AlignX} instance
     */

    public void setAlignX(AlignX alignX) {
        if (alignX != null)
            this.alignX = alignX;
    }

    /**
     * Set the text y-alignment
     *
     * @param alignY a not null {@link AlignY} instance
     */

    public void setAlignY(AlignY alignY) {
        if (alignY != null)
            this.alignY = alignY;
    }

    /**
     * @return true if the given alignX is equal to this x-alignment
     */

    public boolean isAlignX(AlignX alignX) {
        return this.alignX.equals(alignX);
    }

    /**
     * @return true if the given alignY is equal to this y-alignment
     */

    public boolean isAlignY(AlignY alignY) {
        return this.alignY.equals(alignY);
    }

    /**
     * @return the text alignment along x-axis
     */

    public AlignX getAlignX() {
        return alignX;
    }

    /**
     * @return the text alignment along y-axis
     */

    public AlignY getAlignY() {
        return alignY;
    }

    /**
     * Enable or disable single line mode
     *
     * @param enableSingleLine true to draw text only on one line
     */

    public void enableSingleLine(boolean enableSingleLine) {
        this.singleLine = enableSingleLine;
        requestJustification = true;
    }

    /**
     * @return true if this view has single line mode enabled
     */

    public final boolean isSingleLineMode() {
        return singleLine;
    }

    /**
     * Enable or disable the auto-justification.
     * <br>
     * <b>Auto-justification functionality automatically justifies this text.</b>
     */

    public void enableAutoJustification(boolean autoJustification) {
        this.autoJustification = autoJustification;
    }

    /**
     * Set the maximum amount of chars for this view
     *
     * @param chars a value {@code >= 0}
     */

    public void setCharsLimit(int chars) {
        buffer.setLimit(chars);
    }

    /**
     * Set the text bound
     *
     * @param textBound a value between [0f, {@link Float#MAX_VALUE}]
     * @apiNote if value is out of range it will be automatically resized
     */

    public void setTextBound(float textBound) {
        this.textBound = Math.max(0f, textBound);
    }

    /**
     * Increase or decrease the size of every text line
     *
     * @param lineFactor a value {@code >= 1} used to scale the line
     */

    public void setLineFactor(float lineFactor) {
        this.lineFactor = max(1, lineFactor);
    }

    /**
     * Set the Paint for the selected text
     *
     * @param paint a not null Paint object
     */

    public void setPaintSelected(Paint paint) {
        if (paint != null)
            paintSelected = paint;
    }

    /**
     * @return the Paint used to color the selected text
     */

    public final Paint getPaintSelected() {
        return paintSelected;
    }

    /**
     * Set the cursor Paint object
     *
     * @param paint a not null Paint object
     */

    public void setCursorPaint(Paint paint) {
        if (paint != null)
            cursor.setPaint(paint);
    }

    /**
     * Set a new scroller to this view
     *
     * @param scroller a non-null scroller instance
     */

    public void setScroller(Scroller scroller) {
        if (scroller != null)
            this.scroller = scroller;
    }

    /**
     * @return the scroller associated to this view
     */

    public final Scroller getScroller() {
        return scroller;
    }

    /**
     * @return the height of a text line
     */

    public final int getLineHeight() {
        return (int) (lineFactor * 1.33f * getFontSize());
    }

    /**
     * @return the number of selected chars
     */

    public final int getSelectionCount() {
        return abs(index - hIndex);
    }

    /**
     * @return true if the text is partially or completed selected
     */

    public final boolean isTextSelected() {
        return getSelectionCount() != 0;
    }

    /**
     * @return the amount of break lines
     */

    public final int getLines() {
        return lines.size();
    }

    /**
     * @return the text height in pixel
     */

    public final int getTextHeight() {
        return textHeight;
    }

    /**
     * @return the longest line in pixel
     */

    public final int getLongestLine() {
        return longestLine;
    }

    /**
     * @return the amount of chars including break lines and white spaces
     */

    public final int getCharCount() {
        return buffer.size();
    }

    /*
     *
     * Text setting
     *
     */

    /**
     * Set a single-line string to display when no text is available
     *
     * @param str a string; it could be null
     */

    public void setDescription(String str) {
        dBuffer.clear();
        dLines.clear();

        if (str != null) {
            dBuffer.add(0, str.toCharArray(), 0, str.length());
            dLines.add(str.length());
        }
    }

    /**
     * Manually request to this view to justify text
     */

    public void requestJustification() {
        requestJustification = true;
    }

    /**
     * Clear text and all its attributes
     */

    public void clear() {
        buffer.clear();

        lines.clear();

        textHeight = 0;
        longestLine = 0;
        index = hIndex = 0;
    }

    /**
     * Set a new text
     *
     * @param in a char array
     */

    public void setText(char[] in) {
        clear();

        if (in != null) {
            buffer.add(0, in, 0, in.length);
            requestJustification = true;
        }
    }

    /**
     * Set a new text
     *
     * @param in a string
     */

    public void setText(String in) {
        setText((in == null) ? null : in.toCharArray());
    }

    /**
     * Add a new piece of text
     *
     * @param i  the index of the char in which insert the new text
     * @param in a char array to draw
     */

    public void addText(int i, char[] in) {
        if (in != null) {
            buffer.add(i, in, 0, in.length);
            requestJustification = true;
        }
    }

    /**
     * Add, at the end of the text, a new piece of text
     *
     * @param in a char array
     */

    public void addText(char[] in) {
        addText(buffer.size(), in);
    }

    /**
     * Add a new piece of text
     *
     * @param i  the index of the char in which insert the new text
     * @param in a string
     */

    public void addText(int i, String in) {
        if (in != null)
            addText(i, in.toCharArray());
    }

    /**
     * Add, at the end of the text, a new piece of text
     *
     * @param in a string
     */

    public void addText(String in) {
        if (in != null)
            addText(in.toCharArray());
    }

    /**
     * @return the text handled by this view
     */

    public String getText() {
        return buffer.toString();
    }

    /**
     * @return the description text
     */

    public String getDescription() {
        return dBuffer.toString();
    }

    /*
     *
     * Internal
     *
     */

    /**
     * Update the cursor position params.
     * Note that the cursor will be located left-to the specified char
     *
     * @param index the char position
     */

    private void updateCursorPosParams(int index) {
        int i = 0;
        int j;
        int br = 0;

        do {
            j = br;
            br = lines.getIntAt(i);
            i++;
        } while (i < lines.size() && br < index);

        cLine = i;

        float lineLength = getWidth(Buffer.getData(buffer), j, index - j);

        if (isAlignX(AlignX.LEFT)) {
            cOffX = lineLength;
        } else if (isAlignX(AlignX.CENTER)) {
            cOffX = lineLength - getWidth(Buffer.getData(buffer), j, br - j) / 2;
        }
    }

    /**
     * @return the min index
     */

    protected int getMinIndex() {
        return min(index, hIndex);
    }

    /**
     * @return the max index
     */

    protected int getMaxIndex() {
        return max(index, hIndex);
    }

    /**
     * Draw the box that highlights the text
     *
     * @param render a not null {@link Render} object
     * @param paint  a not null {@link Paint} object
     * @param px     the position along x-axis
     * @param py     the position along y-axis
     * @param ax     an integer between {@code {0,1,2}} that represents the x-alignment
     */

    private void drawSelected(Render render, Paint paint, int px, int py, int ax) {
        int minIndex = getMinIndex();
        int maxIndex = getMaxIndex();
        int lineHeight = getLineHeight();

        int i = 0;
        int j = 0;
        int br = 0;

        // calculate the offset of the first box
        while (i < lines.size() && br <= minIndex) {
            j = br;
            br = lines.getIntAt(i);
            i++;
        }


        float offset = getWidth(Buffer.getData(buffer), j, minIndex - j);

        render.setPaint(paint);

        i -= 1;
        br = j;

        while (i < lines.size() && br < maxIndex) {
            j = br;
            br = lines.getIntAt(i);

            float lineWidth = getWidth(Buffer.getData(buffer), j, br - j);
            int width = (int) getWidth(Buffer.getData(buffer), minIndex, Math.min(maxIndex, br) - minIndex);

            render.drawRect((int) (px + offset - ax * lineWidth / 2f), py + i * lineHeight, width, lineHeight);

            offset = 0f;
            minIndex = br;
            i++;
        }
    }

    /**
     * Helper method used to render text
     *
     * @param render the {@link Render} used to draw text
     * @param bChar  the buffer of chars to draw on screen
     * @param lines  the text break lines
     * @param px     the position along x-axis
     * @param py     the position along y-axis
     * @param ax     a factor used to move text along a-axis
     */

    private void drawText(Render render,
                          BufferChar bChar, BufferInt lines,
                          int px, int py, int ax) {
        longestLine = 0;

        float top = py() - dy() / 2f;
        float bot = py() + dy() / 2f;
        int lineHeight = getLineHeight();
        int off = (int) (getFontSize() / 3f);

        int i = 0;
        int j;
        int br = 0;

        char[] data = Buffer.getData(bChar);

        while (i < lines.size()) {
            j = br;
            br = lines.getIntAt(i);

            int lineLength = (int) getWidth(data, j, br - j);

            if (py + (i + 2) * lineHeight >= top && py + (i - 1) * lineHeight <= bot)
                render.drawText(data, j, br - j, px - ax * lineLength / 2, py + (int) ((i + 0.5f) * lineHeight + off));

            if (lineLength > longestLine)
                longestLine = lineLength;

            i++;
        }
    }

    /**
     * Complexity: T(n) = Theta(n).
     * <br>
     * <b>Note that works either for multi-line and single-line.</b>
     *
     * @return the index of the char currently user-covered
     */

    private int getIndex(int px, int py,
                         int mx, int my, int xOff) {
        float height = 0;
        int fSize = getLineHeight();

        int i = 0;
        int j;
        int br = 0;

        while (i < lines.size()) {
            j = br;
            br = lines.getIntAt(i);

            height += fSize;

            if (Utils.AABB(mx, my, 1, 1, dx() / 2, py + height - fSize / 2f, dx(), fSize)) {
                float d;
                float length = 0;
                float xLength = 0.5f * xOff * getWidth(Buffer.getData(buffer), j, br - j);

                // There is a problem here. Error index calculation
                while (j < br && !Utils.AABB(mx, my, 1, 1, px + xLength + length
                        + (d = getWidth(buffer.getCharAt(j))) / 2, py + height - fSize / 2f, d, fSize)) {
                    length += d;
                    j++;
                }

                return j;
            }

            i++;
        }

        return 0;
    }

    private int oIndex;
    private float pdx;
    private float pdy;

    @Override
    protected void postDraw(Render render) {
        // Update scroller
        if (arePointersOver())
            scroller.update();

        // Justify text when necessary
        if (requestJustification
                || Float.compare(dx(), pdx) != 0
                || Float.compare(dy(), pdy) != 0) {
            requestJustification = false;

            if (singleLine) {
                lines.clear();
                lines.add(buffer.size());
            } else {
                justify(buffer, lines, measures, autoJustification ? textBound * dx() : Float.MAX_VALUE);
            }
        }

        // Update text height
        textHeight = getLineHeight() * max(1, lines.size());

        // Update cursor params
        if (oIndex != index) {
            oIndex = index;
            updateCursorPosParams(index);
        }


        pdx = dx();
        pdy = dy();

        int ax = map(alignX);
        int px = (int) (px() + (ax - 1) * dx() / 2f);
        int py = (int) (py() - (isAlignY(AlignY.TOP) ? dy() : textHeight) / 2f - scroller.getOffset());

        // Clip this region
        Path oPath = render.getClip();
        render.clip(getPath());

        // Highlight, when necessary, the selected text
        if (isTextSelected())
            drawSelected(render, paintSelected, px, py, ax);

        // Set Font and Paint
        render.setFont(font);
        render.setPaint(paintText);

        // Draw text
        drawText(render,
                buffer.size() > 0 ? buffer : dBuffer,
                buffer.size() > 0 ? lines : dLines,
                px, py, ax);

        // Draw cursor
        if (cursor != null && isFocused()) {
            int lineHeight = getLineHeight();
            cursor.setPos(px + cOffX, py + (cLine - 0.5f) * lineHeight);
            cursor.setDim(2, lineHeight / lineFactor);
            cursor.draw(render);
        }

        render.setClip(oPath);

        // Set scroller max length
        scroller.setMax(textHeight - 0.98f * dy());
        scroller.setFactor(1f / getLineHeight());
    }

    /**
     * Compute the position of the break lines and store them inside a given BufferInt.
     * <br>
     * Time required for computation: T(n) = Theta(n).
     *
     * @param buffer   a not null {@link BufferChar} object
     * @param lines    a not null {@link BufferInt} object used to store break lines indices
     * @param measures a not null {@link BufferFloat} object already filled with char measures expressed in pixels
     * @param width    a value {@code > 0} used to set the maximum line's width in pixel
     */

    public static void justify(BufferChar buffer, BufferInt lines,
                               BufferFloat measures, float width) {
        if (width >= 0
                && buffer != null
                && lines != null
                && measures != null) {
            char test = '\r';
            int gap = 0;
            float lineLength = 0;
            float subLength = 0;

            // remove the algorithm generated break lines. T(n) = Theta(n)
            buffer.remove(test);

            // clear lines
            lines.clear();

            // T(n) = Theta(n)
            for (int i = 0; i < buffer.size(); i++) {
                char t = buffer.getCharAt(i);
                float w = measures.getFloatAt(t);

                lineLength += w;
                subLength += w;

                if (t == '\n') {
                    lineLength = subLength = 0;
                } else if (lineLength > width) {

                    if (subLength < width) {
                        lines.add(gap + 1);
                        lineLength = subLength;
                    } else {
                        lines.add(i);
                        lineLength = subLength = w;
                    }
                }

                if (t == ' ') {
                    gap = i;
                    subLength = 0;
                }
            }

            // add to the buffer the algorithm generated lines. T(n) = Theta(n)
            buffer.add(Buffer.getData(lines), 0, lines.size(), test);

            lines.clear();

            // T(n) = Theta(n)
            for (int i = 0; i < buffer.size(); i++) {
                char t = buffer.getCharAt(i);

                if (t == '\n' || t == '\r')
                    lines.add(i);
            }

            if (buffer.size() > 0)
                lines.add(buffer.size());
        }
    }

    /**
     * Return the longest line of the given String.
     * <br>
     * Time required: T(n) = Theta(n).
     *
     * @param v     a not null {@link TextView}
     * @param input a not null String to measure
     * @return the longest line in pixels according to the given TextView Font metrics
     */

    public static float longestLine(TextView v, String input) {
        float longest = 0f;

        if (input != null && v != null) {
            int i = 0;
            char[] data = input.toCharArray();

            // First control
            for (int j = 0; j < data.length; j++) {

                if (data[j] == '\n') {
                    float pixelLength = v.getWidth(data, i, j - i);

                    i = j;

                    if (pixelLength > longest)
                        longest = pixelLength;
                }
            }

            // Last control
            float pixelLength = v.getWidth(data, i, data.length - i);

            if (pixelLength > longest)
                longest = pixelLength;
        }

        return longest;
    }

    /**
     * Create a new TextView with standard functions already attached
     *
     * @see TextView#funShortcut()
     * @see TextView#funTxtSelection()
     */

    public static TextView createFriendly(Context context,
                                          float px, float py,
                                          float dx, float dy) {
        TextView out = new TextView(context, px, py, dx, dy);
        out.addEvent(out.funShortcut());
        out.addEvent(out.funTxtSelection());
        return out;
    }
}
