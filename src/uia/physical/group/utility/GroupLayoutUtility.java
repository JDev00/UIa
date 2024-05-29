package uia.physical.group.utility;

import uia.core.ui.View;

import java.util.Objects;
import java.util.List;

/**
 * GroupLayoutUtility collects utility functions for measuring group layout.
 */
public class GroupLayoutUtility {

    private GroupLayoutUtility() {
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
     */

    public static float[] measureBoundaries(List<View> views) {
        Objects.requireNonNull(views);

        float[] result = {0f, 0f, 0f, 0f};
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            float[] bounds = view.getBounds();
            float xi = bounds[0];
            float yi = bounds[1];

            if (i == 0) {
                result[0] = result[2] = xi;
                result[1] = result[3] = yi;
            }
            if (xi < result[0]) result[0] = xi;
            if (yi < result[1]) result[1] = yi;
            if (xi + bounds[2] > result[2]) result[2] = xi + bounds[2];
            if (yi + bounds[3] > result[3]) result[3] = yi + bounds[3];
        }

        // adjusts with and height according to the starting point of the container
        result[2] -= result[0];
        result[3] -= result[1];
        return result;
    }
}
