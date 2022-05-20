package uia.core.policy;

/**
 * ADT used to describe a shape.
 * <br>
 * <b>This ADT describes an abstraction from the native platform.
 * It is developed to create a common ground regardless of the platform or implementation.</b>
 */

public interface Path extends Bundle {

    /**
     * Reset geometry
     */

    void reset();

    /**
     * Reset geometry but keeps data structures for faster access.
     * <b>Note that this method is platform dependent.
     * <br>
     * If platform/native path does not support it, it will be implemented with {@link Path#reset()}</b>
     *
     * @see Path#reset()
     */

    void rewind();

    /**
     * Set the beginning of the next contour to the point (x,y)
     *
     * @param x the x-coordinate of the start of a new contour
     * @param y the y-coordinate of the start of a new contour
     */

    void moveTo(float x, float y);

    /**
     * Add a line from the last point to the specified point (x,y).
     * If no moveTo() call has been made for this contour, the first point is automatically set to (0,0).
     *
     * @param x the x-coordinate of the end of a line
     * @param y the y-coordinate of the end of a line
     */

    void lineTo(float x, float y);

    /**
     * Close the current contour.
     * If the current point is not equal to the first point of the contour, a line segment is automatically added.
     */

    void close();

    /**
     * Add an existing path to this one
     *
     * @param path    a not null Path
     * @param connect true to connect paths. <b>Note that this option is platform dependent</b>
     */

    void addPath(Path path, boolean connect);

    /**
     * Add an existing path to this one
     *
     * @param path    a not null <b>platform native</b> shape
     * @param connect true to connect paths. <b>Note that this option is platform dependent</b>
     */

    void addPath(Object path, boolean connect);

    /**
     * @return true if this Path contains the given point
     */

    boolean contains(double x, double y);

    /**
     * @return true if this Path contains the given plane centered on (x,y)
     */

    boolean contains(double x, double y, double w, double h);

    /**
     * @return true if this Path contains the given one
     */

    boolean contains(Path path);

    /**
     * @param path the native shape object
     * @return true if this Path contains the given one
     */

    boolean contains(Object path);

    /**
     * @return true if this Path intersects with the given plane centered on (x,y)
     */

    boolean intersect(double x, double y, double w, double h);

    /**
     * @return true if this Path intersects with given one
     */

    boolean intersect(Path path);

    /**
     * @param path the native shape object
     * @return true if this Path intersects with given one
     */

    boolean intersect(Object path);
}
