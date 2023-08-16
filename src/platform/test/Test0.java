package platform.test;

import uia.application.BarUI;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewGroup;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.wrapper.WrapperViewGroup;

public class Test0 extends WrapperViewGroup {

    public Test0(Context context) {
        super(new ComponentGroup(new Component("Test0", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(50);


        BarUI barUI = new BarUI(new Component("Bar", 0.5f, 0.5f, 0.25f, 0.1f));
        barUI.setRange(0, 100);
        barUI.setValue(50);


        ComponentGroup group = new ComponentGroup(new Component("Group", 0.5f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1.005f, 1.005f));
        group.getPaint().setColor(0, 255, 0);
        group.add(barUI);


        TrackerComponent trackerComponent = new TrackerComponent(new Component("ViewFPS", 0.05f, 0.05f, 0.1f, 0.1f), context);


        add(group, trackerComponent);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        setRotation(desc()[2] + 0.005f);
    }

    /**
     * Test function
     */

    public static void createChessboard(int m, int n, ViewGroup group) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int r = (i + j) % 2;
                View tile = new Component("Tile",
                        (j + 0.5f) / n,
                        (i + 0.5f) / m, 1f / n, 1f / m);
                tile.getPaint().setColor(255 * r, 255 * r, 255 * r, 100);
                tile.setConsumer(CONSUMER.POINTER, false);

                group.add(tile);
            }
        }
    }
}
