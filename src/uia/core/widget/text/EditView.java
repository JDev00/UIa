package uia.core.widget.text;

import uia.core.event.Event;
import uia.core.gesture.Scroller;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Key;
import uia.core.platform.policy.Graphic;
import uia.structure.BInt;
import uia.utils.Timer;

public class EditView extends TextView {
    public enum Rule {ALPHANUMERIC, NUMBERS}

    private final BInt illegalCodes = new BInt(20);

    private Rule rule = Rule.ALPHANUMERIC;

    private TextCursor textCursor;

    public EditView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        getEventQueue().addEvent((v, s) -> {
            if (s == Event.KEY_PRESSED) {
                Key e = v.getKey();
                int code = e.getKeyCode();

                if (defaultTypeActions(e, isTextSelected())) {
                    textCursor.resetTimer();
                } else if (!illegalCodes.contains(code) && (rule.equals(Rule.ALPHANUMERIC)
                        || (rule.equals(Rule.NUMBERS) && code >= '0' && code <= '9'))) {
                    textCursor.resetTimer();
                    if (isTextSelected()) clearSelected();
                    addText(getIndex(), e.getKeyChar());
                }
            }
        });

        illegalCodes.add(9);    // TAB
        illegalCodes.add(16);   // SHIFT
        illegalCodes.add(17);   // CONTROL
        illegalCodes.add(18);   // ALT
        illegalCodes.add(20);   // CAPS_LOCK
        illegalCodes.add(27);   // ESCAPE
        illegalCodes.add(33);   // PAGE_UP
        illegalCodes.add(34);   // PAGE_DOWN
        illegalCodes.add(38);   // UP
        illegalCodes.add(40);   // DOWN
        illegalCodes.add(144);  // NUM_LOCK
        illegalCodes.add(155);  // INSERT
        illegalCodes.add(226);  // KEYPAD LEFT_ARROW
        illegalCodes.add(227);  // KEYPAD RIGHT_ARROW
        illegalCodes.add(65406);// ALT_GRAPH

        textCursor = new TextCursor();
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

    private boolean defaultTypeActions(Key key, boolean selected) {
        if (key.codeTextSelectionAll() || key.codeTextSelectionLeft() || key.codeTextSelectionRight()) {
            return true;
        }

        if (key.codeTextCopy()) {
            if (selected)
                getContext().copyIntoClipboard(new String(getChars(), getMinIndex(), getSelectionCount()));
            return true;
        }

        if (key.codeTextPaste()) {
            if (selected) clearSelected();
            addText(getIndex(), getContext().pasteFromClipboard().toCharArray());
            return true;
        }

        switch (key.getKeyCode()) {

            case 37: // ARROW-LEFT
                int k = getMinIndex() - 1;
                setIndex(k);
                setHIndex(k);
                return true;

            case 38: // ARROW-UP
                k = getIndex(-1);
                setIndex(k);
                setHIndex(k);
                return true;

            case 39: // ARROW-RIGHT
                k = getMaxIndex() + 1;
                setIndex(k);
                setHIndex(k);
                return true;

            case 40: // ARROW-DOWN
                k = getIndex(1);
                setIndex(k);
                setHIndex(k);
                return true;

            case '\n':
                if (!isSingleLineEnabled()) {
                    addText(getIndex(), '\n');

                    // adjust scroller position
                    Scroller scroller = getScrollerY();
                    if (getCursorY() + getLineHeight() >= y() + 0.5f * height()) {
                        scroller.setValue(scroller.getValue() + getLineHeight());
                    }
                }
                return true;

            case '\b':
                if (selected) {
                    clearSelected();
                } else {
                    removeText(getIndex() - 1);
                }
                return true;

            case 127:// canc key
                if (selected) {
                    clearSelected();
                } else {
                    removeText(getIndex());
                }
                return true;
        }

        return false;
    }

    /**
     * Decide what kind of chars are allowed when typing text
     *
     * @param rule a not null {@link Rule}
     */

    public void setRule(Rule rule) {
        if (rule != null) this.rule = rule;
    }

    /**
     * Set a new TextCursor
     *
     * @param textCursor a not null {@link TextCursor}
     */

    public void setTextCursor(TextCursor textCursor) {
        if (textCursor != null) this.textCursor = textCursor;
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        // draw cursor
        if (isFocused()) {
            graphic.setClip(getShape());
            textCursor.setPosition(getCursorX(), getCursorY());
            textCursor.setDimension(2, getLineHeight() / getLineFactor());
            textCursor.draw(graphic);
            graphic.restoreClip();
        }
    }

    /**
     * @return the associated {@link TextCursor}
     */

    public TextCursor getTextCursor() {
        return textCursor;
    }

    /**
     * Text cursor representation
     */

    public static class TextCursor extends Shape {
        private final Paint paint;

        protected final Timer timer;

        public TextCursor() {
            super(4);
            Figure.rect(this);

            paint = new Paint(255, 0, 0);

            timer = new Timer();
        }

        public void resetTimer() {
            timer.reset();
        }

        @Override
        public void draw(Graphic graphic) {
            float seconds = timer.seconds();

            if (seconds <= 0.5f) {
                graphic.setPaint(paint);
                super.draw(graphic);
            } else if (seconds >= 1f) timer.reset();
        }

        public Paint getPaint() {
            return paint;
        }
    }
}
