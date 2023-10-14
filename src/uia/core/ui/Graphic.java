package uia.core.ui;

import uia.core.*;

/**
 * Graphic ADT.
 * <br>
 * A Graphic is responsible for drawing things on the screen. It is designed to provide a set of functionalities
 * that are unrelated to the specific platform. Eventually, it is possible to access the native Graphic
 * through {@link #getNative()}.
 */

public interface Graphic {

    /**
     * Set the native objects
     *
     * @param nativeObject a not null native objects
     */

    void setNative(Object nativeObject);

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
     * @param shape a {@link Shape} to use to set the clip; it could be null
     */

    void setClip(Shape shape);

    /**
     * Restore the previous clipping area
     */

    void restoreClip();

    /**
     * Set a Paint used to control the graphic properties of a Shape/text.
     * <br>
     * Every piece of Shape/text rendered after {@code setPaint(Paint)} will be affected.
     *
     * @param paint a not null {@link Paint}
     */

    void setPaint(Paint paint);

    /**
     * Set the Font used to display text.
     * <br>
     * Every text rendered after {@code setFont(Font)} will be affected.
     *
     * @param font a not null {@link Font}
     */

    void setFont(Font font);

    /**
     * Draw the given {@link Shape} on this Graphic.
     *
     * @param shape a not null {@link Shape} to be drawn
     */

    void drawShape(Shape shape);

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
     * @param image    a not null {@link Image} to draw
     * @param x        the <i>x</i> coordinate
     * @param y        the <i>y</i> coordinate
     * @param width    the width of the rectangle
     * @param height   the height of the rectangle
     * @param rotation the image's rotation around the pivot
     */

    void drawImage(Image image, float x, float y, float width, float height, float rotation);
}
