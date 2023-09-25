package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.physical.Component;

import static uia.utility.TrigTable.*;

/**
 * Implementation of {@link TextRenderer} for single line text
 */

public class InlineRendererV1 implements TextRenderer {

    @Override
    public float draw(ViewText view, Graphic graphic, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        char[] chars = text.toCharArray();

        float[] bounds = view.bounds();

        float lineWidth = bounds[2];
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(0, chars.length, chars);

        float off_x = TextRenderer.map(view.getAlignX()) * (lineWidth - longestLine) / 2f;
        float off_y = TextRenderer.map(view.getAlignY()) * (view.getHeight() - 0.75f * lineHeight) / 2f + 0.75f * lineHeight;
        float rot = bounds[4];

        graphic.drawText(chars, 0, chars.length,
                x + rotX(off_x, off_y, cos(rot), sin(rot)),
                y + rotY(off_x, off_y, cos(rot), sin(rot)),
                rotation);

        return longestLine;
    }
}
