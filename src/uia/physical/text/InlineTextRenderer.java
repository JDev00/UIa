package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.core.ui.ViewText;

/**
 * Single line text renderer.
 */

public class InlineTextRenderer implements TextRenderer {

    // BUG: change text rotation

    @Override
    public float draw(ViewText view, Graphics graphics, String text, float x, float y, float rotation) {
        Font font = view.getFont();

        char[] chars = text.toCharArray();

        float lineWidth = view.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(0, chars.length, chars);

        float xDist = TextRenderer.map(view.getAlignX()) * (lineWidth - longestLine) / 2f;
        float yDist = TextRenderer.map(view.getAlignY()) * (view.getHeight() - 0.75f * lineHeight) / 2f + 0.75f * lineHeight;
        float rot = view.getBounds()[4];

        graphics.drawText(chars, 0, chars.length,
                View.getPositionOnX(x, 0f, xDist, yDist, rot),
                View.getPositionOnY(y, 0f, xDist, yDist, rot),
                rotation);

        return longestLine;
    }
}
