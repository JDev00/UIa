package uia.physical.ui.component.text;

import uia.physical.ui.component.utility.ComponentUtility;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.ui.style.Style;
import uia.core.ui.ViewText;
import uia.core.rendering.Graphics;
import uia.core.rendering.font.Font;

import java.util.Objects;

/**
 * Multiline text renderer.
 */

public class MultilineTextRenderer implements TextRenderer {

    @Override
    public float draw(ViewText viewText, Graphics graphics, String text, float x, float y, float rotation) {
        Objects.requireNonNull(viewText);
        Objects.requireNonNull(graphics);
        Objects.requireNonNull(text);

        Style style = viewText.getStyle();
        TextHorizontalAlignment horizontalAlignment = style.getHorizontalTextAlignment();
        TextVerticalAlignment verticalAlignment = style.getVerticalTextAlignment();
        Font font = style.getFont();

        char[] chars = text.toCharArray();
        int length = chars.length;

        float lineWidth = viewText.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;
        float textHeight = ViewText.countLines(text) * lineHeight;

        float yAdj = TextVerticalAlignment.map(verticalAlignment)
                * (viewText.getHeight() - textHeight - 0.75f * lineHeight)
                / 2f;
        float rot = viewText.getBounds()[4];

        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(sol, eol - sol, chars);

                float xDist = TextHorizontalAlignment.map(horizontalAlignment) * (lineWidth - lineLength) / 2f;
                float yDist = (lines + 0.75f) * lineHeight + yAdj;

                graphics.drawText(chars, sol, eol - sol,
                        ComponentUtility.getPositionOnX(x, 0f, xDist, yDist, rot),
                        ComponentUtility.getPositionOnY(y, 0f, xDist, yDist, rot),
                        rotation);

                if (lineLength > longestLine) {
                    longestLine = lineLength;
                }
                lines++;
            }
        }

        return longestLine;
    }
}
