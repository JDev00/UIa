package uia.physical.ui.text;

import uia.core.Font;
import uia.core.architecture.Graphic;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewText.AlignX;

import static uia.utils.TrigTable.*;

/**
 * Implementation of {@link TextRenderer}
 */

public class TextRendererV1 implements TextRenderer {

    @Override
    public float drawInline(View view, Graphic graphic, Font font, AlignX alignX, String text,
                            float x, float y, float rotation) {
        // TODO: 21/07/2023 implement text rotation

        float[] bounds = view.bounds();
        float[] bounds_s = view.boundsShape();

        char[] chars = text.toCharArray();

        float lineWidth = bounds[2] - font.getWidth('o');
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(chars, 0, chars.length);

        int ax = TextRenderer.map(alignX);

        float dx = ((ax - 1f) * lineWidth - ax * longestLine) / 2f;
        float dy = 0.75f * lineHeight;
        float rot = bounds[4] + bounds_s[4];
        float tx = rotX(dx, dy, cos(rot), sin(rot));
        float ty = rotY(dx, dy, cos(rot), sin(rot));

        graphic.drawText(chars, 0, chars.length,
                x + tx,
                y + ty,
                rotation);

        return longestLine;
    }

    @Override
    public float draw(View view, Graphic graphic, Font font, AlignX alignX, String text,
                      float x, float y, float rotation) {
        // TODO: 21/07/2023 implement text rotation

        float[] bounds = view.bounds();
        float[] bounds_s = view.boundsShape();

        char[] chars = text.toCharArray();
        int length = chars.length;

        float top = bounds[1];
        float bot = bounds[1] + bounds[3];
        float lineWidth = bounds[2] - font.getWidth('o');
        float lineHeight = font.getLineHeight();
        float longestLine = 0f;

        int ax = TextRenderer.map(alignX);

        int sol; // start of line
        int eol = -1; // end of line
        int lines = 0;

        for (int i = 0; i <= length; i++) {

            if (i == length || chars[i] == '\n') {
                sol = eol + 1;
                eol = i;

                float lineLength = font.getWidth(chars, sol, eol - sol);

                if (y + (lines + 2) * lineHeight >= top && y + (lines - 1) * lineHeight <= bot)
                    graphic.drawText(chars, sol, eol - sol,
                            x + ((ax - 1f) * lineWidth - ax * lineLength) / 2f,
                            y + (lines + 0.75f) * lineHeight,
                            0f);

                if (lineLength > longestLine) longestLine = lineLength;

                lines++;
            }
        }

        return longestLine;
    }
}