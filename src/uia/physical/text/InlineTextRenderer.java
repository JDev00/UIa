package uia.physical.text;

import uia.core.Font;
import uia.core.ui.Graphics;
import uia.core.ui.View;
import uia.core.ui.ViewText;

import java.util.Objects;

/**
 * Single line text renderer.
 */

public class InlineTextRenderer implements TextRenderer {

    // BUG: change text rotation

    @Override
    public float draw(ViewText viewText, Graphics graphics, String text, float x, float y, float rotation) {
        Objects.requireNonNull(viewText);
        Objects.requireNonNull(graphics);
        Objects.requireNonNull(text);

        Font font = viewText.getFont();

        char[] chars = text.toCharArray();

        float lineWidth = viewText.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(0, chars.length, chars);

        float xDist = TextRenderer.map(viewText.getAlignX()) * (lineWidth - longestLine) / 2f;
        float yDist = TextRenderer.map(viewText.getAlignY()) * (viewText.getHeight() - 0.75f * lineHeight) / 2f + 0.75f * lineHeight;
        float rot = viewText.getBounds()[4];

        graphics.drawText(chars, 0, chars.length,
                View.getPositionOnX(x, 0f, xDist, yDist, rot),
                View.getPositionOnY(y, 0f, xDist, yDist, rot),
                rotation);

        return longestLine;
    }
}
