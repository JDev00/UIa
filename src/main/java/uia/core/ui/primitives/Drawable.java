package uia.core.ui.primitives;

import uia.core.rendering.geometry.Geometry;
import uia.core.rendering.Graphics;
import uia.core.ui.style.Style;

/**
 * Drawable is designed to group together all the aspects that characterise a complex graphical object
 * that can be rendered on a {@link Graphics}.
 */

public interface Drawable {

    /**
     * @return the Drawable style
     */

    Style getStyle();

    /**
     * @return the Drawable geometry
     */

    Geometry getGeometry();

    /**
     * Draws this Drawable on the specified Graphic
     *
     * @param graphics a not null {@link Graphics}
     * @throws NullPointerException if {@code graphic == null}
     */

    void draw(Graphics graphics);

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
