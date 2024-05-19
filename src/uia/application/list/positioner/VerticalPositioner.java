package uia.application.list.positioner;

import uia.core.ui.ViewGroup;
import uia.core.ui.View;

/**
 * Implementation of {@link ViewPositioner}. Aligns views vertically within a list view.
 */
public class VerticalPositioner implements ViewPositioner {
    private final float[] sum = {0f};
    private final float[] bounds;
    private final float gapBetweenViews;

    public VerticalPositioner(ViewGroup group, float gapBetweenViews) {
        this.gapBetweenViews = gapBetweenViews;
        bounds = group.getBounds();
    }

    @Override
    public void place(View view, int index) {
        if (bounds[3] != 0) {
            float h = gapBetweenViews * view.getBounds()[3] / (2 * bounds[3]);
            if (index == 0) {
                sum[0] = 0f;
            }

            sum[0] += h;
            view.setPosition(0.5f, sum[0]);
            sum[0] += h;
        }
    }
}
