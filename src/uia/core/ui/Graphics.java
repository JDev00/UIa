package uia.core.ui;

import uia.core.*;
import uia.core.paint.Paint;
import uia.core.shape.Shape;

/**
 * Graphics ADT.
 * <br>
 * A Graphics is responsible for drawing things on the screen. It is designed to provide a set of functionalities
 * that are unrelated to the specific platform.
 * <br>
 * <br>
 * <b>Purpose</b>:
 * <br>
 * to provide a set of features, that are platform independent, to:
 * <ul>
 *     <li>draw a shape;</li>
 *     <li>draw a text;</li>
 *     <li>draw an image.</li>
 * </ul>
 * And, for each drawing operation, make it possible to modify the graphical appearance of the object
 * (depending on the drawing operation).
 */

public interface Graphics {

    /**
     * Dispose of this Graphics and releases any system resources that it is using.
     * A Graphics object cannot be used after <code>dispose</code>has been called.
     */

    void dispose();

    /**
     * Sets the current clipping area to an arbitrary clip shape.
     * This method set the user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param shape a {@link Shape} to use to set the clip; it could be null
     */

    void setClip(Shape shape);

    /**
     * Restores the previous clipping area.
     */

    void restoreClip();

    /**
     * Sets a Paint used to control the graphic properties of a Shape/text.
     * <br>
     * Every piece of Shape/text rendered after {@code setPaint(Paint)} will be affected.
     *
     * @param paint a not null {@link Paint}
     * @throws NullPointerException if {@code paint == null}
     */

    void setPaint(Paint paint);

    /**
     * Sets the Font used to display text.
     * <br>
     * Every text rendered after {@code setFont(Font)} will be affected.
     *
     * @param font a not null {@link Font}
     * @throws NullPointerException if {@code font == null}
     */

    void setFont(Font font);

    /**
     * Draws an on-the-fly shape on this Graphics.
     *
     * @param vertices an array with the shape vertices. The array shape must be: [x1,y1, x2,y2, x3,y3, ...]
     * @throws NullPointerException     if {@code vertices == null}
     * @throws IllegalArgumentException if {@code vertices doesn't follow the required array shape}
     * @apiNote drawShape(float...) is less powerful compared to {@link #drawShape(Shape)}, in fact
     * vertices don't support translation, scaling and rotation. However, vertices can be expressed in a free form
     * without value constraints.
     * @implSpec This method must draw a self-contained shape and must not interfere with the {@link #drawShape(Shape)}.
     * The first vertex must be used to close the shape.
     * @since 1.4
     */

    void drawShape(float... vertices);

    /**
     * Draws the given {@link Shape} on this Graphics.
     *
     * @param shape a not null {@link Shape} to be drawn
     * @throws NullPointerException if {@code font == null}
     */

    void drawShape(Shape shape);

    /**
     * Draws the given character array. The baseline of the first character is at position (<i>x</i>,<i>y</i>).
     *
     * @param data     a not null array of characters to draw
     * @param offset   the start offset in the data
     * @param length   the number of characters to draw
     * @param x        the <i>x</i> coordinate of the baseline of the text
     * @param y        the <i>y</i> coordinate of the baseline of the text
     * @param rotation the text's rotation in radians
     * @throws NullPointerException if {@code data == null}
     */

    void drawText(char[] data, int offset, int length, float x, float y, float rotation);

    /**
     * Draws as much of the specified image as has already been scaled to fit inside the specified rectangle.
     * The image is drawn inside the specified rectangle and is scaled if necessary.
     * Transparent pixels do not affect whatever pixels are already there.
     *
     * @param image    a not null {@link Image} to draw
     * @param x        the <i>x</i> coordinate
     * @param y        the <i>y</i> coordinate
     * @param width    the width of the rectangle
     * @param height   the height of the rectangle
     * @param rotation the image's rotation around the pivot
     * @throws NullPointerException if {@code image == null}
     */

    void drawImage(Image image, float x, float y, float width, float height, float rotation);
}
