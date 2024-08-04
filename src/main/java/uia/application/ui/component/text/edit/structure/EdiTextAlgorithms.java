package uia.application.ui.component.text.edit.structure;

import uia.core.ui.style.TextHorizontalAlignment;
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

    public static int getIndexForMultilineText(ViewText viewText, char[] chars, int length, float mx, float my) {
        Style style = viewText.getStyle();
        Font font = style.getFont();
        float[] bounds = viewText.getBounds();
        float heightLine = font.getLineHeight();

        int ax = TextHorizontalAlignment.map(style.getHorizontalTextAlignment());
        float y = -viewText.getScrollValue()[1];

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
}
