package uia.develop;

import uia.application.desktop.ContextSwing;
import uia.core.*;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.core.ui.callbacks.OnMouseHover;
import uia.core.ui.context.Context;
import uia.physical.*;
import uia.physical.text.TextRenderer;
import uia.physical.theme.Theme;
import uia.utility.GeometryFactory;
import uia.utility.Timer;
import uia.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * Standard UIa component.
 * <br>
 * Component designed to edit text.
 */

public class UIEditText extends WrapperViewText {

    public enum Rule {ALPHANUMERIC, NUMBERS}

    private final List<Integer> illegalCodes = new ArrayList<>();
    private final CharList charList = new CharList(10);

    private Rule rule = Rule.ALPHANUMERIC;

    private Cursor cursor;

    private final Shape highlight;
    private Paint paintHighlight;

    private int index;
    private int hIndex;
    private float cursorX;
    private float cursorY;

    public UIEditText(View view) {
        super(new ComponentText(view));

        registerCallback((OnKeyPressed) key -> {
            int code = key.getKeyCode();
            char keyChar = key.getKeyChar();
            boolean selected = getSelectionCount() > 0;

            if (defaultAction(key, selected)) {
                cursor.resetTimer();
            } else if (!illegalCodes.contains(code) && (rule.equals(Rule.ALPHANUMERIC)
                    || (rule.equals(Rule.NUMBERS) && keyChar >= '0' && keyChar <= '9'))) {
                cursor.resetTimer();
                if (selected) clearSelected();
                addText(index, key.getKeyChar());
            }
        });

        final boolean[] selected = {false};
        registerCallback((OnMouseHover) touches -> {
            ScreenTouch p = touches.get(0);
            if (p.getAction().equals(ScreenTouch.Action.PRESSED)) {
                int ind = getIndex(charList.toArray(), chars(), p.getX(), p.getY());
                //System.out.println(ind);
                if (ind != -1) {
                    index = ind;
                    if (!selected[0]) {
                        selected[0] = true;
                        hIndex = index;
                    }
                }
            } else if (p.getAction().equals(ScreenTouch.Action.RELEASED)) {
                selected[0] = false;
            }
        });


        highlight = new Shape();
        GeometryFactory.rect(highlight.getGeometry());

        paintHighlight = new Paint().setColor(new Paint.Color(80, 120, 255));

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

        cursor = new Cursor();
        cursor.getPaint().setColor(Theme.BLACK);
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

    public void addText(int i, char[] in) {
        if (charList.add(i, in, 0, in.length)) {
            index += in.length;
            hIndex = index;
            refreshText();
        }
    }

    public void removeText(int i) {
        if (charList.remove(i)) {
            hIndex = index = min(i, chars());
            refreshText();
        }
    }

    public void removeText(int i, int j) {
        if (charList.remove(i, j)) {
            hIndex = index = Utility.constrain(i, 0, chars());
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
        return Math.max(index, hIndex);
    }

    /**
     * Reset the selection box
     */

    public void resetTextBox() {
        index = hIndex = 0;
    }

    // test functionality
    private void setIndex(int i) {
        if (i >= 0) index = Utility.constrain(i, 0, chars());
    }

    // test functionality
    private void setHIndex(int i) {
        if (i >= 0) hIndex = Utility.constrain(i, 0, chars());
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

        return false;
    }

    /**
     * Decide what kind of chars are allowed when typing text
     *
     * @param rule a not null {@link Rule}
     */

    public void setRule(Rule rule) {
        if (rule != null) {
            this.rule = rule;
        }
    }

    /**
     * Draw a rectangle that highlights the text.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1)
     *
     * @param graphic a not null {@link Graphic}
     * @param chars   a not null char array
     * @param length  the array's length
     */

    private void drawBox(Graphic graphic, char[] chars, int length) {
        Font font = getFont();

        int iMin = getMinIndex();
        int iMax = getMaxIndex();
        int ax = TextRenderer.map(getAlignX());

        float[] bounds = getBounds();
        float[] textBounds = getTextBounds();
        float width = bounds[2] - font.getWidth('o');
        float lineHeight = textBounds[3];

        float x = textBounds[0];
        float y = textBounds[1];

        if (isSingleLine()) {
            x += font.getWidth(0, iMin, chars);// offset
            x += ((ax - 1f) * width - ax * font.getWidth(0, length, chars)) / 2f;// aligner

            float frame = font.getWidth(iMin, iMax - iMin, chars);

            highlight.setPosition(x + frame / 2f, y + 0.5f * lineHeight);
            highlight.setDimension(frame / 2f, lineHeight / 2f);
            graphic.drawShape(highlight);
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
                    if (lineWidth == 0f) lineWidth = wShape = 10f;

                    diff += ((ax - 1f) * width - ax * lineWidth) / 2f;// aligner

                    highlight.setPosition(x + diff + wShape / 2f, y + (line - 0.5f) * lineHeight);
                    highlight.setDimension(wShape / 2f, lineHeight / 2f);
                    graphic.drawShape(highlight);

                    o = i;
                    line++;
                    diff = 0f;
                }
            }
        }
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (isVisible()) {
            Font font = getFont();

            int sol = 0;  // start of line
            int eol = -1; // end of line
            int cLine = 0;
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
                            cLine++;
                        }
                    }
                }

            } else {
                cLine = 1;
                eol = length;
            }

            int ax = TextRenderer.map(getAlignX());
            float width = getBounds()[2] - font.getWidth('o');
            float[] bounds = getBounds();
            float[] textBounds = getTextBounds();
            float lineHeight = font.getLineHeight();
            float lineFactor = 1f;

            // update cursor's position along x-axis
            cursorX = (textBounds[0] + ((ax - 1f) * width - ax * font.getWidth(sol, Math.max(0, eol - sol), chars)) / 2f
                    + font.getWidth(sol, index - sol, chars)) / bounds[2];

            // update cursor's position along y-axis
            cursorY = (cLine - 0.5f) * lineHeight / bounds[3];

            // update cursor
            if (isOnFocus()) {
                cursor.setPosition(cursorX, cursorY);
                cursor.setDimension(2 / bounds[2], (lineHeight / lineFactor) / bounds[3]);
                cursor.update(this);
            }
        }
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            //graphic.setClip(clipShape);

            // highlight the selected text
            if (getSelectionCount() > 0) {
                graphic.setPaint(paintHighlight);
                drawBox(graphic, charList.toArray(), chars());
            }

            if (isOnFocus()) {
                cursor.draw(graphic);
            }

            //graphic.restoreClip();
        }
    }

    /**
     * Given a pointer coordinates, find the nearest array's element and return its index.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1).
     *
     * @param chars  a not null char array
     * @param length the array's length
     * @param mx     the pointer's position along x-axis
     * @param my     the pointer's position along y-axis
     * @return the array's index currently user-covered otherwise -1
     */

    private int getIndex(char[] chars, int length, float mx, float my) {
        Font font = getFont();

        float[] bounds = getBounds();
        float[] textBounds = getTextBounds();
        float width = bounds[2] - font.getWidth('o');
        float heightLine = font.getLineHeight();

        int ax = TextRenderer.map(getAlignX());
        float x = ax * (bounds[2] - textBounds[2]) / 2f;
        float y = 0f;

        if (isSingleLine()) {

            if (my > y && my < y + heightLine) {
                float dim;

                x += ((ax - 1f) * width - ax * font.getWidth(0, length, chars)) / 2f;

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
     * @return the text cursor
     */

    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Text cursor representation
     */

    public static class Cursor extends WrapperView {
        private final Timer timer;

        public Cursor() {
            super(new Component("Cursor", 0f, 0f, 1f, 1f));

            timer = new Timer();
        }

        public void resetTimer() {
            timer.reset();
        }

        @Override
        public void draw(Graphic graphic) {
            float seconds = timer.seconds();

            if (seconds <= 0.5f) {
                super.draw(graphic);
            } else if (seconds >= 1f) timer.reset();
        }
    }

    /**
     * Return the next break line.
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

    public static void main(String[] args) {
        UIEditText editText = new UIEditText(
                new Component("", 0.5f, 0.5f, 0.5f, 0.5f)
        );
        editText.setText("Test!");
        editText.getPaint().setColor(Theme.SILVER);

        ViewGroup group = new ComponentGroup(
                new Component("", 0.5f, 0.5f, 1f, 1f)
        );
        ViewGroup.insert(group, editText);

        Context context = ContextSwing.createAndStart(1000, 500);
        context.setView(group);
    }
}
