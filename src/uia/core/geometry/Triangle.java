package uia.core.geometry;

/**
 * Standard triangle shape representation
 */

public class Triangle extends Shape {

    public Triangle() {
        super(3);
        addVertices(
                -1, -1,
                1, 0,
                -1, 1);
    }
}
