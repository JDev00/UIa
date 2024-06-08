package uia.physical.component.text;

import uia.physical.component.utility.ComponentUtility;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.ui.style.Style;
import uia.core.ui.ViewText;
import uia.core.ui.Graphics;
import uia.core.font.Font;

import java.util.Objects;

/**
 * Single line text renderer.
 */

public class InlineTextRenderer implements TextRenderer {

    // TODO: BUG - change text rotation
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

        float lineWidth = viewText.getWidth();
        float lineHeight = font.getLineHeight();
        float longestLine = font.getWidth(0, chars.length, chars);

        float xDist = TextHorizontalAlignment.map(horizontalAlignment) * (lineWidth - longestLine) / 2f;
        float yDist = TextVerticalAlignment.map(verticalAlignment) * (viewText.getHeight() - 0.75f * lineHeight) / 2f + 0.75f * lineHeight;
        float rot = viewText.getBounds()[4];

        graphics.drawText(chars, 0, chars.length,
                ComponentUtility.getPositionOnX(x, 0f, xDist, yDist, rot),
                ComponentUtility.getPositionOnY(y, 0f, xDist, yDist, rot),
                rotation);

        return longestLine;
    }
}
