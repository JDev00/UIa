package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.core.ui.ViewText;

/**
 * Multi line text renderer
 */

public class MultilineRendererV1 implements TextRenderer {

    @Override
    public float draw(ViewText view, Graphics graphics, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        char[] chars = text.toCharArray();
        int length = chars.length;

        float lineWidth = view.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;
        float textHeight = ViewText.countLines(text) * lineHeight;

        float y_adj = TextRenderer.map(view.getAlignY()) * (view.getHeight() - textHeight - 0.75f * lineHeight) / 2f;
        float rot = view.getBounds()[4];

        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(sol, eol - sol, chars);

                float xDist = TextRenderer.map(view.getAlignX()) * (lineWidth - lineLength) / 2f;
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
