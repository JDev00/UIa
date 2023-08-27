package uia.core.basement;

import uia.core.Font;
import uia.core.Geom;
import uia.core.Image;
import uia.core.Paint;

/**
 * Graphic ADT.
 * <br>
 * A Graphic has the responsibility to draw things on screen. As consequence of the fact that the Graphic class is a bridge,
 * the native object is encapsulated inside it. The result is the possibility to access common methods from different
 * platforms or different implementations.
 */

public interface Graphic {

    /**
     * Set the native objects
     *
     * @param objects a not null array of objects
     */

    void setNative(Object... objects);

    /**
     * @return the native object or null according to the implementation
     */

    Object getNative();

    /**
     * Dispose of this Graphic and releases any system resources that it is using.
     * A <code>Graphic</code> object cannot be used after <code>dispose</code>has been called.
     */

    void dispose();

    /**
     * Set the current clipping area to an arbitrary clip shape.
     * This method set the user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param geom a {@link Geom} to use to set the clip; it could be null
     */

    void setClip(Geom geom);

    /**
     * Restore the previous clipping area
     */

    void restoreClip();

    /**
     * Set the Font used to display text
     *
     * @param font a not null {@link Font}
     */

    void setFont(Font font);

    /**
     * Set a Paint used to control graphic properties of geometry.
     * <br>
     * Every piece of geometry rendered after {@code setPaint(Paint)} will be affected by the set Paint.
     *
     * @param paint a not null {@link Paint}
     */

    void setPaint(Paint paint);

    /**
     * Start a new piece of geometry.
     * <br>
     * <b>Call this method to initialize che creation of a new shape.</b>
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    void openShape(float x, float y);

    /**
     * Add a new piece of geometry.
     * <br>
     * <b>It must be called after {@link #openShape} and before {@link #closeShape}</b>
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    void vertex(float x, float y);

    /**
     * Call this method to finalize the creation of a shape.
     * Call it after {@link #vertex(float, float)}.
     */

    void closeShape();

    /**
     * Draw the given {@link Geom} object on this Graphic.
     *
     * @param geom a not null {@link Geom} object
     */

    void drawGeometry(Geom geom);

    /**
     * Draw the given character array. The baseline of the first character is at position (<i>x</i>,<i>y</i>).
     *
     * @param data     a not null array of characters to draw
     * @param offset   the start offset in the data
     * @param length   the number of characters to draw
     * @param x        the <i>x</i> coordinate of the baseline of the text
     * @param y        the <i>y</i> coordinate of the baseline of the text
     * @param rotation the text's rotation in radians
     */

    void drawText(char[] data, int offset, int length, float x, float y, float rotation);

    /**
     * Draw as much of the specified image as has already been scaled to fit inside the specified rectangle.
     * The image is drawn inside the specified rectangle and is scaled if necessary.
     * Transparent pixels do not affect whatever pixels are already there.
     *
     * @param img      a not null {@link Image} to draw
     * @param x        the <i>x</i> coordinate
     * @param y        the <i>y</i> coordinate
     * @param width    the width of the rectangle
     * @param height   the height of the rectangle
     * @param rotation the image's rotation around the pivot
     */

    void drawImage(Image img, float x, float y, float width, float height, float rotation);
}
