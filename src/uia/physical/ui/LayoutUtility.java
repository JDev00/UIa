package uia.physical.ui;

import uia.core.ui.View;

import java.util.Objects;

/**
 * LayoutUtility collects utility functions for measuring layout.
 */
public final class LayoutUtility {

    private LayoutUtility() {
    }

    /**
     * Calculates the boundaries of the given views. More formally,
     * calculates the minimum rectangular container that can contain all the given views.
     * <br>
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1)
     *
     * @param views the views to measure the boundary
     * @return the boundary as an array made of:
     * <ul>
     *     <li>the upper left-hand corner on the x-axis</li>
     *     <li>the upper left-hand corner on the y-axis</li>
     *     <li>the boundary width</li>
     *     <li>the boundary height</li>
     * </ul>
     * @throws NullPointerException if {@code views == null}
     */

    public static float[] measureBoundaries(Iterable<View> views) {
        Objects.requireNonNull(views);

        float[] result = {0f, 0f, 0f, 0f};

        int index = 0;
        for (View view : views) {
            float[] bounds = view.getBounds();
            float xi = bounds[0];
            float yi = bounds[1];

            if (index == 0) {
                result[0] = result[2] = xi;
                result[1] = result[3] = yi;
            }
            if (xi < result[0]) result[0] = xi;
            if (yi < result[1]) result[1] = yi;
            if (xi + bounds[2] > result[2]) result[2] = xi + bounds[2];
            if (yi + bounds[3] > result[3]) result[3] = yi + bounds[3];

            index++;
        }

        // adjusts with and height according to the starting point of the container
        result[2] -= result[0];
        result[3] -= result[1];
        return result;
    }
}
