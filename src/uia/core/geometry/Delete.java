package uia.core.geometry;

import uia.utils.Utils;

/**
 * Standard delete shape representation
 */

public class Delete extends Shape {

    public Delete(float thickness) {
        super(12);
        buildFigure(thickness);
    }

    private void buildFigure(float thickness) {
        float th = Utils.constrain(Math.abs(thickness), 0, 0.98f);

        clear();
        addVertices(
                -1 + th, -1,
                -1, -1,
                -th / 2f, 0,
                -1, 1,
                -1 + th, 1,
                0, th / 2f,
                1 - th, 1,
                1, 1,
                th / 2f, 0,
                1, -1,
                1 - th, -1,
                0, -th / 2f);
    }

    /**
     * Set the line thickness
     *
     * @param thickness the line thickness between [0, 0.98]
     */

    public void setThickness(float thickness) {
        buildFigure(thickness);
    }
}
