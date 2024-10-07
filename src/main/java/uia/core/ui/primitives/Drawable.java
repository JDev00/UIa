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
     * Returns the absolute width of the Drawable in pixels.
     * The width measurement is not affected by rotation. This means
     * that the width will be the same even when the Drawable is rotated.
     *
     * @return the absolute width of the Drawable in pixels
     */

    float getWidth();

    /**
     * Returns the absolute height of the Drawable in pixels.
     * The height measurement is not affected by rotation. This means
     * that the height will be the same even when the Drawable is rotated.
     *
     * @return the absolute height of the Drawable in pixels
     */

    float getHeight();
}
