package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.ViewText;

import static uia.utility.TrigTable.*;
import static uia.utility.TrigTable.sin;

/**
 * Multi line text renderer
 */

public class MultilineRendererV1 implements TextRenderer {

    // TODO: improve performance
    // TODO: improve code
    @Override
    public float draw(ViewText view, Graphic graphic, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        float[] bounds = view.bounds();

        char[] chars = text.toCharArray();
        int length = chars.length;

        float lineWidth = view.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;
        float textHeight = ViewText.countLines(text) * lineHeight;


        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        float y_adj = TextRenderer.map(view.getAlignY()) * (view.getHeight() - textHeight - 0.75f * lineHeight) / 2f;
        float rot = bounds[4];

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(sol, eol - sol, chars);

                //if (y + (lines + 2) * lineHeight >= top && y + (lines - 1) * lineHeight <= bot) {
                float off_x = TextRenderer.map(view.getAlignX()) * (lineWidth - lineLength) / 2f;
                float off_y = (lines + 0.75f) * lineHeight + y_adj;
                float xDist = rotX(off_x, off_y, cos(rot), sin(rot));
                float yDist = rotY(off_x, off_y, cos(rot), sin(rot));

                graphic.drawText(chars, sol, eol - sol,
                        View.getPositionOnX(x, 0f, xDist, yDist, rot),
                        View.getPositionOnY(y, 0f, xDist, yDist, rot),
                        rotation);
                //}

                if (lineLength > longestLine) longestLine = lineLength;

                lines++;
            }
        }

        return longestLine;
    }
}
