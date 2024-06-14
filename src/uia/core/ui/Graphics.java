package uia.core.ui;

import uia.core.ui.primitives.shape.Transform;
import uia.core.ui.primitives.color.Color;
import uia.core.ui.primitives.font.Font;
import uia.core.ui.primitives.Image;

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
 * being rendered.
 */

public interface Graphics {

    /**
     * Dispose of this Graphics and releases any system resources that it is using.
     * A Graphics object cannot be used after <code>dispose</code>has been called.
     */

    void dispose();

    // shape

    /**
     * Sets the width of the line used to display the shape border.
     * <br>
     * <i>Every Shape rendered after this method will be affected.</i>
     *
     * @param lineWidth the line width greater than or equal to zero
     * @return this Graphics
     * @throws IllegalArgumentException if {@code lineWidth < 0}
     * @since 1.6.0
     */

    Graphics setShapeBorderWidth(float lineWidth);

    /**
     * Sets a Color object that will be used to paint the shapes rendered after this method is called.
     * <br>
     * <i>Every Shape rendered after this method will be affected.</i>
     *
     * @param color the Color used to paint shapes; a null color is ignored
     * @return this Graphics
     * @since 1.6.0
     */

    Graphics setShapeColor(Color color);

    /**
     * Sets a Color object that will be used to paint the shapes rendered after this method is called.
     * <br>
     * <i>Every Shape rendered after this method will be affected.</i>
     *
     * @param color       the Color used to paint shapes; a null color is ignored
     * @param borderColor the Color used to paint the shape border; a null color is ignored
     * @return this Graphics
     * @since 1.6.0
     */

    Graphics setShapeColor(Color color, Color borderColor);

    /**
     * Draws a shape on this Graphics.
     *
     * @param transform the shape transformation; it could be null
     * @param vertices  the shape vertices. The array shape must be: [x1,y1, x2,y2, x3,y3, ...]
     * @return this Graphics
     * @throws NullPointerException     if {@code vertices == null}
     * @throws IllegalArgumentException if {@code 'vertices' doesn't follow the required array shape}
     * @since 1.6.0
     */

    Graphics drawShape(Transform transform, float... vertices);

    /**
     * Sets the current clipping area to an arbitrary clip shape.
     * This method set the user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param transform the shape transformation; it could be null
     * @param vertices  the shape vertices. The array shape must be: [x1,y1, x2,y2, x3,y3, ...]
     * @return this Graphics
     * @throws NullPointerException     if {@code vertices == null}
     * @throws IllegalArgumentException if {@code 'vertices' doesn't follow the required array shape}
     * @since 1.6.0
     */

    Graphics setClip(Transform transform, float... vertices);

    /**
     * Restores the previous clipping area.
     */

    void restoreClip();

    // text

    /**
     * Sets the Font used to display text.
     * <br>
     * <i>Every text rendered after {@code setFont(Font)} will be affected.</i>
     *
     * @param font a not null {@link Font}
     * @return this Graphics
     * @throws NullPointerException if {@code font == null}
     */

    Graphics setFont(Font font);

    /**
     * Sets a Color object that will be used to color text after this method is called.
     * <br>
     * <i>Every text rendered after this method will be affected.</i>
     *
     * @param color the Color used to paint text; a null color is ignored
     * @return this Graphics
     * @since 1.6.0
     */

    Graphics setTextColor(Color color);

    /**
     * Draws the given character array. The baseline of the first character
     * is at position (<i>x</i>,<i>y</i>).
     *
     * @param data     a not null array of characters to draw
     * @param offset   the start offset in the data
     * @param length   the number of characters to draw
     * @param x        the <i>x</i> coordinate of the baseline of the text
     * @param y        the <i>y</i> coordinate of the baseline of the text
     * @param rotation the text's rotation in radians
     * @return this Graphics
     * @throws NullPointerException if {@code data == null}
     */

    Graphics drawText(char[] data, int offset, int length, float x, float y, float rotation);

    // image

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
     * @return this Graphics
     * @throws NullPointerException if {@code image == null}
     */

    Graphics drawImage(Transform transform, Image image, float x, float y, float width, float height, float rotation);
}
