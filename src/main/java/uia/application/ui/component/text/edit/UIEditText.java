package uia.application.ui.component.text.edit;

import uia.application.ui.component.text.edit.structure.CharList;
import uia.application.ui.component.utility.ComponentUtility;
import uia.application.ui.component.text.WrapperViewText;
import uia.application.ui.component.text.ComponentText;
import uia.core.rendering.geometry.GeometryCollection;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.core.ui.callbacks.OnMouseHover;
import uia.core.ui.callbacks.OnMouseExit;
import uia.core.rendering.color.Color;
import uia.core.rendering.Transform;
import uia.core.rendering.font.Font;
import uia.core.rendering.Graphics;
import uia.core.ui.primitives.Key;
import uia.core.ui.style.Style;
import uia.utility.MathUtility;
import uia.core.ui.View;

import java.util.function.Consumer;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static uia.application.ui.component.text.edit.structure.EdiTextAlgorithms.*;

// TODO: to refactor

/**
 * UIEditText is a component designed for editing text, either for single line
 * or multi line text.
 */

public class UIEditText extends WrapperViewText {
    private final CharList charList = new CharList(10);
    private final KeyHandler keyHandler;

    private final Color hightlightColor;
    private final Transform highlightTransform;
    private final Geometry highlightGeometry;
    private final Transform clipTransform;
    private final UITextCursor textCursor;

    private int index;
    private int hIndex;

    public UIEditText(View view) {
        super(new ComponentText(view));

        final boolean[] textSelectionResult = {false};
        registerCallback((OnMouseHover) touches -> {
            ScreenTouch screenTouch = touches[0];
            selectText(screenTouch, textSelectionResult);
        });
        registerCallback((OnMouseExit) empty -> textSelectionResult[0] = false);
        registerCallback((OnKeyPressed) this::handleKey);

        keyHandler = new KeyHandler();

        textCursor = new UITextCursor("EDIT_TEXT_CURSOR_" + view.getID());

        // highlight
        hightlightColor = Color.createColor(65, 105, 225, 126);
        highlightGeometry = GeometryCollection.rect(new Geometry());
        highlightTransform = new Transform();

        // clip
        clipTransform = new Transform();
    }

    /**
     * Helper function. Handles the given key.
     *
     * @param key the key to handle
     */

    private void handleKey(Key key) {
        boolean hasKeyBeenHandled = keyHandler.handleKey(key);
        if (hasKeyBeenHandled) {
            textCursor.resetTimer();
        }
    }

    /**
     * Helper function. Selects text.
     *
     * @param screenTouch     a not null screenTouch
     * @param selectionResult the array used to store the selection result
     */

