package uia.core.shape;

import java.awt.*;

import static uia.utils.Utils.*;

public class Oval extends Figure {
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
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * this.dx;
        float dy = 0.5f * this.dy;
        float a;

        path.reset();
        path.moveTo(px + dx, py);

        for (int i = 0; i <= vertices; i++) {
            a = TWO_PI * i / vertices;
            path.lineTo(px + pcos(a) * dx, py + psin(a) * dy);
        }

        path.closePath();

        canvas.fill(path);
    }

    /**
     * Create a new Oval with 60 vertices
     */

    public static Oval create() {
        return new Oval(60);
    }
}
