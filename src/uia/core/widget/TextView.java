package uia.core.widget;

import uia.core.Context;
import uia.core.event.State;
import uia.core.utility.TextSuite;
import uia.core.View;
import uia.core.event.Key;
import uia.core.event.Mouse;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.shape.Rect;
import uia.structure.list.Buffer;
import uia.structure.list.BufferChar;
import uia.structure.list.BufferInt;
import uia.utils.Timer;
import uia.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Math.*;
import static uia.core.utility.TextSuite.*;

/**
 * Widget designed for text rendering and text handling.
 * Note that, by construction, text properties such as: longest line, justification and so on
 * are calculated <u>only</u> when text is drawn.
 */

// @Test
// @Bug see shape scaling and correlation with scroller
public class TextView extends View {
    protected TextCursor cursor;

    protected final BufferChar buffer;
    protected final BufferInt lines;

    private final TextSuite textSuite;

    private Scroller scroller;

    private Color selectedColor = new Color(80, 120, 255);

    /*
     * Cursor attributes
     */

    private int cLine = 1;
    private float cOffX;

    /*
     * Text attributes
     */

    private final BufferChar dBuffer;
    private final BufferInt dLines;

    private int textHeight = 0;
    private int longestLine = 0;
    private float textBound = 0.95f;
    private float lineFactor = 1f;

    private boolean selected = false;
    private boolean singleLine = false;

    protected int index;
    protected int hIndex;
    protected boolean justify = false;

    public TextView(Context context,
                    float px, float py,
                    float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setFigure(new Rect());
        super.setExpansion(0f, 0f);
        super.addEvent(textSelection());
        super.addEvent(keyShortcuts());

        buffer = new BufferChar(100);

        lines = new BufferInt(10);

        textSuite = new TextSuite(context, 384);

        scroller = new WheelScroller(true);

        dBuffer = new BufferChar(100);

        dLines = new BufferInt(1);
    }

    /**
     * Key shortcut implementation
     */

    private Key keyShortcuts() {
        return (v, s) -> {
            KeyEvent e = (KeyEvent) v.getKey().getNative();

            if (ctrlA(e)) {
                hIndex = 0;
                index = buffer.size();
            } else if (ctrlC(e) && isTextSelected()) {
                Context.copy(String.valueOf(Buffer.getData(buffer), getMinIndex(), getSelectionCount()));
            }
        };
    }

    /**
     * Text selection implementation
     */

    private Mouse textSelection() {
        return (v, s) -> {
            MotionEvent e = v.getMotionEvent();

            if (e.isMousePressed()) {
                int ax = -textSuite.getAlignX().getNumber();

                index = getIndex(
                        (int) (-ax * dx() / 2f),
                        (int) ((textSuite.isAlignY(AlignY.TOP) ? 0 : (dy() - textHeight) / 2) - scroller.getOffset()),
                        e.getX(), e.getY(), ax);

                if (!selected) {
                    selected = true;
                    hIndex = index;
                }

            } else if (e.isMouseReleased()) {
                selected = false;
            } else if (e.isMouseWheeling()) {
                scroller.wheelUpdate(e.getWheelRotation());
            }
        };
    }

    /**
     * Reset the selected text
     */

