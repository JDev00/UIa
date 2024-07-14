package uia.preview;

import uia.core.ui.primitives.Key;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.rendering.color.Color;
import uia.core.rendering.geometry.Geometry;
import uia.core.rendering.Graphics;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.core.ui.callbacks.OnMouseExit;
import uia.core.ui.callbacks.OnMouseHover;
import uia.core.rendering.font.Font;
import uia.core.rendering.Transform;
import uia.core.ui.style.Style;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.physical.ui.component.Component;
import uia.physical.ui.component.text.ComponentText;
import uia.physical.ui.component.WrapperView;
import uia.physical.ui.component.text.WrapperViewText;
import uia.physical.ui.Theme;
import uia.physical.ui.component.utility.ComponentUtility;
import uia.utility.Geometries;
import uia.utility.Timer;
import uia.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

// TODO: to refactor

/**
 * Standard UIa component.
 * <br>
 * Component designed to edit text.
 */

public class UIEditText extends WrapperViewText {
    private final CharList charList = new CharList(10);
    private final List<Integer> illegalCodes = new ArrayList<>();

    private final Color hightlightColor;
    private final Transform highlightTransform;
    private final Geometry highlightGeometry;
    private final Transform clipTransform;
    private Cursor cursor;

    private int index;
    private int hIndex;