    private void selectText(ScreenTouch screenTouch, boolean[] selectionResult) {
        boolean pressed = ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.PRESSED);
        boolean dragged = ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.DRAGGED);
        boolean released = ScreenTouch.madeAction(screenTouch, ScreenTouch.Action.RELEASED);

        if (pressed || dragged) {
            int charPosition = getIndex(screenTouch.getX(), screenTouch.getY());
            if (charPosition != -1) {
                index = charPosition;
                if (!selectionResult[0]) {
                    selectionResult[0] = true;
                    hIndex = index;
                }
            }
        } else if (released) {
            selectionResult[0] = false;
        }
    }

    /**
     * Helper function. Finds the nearest character and return its index.
     *
     * @param cursorX the cursor position on the x-axis
     * @param cursorY the cursor position on the y-axis
     * @return the character index covered by cursor otherwise -1
     */

    private int getIndex(float cursorX, float cursorY) {
        int result;
        int length = chars();
        char[] chars = charList.toArray();
        if (isSingleLine()) {
            result = getIndexForInlineText(this, chars, length, cursorX, cursorY);
        } else {
            result = getIndexForMultilineText(this, chars, length, cursorX, cursorY);
        }
        return result;
    }

    @Override
    public void setText(String text) {
        super.setText(text);

        charList.clear();
        charList.add(0, text.toCharArray(), 0, text.length());
        index = hIndex = 0;
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
                eol = getNextBreakLine(chars, length, i);

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
     * Helper function. Updates the text cursor.
     */

    private void updateCursor(float[] cursorPosition) {
        if (isOnFocus()) {
            Font font = getStyle().getFont();
            float[] bounds = getBounds();
            float lineHeight = font.getLineHeight();

            textCursor.setPosition(cursorPosition[0], cursorPosition[1]);
            textCursor.setDimension(2f / bounds[2], lineHeight / bounds[3]);
            textCursor.update(this);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        if (isVisible()) {
            ComponentUtility.makeTransformForClipRegion(this, 1f, 1f, clipTransform);

            int startOfLine = 0;
            int currentLine = 0;

            if (!isSingleLine()) {
                int endOfLine = -1;
                int length = chars();
                char[] chars = charList.toArray();
                // calculate the text cursor line.
                // Time complexity: T(n).
                // Space complexity: O(1)
                for (int i = 0; i <= length; i++) {
                    if ((i == length || chars[i] == '\n') && index > endOfLine) {
                        startOfLine = endOfLine + 1;
                        endOfLine = getNextBreakLine(chars, length, i);
                        currentLine++;

                    }
                }
            } else {
                currentLine = 1;
            }

            // updates cursor
            float[] cursorPosition = calculateCursorPosition(currentLine, startOfLine);
            updateCursor(cursorPosition);
        }
    }

    /**
     * Helper function. Draws a rectangle around the highlighted text.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
     *
     * @param graphics a not null {@link Graphics}
     */

    private void drawBox(Graphics graphics) {
        if (isTextSelected()) {
            graphics.setShapeColor(hightlightColor);

            if (isSingleLine()) {
                float[] boxPosition = getSingleLineTextBoxPosition(this, getMinIndex());
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
                textCursor.draw(graphics);
            }
            graphics.restoreClip();
        }
    }

    /**
     * @return true if the text is partially or fully selected
     */

    private boolean isTextSelected() {
        int selectionCount = Math.abs(index - hIndex);
        return selectionCount > 0;
    }

    /**
     * KeyHandler is responsible for handling the received key.
     */

    private final class KeyHandler {
        private static final int KEY_CANC = 127;
        private static final int KEY_A = 65;
        private static final int KEY_C = 67;
        private static final int KEY_V = 86;

        private final Set<Integer> unhandledKeys = new HashSet<>();
        private final Map<Integer, Consumer<Key>> keyMapper;

        private KeyHandler() {
            // registers the unhandled keys
            int[] specialKeys = SpecialKeys.getAllKeys();
            for (int unhandledKey : specialKeys) {
                unhandledKeys.add(unhandledKey);
            }

            // registers the function to be used when a mapped key is detected
            keyMapper = new HashMap<>();
            keyMapper.put(KEY_CANC, receivedKey -> {
                if (isTextSelected()) {
                    removeSelectedText();
                } else {
                    removeText(index);
                }
            });
            keyMapper.put(SpecialKeys.BACKSPACE.getKeyCode(), receivedKey -> {
                if (isTextSelected()) {
                    removeSelectedText();
                } else {
                    removeText(index - 1);
                }
            });
            keyMapper.put(SpecialKeys.KEY_UP.getKeyCode(), receivedKey -> {
                boolean isKeyStroke = receivedKey.isKeystroke(1, SpecialKeys.KEY_UP.getKeyCode());
                if (isKeyStroke) {
                    //int ind = getIndex(-1);
                    //if (ind >= 0) index = ind;
                } else {
                /*int ind = getIndex(1);
                if (ind >= 0) {
                    index = ind;
                }*/
                }
            });
            keyMapper.put(SpecialKeys.KEY_DOWN.getKeyCode(), receivedKey -> {
                boolean isKeyStroke = receivedKey.isKeystroke(1, SpecialKeys.KEY_DOWN.getKeyCode());
                if (isKeyStroke) {
                /*int index = getIndex(1);
                if (index >= 0) {
                    this.index = index;
                }*/
                } else {
                /*int ind = getIndex(1);
                if (ind >= 0) {
                    index = ind;
                }*/
                }
            });
            keyMapper.put(SpecialKeys.KEY_LEFT.getKeyCode(), receivedKey -> {
                boolean isKeyStroke = receivedKey.isKeystroke(1, SpecialKeys.KEY_LEFT.getKeyCode());
                if (isKeyStroke) {
                    index = max(0, index - 1);
                } else {
                    int cursorIndex = getMinIndex() - 1;
                    setIndex(cursorIndex);
                    setHIndex(cursorIndex);
                }
            });
            keyMapper.put(SpecialKeys.KEY_RIGHT.getKeyCode(), receivedKey -> {
                boolean isKeyStroke = receivedKey.isKeystroke(1, SpecialKeys.KEY_RIGHT.getKeyCode());
                if (isKeyStroke) {
                    index = min(index + 1, chars());
                } else {
                    int cursorIndex = getMaxIndex() + 1;
                    setIndex(cursorIndex);
                    setHIndex(cursorIndex);
                }
            });
        }

        /**
         * Helper function. Refreshes the component text.
         */

        private void refreshText() {
            UIEditText.super.setText(charList.toString());
        }

        /**
         * Helper function. Adds the given char.
         */

        private void addText(int i, char charToAdd) {
            if (charList.add(i, charToAdd)) {
                index++;
                hIndex = index;
                refreshText();
            }
        }

        /**
         * Helper function. Removes the specified char from text.
         */

        private void removeText(int charPosition) {
            if (charList.remove(charPosition)) {
                hIndex = index = min(charPosition, chars());
                refreshText();
            }
        }

        /**
         * Helper function. Removes the selected text.
         */

        private void removeSelectedText() {
            int start = getMinIndex();
            int stop = getMaxIndex() - 1;
            if (charList.remove(start, stop)) {
                hIndex = index = MathUtility.constrain(start, 0, chars());
                refreshText();
            }
        }

        /**
         * Helper function.
         */

        private void setIndex(int i) {
            if (i >= 0) {
                index = MathUtility.constrain(i, 0, chars());
            }
        }

        /**
         * Helper function.
         */

        private void setHIndex(int i) {
            if (i >= 0) {
                hIndex = MathUtility.constrain(i, 0, chars());
            }
        }

        /**
         * Handles the given key.
         *
         * @param key the key to handle
         * @return true if the key has been handled correctly; false otherwise
         */

        private boolean handleKey(Key key) {
            // special operations
            if (key.isKeystroke(2, KEY_A)) {
                hIndex = 0;
                index = chars();
                return true;
            } else if (key.isKeystroke(2, KEY_C)) {
                //getContext().copyIntoClipboard(String.valueOf(buffer.toArray(), getMinIndex(), getSelectionCount()));
                return true;
            } else if (key.isKeystroke(2, KEY_V)) {
                /*if (isTextSelected()) {
                    clearSelected();
                }
                addText(getIndex(), getContext().pasteFromClipboard().toCharArray());*/
                return true;
            }

            // operations on the mapped keys
            int keyCode = key.getKeyCode();
            Consumer<Key> consumeKey = keyMapper.get(keyCode);
            if (consumeKey != null) {
                consumeKey.accept(key);
                return true;
            } else if (!unhandledKeys.contains(keyCode)) {
                // deletes the selected text
                if (isTextSelected()) {
                    removeSelectedText();
                }
                // adds the key to text
                char keyChar = key.getKeyChar();
                addText(index, keyChar);
                return true;
            }

            return false;
        }
    }
}
