package uia.core.geometry;

import uia.core.policy.Path;

import static uia.utils.Utils.*;

public class Oval implements Figure {
    private int vertices;

    public Oval(int vertices) {
        this.vertices = vertices;
    }

    /**
     * Set the number of vertices used to create this Oval
     *
     * @param vertices the number of vertices; higher number means more smoothness
     */

    public void setVertices(int vertices) {
        this.vertices = Math.max(0, vertices);
    }

    @Override
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

        path.reset();
        path.moveTo(px + dx, py);

        float a;
        for (int i = 0; i <= vertices; i++) {
            a = TWO_PI * i / vertices;
            path.lineTo(px + pcos(a) * dx, py + psin(a) * dy);
        }

        path.close();
    }

    /**
     * Create a new Oval with 60 vertices
     */

    public static Oval create() {
        return new Oval(60);
    }
}