    public UIEditText(View view) {
        super(new ComponentText(view));
        registerCallback((OnKeyPressed) key -> {
            boolean selectedText = getSelectionCount() > 0;
            if (defaultAction(key, selectedText)) {
                cursor.resetTimer();
            } else if (!illegalCodes.contains(key.getKeyCode())) {
                cursor.resetTimer();
                if (selectedText) {
                    clearSelected();
                }
                addText(index, key.getKeyChar());
            }
        });
        final boolean[] selected = {false};
        registerCallback((OnMouseHover) touches -> {
            ScreenTouch screenTouch = touches[0];
            if (ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.PRESSED)
                    || ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.DRAGGED)) {
                int ind = getIndex(screenTouch.getX(), screenTouch.getY());
                if (ind != -1) {
                    index = ind;
                    if (!selected[0]) {
                        selected[0] = true;
                        hIndex = index;
                    }
                }
            } else if (ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.RELEASED)) {
                selected[0] = false;
            }
        });
        registerCallback((OnMouseExit) o -> selected[0] = false);

        // shift, ctrl, alt gr, esc, caps-lock
        illegalCodes.addAll(Arrays.asList(16, 17, 18, 27, 20));

        /*illegalCodes.add(Key.KEY_TAB);
        illegalCodes.add(Key.KEY_SHIFT);
        illegalCodes.add(Key.KEY_CTRL);
        illegalCodes.add(Key.KEY_ALT);
        illegalCodes.add(Key.KEY_CAPS_LOCK);
        illegalCodes.add(Key.KEY_ESCAPE);
        illegalCodes.add(Key.KEY_NUM_LOCK);
        illegalCodes.add(Key.KEY_INSERT);
        illegalCodes.add(Key.KEY_ALT_GRAPH);
        illegalCodes.add(33);   // PAGE_UP
        illegalCodes.add(34);   // PAGE_DOWN
        illegalCodes.add(226);  // KEYPAD LEFT_ARROW
        illegalCodes.add(227);  // KEYPAD RIGHT_ARROW*/

        cursor = new Cursor(view.getID());

        // highlight
        hightlightColor = Color.createColor(65, 105, 225, 126);

        highlightTransform = new Transform();

        highlightGeometry = Geometries.rect(new Geometry());

        // clip
        clipTransform = new Transform();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        charList.clear();
        charList.add(0, text.toCharArray(), 0, text.length());
        index = hIndex = 0;
    }

    private void refreshText() {
        super.setText(charList.toString());
    }

    /*
     * To remove.
     */

    public void addText(int i, char in) {
        if (charList.add(i, in)) {
            index++;
            hIndex = index;
            refreshText();
        }
    }

    private void addText(int i, char[] in) {
        if (charList.add(i, in, 0, in.length)) {
            index += in.length;
            hIndex = index;
            refreshText();
        }
    }

    private void removeText(int i) {
        if (charList.remove(i)) {
            hIndex = index = min(i, chars());
            refreshText();
        }
    }

    private void removeText(int i, int j) {
        if (charList.remove(i, j)) {
            hIndex = index = MathUtility.constrain(i, 0, chars());
            refreshText();
        }
    }

    private int chars() {
        return charList.size();
    }

    /**
     * @return the minimum highlighted index
     */

    private int getMinIndex() {
        return min(index, hIndex);
    }

    /**
     * @return the maximum highlighted index
     */

    private int getMaxIndex() {
        return max(index, hIndex);
    }

    /**
     * Reset the selection box
     */

    private void resetTextBox() {
        index = hIndex = 0;
    }

    // test functionality
    private void setIndex(int i) {
        if (i >= 0) index = MathUtility.constrain(i, 0, chars());
    }

    // test functionality
    private void setHIndex(int i) {
        if (i >= 0) hIndex = MathUtility.constrain(i, 0, chars());
    }

    /**
     * Remove the highlighted chars
     */

    private void clearSelected() {
        removeText(getMinIndex(), getMaxIndex() - 1);
    }

    /**
     * Default typing actions
     *
     * @param key      the last key
     * @param selected true if a part of the text is selected
     */

    private boolean defaultAction(Key key, boolean selected) {
        /*if (key.isKeystroke(Key.MASK_CTRL, Key.KEY_Z - 4)) {
            if (selected) clearSelected();
            //addText(getIndex(), getContext().pasteFromClipboard().toCharArray());
            return true;
        }

        if (key.isKeystroke(Key.MASK_CTRL, Key.KEY_A)) {
            hIndex = 0;
            index = chars();
            return true;
        }

        if (key.isKeystroke(Key.MASK_SHIFT, Key.KEY_LEFT)) {
            index = Math.max(0, index - 1);
            return true;
        }

        if (key.isKeystroke(Key.MASK_SHIFT, Key.KEY_RIGHT)) {
            index = min(index + 1, chars());
            return true;
        }

        if (key.isKeystroke(Key.MASK_SHIFT, Key.KEY_UP)) {
            int ind = getIndex(-1);
            if (ind >= 0) index = ind;
            return true;
        }

        if (key.isKeystroke(Key.MASK_SHIFT, Key.KEY_DOWN)) {
            int ind = getIndex(1);
            if (ind >= 0) index = ind;
            return true;
        }*/

        /*else if (key.isCode(Key.MASK_CTRL, Key.KEY_A + 2) && isTextSelected()) {
            //getContext().copyIntoClipboard(String.valueOf(buffer.toArray(), getMinIndex(), getSelectionCount()));
        }*/

        /*switch (key.getKeyCode()) {

            case Key.KEY_LEFT:
                int k = getMinIndex() - 1;
                setIndex(k);
                setHIndex(k);
                return true;

            case Key.KEY_UP:
                k = getIndex(-1);
                setIndex(k);
                setHIndex(k);
                return true;

            case Key.KEY_RIGHT:
                k = getMaxIndex() + 1;
                setIndex(k);
                setHIndex(k);
                return true;

            case Key.KEY_DOWN:
                k = getIndex(1);
                setIndex(k);
                setHIndex(k);
                return true;

            case Key.KEY_ENTER:
                if (!singleLine) {
                    addText(index, '\n');

                    // adjust scroller position
                    //if (getCursorY() + getLineHeight() >= y() + 0.5f * height()) scrollY(false);
                }
                return true;

            case Key.KEY_BACKSPACE:
                if (selected) {
                    clearSelected();
                } else {
                    removeText(index - 1);
                }
                return true;

            case Key.KEY_DELETE:
                if (selected) {
                    clearSelected();
                } else {
                    removeText(index);
                }
                return true;
        }*/

        if (key.getKeyCode() == 8) {// backspace
            if (selected) {
                clearSelected();
            } else {
                removeText(index - 1);
            }
            return true;
        } else if (key.getKeyCode() == 127) {// canc
            if (selected) {
                clearSelected();
            } else {
                removeText(index);
            }
            return true;
        } else if (key.getKeyCode() == 40) {// key-down
            /*int ind = getIndex(1);
            if (ind >= 0) {
                index = ind;
            }*/
            return true;
        } else if (key.getKeyCode() == 38) {// key-up
            /*int ind = getIndex(1);
            if (ind >= 0) {
                index = ind;
            }*/
            return true;
        } else if (key.isKeystroke(1, 37)) {// shift-left
            index = max(0, index - 1);
            return true;
        } else if (key.getKeyCode() == 37) {// key-left
            int k = getMinIndex() - 1;
            setIndex(k);
            setHIndex(k);
            return true;
        } else if (key.isKeystroke(1, 39)) {// shift-right
            index = min(index + 1, chars());
            return true;
        } else if (key.getKeyCode() == 39) {// key-right
            int k = getMaxIndex() + 1;
            setIndex(k);
            setHIndex(k);
            return true;
        } else if (key.isKeystroke(2, 65)) {// ctrl-a
            hIndex = 0;
            index = chars();
            return true;
        } else if (key.isKeystroke(2, 67)) {// ctrl-c
            return true;
        } else if (key.isKeystroke(2, 86)) {// ctrl-v
            return true;
        }
        return false;
    }

    /**
     *
     */
    private float[] calculateInlineBoxDimension() {
        Style style = getStyle();
        Font font = style.getFont();
        int iMin = getMinIndex();

        char[] chars = charList.toArray();
        float[] bounds = getBounds();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        int ay = TextVerticalAlignment.map(style.getVerticalTextAlignment());
        float deltaTextX = ax * (bounds[2] - font.getWidth(0, chars(), chars)) / 2f;
        float deltaTextY = ay * (bounds[3] - getTextBounds()[3]) / 2f;
        return new float[]{
                bounds[0]
                        + deltaTextX
                        + font.getWidth(0, iMin, chars)
                        - (ax - 1f) * 4f,
                bounds[1] + deltaTextY
        };
    }

    /**
     * Helper function. Draws a box on single line text.
     */

    private void drawInlineBox(Graphics graphics, float[] boxPosition) {
        char[] chars = charList.toArray();
        Font font = getStyle().getFont();

        int iMin = getMinIndex();
        int iMax = getMaxIndex();
        float width = font.getWidth(iMin, iMax - iMin, chars);
        float height = font.getLineHeight();

        highlightTransform
                .setTranslation(
                        boxPosition[0] + width / 2f,
                        boxPosition[1] + height / 2f
                )
                .setScale(width, height)
                .setRotation(0f);
        graphics.drawShape(highlightTransform, highlightGeometry.vertices(), highlightGeometry.toArray());
    }

    /**
     * Helper function. Draws a box on multiline text.
     */

    private void drawMultilineBox(Graphics graphics) {
        // TODO: handle the centered text

        Style style = getStyle();
        Font font = style.getFont();
        char[] chars = charList.toArray();
        int length = chars();
        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());

        float[] bounds = getBounds();
        float[] textBounds = getTextBounds();
        float width = bounds[2];
        float lineHeight = font.getLineHeight();

        float x = textBounds[0];
        float y = textBounds[1];

        int iMin = getMinIndex();
        int iMax = getMaxIndex();
        int sol = 0; // start of line
        int eol;     // end of line
        int o;
        int line = 1;

        // calculate the first highlight's offset position along x-axis
        for (int i = 0; i < iMin; i++) {
            if (chars[i] == '\n') {
                sol = i + 1;
                line++;
            }
        }

        float diff = font.getWidth(sol, iMin - sol, chars);// calculate the offset
        o = iMin;
        eol = sol - 1;

        for (int i = iMin; i <= iMax; i++) {

            if (i == iMax || chars[i] == '\n') {
                sol = eol + 1;
                eol = getBr(chars, length, i);

                float lineWidth = font.getWidth(sol, eol - sol, chars);
                float wShape = font.getWidth(o, i - o, chars);

                // highlight all the line long
                if (lineWidth == 0f) {
                    lineWidth = wShape = 10f;
                }

                diff += ax * (width - lineWidth) / 2f;// aligner

                highlightTransform
                        .setTranslation(
                                x + diff + wShape / 2f,
                                y + (line - 0.5f) * lineHeight
                        )
                        .setScale(wShape, lineHeight)
                        .setRotation(0f);
                graphics.drawShape(highlightTransform, highlightGeometry.vertices(), highlightGeometry.toArray());

                o = i;
                line++;
                diff = 0f;
            }
        }
    }

    /**
     * Calculates the relative cursor position according to the give line and start of line.
     */

    private float[] calculateCursorPosition(int currentLine, int startOfLine) {
        char[] chars = charList.toArray();
        float[] bounds = getBounds();

        Style style = getStyle();
        Font font = style.getFont();
        float lineHeight = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        int ay = TextVerticalAlignment.map(style.getVerticalTextAlignment());
        float deltaTextX = ax * (bounds[2] - font.getWidth(0, chars(), chars)) / 2f;
        float deltaTextY = ay * (bounds[3] - getTextBounds()[3]) / 2f;
        return new float[]{
                (deltaTextX
                        + font.getWidth(startOfLine, index - startOfLine, chars)
                        - (ax - 1f) * 4f
                ) / bounds[2],
                (deltaTextY
                        + (currentLine - 0.5f) * lineHeight
                        - getScrollValue()[1]
                ) / bounds[3]
        };
    }

    /**
     * Updates cursor.
     */

    private void updateCursor(float[] cursorPosition) {
        if (isOnFocus()) {
            Style style = getStyle();
            float[] bounds = getBounds();
            float lineHeight = style.getFont().getLineHeight();
            cursor.setPosition(
                    cursorPosition[0],
                    cursorPosition[1]
            );
            cursor.setDimension(
                    2f / bounds[2],
                    lineHeight / bounds[3]
            );
            cursor.update(this);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        if (isVisible()) {
            ComponentUtility.makeTransformForClipRegion(this, 1f, 1f, clipTransform);

            int sol = 0;  // start of line
            int eol = -1; // end of line
            int currentLine = 0;
            int length = chars();
            char[] chars = charList.toArray();

            if (!isSingleLine()) {
                // calculate the cursor's line.
                // Time required: T(n). Space required: O(1)
                for (int i = 0; i <= length; i++) {
                    if (i == length || chars[i] == '\n') {
                        if (index > eol) {
                            sol = eol + 1;
                            eol = getBr(chars, length, i);
                            currentLine++;
                        }
                    }
                }
            } else {
                currentLine = 1;
                eol = length;
            }

            // updates cursor
            float[] cursorPosition = calculateCursorPosition(currentLine, sol);
            updateCursor(cursorPosition);
        }
    }

    /**
     * Draws a rectangle around the highlighted text.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1)
     *
     * @param graphics a not null {@link Graphics}
     */

    private void drawBox(Graphics graphics) {
        if (getSelectionCount() > 0) {
            graphics.setShapeColor(hightlightColor);

            if (isSingleLine()) {
                float[] boxPosition = calculateInlineBoxDimension();
                drawInlineBox(graphics, boxPosition);
            } else {
                drawMultilineBox(graphics);
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        if (isVisible()) {
            Geometry geometry = getGeometry();
            graphics.setClip(clipTransform, geometry.vertices(), geometry.toArray());

            drawBox(graphics);
            if (isOnFocus()) {
                cursor.draw(graphics);
            }
            graphics.restoreClip();
        }
    }

    /**
     * Finds the nearest character and return its index.
     *
     * @param mx the cursor position on the x-axis
     * @param my the cursor position on the y-axis
     * @return the character index covered by cursor otherwise -1
     */

    private int getIndex(float mx, float my) {
        int result;
        int length = chars();
        char[] chars = charList.toArray();
        if (isSingleLine()) {
            result = getIndexForInlineText(chars, length, mx, my);
        } else {
            result = getIndexForMultilineText(chars, length, mx, my);
        }
        return result;
    }

    /**
     * Helper function. Returns the character index covered by cursor.
     * <br>
     * More formally: given a pointer coordinates, find the nearest character and return its index.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1).
     *
     * @return the character index or -1
     */

    private int getIndexForInlineText(char[] chars, int length, float mx, float my) {
        Style style = getStyle();
        Font font = style.getFont();

        float[] bounds = getBounds();
        float heightLine = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        float y = TextVerticalAlignment.map(style.getVerticalTextAlignment())
                * (bounds[3] - heightLine) / 2f;

        if (my > y && my < y + heightLine) {
            float x = ax * (bounds[2] - font.getWidth(0, length, chars)) / 2f;

            float dim;
            int j = 0;
            while (j < length) {
                dim = font.getWidth(chars[j]);
                if (mx < x + dim / 2f) {
                    break;
                }
                x += dim;
                j++;
            }

            return j;
        }

        return -1;
    }

    /**
     * Helper function. Returns the character index covered by cursor.
     * <br>
     * More formally: given a pointer coordinates, find the nearest character and return its index.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1).
     *
     * @return the character index or -1
     */

    private int getIndexForMultilineText(char[] chars, int length, float mx, float my) {
        Style style = getStyle();
        Font font = style.getFont();
        float[] bounds = getBounds();
        float heightLine = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        float y = -getScrollValue()[1];

        int sol;      // start of line
        int eol = -1; // end of line

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;
                y += heightLine;
                if (my > y - heightLine && my < y) {
                    float x = ax * (bounds[2] - font.getWidth(sol, eol - sol, chars)) / 2f;
                    float dim;
                    int j = sol;
                    while (j < eol) {
                        dim = font.getWidth(chars[j]);
                        if (mx < x + dim / 2f) {
                            break;
                        }
                        x += dim;
                        j++;
                    }

                    return j;
                }
            }
        }

        return -1;
    }

    /*
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
     *

    @Deprecated
    private int getIndex(int i) {
        return getIndex(getText(), chars(),
                cursorX - textPos[0] + getBounds()[2] / 2f,
                cursorY - textPos[1] + i * getLineHeight());
    }*/

    /**
     * @return the number of selected chars
     */

    public int getSelectionCount() {
        return Math.abs(index - hIndex);
    }

    /**
     * Text cursor representation
     */

    public static class Cursor extends WrapperView {
        private final Timer timer;

        public Cursor(String id) {
            super(new Component("EDIT_TEXT_CURSOR_" + id, 0f, 0f, 1f, 1f));
            getStyle().setBackgroundColor(Theme.BLACK);

            timer = new Timer();
        }

        public void resetTimer() {
            timer.reset();
        }

        @Override
        public void draw(Graphics graphics) {
            float seconds = timer.seconds();
            if (seconds <= 0.5f) {
                super.draw(graphics);
            } else if (seconds >= 1f) {
                timer.reset();
            }
        }
    }

    /**
     * Returns the next break line.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1)
     *
     * @param chars  a not null array of chars
     * @param length the number of elements to scan
     * @param i      the scanner start position
     * @return the next break line position or {@code length}
     */

    private static int getBr(char[] chars, int length, int i) {
        while (i < length && chars[i] != '\n') i++;
        return i;
    }
}
