package uia.core.geometry;

import uia.core.policy.Path;

import static uia.utils.Utils.*;

public class TriangleSmooth implements Figure {
    private int vertices;

    private float a;
    private float b;
    private float c;

    public TriangleSmooth(int vertices, float a, float b, float c) {
        this.vertices = vertices;
        this.a = a;
        this.b = b;
        this.c = c;
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
     * @param b the right corner roundness; a value between [0, 1]
     * @param c the lower-left corner roundness; a value between [0, 1]
     */

    public void setParams(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

        float an;

        float fdx = dx / 4f, fdy = dy / 4f;
        float dxa = a * fdx, dya = a * fdy;
        float dxb = b * fdx, dyb = b * fdy;
        float dxc = c * fdx, dyc = c * fdy;

        float xa = dx - dxa, ya = dy - dya;
        float xb = dx - dxb;
        float xc = dx - dxc, yc = dy - dyc;

        path.reset();
        path.moveTo(px - dx, py - ya);

        for (int i = 0; i <= vertices; i++) {
            an = -PI + (3 * PI / 5f) * i / vertices;
            path.lineTo(px - xa + pcos(an) * dxa, py - ya + psin(an) * dya);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -PI / 3 + (2 * PI / 3) * i / vertices;
            path.lineTo(px + xb + pcos(an) * dxb, py + psin(an) * dyb);
        }

        for (int i = 0; i <= vertices; i++) {
            an = -2 * PI / 5 - (3 * PI / 5f) * i / vertices;
            path.lineTo(px - xc + pcos(an) * dxc, py + yc - psin(an) * dyc);
        }

        path.close();
    }

    /**
     * Create a new TriangleSmooth filled with default values
     */

    public static TriangleSmooth create() {
        return new TriangleSmooth(RectSmooth.DEFAULT_VERTICES,
                RectSmooth.DEFAULT_SMOOTH, RectSmooth.DEFAULT_SMOOTH, RectSmooth.DEFAULT_SMOOTH);
    }

    /**
     * Create a new TriangleSmooth
     *
     * @param vertices  the number of vertices for each corner; higher number means more smoothness
     * @param roundness a value between [0, 1] used to curve edges
     */

    public static TriangleSmooth create(int vertices, float roundness) {
        return new TriangleSmooth(vertices, roundness, roundness, roundness);
    }
}
