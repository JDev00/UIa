package uia.core.geometry;

import uia.utils.Utils;

/**
 * Standard plus shape representation
 */

public class Plus extends Shape {

    public Plus(float thickness) {
        super(12);
        buildFigure(thickness);
    }

    private void buildFigure(float thickness) {
        float th = Utils.constrain(Math.abs(thickness), 0, 0.75f);

        clear();
        addVertices(
                -th, -1,
                th, -1,
                th, -th,
                1, -th,
                1, th,
                th, th,
                th, 1,
                -th, 1,
                -th, th,
                -1, th,
                -1, -th,
                -th, -th);
    }

    /**
     * Set the line thickness
     *
     * @param thickness the line thickness between [0, 0.75]
     */

    public void setThickness(float thickness) {
        buildFigure(thickness);
    }
}
