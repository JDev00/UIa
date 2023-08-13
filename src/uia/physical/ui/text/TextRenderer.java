package uia.physical.ui.text;

import uia.core.Font;
import uia.core.architecture.Graphic;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewText.AlignX;

/**
 * TextRenderer ADT.
 * <br>
 * This class is responsible to display a given text on screen.
 */

public interface TextRenderer {

    /**
     * Draw an array of chars on the given Graphic as a single line of text.
     * <br>
     * Time required: T(n);
     * <br>
     * Space required: O(1).
     *
     * @param view     a not null {@link View}
     * @param graphic  a not null {@link Graphic} used to render text
     * @param font     a not null {@link Font} used to specify text parameters
     * @param alignX   a not null {@link AlignX} used to align text along x-axis
     * @param text     a not null String to display on screen
     * @param x        the position along x-axis
     * @param y        the position along y-axis
     * @param rotation the text rotation in radians
     * @return the longest text line in pixel
     */

    float drawInline(View view, Graphic graphic, Font font, AlignX alignX, String text,
                     float x, float y, float rotation);

    /**
     * Draw an array of chars on the given Graphic.
     * <br>
     * Time required: T(n);
     * <br>
     * Space required: O(1).
     *
     * @param view     a not null {@link View}
     * @param graphic  a not null {@link Graphic} used to render text
     * @param font     a not null {@link Font} used to specify text parameters
     * @param alignX   a not null {@link AlignX} used to align text along x-axis
     * @param text     a not null String to display on screen
     * @param x        the position along x-axis
     * @param y        the position along y-axis
     * @param rotation the text rotation in radians
     * @return the longest text line in pixel
     */

    float draw(View view, Graphic graphic, Font font, AlignX alignX, String text,
               float x, float y, float rotation);

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
}
