package uia.application.list.positioner;

import uia.core.ui.ViewGroup;

/**
 * ViewPositionerFactory is responsible for creating the correct {@link ViewPositioner}.
 */
public class ViewPositionerFactory {

    private ViewPositionerFactory() {
    }

    /**
     * Creates a new ViewPositioner.
     *
     * @param group           the group on which the ViewPositioner acts
     * @param gapBetweenViews the gap (> 0) between each view. It is expressed as a relative value on the view boundaries.
     * @param vertical        true to align views vertically
     * @return a new ViewPositioner
     */
    public static ViewPositioner create(ViewGroup group, float gapBetweenViews, boolean vertical) {
        if (vertical) {
            return new VerticalPositioner(group, gapBetweenViews);
        } else {
            return new HorizontalPositioner(group, gapBetweenViews);
        }
    }
}
