package uia.core.geometry;

/**
 * Standard rect shape representation
 */

public class Rect extends Shape {

    public Rect() {
        super(4);
        addVertices(
                -1, -1,
                1, -1,
                1, 1,
                -1, 1);
    }
}
