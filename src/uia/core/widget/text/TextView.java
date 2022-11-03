package uia.core.widget.text;

import uia.core.View;
import uia.core.event.Event;
import uia.core.gesture.Scroller;
import uia.core.gesture.WheelScroller;
import uia.core.platform.independent.Font;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Key;
import uia.core.platform.policy.Graphic;
import uia.structure.BChar;
import uia.utils.Utils;

import static java.lang.Math.*;

/**
 * Widget designed for text rendering.
 * <br>
 * <b>Text properties</b> are calculated at rendering time.
 */

public class TextView extends View {

    /**
     * Align text along x-axis.
     */
    public enum AlignX {
        LEFT, CENTER, RIGHT;

        /**
         * @return a number between {0,1,2}: LEFT = 0, CENTER = 1 and RIGHT = 2
         */

        public int map() {
            switch (this) {
                case LEFT:
                    return 0;
                case CENTER:
                    return 1;
                default:
                    return 2;
            }
        }
    }

    /**
     * Align text along y-axis
     */
    public enum AlignY {TOP, CENTER}

    /* Scroller */

    private Scroller scrollerX;
    private Scroller scrollerY;

    /* Text attributes */

    private int index;
    private int hIndex;
    private int lines;

    private float cursorX;
    private float cursorY;
    private float textHeight = 0f;
    private float longestLine = 0f;
    private float lineFactor = 1f;

    private boolean singleLine = false;

    private String description;
    private BChar buffer;

    private AlignX alignX = AlignX.CENTER;
    private AlignY alignY = AlignY.TOP;

    private Font font;
    private Shape highlight;
    private Paint paintText;
    private Paint paintSelected;

    public TextView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setExpansion(0f, 0f);

        final boolean[] selected = {false};
        getEventQueue()
                .addEvent((v, s) -> {
                    switch (s) {

                        case Event.POINTER_HOVER:
                            PointerEvent e = v.getPointerEvent();

                            if (e.isMousePressed()) {
                                int ind = getIndex(buffer.toArray(), buffer.size(), e.getX(), e.getY());
                                if (ind != -1) {
                                    index = ind;

                                    if (!selected[0]) {
                                        selected[0] = true;
                                        hIndex = index;
                                    }
                                }
                            } else if (e.isMouseReleased()) {
                                selected[0] = false;
                            }

                            // update scroller
                            if (singleLine) {
                                scrollerX.update(v.getPointerEvent());
                            } else {
                                scrollerY.update(v.getPointerEvent());
                            }
                            break;

                        case Event.KEY_PRESSED:
                            Key key = v.getKey();

                            if (key.codeTextSelectionAll()) {
                                hIndex = 0;
                                index = buffer.size();
                            } else if (key.codeTextCopy() && isTextSelected()) {
                                getContext().copyIntoClipboard(String.valueOf(buffer.toArray(), getMinIndex(), getSelectionCount()));
                            } else if (key.codeTextSelectionLeft()) {
                                index = max(0, index - 1);
                            } else if (key.codeTextSelectionRight()) {
                                index = min(index + 1, buffer.size());
                            } else if (Key.code(key, 1, 38)) {// SHIFT MASK + KEY CODE ARROW UP
                                int ind = getIndex(-1);
                                if (ind >= 0) index = ind;
                            } else if (Key.code(key, 1, 40)) {// SHIFT MASK + KEY CODE ARROW DOWN
                                int ind = getIndex(1);
                                if (ind >= 0) index = ind;
                            }
                            break;
                    }
                });

        highlight = Figure.rect(null);

        paintText = new Paint(0, 0, 0);

        paintSelected = new Paint(80, 120, 255);

        buffer = new BChar(100);

        scrollerX = new WheelScroller();
        scrollerY = new WheelScroller();

        font = new Font("Arial", Font.STYLE.PLAIN, 20f);

