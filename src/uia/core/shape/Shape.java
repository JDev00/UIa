package uia.core.shape;

import uia.core.basement.Collider;

import java.util.Arrays;
import java.util.Objects;

import static uia.utility.TrigTable.*;

/**
 * Shape is a graphical object defined by:
 * <ul>
 *     <li>a Geometry that stores its vertices (in a normalized form);</li>
 *     <li>a set of primitives operations on such geometry.</li>
 * </ul>
 * <br>
 * Shape responsibility is to transform a Geometry object.
 * <br>
 * <b>By design, shape geometry is center based.</b>
 */

public class Shape implements Collider {
    private Geometry geometry;
    private ColliderPolicy policy = ColliderPolicy.SAT;

    private final float[] bounds;
    private float r_width, r_height;

    public Shape() {
        geometry = new Geometry();
        bounds = new float[5];
    }

    @Override
    public String toString() {
        return "Shape{" +
                "vertices=" + geometry.vertices() +
                "policy=" + policy +
                ", bounds=" + Arrays.toString(bounds) +
                '}';
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
     * Transforms the specified Vertex according to the shape position, dimension and rotation
     * and stores the result inside a 'target' Vertex.
     * <br>
     * The specified Vertex won't be modified.
     * <br>
     * Time required: T(1)
     * <br>
     * Space required: O(1)
     */

    private void transform(Geometry.Vertex source, TransformedVertex target) {
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
    public void setColliderPolicy(ColliderPolicy policy) {
        this.policy = Objects.requireNonNull(policy);
    }

    /**
     * Set the Shape absolute position
     *
     * @param x the Shape absolute position on the x-axis
     * @param y the Shape absolute position on the y-axis
     */

    public void setPosition(float x, float y) {
        bounds[0] = x;
        bounds[1] = y;
    }

    /**
     * Set the Shape dimension
     *
     * @param width  the Shape width (>= 0) in pixels
     * @param height the Shape height (>= 0) in pixels
     */

    public void setDimension(float width, float height) {
        bounds[2] = Math.max(0, width);
        bounds[3] = Math.max(0, height);
    }

    /**
     * Set the Shape rotation
     *
     * @param radians the rotation in radians
     */

    public void setRotation(float radians) {
        bounds[4] = radians % TWO_PI;
    }

    /**
     * @return the Shape width without rotation applied
     */

    public float getWidth() {
        return bounds[2];
    }

    /**
     * @return the Shape height without rotation applied
     */

    public float getHeight() {
        return bounds[3];
    }

    private final float[] outBounds = new float[5];

    @Override
    public float[] getBounds() {
        updateDimension();
        outBounds[0] = bounds[0] - 0.5f * r_width;
        outBounds[1] = bounds[1] - 0.5f * r_height;
        outBounds[2] = r_width;
        outBounds[3] = r_height;
        outBounds[4] = bounds[4];
        return outBounds;
    }

    /**
     * Helper function. Checks if the specified point is inside this Shape according to the RECT collider.
     */

    private boolean containsRECT(float x, float y) {
        return Collider.intersects(bounds[0], bounds[1], r_width, r_height, x, y, 1, 1);
    }

    /**
     * Helper function. Checks if the given point is inside this Shape according to the CIRCLE collider.
     */

    private boolean containsCIRCLE(float x, float y) {
        float dx = Math.abs(bounds[0] - x);
        float dy = Math.abs(bounds[1] - y);
        return Math.sqrt(dx * dx + dy * dy) <= 0.5f * bounds[2];
    }

    /**
     * Helper function. Checks if the given point is inside this Shape according to the SAT collider.
     */

    private boolean containsSAT(float x, float y) {
        boolean inside = false;
        if (containsRECT(x, y)) {
            int size = geometry.vertices();
            TransformedVertex vi = new TransformedVertex();
            TransformedVertex vj = new TransformedVertex();

            // check if the given point is inside this shape. https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
            for (int i = 0, j = size - 1; i < size; j = i++) {
                transform(geometry.get(i), vi);
                transform(geometry.get(j), vj);
                if ((vi.y > y) != (vj.y > y) && x < (vj.x - vi.x) * (y - vi.y) / (vj.y - vi.y) + vi.x)
                    inside = !inside;
            }
        }
        return inside;
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

    /**
     * Transforms the source Vertex according to the shape position, dimension and rotation
     * and stores the result inside a 'target' Vertex.
     * <br>
     * <b>The source Vertex won't be modified.</b>
     *
     * @param shape  a not null {@link Shape} object
     * @param source the vertex to transform
     * @param target the vertex obtained after the transformation
     * @throws NullPointerException if {@code shape == null or source == null or target == null}
     */

    public static void transform(Shape shape, Geometry.Vertex source, TransformedVertex target) {
        Objects.requireNonNull(shape);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        shape.transform(source, target);
    }
}