    public State resetSelected() {
        return (v, s) -> {
            if (s == State.FOCUS_LOST)
                index = hIndex = 0;
        };
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
     * Enable or disable single line mode
     *
     * @param enableSingleLine true to draw text only on one line
     */

    public void enableSingleLine(boolean enableSingleLine) {
        this.singleLine = enableSingleLine;
        justify = true;
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
     * Set the color for the selected text
     *
     * @param color a {@link Color} instance
     */

    public void setSelectedColor(Color color) {
        if (color != null)
            selectedColor = color;
    }

    /**
     * Set the text cursor color
     *
     * @param color a non-null color
     */

    public void setCursorColor(Color color) {
        if (color != null)
            cursor.setColor(color);
    }

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
            justify = true;
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
            justify = true;
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

        float lineLength = textSuite.getWidth(Buffer.getData(buffer), j, index - j);

        if (textSuite.isAlignX(AlignX.LEFT)) {
            cOffX = lineLength;
        } else if (textSuite.isAlignX(AlignX.CENTER)) {
            cOffX = lineLength - textSuite.getWidth(Buffer.getData(buffer), j, br - j) / 2;
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
     * @param canvas a non-null {@link Graphics2D}
     * @param color  a non-null {@link Color}
     * @param px     the position along x-axis
     * @param py     the position along y-axis
     * @param ax     an integer between {@code {0,1,2}} that represents the x-alignment
     */

    private void drawSelected(Graphics2D canvas, Color color, int px, int py, int ax) {
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


        float offset = textSuite.getWidth(Buffer.getData(buffer), j, minIndex - j);

        canvas.setColor(color);

        i -= 1;
        br = j;

        while (i < lines.size() && br < maxIndex) {
            j = br;
            br = lines.getIntAt(i);

            float lineWidth = textSuite.getWidth(Buffer.getData(buffer), j, br - j);
            int width = (int) textSuite.getWidth(Buffer.getData(buffer), minIndex, Math.min(maxIndex, br) - minIndex);

            canvas.fillRect((int) (px + offset - ax * lineWidth / 2f), py + i * lineHeight, width, lineHeight);

            offset = 0f;
            minIndex = br;
            i++;
        }
    }

    /**
     * Helper method used to render text
     *
     * @param canvas the graphics used to draw text
     * @param bChar  the buffer of chars to draw on screen
     * @param lines  the text break lines
     * @param px     the position along x-axis
     * @param py     the position along y-axis
     * @param ax     a factor used to move text along a-axis
     */

    private void drawText(Graphics2D canvas,
                          BufferChar bChar, BufferInt lines,
                          int px, int py, int ax) {
        longestLine = 0;

        float top = py() - dy() / 2f;
        float bot = py() + dy() / 2f;
        int lineHeight = getLineHeight();
        int off = (int) (textSuite.getFontSize() / 3f);

        int i = 0;
        int j;
        int br = 0;

        char[] data = Buffer.getData(bChar);

        while (i < lines.size()) {
            j = br;
            br = lines.getIntAt(i);

            int lineLength = (int) textSuite.getWidth(data, j, br - j);

            if (py + (i + 2) * lineHeight >= top && py + (i - 1) * lineHeight <= bot)
                canvas.drawChars(data, j, br - j, px - ax * lineLength / 2, py + (int) ((i + 0.5f) * lineHeight + off));

            if (lineLength > longestLine)
                longestLine = lineLength;

            i++;
        }
    }

    /**
     * Complexity: T(n) = Theta(n).
     * Note that works either for multi-line and single-line.
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
                float xLength = 0.5f * xOff * textSuite.getWidth(Buffer.getData(buffer), j, br - j);

                // There is a problem here. Error index calculation
                while (j < br && !Utils.AABB(mx, my, 1, 1, px + xLength + length
                        + (d = textSuite.getWidth(buffer.getCharAt(j))) / 2, py + height - fSize / 2f, d, fSize)) {
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
    protected void postDraw(Graphics2D canvas) {
        // Update scroller
        if (isMouseOver())
            scroller.update();

        // Justify text when necessary
        if (justify
                || Float.compare(dx(), pdx) != 0
                || Float.compare(dy(), pdy) != 0) {
            justify = false;

            if (singleLine) {
                lines.clear();
                lines.add(buffer.size());
            } else {
                justify(buffer, lines, textSuite, textBound * dx());
            }
        }

        // update text height
        textHeight = getLineHeight() * max(1, lines.size());

        // update cursor params
        if (oIndex != index) {
            oIndex = index;
            updateCursorPosParams(index);
        }


        pdx = dx();
        pdy = dy();

        int ax = textSuite.getAlignX().getNumber();
        int px = (int) (px() + (ax - 1) * dx() / 2f);
        int py = (int) (py() - (textSuite.isAlignY(AlignY.TOP) ? dy() : textHeight) / 2f - scroller.getOffset());

        // If shape exists & clipRegion = true -> clip this region
        Shape oldClip = canvas.getClip();
        canvas.clip(getFigure());

        // highlight when necessary the selected text
        if (isTextSelected())
            drawSelected(canvas, selectedColor, px, py, ax);

        // set text font and color
        canvas.setFont(textSuite.getFont());
        canvas.setColor(textSuite.getColor());

        // draw text
        drawText(canvas,
                buffer.size() > 0 ? buffer : dBuffer,
                buffer.size() > 0 ? lines : dLines,
                px, py, ax);

        // draw cursor
        if (cursor != null && isFocused()) {
            int lineHeight = getLineHeight();
            cursor.setPos(px + cOffX, py + (cLine - 0.5f) * lineHeight);
            cursor.setDim(2, lineHeight / lineFactor);
            cursor.draw(canvas);
        }

        canvas.setClip(oldClip);

        // Set scroller max length
        scroller.setMax(textHeight - 0.98f * dy());
        scroller.setFactor(1f / getLineHeight());
    }

    /**
     * @return the height of a text line
     */

    private int getLineHeight() {
        return (int) (lineFactor * 1.33f * textSuite.getFontSize());
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

    /**
     * @return the number of selected chars
     */

    public int getSelectionCount() {
        return abs(index - hIndex);
    }

    /**
     * @return true if the text is partially or completed selected
     */

    public boolean isTextSelected() {
        return getSelectionCount() != 0;
    }

    /**
     * @return the color used to text selection
     */

    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * @return the amount of break lines
     */

    public int getLines() {
        return lines.size();
    }

    /**
     * @return the text height in pixel
     */

    public int getTextHeight() {
        return textHeight;
    }

    /**
     * @return the longest line in pixel
     */

    public int getLongestLine() {
        return longestLine;
    }

    /**
     * @return the amount of chars including break lines and white spaces
     */

    public int getCharCount() {
        return buffer.size();
    }

    /**
     * @param off the position of the first char
     * @param len the amount of chars to measure
     * @return the text length
     */

    public float getWidth(int off, int len) {
        return textSuite.getWidth(Buffer.getData(buffer), off, len);
    }

    /**
     * @return true if this view has single line mode enabled
     */

    public final boolean isSingleLineMode() {
        return singleLine;
    }

    /**
     * @return the scroller associated to this view
     */

    public final Scroller getScroller() {
        return scroller;
    }

    /**
     * @return the text suite associated to this view
     */

    public final TextSuite getTextSuite() {
        return textSuite;
    }

    /*
     *
     * Text cursor
     *
     */

    /**
     * Widget used to draw a text cursor
     */

    public static class TextCursor extends View {
        private final Timer time;

        public TextCursor(Context context,
                          float px, float py,
                          float dx, float dy) {
            super(context, px, py, dx, dy);
            super.setColor(Color.RED);

            time = new Timer();
        }

        public void resetTimer() {
            time.reset();
        }

        @Override
        public void updateState() {
            super.setVisible(!time.isOver(0.5f));

            if (time.isOver(1f))
                time.reset();
        }
    }
}
