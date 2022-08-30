package uia.core.geometry;

import static uia.utils.Utils.*;

public class Oval extends Shape {

    public Oval(int vertices) {
        super(vertices);
        buildFigure(vertices);
    }

    private void buildFigure(int vertices) {
        clear();

        float a;
        for (int i = 0; i <= vertices; i++) {
            a = TWO_PI * i / vertices;
            addVertex(pcos(a), psin(a));
        }
    }

    /**
     * Set the number of vertices
     *
     * @param vertices the number of vertices
     */

    public void setVertices(int vertices) {
        if (vertices >= 0)
            buildFigure(vertices);
    }

    /**
     * Create a new Oval with 60 vertices
     */

    public static Oval create() {
        return new Oval(60);
    }
}
