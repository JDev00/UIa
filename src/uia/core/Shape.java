package uia.core;

import uia.core.basement.Collider;
import uia.core.basement.Movable;

import static uia.utility.TrigTable.*;

/**
 * Shape is a graphical object defined by:
 * <ul>
 *     <li>a Geometry that stores its vertices (in a normalized form);</li>
 *     <li>a set of primitives operations on such geometry.</li>
 * </ul>
 * <br>
 * Shape's responsibility is to transform a Geometry object.
 * <br>
 * <b>By design, shape's geometry is centered based.</b>
 */

public class Shape implements Movable, Collider {
    private Geometry geometry;
    private COLLIDER_POLICY policy = COLLIDER_POLICY.SAT;

    private final float[] bounds;
    private float r_width, r_height;

    public Shape() {
        geometry = new Geometry();

        bounds = new float[5];
    }

    /**
     * Transformed vertex representation
     */

    public static class TransformedVertex {
        public float x;
        public float y;
        public boolean primer;
    }

    /**
     * Transform the given Vertex according to the shape's position, dimension and rotation and store the result
     * inside a 'target' Vertex.
     * <br>
     * The given Vertex won't be modified.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    public void transformAndStoreInto(Geometry.Vertex source, TransformedVertex target) {
        float x = bounds[2] * source.getX();
        float y = bounds[3] * source.getY();
        float cos = cos(bounds[4]);
        float sin = sin(bounds[4]);

        target.x = bounds[0] + rotX(x, y, cos, sin);
        target.y = bounds[1] + rotY(x, y, cos, sin);
        target.primer = source.isPrimer();
    }

    /**
     * Helper method. Update shape's width and height.
     */

    private void updateDimension() {
        float cos = cos(bounds[4]);
        float sin = sin(bounds[4]);
        r_width = boundX(bounds[2], bounds[3], cos, sin);
        r_height = boundY(bounds[2], bounds[3], cos, sin);
    }

    /**
     * Set the shape's geometry
     *
     * @param geometry a not null {@link Geometry}
     */

    public void setGeometry(Geometry geometry) {
        if (geometry != null) this.geometry = geometry;
    }

    /**
     * @return the attached {@link Geometry}
     */

    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public void setPosition(float x, float y) {
        bounds[0] = x;
        bounds[1] = y;
    }

    @Override
    public void setDimension(float width, float height) {
        bounds[2] = Math.max(0, width);
        bounds[3] = Math.max(0, height);
    }

    @Override
    public void setRotation(float radians) {
        bounds[4] = radians % TWO_PI;
    }

    @Override
    public void setColliderPolicy(COLLIDER_POLICY policy) {
        this.policy = policy;
    }

    private final float[] outBounds = new float[5];

    @Override
    public float[] bounds() {
        updateDimension();
        outBounds[0] = bounds[0] - 0.5f * r_width;
        outBounds[1] = bounds[1] - 0.5f * r_height;
        outBounds[2] = r_width;
        outBounds[3] = r_height;
        outBounds[4] = bounds[4];
        return outBounds;
    }

    @Override
    public boolean contains(float x, float y) {
        updateDimension();

        switch (policy) {
            case SAT:
                return containsSAT(x, y);
            case CIRCLE:
                return containsCIRCLE(x, y);
            default:
                return containsRECT(x, y);
        }
    }

    private boolean containsRECT(float x, float y) {
        return Collider.intersects(bounds[0], bounds[1], r_width, r_height, x, y, 1, 1);
    }

    private boolean containsCIRCLE(float x, float y) {
        float dx = Math.abs(bounds[0] - x);
        float dy = Math.abs(bounds[1] - y);
        return Math.sqrt(dx * dx + dy * dy) <= 0.5f * bounds[2];
    }

    private boolean containsSAT(float x, float y) {
        boolean inside = false;

        int size = geometry.vertices();
        TransformedVertex vi = new TransformedVertex();
        TransformedVertex vj = new TransformedVertex();

        // check if the given point is inside this shape.
        // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        for (int i = 0, j = size - 1; i < size; j = i++) {
            transformAndStoreInto(geometry.get(i), vi);
            transformAndStoreInto(geometry.get(j), vj);

            if ((vi.y > y) != (vj.y > y) && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + vi.x)
                inside = !inside;
        }

        return inside;
    }
}
