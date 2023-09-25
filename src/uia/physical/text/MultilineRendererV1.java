package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphic;
import uia.core.ui.ViewText;

import static uia.utility.TrigTable.*;
import static uia.utility.TrigTable.sin;

/**
 * Implementation of {@link TextRenderer} for multi line text
 */

public class MultilineRendererV1 implements TextRenderer {

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

                // TODO: improve performance
                //if (y + (lines + 2) * lineHeight >= top && y + (lines - 1) * lineHeight <= bot) {
                float off_x = TextRenderer.map(view.getAlignX()) * (lineWidth - lineLength) / 2f;
                float off_y = (lines + 0.75f) * lineHeight + y_adj;
                float tx = rotX(off_x, off_y, cos(rot), sin(rot));
                float ty = rotY(off_x, off_y, cos(rot), sin(rot));

                graphic.drawText(chars, sol, eol - sol, x + tx, y + ty, rotation);
                //}

                if (lineLength > longestLine) longestLine = lineLength;

                lines++;
            }
        }

        return longestLine;
    }
}
