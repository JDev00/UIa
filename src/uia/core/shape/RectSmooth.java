package uia.core.shape;

import java.awt.*;

import static uia.utils.Utils.*;

public class RectSmooth extends Figure {
    public static final int DEFAULT_VERTICES = 15;
    public static final float DEFAULT_SMOOTH = 0.1f;

    private int vertices;

    private float a;
    private float b;
    private float c;
    private float d;

    public RectSmooth(int vertices, float a, float b, float c, float d) {
        this.vertices = vertices;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Set the number of vertices used to create each corner
     *
     * @param vertices the number of vertices for each corner; higher number means more smoothness
     */

    public void setVertices(int vertices) {
        this.vertices = Math.max(0, vertices);
    }

    /**
     * Set the roundness of each corner
     *
     * @param a the upper-left corner roundness; a value between [0, 1]
     * @param b the upper-right corner roundness; a value between [0, 1]
     * @param c the lower-right corner roundness; a value between [0, 1]
     * @param d the lower-left corner roundness; a value between [0, 1]
     */

    public void setParams(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * this.dx;
        float dy = 0.5f * this.dy;

        float an;
        float v = Math.min(dx, dy);
        float sa = a * v;
        float sb = b * v;
        float sc = c * v;
        float sd = d * v;

        float xa = dx - sa, ya = dy - sa;
        float xb = dx - sb, yb = dy - sb;
        float xc = dx - sc, yc = dy - sc;
        float xd = dx - sd, yd = dy - sd;

        path.reset();
        path.moveTo(px - xa - sa, py - ya);

        for (int i = 0; i <= vertices; i++) {
            an = -PI - HALF_PI * i / vertices;
            path.lineTo(px - xa + pcos(an) * sa, py - ya - psin(an) * sa);
        }

        for (int i = 0; i <= vertices; i++) {
            an = HALF_PI - HALF_PI * i / vertices;
            path.lineTo(px + xb + pcos(an) * sb, py - yb - psin(an) * sb);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI * i / vertices;
            path.lineTo(px + xc + pcos(an) * sc, py + yc - psin(an) * sc);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -HALF_PI - HALF_PI * i / vertices;
            path.lineTo(px - xd + pcos(an) * sd, py + yd - psin(an) * sd);
        }

        path.closePath();

        canvas.fill(path);
    }

    /**
     * Create a new RectSmooth filled with default values
     */

    public static RectSmooth create() {
        return new RectSmooth(DEFAULT_VERTICES, DEFAULT_SMOOTH, DEFAULT_SMOOTH, DEFAULT_SMOOTH, DEFAULT_SMOOTH);
    }

    /**
     * Create a new RectSmooth
     *
     * @param vertices  the number of vertices used to create each corner; higher number means more smoothness
     * @param roundness a value between [0, 1] used to curve edges
     */

    public static RectSmooth create(int vertices, float roundness) {
        return new RectSmooth(vertices, roundness, roundness, roundness, roundness);
    }
}
