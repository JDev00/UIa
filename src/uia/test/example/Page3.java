package uia.test.example;

import uia.core.Page;
import uia.core.platform.policy.Context;
import uia.core.widget.math.BoardView;
import uia.core.widget.math.data.DistributionDrawable;
import uia.core.widget.text.TextView;

public class Page3 extends Page {

    public Page3(Context context) {
        super(context);

        float w = width();
        float h = height();

        BoardView boardView = new BoardView(context, 0f, 0f, 0.5f * w, 0.5f * w);
        boardView.getDistribution(0)
                .add(i -> new float[]{i, i * i}, 0, 10);
        boardView.getDistribution(1)
                .add(i -> new float[]{i, 10 * i}, 0, 10);
        boardView.setDrawable(0, new DistributionDrawable()
                .setLineWidth(2)
                .enablePoint(false));
        boardView.setDrawable(1, new DistributionDrawable()
                .setLineWidth(4));


        TextView tracker = new TextView(context, -0.4f * w, -0.45f * h, 0.2f * w, 0.1f * h) {
            @Override
            public void update() {
                super.update();
                long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                setText("FPS: " + getContext().getFrameRate() + "\n Mem: " + mem + " MB");
            }
        };

        add(boardView);
        add(tracker);
    }
}
