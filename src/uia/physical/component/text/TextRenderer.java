package uia.physical.component.text;

import uia.core.ui.Graphics;
import uia.core.ui.ViewText;
import uia.core.ui.ViewText.AlignX;
import uia.core.ui.ViewText.AlignY;

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

    /**
     * Maps the specified {@link AlignX} value to an integer.
     *
     * @return a number between {0,1,2} where: LEFT = 0, CENTER = 1 and RIGHT = 2
     */

    static int map(AlignX alignX) {
        switch (alignX) {
            case LEFT:
                return 0;
            case CENTER:
                return 1;
            default:
                return 2;
        }
    }

    /**
     * Maps the specified {@link AlignY} value to an integer.
     *
     * @return a number between {0, 1} where: TOP = 0 and CENTER = 1
     */

    static int map(AlignY alignY) {
        return AlignY.TOP.equals(alignY) ? 0 : 1;
    }
}
