package platform.test;

import uia.core.architecture.ui.View;
import uia.physical.ui.wrapper.WrapperView;
import uia.core.architecture.ui.ViewGroup;
import uia.physical.ui.Component;

/**
 * Test
 */

public class TestChessboard extends WrapperView {

    public TestChessboard(int m, int n) {
        super(new Component("Chessboard", 0.5f, 0.5f, 0.5f, 0.5f).setExpanseLimit(1f, 1f));

        ViewGroup group = (ViewGroup) getView();

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
