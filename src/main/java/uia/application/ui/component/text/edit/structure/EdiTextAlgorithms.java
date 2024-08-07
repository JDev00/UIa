package uia.application.ui.component.text.edit.structure;

import uia.core.rendering.Graphics;
import uia.core.rendering.Transform;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.rendering.font.Font;
import uia.core.ui.style.Style;
import uia.core.ui.ViewText;

public final class EdiTextAlgorithms {

    private EdiTextAlgorithms() {
    }

    /**
     * Returns the next break line.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
     *
     * @param chars      a not null array of chars
     * @param length     the number of elements to scan
     * @param startIndex the scanner start position
     * @return the next break line position or {@code length}
     */

    public static int getNextBreakLine(char[] chars, int length, int startIndex) {
        int position = startIndex;
        while (position < length && chars[position] != '\n') {
            position++;
        }
        return position;
    }

    /**
     * Returns the character index covered by cursor.
     * <br>
     * Specifically, given a pointer coordinates (px, py), find the nearest character
     * and return its index.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
     *
     * @return the character index or -1
     */

    public static int getIndexForInlineText(ViewText viewText,
                                            char[] chars, int length, float pointerX, float pointerY) {
        Style style = viewText.getStyle();
        Font font = style.getFont();

        float[] bounds = viewText.getBounds();
        float heightLine = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        float y = TextVerticalAlignment.map(style.getVerticalTextAlignment())
                * (bounds[3] - heightLine) / 2f;

        if (pointerY > y && pointerY < y + heightLine) {
            float x = ax * (bounds[2] - font.getWidth(0, length, chars)) / 2f;

            float dim;
            int j = 0;
            while (j < length) {
                dim = font.getWidth(chars[j]);
                if (pointerX < x + dim / 2f) {
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
     * Returns the character index covered by the text cursor.
     * <br>
     * Specifically, given a pointer coordinates (px, py), find the nearest
     * character and return its index.
     * <br>
     * Time required: O(n);
     * <br>
     * Space required: O(1).
     *
     * @return the character index or -1
     */

    public static int getIndexForMultilineText(ViewText viewText,
                                               char[] chars, int length, float pointerX, float pointerY) {
        Style style = viewText.getStyle();
        Font font = style.getFont();
        float[] bounds = viewText.getBounds();
        float heightLine = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        float y = -viewText.getScrollValue()[1];

        int startOfLine;
        int endOfLine = -1;

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                startOfLine = endOfLine + 1;
                endOfLine = i;
                y += heightLine;
                if (pointerY > y - heightLine && pointerY < y) {
                    float x = ax * (bounds[2] - font.getWidth(startOfLine, endOfLine - startOfLine, chars)) / 2f;
                    float dim;
                    int j = startOfLine;
                    while (j < endOfLine) {
                        dim = font.getWidth(chars[j]);
                        if (pointerX < x + dim / 2f) {
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

    /**
     * Calculates the position of the box around the selected text.
     * <br>
     * This algorithm is designed to work on single line text only.
     *
     * @param view            a not null view text to highlight the selected text
     * @param highlightOffset the index, between [0, text.length()) with respect to the text,
     *                        where the selection starts
     * @return the position of the box around the text
     */

    public static float[] getSingleLineTextBoxPosition(ViewText view, int highlightOffset) {
        Style style = view.getStyle();
        Font font = style.getFont();

        char[] text = view.getText().toCharArray();
        float[] textBounds = view.getTextBounds();
        float[] bounds = view.getBounds();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        int ay = TextVerticalAlignment.map(style.getVerticalTextAlignment());
        float deltaTextX = ax * 0.5f * (bounds[2] - font.getWidth(0, text.length, text));
        float deltaTextY = ay * 0.5f * (bounds[3] - textBounds[3]);

        return new float[]{
                bounds[0]
                        + deltaTextX
                        + font.getWidth(0, highlightOffset, text)
                        - 4f * (ax - 1f),
                bounds[1] + deltaTextY
        };
    }

    /**
     * Helper function. Draws a box on single line text.
     */

    public static void drawInlineBox(ViewText view, Geometry geometry, Transform transform, Graphics graphics,
                                      int[] selectionRange, float[] boxPosition) {
        Font font = view.getStyle().getFont();

        char[] text = view.getText().toCharArray();
        int start = selectionRange[0];
        int stop = selectionRange[1];
        float width = font.getWidth(start, stop - start, text);
        float height = font.getLineHeight();

        transform
                .setTranslation(boxPosition[0] + width / 2f, boxPosition[1] + height / 2f)
                .setScale(width, height)
                .setRotation(0f);
        graphics.drawShape(transform, geometry.vertices(), geometry.toArray());
    }
}
