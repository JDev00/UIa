package uia.core.policy;

/**
 * Render ADT.
 * <br>
 * <b>Since Render is a bridge, the native object responsible for drawing things on screen, is encapsulated
 * inside it. The result is the possibility to access common methods from different platforms
 * or different implementations.</b>
 */

public interface Render extends Bundle {

    /**
     * Dispose of this render and releases any system resources that it is using.
     * A <code>Render</code> object cannot be used after <code>dispose</code>has been called.
     * When a Java program runs, a large number of <code>Render</code>
     * objects can be created within a short time frame.
     * Although the finalization process of the garbage collector
     * also disposes of the same system resources, it is preferable
     * to manually free the associated resources by calling this
     * method rather than to rely on a finalization process which
     * may not run to completion for a long period of time.
     * Render objects which are provided as arguments to the <code>paint</code> and <code>update</code> methods
     * of components are automatically released by the system when
     * those methods return. For efficiency, programmers should call <code>dispose</code> when finished using
     * a <code>Render</code> object only if it was created directly from a component or another <code>Graphics</code> object.
     */

    void dispose();

    /**
     * Dispose of this render once it is no longer referenced
     *
     * @see #dispose
     */

    void finalize();

    /**
     * Translate the origin of the <code>Graphics2D</code> context to the
     * point (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system.
     * Modifies the <code>Graphics2D</code> context so that its new origin
     * corresponds to the point (<i>x</i>,&nbsp;<i>y</i>) in the
     * <code>Graphics2D</code> context's former coordinate system.  All
     * coordinates used in subsequent rendering operations on this graphics
     * context are relative to this new origin.
     *
     * @param x the specified x coordinate
     * @param y the specified y coordinate
     */

    void translate(int x, int y);

    /**
     * Concatenates the current
     * <code>Graphics2D</code> <code>Transform</code>
     * with a translation transform.
     * Subsequent rendering is translated by the specified
     * distance relative to the previous position.
     * This is equivalent to calling transform(T), where T is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   1    0    tx  ]
     *          [   0    1    ty  ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param tx the distance to translate along the x-axis
     * @param ty the distance to translate along the y-axis
     */

