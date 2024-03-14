package uia.physical.component.text;

import uia.core.Font;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.core.ui.ViewText;

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

        Font font = viewText.getFont();

        char[] chars = text.toCharArray();
        int length = chars.length;

        float lineWidth = viewText.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;
        float textHeight = ViewText.countLines(text) * lineHeight;

        float y_adj = TextRenderer.map(viewText.getAlignY()) * (viewText.getHeight() - textHeight - 0.75f * lineHeight) / 2f;
        float rot = viewText.getBounds()[4];

        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(sol, eol - sol, chars);

                float xDist = TextRenderer.map(viewText.getAlignX()) * (lineWidth - lineLength) / 2f;
                float yDist = (lines + 0.75f) * lineHeight + y_adj;

                graphics.drawText(chars, sol, eol - sol,
                        View.getPositionOnX(x, 0f, xDist, yDist, rot),
                        View.getPositionOnY(y, 0f, xDist, yDist, rot),
                        rotation);

                if (lineLength > longestLine) longestLine = lineLength;

                lines++;
            }
        }

        return longestLine;
    }
}
