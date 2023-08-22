package uia.physical.text;

import uia.core.Font;
import uia.core.basement.Graphic;
import uia.core.ui.ViewText;

import static uia.utils.TrigTable.*;

/**
 * Implementation of {@link TextRenderer}
 */

public class TextRendererV1 implements TextRenderer {

    @Override
    public float drawInline(ViewText view, Graphic graphic, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        char[] chars = text.toCharArray();

        float[] bounds = view.bounds();

        float lineWidth = bounds[2];
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(0, chars.length, chars);

        float off_x = TextRenderer.map(view.getAlignX()) * (lineWidth - longestLine) / 2f;
        float off_y = TextRenderer.map(view.getAlignY()) * (view.desc()[1] - 0.75f * lineHeight) / 2f + 0.75f * lineHeight;
        float rot = bounds[4];

        graphic.drawText(chars, 0, chars.length,
                x + rotX(off_x, off_y, cos(rot), sin(rot)),
                y + rotY(off_x, off_y, cos(rot), sin(rot)),
                rotation);

        return longestLine;
    }

    @Override
    public float draw(ViewText view, Graphic graphic, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        float[] bounds = view.bounds();
        float[] desc = view.desc();

        char[] chars = text.toCharArray();
        int length = chars.length;

        //float top = bounds[1];
        //float bot = bounds[1] + bounds[3];
        float lineWidth = desc[0];
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;
        float textHeight = ViewText.countLines(text) * lineHeight;


        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        float y_adj = TextRenderer.map(view.getAlignY()) * (desc[1] - textHeight - 0.75f * lineHeight) / 2f;
        float rot = bounds[4];

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(sol, eol - sol, chars);

                // TODO: 16/08/2023 improve performance
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
