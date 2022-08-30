package uia.core.geometry;

/**
 * Standard pause shape representation
 */

public class Pause extends Shape {

    public Pause() {
        super(8);
        addVertices(
                -1, -1,
                -0.33f, -1,
                -0.33f, 1,
                -1, 1,
                1, -1,
                0.33f, -1,
                0.33f, 1,
                1, 1);
        beginGeom(4, true);
    }
}
