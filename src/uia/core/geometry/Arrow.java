package uia.core.geometry;

/**
 * Standard arrow shape representation
 */

public class Arrow extends Shape {

    public Arrow() {
        super(4);
        addVertices(
                -1, -1,
                1, 0,
                -1, 1,
                -2f / 3, 0);
    }
}
