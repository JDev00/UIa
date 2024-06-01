package uia.physical.component.text;

import uia.core.ui.Graphics;
import uia.core.ui.ViewText;

/**
 * TextRenderer ADT.
 * <br>
 * TextRenderer is responsible to display a text on a {@link Graphics}.
 */

public interface TextRenderer {

    /**
     * Draws an array of chars on the specified graphics.
     *
     * @param viewText a not null {@link ViewText}
     * @param graphics a not null {@link Graphics} used to render text
     * @param text     the text to display
     * @param x        the text position on the x-axis
     * @param y        the text position on the y-axis
     * @param rotation the text rotation in radians
     * @throws NullPointerException if {@code viewText == null || graphics == null || text == null}
     */

    float draw(ViewText viewText, Graphics graphics, String text, float x, float y, float rotation);
}
