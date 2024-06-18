package uia.core.rendering.geometry;

import uia.core.basement.Collidable;

import java.util.Objects;

import static uia.utility.MathUtility.rotateX;
import static uia.utility.MathUtility.rotateY;

/**
 * The GeometryUtility is a collection of utility functions for working with geometry.
 */

public final class GeometryUtility {

    private GeometryUtility() {
    }

    /**
     * Transforms the given vertex according to the Transform object
     * and stores the result inside a 'transformed' vertex.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @param transform         the Transform object used to transform the given vertex
     * @param vertexX           the vertex on the x-axis to be transformed
     * @param vertexY           the vertex on the y-axis to be transformed
     * @param transformedVertex a two-element array used to store the transformed vertex
     */

    public static void computeTransformedVertex(Transform transform, float vertexX, float vertexY,
                                                float[] transformedVertex) {
        float x = transform.getTranslationX();
        float y = transform.getTranslationY();
        float width = transform.getScaleX();
        float height = transform.getScaleY();
        float rotation = transform.getRotation();

        // computes the transformed vertex
        float newVertexX = width * vertexX;
        float newVertexY = height * vertexY;
        transformedVertex[0] = x + rotateX(newVertexX, newVertexY, rotation);
        transformedVertex[1] = y + rotateY(newVertexX, newVertexY, rotation);
    }

    /**
     * Computes the geometry bounds (rectangular container) according to the given Transform object.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @param geometryTransform the geometry transform object
     * @param targetBounds      a five-element array to store the calculated bounds structured as follows:
     *                          <ul>
     *                          <li>the upper-left corner on the x-axis of the rectangular container</li>
     *                          <li>the upper-left corner on the y-axis of the rectangular container</li>
     *                          <li>the width of the rectangular container</li>
     *                          <li>the height of the rectangular container</li>
     *                          <li>the rectangular container rotation</li>
     *                          </ul>
     * @throws NullPointerException     if {@code geometryTransform == null || targetBounds == null}
     * @throws IllegalArgumentException if {@code targetBounds.length < 5}
     */

    public static void computeBounds(Transform geometryTransform, float[] targetBounds) {
        Objects.requireNonNull(geometryTransform);
        Objects.requireNonNull(targetBounds);
        if (targetBounds.length < 5) {
            throw new IllegalArgumentException("targetBounds.length is lower than five");
        }

        float width = geometryTransform.getScaleX();
        float height = geometryTransform.getScaleY();
        float rotation = geometryTransform.getRotation();

        // updates bounds
        float colliderWidth = Collidable.colliderWidth(width, height, rotation);
        float colliderHeight = Collidable.colliderHeight(width, height, rotation);
        targetBounds[0] = geometryTransform.getTranslationX() - colliderWidth / 2f;
        targetBounds[1] = geometryTransform.getTranslationY() - colliderHeight / 2f;
        targetBounds[2] = colliderWidth;
        targetBounds[3] = colliderHeight;
        targetBounds[4] = rotation;
    }

    /**
     * Helper function. Checks if the specified point is inside the given
     * geometry bounds according to the RECT collider.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     */

    private static boolean containsRECT(float[] geometryBounds, float x, float y) {
        float width = geometryBounds[2];
        float height = geometryBounds[3];
        return Collidable.intersects(
                geometryBounds[0] + width / 2f, geometryBounds[1] + height / 2f,
                width, height,
                x, y,
                1, 1
        );
    }

    /**
     * Helper function. Checks if the given point is inside the given
     * geometry bounds according to the CIRCLE collider.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     */

    private static boolean containsCIRCLE(float[] geometryBounds, float x, float y) {
        float dx = Math.abs(geometryBounds[0] - x);
        float dy = Math.abs(geometryBounds[1] - y);
        return Math.sqrt(dx * dx + dy * dy) <= 0.5f * geometryBounds[2];
    }

    /**
     * Helper function. Checks if the given point is inside this Shape according to the SAT collider.
     * <br>
     * Time complexity: T(n)
     * <br>
     * Space complexity: O(1)
     */

    private static boolean containsSAT(Transform transform, Geometry geometry, float[] geometryBounds,
                                       float x, float y) {
        boolean inside = false;
        if (containsRECT(geometryBounds, x, y)) {
            float[] vi = {0f, 0f};
            float[] vj = {0f, 0f};

            // checks if the given point is inside this shape.
            // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
            int size = geometry.vertices();
            for (int i = 0, j = size - 1; i < size; j = i++) {
                computeTransformedVertex(transform, geometry.getX(i), geometry.getY(i), vi);
                computeTransformedVertex(transform, geometry.getX(j), geometry.getY(j), vj);
                if ((vi[1] > y) != (vj[1] > y) && x < (vj[0] - vi[0]) * (y - vi[1]) / (vj[1] - vi[1]) + vi[0]) {
                    inside = !inside;
                }
            }
        }
        return inside;
    }

    /**
     * Checks if the given transformed geometry (shape) contains the given point.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
     *
     * @param colliderPolicy the collider to be used to check if the point is contained in the transformed geometry
     * @param transform      the Transform object used to transform the geometry
     * @param geometry       the Geometry object
     * @param x              the absolute point position, in pixels, along the x-axis
     * @param y              the absolute point position, in pixels, along the y-axis
     * @return true if the given point is contained inside the transformed geometry
     */

    public static boolean contains(Collidable.ColliderPolicy colliderPolicy,
                                   Transform transform, Geometry geometry,
                                   float x, float y) {
        float[] geometryBounds = new float[5];
        computeBounds(transform, geometryBounds);
        switch (colliderPolicy) {
            case SAT:
                return containsSAT(transform, geometry, geometryBounds, x, y);
            case CIRCLE:
                return containsCIRCLE(geometryBounds, x, y);
            default:
                return containsRECT(geometryBounds, x, y);
        }
    }
}
