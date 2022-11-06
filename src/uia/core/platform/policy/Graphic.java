package uia.core.platform.policy;

import uia.core.platform.independent.Font;
import uia.core.platform.independent.Image;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.independent.paint.Paint;

/**
 * Graphic ADT.
 * <br>
 * The native object responsible for drawing things on screen, is encapsulated inside Graphic;
 * the result is the possibility to access common methods from different platforms or different implementations.
 */

public interface Graphic {

    /**
     * Encapsulate the native Graphic
     *
     * @param o a not null object
     */

    void setNative(Object o);

    /**
     * @return the native Graphic
     */

    Object getNative();

    /**
     * Dispose of this Graphic and releases any system resources that it is using.
     * A <code>Graphic</code> object cannot be used after <code>dispose</code>has been called.
     * Although the finalization process of the garbage collector
     * also disposes of the same system resources, it is preferable
     * to manually free the associated resources by calling this
     * method rather than to rely on a finalization process which
     * may not run to completion for a long period of time.
     */

    void dispose();

    /**
     * Dispose of this Graphic once it is no longer referenced
     *
     * @see #dispose
     */

    void finalize();

    /**
     * Set the current clipping area to an arbitrary clip shape.
     * This method set the user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param s a not null {@link Shape} to use to set the clip
     */

    void setClip(Shape s);

    /**
     * Restore the previous clipping area
     */

    void restoreClip();

    /**
     * Return the current clipping area.
     * This method returns the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using <code>setClip(null)</code>, this method returns <code>null</code>.
     *
     * @return a {@link Shape} representing the current clipping area
     */

    Shape getClip();

    /**
     * Return true if the specified rectangular area might intersect the current clipping area.
     * This method may use an algorithm that calculates a result quickly
     * but which sometimes might return true even if the specified
     * rectangular area does not intersect the clipping area.
     * The specific algorithm employed may thus trade off accuracy for
     * speed, but it will never return false unless it can guarantee
     * that the specified rectangular area does not intersect the
     * current clipping area.
     * The clipping area used by this method can represent the
     * intersection of the user clip as specified through the clip
     * methods of this graphics context as well as the clipping
     * associated with the device or image bounds and window visibility.
     *
     * @param x      the x coordinate of the rectangle to test against the clip
     * @param y      the y coordinate of the rectangle to test against the clip
     * @param width  the width of the rectangle to test against the clip
     * @param height the height of the rectangle to test against the clip
     * @return <code>true</code> if the specified rectangle intersects
     * the bounds of the current clip; <code>false</code> otherwise.
     */

    boolean hitClip(int x, int y, int width, int height);

    /**
     * Return the bounding rectangle of the current clipping area.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using <code>setClip(null)</code>, this method returns <code>null</code>.
     *
     * @return the bounding rectangle of the current clipping area, or <code>null</code> if no clip is set
     */

    int[] getClipBounds();

    /**
     * Return the bounding rectangle of the current clipping area.
     * This method differs from {@link #getClipBounds()} in that an existing
     * rectangle is used instead of allocating a new one.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using <code>setClip(null)</code>, this method returns the
     * specified <code>Rectangle</code>.
     *
     * @param r the rectangle where the current clipping area is copied to. Any current values in this rectangle are overwritten.
     * @return the bounding rectangle of the current clipping area
     */

    int[] getClipBounds(int[] r);

    /**
     * Set the Font used to display text
     *
     * @param font a not null {@link Font}
     */

    void setFont(Font font);

    /**
     * @return the current {@link Font}
     */

    Font getFont();

    /**
     * Set a Paint used to control graphic properties of a Shape.
     * <br>
     * Every Shape rendered after {@code setPaint(Paint)} will be affected by the set Paint.
     *
     * @param paint a not null {@link Paint}
     */

    void setPaint(Paint paint);

    /**
     * @return the current {@link Paint}
     */

    Paint getPaint();

    /**
     * Add a new start vertex. A start vertex is used to start a new piece of geometry.
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    void vertexBegin(float x, float y);

    /**
     * Add a new vertex. A vertex is piece of geometry.
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
     * Draw a line between the points <code>(x1,y1)</code> and <code>(x2,y2)</code>
     *
     * @param x1 the first point's <i>x</i> coordinate
     * @param y1 the first point's <i>y</i> coordinate
     * @param x2 the second point's <i>x</i> coordinate
     * @param y2 the second point's <i>y</i> coordinate
     */

    void drawLine(float x1, float y1, float x2, float y2);

    /**
     * Draw the given String.
     * The baseline of the leftmost character is at position (<i>x</i>,<i>y</i>).
     *
     * @param str the string to be drawn
     * @param x   the <i>x</i> coordinate
     * @param y   the <i>y</i> coordinate
     */

    void drawText(String str, float x, float y);

    /**
     * Draw the given character array.
     * The baseline of the first character is at position (<i>x</i>,<i>y</i>).
     *
     * @param data   the array of characters to be drawn
     * @param offset the start offset in the data
     * @param length the number of characters to be drawn
     * @param x      the <i>x</i> coordinate of the baseline of the text
     * @param y      the <i>y</i> coordinate of the baseline of the text
     * @throws IndexOutOfBoundsException if <code>offset</code> or
     *                                   <code>length</code>is less than zero, or
     *                                   <code>offset+length</code> is greater than the length of the
     *                                   <code>data</code> array.
     */

    void drawText(char[] data, int offset, int length, float x, float y);

    /**
     * Draw as much of the specified image as is currently available.
     * The image is drawn with its top-left corner at (<i>x</i>,<i>y</i>).
     * Transparent pixels in the image do not affect whatever pixels are already there.
     *
     * @param img      a not null {@link Image} to draw
     * @param x        the <i>x</i> coordinate
     * @param y        the <i>y</i> coordinate
     * @param rotation the image's rotation around the pivot
     */

    void drawImage(Image img, float x, float y, float rotation);

    /**
     * Draw as much of the specified image as has already been scaled to fit inside the specified rectangle.
     * The image is drawn inside the specified rectangle and is scaled if
     * necessary. Transparent pixels do not affect whatever pixels are already there.
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
