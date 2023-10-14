package uia.physical.text;

import uia.core.ui.Graphic;
import uia.core.ui.ViewText;
import uia.core.ui.ViewText.AlignX;
import uia.core.ui.ViewText.AlignY;

/**
 * TextRenderer ADT.
 * <br>
 * This class is responsible to display a text on a {@link Graphic}.
 */

public interface TextRenderer {

    /**
     * Draw an array of chars on the given Graphic.
     *
     * @param view     a not null {@link ViewText}
     * @param graphic  a not null {@link Graphic} used to render text
     * @param text     a not null String to display on screen
     * @param x        the position along x-axis
     * @param y        the position along y-axis
     * @param rotation the text rotation in radians
     */

    float draw(ViewText view, Graphic graphic, String text, float x, float y, float rotation);

    /**
     * Map the given {@link AlignX} into an integer.
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
     * Map the given {@link AlignY} into an integer.
     *
     * @return a number between {0,1} where: TOP = 0 and CENTER = 1
     */

    static int map(AlignY alignY) {
        return AlignY.TOP.equals(alignY) ? 0 : 1;
    }
}
