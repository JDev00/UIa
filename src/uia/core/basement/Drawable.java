package uia.core.basement;

import uia.core.Geometry;
import uia.core.Paint;
import uia.core.ui.Graphic;
import uia.utility.GeometryFactory;

import java.util.function.Consumer;

/**
 * Drawable is designed to group together all the aspects that characterise a complex graphical object
 * that can be rendered on a {@link uia.core.ui.Graphic}.
 */

public interface Drawable {

    /**
     * @return the {@link Paint} object
     */

    Paint getPaint();

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
     * Make this Drawable visible or not visible.
     *
     * @param visible true to set this Drawable visible
     */

    void setVisible(boolean visible);

    /**
     * @return true if this Drawable is visible
     */

    boolean isVisible();

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
     * @see GeometryFactory#rect(Geometry, int, float, float, float, float, float)
     */

    static void buildRect(Geometry geometry, float width, float height, float radius) {
        GeometryFactory.rect(geometry, GeometryFactory.STD_VERT, radius, width / height);
    }
}