    void translate(double tx, double ty);

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a rotation transform.
     * Subsequent rendering is rotated by the specified radians relative
     * to the previous origin.
     * This is equivalent to calling <code>transform(R)</code>, where R is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   cos(theta)    -sin(theta)    0   ]
     *          [   sin(theta)     cos(theta)    0   ]
     *          [       0              0         1   ]
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     *
     * @param theta the angle of rotation in radians
     */

    void rotate(double theta);

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a translated rotation
     * transform.  Subsequent rendering is transformed by a transform
     * which is constructed by translating to the specified location,
     * rotating by the specified radians, and translating back by the same
     * amount as the original translation.  This is equivalent to the
     * following sequence of calls:
     * <pre>
     *          translate(x, y);
     *          rotate(theta);
     *          translate(-x, -y);
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     *
     * @param theta the angle of rotation in radians
     * @param x     the x coordinate of the origin of the rotation
     * @param y     the y coordinate of the origin of the rotation
     */

    void rotate(double theta, double x, double y);

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a scaling transformation
     * Subsequent rendering is resized according to the specified scaling
     * factors relative to the previous scaling.
     * This is equivalent to calling <code>transform(S)</code>, where S is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   sx   0    0   ]
     *          [   0    sy   0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param sx the amount by which X coordinates in subsequent
     *           rendering operations are multiplied relative to previous
     *           rendering operations.
     * @param sy the amount by which Y coordinates in subsequent
     *           rendering operations are multiplied relative to previous
     *           rendering operations.
     */

    void scale(double sx, double sy);

    /**
     * Concatenates the current <code>Graphics2D</code>
     * <code>Transform</code> with a shearing transform.
     * Subsequent renderings are sheared by the specified
     * multiplier relative to the previous position.
     * This is equivalent to calling <code>transform(SH)</code>, where SH
     * is an <code>AffineTransform</code> represented by the following
     * matrix:
     * <pre>
     *          [   1   shx   0   ]
     *          [  shy   1    0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param shx the multiplier by which coordinates are shifted in
     *            the positive X axis direction as a function of their Y coordinate
     * @param shy the multiplier by which coordinates are shifted in
     *            the positive Y axis direction as a function of their X coordinate
     */

    void shear(double shx, double shy);

    /*
     *
     * Clip
     *
     */

    /**
     * Set the clip region to the intersection of the current clipping region and <code>s</code>.
     *
     * @param s a not null {@link Path} to intersect with the current clipping region
     * @see Render#setClip(Path)
     */

    void clip(Path s);

    /**
     * Set the current clipping area to an arbitrary clip shape.
     * Not all objects that implement the <code>Figure</code> can be used to set the clip.
     * The only <code>Figure</code> objects that are guaranteed to be
     * supported are <code>Figures</code> objects that are
     * obtained via the <code>getClip</code> method and via
     * <code>Rectangle</code> objects. This method set the
     * user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param s a not null {@link Path} to use to set the clip
     */

    void setClip(Path s);

    /**
     * Return the current clipping area.
     * This method returns the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using <code>setClip(null)</code>, this method returns <code>null</code>.
     *
     * @return a {@link Path} representing the current clipping area
     */

    Path getClip();

    /**
     * Return true if the specified rectangular area might intersect the current clipping area.
     * The coordinates of the specified rectangular area are in the
     * user coordinate space and are relative to the coordinate
     * system origin of this graphics context.
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
     * The coordinates in the rectangle are relative to the coordinate system origin of this render.
     *
     * @return the bounding rectangle of the current clipping area, or <code>null</code> if no clip is set.
     */

    int[] getClipBounds();

    /**
     * Return the bounding rectangle of the current clipping area.
     * The coordinates in the rectangle are relative to the coordinate
     * system origin of this graphics context.  This method differs
     * from {@link #getClipBounds() getClipBounds} in that an existing
     * rectangle is used instead of allocating a new one.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using <code>setClip(null)</code>, this method returns the
     * specified <code>Rectangle</code>.
     *
     * @param r the rectangle where the current clipping area is copied to. Any current values in this rectangle are overwritten.
     * @return the bounding rectangle of the current clipping area.
     */

    int[] getClipBounds(int[] r);

    /*
     *
     * Font
     *
     */

    /**
     * Set the font used to render text
     *
     * @param font a not null {@link Font}
     */

    void setFont(Font font);

    /**
     * @return the current {@link Font}
     */

    Font getFont();

    /*
     *
     * Paint
     *
     */

    /**
     * Set the render {@link Paint}
     */

    void setPaint(Paint paint);

    /**
     * @return the {@link Paint} currently used
     */

    Paint getPaint();

    /*
     *
     * Geometry
     *
     */

    /**
     * Fill the specified Path using the current paint
     *
     * @param path a not null Path to draw
     */

    void draw(Path path);

    /**
     * Draw a line, using the current color, between the points <code>(x1,&nbsp;y1)</code> and <code>(x2,&nbsp;y2)</code>
     *
     * @param x1 the first point's <i>x</i> coordinate
     * @param y1 the first point's <i>y</i> coordinate
     * @param x2 the second point's <i>x</i> coordinate
     * @param y2 the second point's <i>y</i> coordinate
     */

    void drawLine(int x1, int y1, int x2, int y2);

    /**
     * Fill the specified rectangle.
     * The left and right edges of the rectangle are at <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>.
     * The top and bottom edges are at <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     * The resulting rectangle covers an area <code>width</code> pixels wide by <code>height</code> pixels tall.
     * The rectangle is filled using the render current color.
     *
     * @param x      the <i>x</i> coordinate of the rectangle to be filled
     * @param y      the <i>y</i> coordinate of the rectangle to be filled
     * @param width  the width of the rectangle to be filled
     * @param height the height of the rectangle to be fille
     */

    void drawRect(int x, int y, int width, int height);

    /**
     * Fill the specified rounded corner rectangle with the current color.
     * The left and right edges of the rectangle
     * are at <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>,
     * respectively. The top and bottom edges of the rectangle are at
     *
     * <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     *
     * @param x         the <i>x</i> coordinate of the rectangle to be filled
     * @param y         the <i>y</i> coordinate of the rectangle to be filled
     * @param width     the width of the rectangle to be filled
     * @param height    the height of the rectangle to be filled
     * @param arcWidth  the horizontal diameter of the arc at the four corners
     * @param arcHeight the vertical diameter of the arc at the four corners
     */

    void drawRoundRect(int x, int y, int width, int height,
                       int arcWidth, int arcHeight);

    /**
     * Fill an oval bounded by the specified rectangle with the current color
     *
     * @param x      the <i>x</i> coordinate of the upper left corner of the oval to be filled
     * @param y      the <i>y</i> coordinate of the upper left corner of the oval to be filled
     * @param width  the width of the oval to be filled
     * @param height the height of the oval to be fille
     */

    void drawOval(int x, int y, int width, int height);

    /**
     * Fill a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at <code>startAngle</code> and extends
     * for <code>arcAngle</code> degrees.
     * Angles are interpreted such that 0&nbsp;degrees
     * is at the 3&nbsp;o'clock position.
     * A positive value indicates a counter-clockwise rotation
     * while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin
     * is (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the
     *
     * <code>width</code> and <code>height</code> arguments.
     * The resulting arc covers an area
     *
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the
     * start and end of the arc segment will be skewed farther along the
     * longer axis of the bounds.
     *
     * @param x          the <i>x</i> coordinate of the upper-left corner of the arc to be filled
     * @param y          the <i>y</i>  coordinate of the upper-left corner of the arc to be filled
     * @param width      the width of the arc to be filled
     * @param height     the height of the arc to be filled
     * @param startAngle the beginning angle
     * @param arcAngle   the angular extent of the arc, relative to the start angle
     */

    void drawArc(int x, int y, int width, int height,
                 int startAngle, int arcAngle);

    /**
     * Fill a closed polygon defined by arrays of <i>x</i> and <i>y</i> coordinates.
     * This method draws the polygon defined by <code>nPoint</code> line
     * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code>
     * line segments are line segments from <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
     * The figure is automatically closed by drawing a line connecting
     * the final point to the first point, if those points are different.
     * The area inside the polygon is defined using an
     * even-odd fill rule, also known as the alternating rule.
     *
     * @param xPoints an array of <code>x</code> coordinates
     * @param yPoints an array of <code>y</code> coordinates
     * @param nPoints the total number of points
     */

    void drawPolygon(int[] xPoints, int[] yPoints,
                     int nPoints);

    /**
     * Draw the given String, using current font and color. The baseline of the
     * leftmost character is at position (<i>x</i>,&nbsp;<i>y</i>).
     *
     * @param str the string to be drawn
     * @param x   the <i>x</i> coordinate
     * @param y   the <i>y</i> coordinate
     */

    void drawText(String str, int x, int y);

    /**
     * Draw the given character array, using current font and color.
     * The baseline of the first character is at position (<i>x</i>,&nbsp;<i>y</i>).
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

    void drawText(char[] data, int offset, int length, int x, int y);

    /**
     * Draw as much of the specified image as is currently available.
     * The image is drawn with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>).
     * Transparent pixels in the image do not affect whatever pixels are already there.
     *
     * @param img a not null {@link Image} to draw
     * @param x   the <i>x</i> coordinate
     * @param y   the <i>y</i> coordinate
     * @apiNote the implementation depends on the current platform
     */

    void drawImage(Image img, int x, int y);

    /**
     * Draw as much of the specified image as has already been scaled to fit inside the specified rectangle.
     * The image is drawn inside the specified rectangle and is scaled if
     * necessary. Transparent pixels do not affect whatever pixels
     * are already there.
     *
     * @param img    a not null {@link Image} to draw
     * @param x      the <i>x</i> coordinate
     * @param y      the <i>y</i> coordinate
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @apiNote the implementation depends on the current platform
     */

    void drawImage(Image img, int x, int y, int width, int height);
}
