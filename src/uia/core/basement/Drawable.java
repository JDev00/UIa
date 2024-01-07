package uia.core.basement;

import uia.core.shape.Geometry;
import uia.core.paint.Paint;
import uia.core.ui.Graphic;
import uia.utility.Geometries;

import java.util.function.Consumer;

/**
 * Drawable is designed to group together all the aspects that characterise a complex graphical object
 * that can be rendered on a {@link uia.core.ui.Graphic}.
 */

public interface Drawable {

    /**
     * Set the Geometry
     *
     * @param builder        a not null {@link Consumer} used to build the geometry
     * @param inTimeBuilding true to build geometry at rendering time (for every frame)
     */

    void setGeometry(Consumer<Geometry> builder, boolean inTimeBuilding);

    /**
     * @return the Drawable {@link Geometry}
     */

    Geometry getGeometry();

    /**
     * @return the {@link Paint} object
     */

    Paint getPaint();

    /**
     * Set the Drawable position
     *
     * @param x the Drawable position on the x-axis
     * @param y the Drawable position on the y-axis
     */

    void setPosition(float x, float y);

    /**
     * Set the Drawable dimension
     *
     * @param width  the Drawable width
     * @param height the Drawable height
     */

    void setDimension(float width, float height);

    /**
     * Set the Drawable rotation
     *
     * @param radians the rotation in radians
     */

    void setRotation(float radians);

    /**
     * Draws this Drawable on the specified Graphic
     *
     * @param graphic a not null {@link Graphic}
     * @throws NullPointerException if {@code graphic == null}
     */

    void draw(Graphic graphic);

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

    /**
     * @return the Drawable rotation in radians
     */

    float getRotation();

    /**
     * Build a rounded rectangle
     *
     * @see Geometries#rect(Geometry, int, float, float, float, float, float)
     */

    static void buildRect(Geometry geometry, float width, float height, float radius) {
        Geometries.rect(geometry, Geometries.STD_VERT, radius, width / height);
    }
}
