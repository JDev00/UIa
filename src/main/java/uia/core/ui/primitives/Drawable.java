package uia.core.ui.primitives;

import uia.core.rendering.geometry.Geometry;
import uia.core.rendering.Graphics;
import uia.core.ui.style.Style;

/**
 * Drawable is the smallest UI object that can be rendered on the screen.
 */

public interface Drawable {

    /**
     * Draws this Drawable on the screen.
     *
     * @param graphics a not null graphics where the Drawable is displayed
     * @throws NullPointerException if {@code graphic == null}
     */

    void draw(Graphics graphics);

    /**
     * @return the Drawable style
     */

    Style getStyle();

    /**
     * @return the Drawable geometry
     */

    Geometry getGeometry();

    /**
     * Returns the Drawable width. Note that width won't change as consequence of a rotation.
     *
     * @return the Drawable width
     */

    float getWidth();

    /**
     * Returns the Drawable height. Note that height won't change as consequence of a rotation.
     *
     * @return the Drawable height
     */

    float getHeight();
}