        setFont(font);
    }

    /**
     * Function used to automatically reset text selection when focus is lost
     */

    public Event<View> funSelectionReset() {
        return (v, s) -> {
            if (s == Event.FOCUS_LOST) index = hIndex = 0;
        };
    }

    /**
     * Set the text font
     *
     * @param f a not null {@link Font}
     */

    public void setFont(Font f) {
        if (f != null) font = f;
    }

    /**
     * Set the text Paint
     *
     * @param paint a not null Paint object
     */

    public void setPaintText(Paint paint) {
        if (paint != null) paintText = paint;
    }

    /**
     * Set the shape used to highlight text
     *
     * @param shape a not null {@link Shape}
     */

    public void setHighlightShape(Shape shape) {
        if (shape != null) highlight = shape;
    }

    /**
     * Set the text x-alignment
     *
     * @param alignX a not null {@link AlignX} instance
     */

    public void setAlignX(AlignX alignX) {
        if (alignX != null) this.alignX = alignX;
    }

    /**
     * Set the text y-alignment
     *
     * @param alignY a not null {@link AlignY} instance
     */

    public void setAlignY(AlignY alignY) {
        if (alignY != null) this.alignY = alignY;
    }

    /**
     * Enable or disable single line mode
     *
     * @param singleLine true to draw text on a single line
     */

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
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
        if (paint != null) paintSelected = paint;
    }

    /**
     * Set a new Scroller used to scroll text along x-axis
     *
     * @param scroller a not null {@link Scroller}
     */

    public void setScrollerX(Scroller scroller) {
        if (scroller != null) scrollerX = scroller;
    }

    /**
     * Set a new Scroller used to scroll text along y-axis
     *
     * @param scroller a not null {@link Scroller}
     */

    public void setScrollerY(Scroller scroller) {
        if (scroller != null) scrollerY = scroller;
    }

    /**
     * Display a description text when no text is available
     *
     * @param str a string; it could be null
     */

    public void setDescription(String str) {
        description = str;
    }

    /**
     * Clear text and all its attributes
     */

    public void clear() {
        buffer.clear();

        scrollerX.reset();
        scrollerY.reset();

        textHeight = 0f;
        longestLine = 0f;
        lines = 0;
        index = hIndex = 0;
    }

    /**
     * Set a new text
     *
     * @param in a char array
     */

    public void setText(char[] in) {
        clear();
        if (in != null) buffer.add(0, in, 0, in.length);
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
     * @param in a char
     */

    public void addText(int i, char in) {
        if (buffer.add(i, in)) {
            index++;
            hIndex = index;
        }
    }

    /**
     * Add a new piece of text
     *
     * @param i  the index of the char in which insert the new text
     * @param in a char array to draw
     */

    public void addText(int i, char[] in) {
        if (in != null && buffer.add(i, in, 0, in.length)) {
            index += in.length;
            hIndex = index;
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
        if (in != null) addText(i, in.toCharArray());
    }

    /**
     * Add, at the end of the text, a new piece of text
     *
     * @param in a string
     */

    public void addText(String in) {
        if (in != null) addText(in.toCharArray());
    }

    /**
     * Remove a piece of text
     *
     * @param i the position of the char to remove
     */

    public void removeText(int i) {
        if (buffer.remove(i)) hIndex = index = min(i, buffer.size());
    }

    /**
     * Remove a piece of text
     *
     * @param i the first index inclusive
     * @param j the last index inclusive
     */

    public void removeText(int i, int j) {
        if (buffer.remove(i, j)) hIndex = index = Utils.constrain(i, 0, buffer.size());
    }

    // test functionality
    public void setIndex(int i) {
        if (i >= 0) index = Utils.constrain(i, 0, buffer.size());
    }

    // test functionality
    public void setHIndex(int i) {
        if (i >= 0) hIndex = Utils.constrain(i, 0, buffer.size());
    }

    /**
     * Given a pointer coordinates, find the nearest array's element and return its index.
     * <br>
     * Time required: O(n),
     * <br>
     * Space required: O(1).
     *
     * @param chars  a not null char array
     * @param length the array's length
     * @param mx     the mouse position along x-axis
     * @param my     the mouse position along y-axis
     * @return the array's index currently user-covered otherwise -1
     */

    private int getIndex(char[] chars, int length, float mx, float my) {
        float width = width() - font.getWidth('o');
        float heightLine = getLineHeight();
        float x = xp - x();
        float y = yp - y();

        int ax = alignX.map();

        mx -= width() / 2f;
        my -= height() / 2f;

        if (singleLine) {

            if (my > y && my < y + heightLine) {
                float dim;

                x += ((ax - 1f) * width - ax * font.getWidth(chars, 0, length)) / 2f;

                int j = 0;
                while (j < length) {
                    dim = font.getWidth(chars[j]);
                    if (mx < x + dim / 2f) break;
                    x += dim;
                    j++;
                }

                return j;
            }

        } else {
            int sol;      // start of line
            int eol = -1; // end of line

            for (int i = 0; i <= length; i++) {

                if (i == length || chars[i] == '\n') {
                    sol = eol + 1;
                    eol = i;
                    y += heightLine;

                    if (my > y - heightLine && my < y) {
                        float dim;

                        x += ((ax - 1f) * width - ax * font.getWidth(chars, sol, eol - sol)) / 2f;

                        int j = sol;
                        while (j < eol) {
                            dim = font.getWidth(chars[j]);
                            if (mx < x + dim / 2f) break;
                            x += dim;
                            j++;
                        }

                        return j;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Time required: O(n),
     * <br>
     * Space required: O(1)
     *
     * @param chars  a not null array of chars
     * @param length the number of elements to scan
     * @param i      the scanner start position
     * @return the first break line position or {@code length}
     */

    private static int getBr(char[] chars, int length, int i) {
        while (i < length && chars[i] != '\n') i++;
        return i;
    }

    /**
     * Draw a shape that highlights the text.
     * <br>
     * Time required: O(n)
     * <br>
     * Space required: O(1)
     *
     * @param graphic a not null {@link Graphic}
     * @param chars   a not null char array
     * @param length  the array's length
     * @param x       the position along x-axis
     * @param y       the position along y-axis
     */

    private void drawSelected(Graphic graphic, char[] chars, int length, float x, float y) {
        int iMin = getMinIndex();
        int iMax = getMaxIndex();
        int ax = alignX.map();

        float width = width() - font.getWidth('o');
        float lineHeight = getLineHeight();

        if (singleLine) {
            x += font.getWidth(chars, 0, iMin);// offset
            x += ((ax - 1f) * width - ax * font.getWidth(chars, 0, length)) / 2f;// aligner

            float frame = font.getWidth(chars, iMin, iMax - iMin);

            highlight.setPosition(x + frame / 2f, y + 0.5f * lineHeight);
            highlight.setDimension(frame, lineHeight);
            highlight.draw(graphic);
        } else {
            int sol = 0;  // start of line
            int eol = -1; // end of line
            int o;
            int line = 1;

            // calculate the first highlight's offset position along x-axis
            for (int i = 0; i < iMin; i++) {
                if (chars[i] == '\n') {
                    sol = i + 1;
                    line++;
                }
            }

            float diff = font.getWidth(chars, sol, iMin - sol);// calculate the offset
            o = iMin;
            eol = sol - 1;

            for (int i = iMin; i <= iMax; i++) {

                if (i == iMax || chars[i] == '\n') {
                    sol = eol + 1;
                    eol = getBr(chars, length, i);// test

                    float lineWidth = font.getWidth(chars, sol, eol - sol);
                    float wShape = font.getWidth(chars, o, i - o);

                    diff += ((ax - 1f) * width - ax * lineWidth) / 2f;// aligner

                    highlight.setPosition(x + diff + wShape / 2f, y + (line - 0.5f) * lineHeight);
                    highlight.setDimension(wShape, lineHeight);
                    highlight.draw(graphic);

                    o = i;
                    line++;
                    diff = 0f;
                }
            }
        }

    }

    /**
     * Draw an array of chars on the given Graphic.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1)
     *
     * @param graphic a not null {@link Graphic}
     * @param chars   an array of chars to draw on screen
     * @param length  the array's length
     * @param x       the position along x-axis
     * @param y       the position along y-axis
     */

    private void drawText(Graphic graphic, char[] chars, int length, float x, float y) {
        float top = y() - height() / 2f;
        float bot = y() + height() / 2f;
        float lineHeight = getLineHeight();
        float width = width() - font.getWidth('o');

        int ax = alignX.map();

        longestLine = 0f;

        if (singleLine) {
            longestLine = font.getWidth(chars, 0, length);
            graphic.drawText(chars, 0, length,
                    x + ((ax - 1f) * width - ax * longestLine) / 2f,
                    y + 0.75f * lineHeight);
        } else {
            int sol; // start of line
            int eol = -1; // end of line
            int lines = 0;

            for (int i = 0; i <= length; i++) {

                if (i == length || chars[i] == '\n') {
                    sol = eol + 1;
                    eol = i;

                    float lineLength = font.getWidth(chars, sol, eol - sol);

                    if (y + (lines + 2) * lineHeight >= top && y + (lines - 1) * lineHeight <= bot) {
                        graphic.drawText(chars, sol, eol - sol,
                                x + ((ax - 1f) * width - ax * lineLength) / 2f,
                                y + (lines + 0.75f) * lineHeight);
                    }

                    if (lineLength > longestLine) longestLine = lineLength;

                    lines++;
                }
            }
        }
    }

    private float xp;
    private float yp;

    @Override
    protected void update() {
        super.update();

        int sol = 0;  // start of line
        int eol = -1; // end of line
        int cLine = 0;
        int length = buffer.size();
        char[] chars = buffer.toArray();

        lines = 1;

        if (!singleLine) {

            // calculate the cursor's line and update the number of text's lines.
            // Time required: T(n). Space required: O(1)
            for (int i = 0; i <= length; i++) {
                if (i == length || chars[i] == '\n') {
                    lines++;
                    if (index > eol) {
                        sol = eol + 1;
                        eol = getBr(chars, length, i);
                        cLine++;
                    }
                }
            }

        } else {
            eol = length;
        }

        // update text's height
        textHeight = lines * getLineHeight();

        // update scrollerX
        scrollerX.setMax(longestLine - 0.975f * width());
        scrollerX.setFactor(font.getWidth('a'));

        // update scrollerY
        scrollerY.setMax(textHeight - height());
        scrollerY.setFactor(textHeight / (getLines() + 1));

        // update position's params
        xp = x() + (alignX.map() - 1f) * scrollerX.getValue();
        yp = y() - (isAlignY(AlignY.TOP) ? height() : textHeight) / 2f - scrollerY.getValue();

        // update cursor's position along x-axis
        int ax = alignX.map();
        float width = width() - font.getWidth('o');
        cursorX = xp + ((ax - 1f) * width - ax * font.getWidth(chars, sol, Math.max(0, eol - sol))) / 2f
                + font.getWidth(chars, sol, index - sol);

        // update cursor's position along y-axis
        cursorY = yp + (cLine - 0.5f) * getLineHeight();
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        // set font
        graphic.setFont(font);

        // clip this drawing area
        graphic.setClip(getShape());

        // highlight the selected text
        if (isTextSelected()) {
            graphic.setPaint(paintSelected);
            drawSelected(graphic, buffer.toArray(), buffer.size(), xp, yp);
        }

        // set text paint
        graphic.setPaint(paintText);

        // draw text
        if (buffer.size() > 0) {
            drawText(graphic, buffer.toArray(), buffer.size(), xp, yp);
        } else if (description != null) {
            drawText(graphic, description.toCharArray(), description.length(), xp, yp);
        }

        // restore the previous clipping area
        graphic.restoreClip();
    }

    /**
     * @return the cursor's position along x-axis
     */

    public float getCursorX() {
        return cursorX;
    }

    /**
     * @return the cursor's position along y-axis
     */

    public float getCursorY() {
        return cursorY;
    }

    /**
     * @return the internal char array
     */

    public char[] getChars() {
        return buffer.toArray();
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
        return description;
    }

    /**
     * @return the position of the char currently cursor covered
     */

    public int getIndex() {
        return index;
    }

    /**
     * <b>Test.</b>
     * <br>
     * Return the position of the char currently cursor covered shifted on a different line.
     * <br>
     * This method has been provided to easily move cursor UP and DOWN between text lines.
     * <br>
     * Time required: O(n),
     * <br>
     * Space required: O(1).
     *
     * @param i the line offset; +1 to move cursor on the next line; -1 to move cursor on the previous line
     * @return the position of the char currently cursor covered
     */

    protected int getIndex(int i) {
        return getIndex(buffer.toArray(), buffer.size(),
                cursorX - xp + width() / 2f,
                cursorY - yp + i * getLineHeight());
    }

    /**
     * @return the minimum highlighted index
     */

    protected int getMinIndex() {
        return min(index, hIndex);
    }

    /**
     * @return the maximum highlighted index
     */

    protected int getMaxIndex() {
        return max(index, hIndex);
    }

    /**
     * @return the amount of break lines
     */

    public int getLines() {
        return lines;
    }

    /**
     * @return the amount of chars including break lines and white spaces
     */

    public int getCharCount() {
        return buffer.size();
    }

    /**
     * @return the number of selected chars
     */

    public int getSelectionCount() {
        return abs(index - hIndex);
    }

    /**
     * @return the line's factor
     */

    public float getLineFactor() {
        return lineFactor;
    }

    /**
     * @return the height of a text line
     */

    public float getLineHeight() {
        return lineFactor * 1.33f * font.getSize();
    }

    /**
     * @return the text height in pixel
     */

    public float getTextHeight() {
        return textHeight;
    }

    /**
     * @return the longest line in pixel
     */

    public float getLongestLine() {
        return longestLine;
    }

    /**
     * @return true if the text is partially or completed selected
     */

    public boolean isTextSelected() {
        return getSelectionCount() != 0;
    }

    /**
     * @return true if this view has single line mode enabled
     */

    public boolean isSingleLineEnabled() {
        return singleLine;
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
     * @return the Scroller used to scroll text along x-axis
     */

    public Scroller getScrollerX() {
        return scrollerX;
    }

    /**
     * @return the Scroller used to scroll text along y-axis
     */

    public Scroller getScrollerY() {
        return scrollerY;
    }

    /**
     * @return the Paint used to color the selected text
     */

    public Paint getPaintSelected() {
        return paintSelected;
    }

    /**
     * @return the text's Paint
     */

    public Paint getPaintText() {
        return paintText;
    }

    /**
     * @return the text font
     */

    public Font getFont() {
        return font;
    }
}
