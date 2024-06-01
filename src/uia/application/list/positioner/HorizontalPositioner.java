package uia.application.list.positioner;

import uia.core.ui.ViewGroup;
import uia.core.ui.View;

/**
 * Implementation of {@link ViewPositioner}. Aligns views horizontally within a list view.
 */
public class HorizontalPositioner implements ViewPositioner {
    private final float[] sum = {0f};
    private final float[] bounds;
    private final float gapBetweenViews;

    public HorizontalPositioner(ViewGroup group, float gapBetweenViews) {
        this.gapBetweenViews = gapBetweenViews;
        bounds = group.getBounds();
    }

    @Override
    public void place(View view, int index) {
        if (bounds[3] != 0) {
            float width = gapBetweenViews * view.getBounds()[2] / (2 * bounds[2]);
            if (index == 0) {
                sum[0] = 0f;
            }

            sum[0] += width;
            view.setPosition(sum[0], 0.5f);
            sum[0] += width;
        }
    }
}
