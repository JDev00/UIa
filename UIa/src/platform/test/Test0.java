package platform.test;

import uia.application.UIBar;
import uia.application.UICalendar;
import uia.core.Font;
import uia.core.Image;
import uia.core.Paint;
import uia.core.basement.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.event.OnClick;
import uia.application.theme.Theme;
import uia.utils.Figure;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentImage;
import uia.physical.wrapper.WrapperViewGroup;
import uia.utils.TrigTable;
import uia.utils.Utils;

public class Test0 extends WrapperViewGroup {

    public Test0(Context context) {
        super(new ComponentGroup(new Component("TEST0", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(Theme.BACKGROUND);


        UIBar bar = new UIBar(new Component("BAR", 0.25f, 0.25f, 0.25f, 0.01f));
        bar.setRange(0, 1000);
        bar.setValue(0f);
        bar.setText(s -> Utils.limitDecimals(s, 1) + " %");
        bar.setRotation(TrigTable.PI);
        bar.getViewText().getFont().setSize(30f).setStyle(Font.STYLE.BOLD);
        bar.addEvent((OnClick) (v, p) -> {
            v.setRotation(v.desc()[2] + TrigTable.HALF_PI);
        });


        ComponentImage image = new ComponentImage(new Component("IMAGE", 0.75f, 0.5f, 0.25f, 0.5f));
        image.getImage().load("..\\uia\\sample\\img0.png").setMode(Image.MODE.CENTER);
        /*image.addEvent((OnClick) (v, pointers) -> {
            image.setImageRotation(image.boundsImage()[4] + 0.1f);
        });*/

        UICalendar calendar = new UICalendar(image);
        calendar.buildGeom((v, g) -> Figure.rect(g), false);
        calendar.setRotation(0.25f);


        TrackerComponent trackerComponent = new TrackerComponent(new Component("FPS", 0.05f, 0.05f, 0.1f, 0.1f), context);


        add(bar, calendar, trackerComponent);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        try {
            UIBar view = (UIBar) get("BAR");
            //view.setRotation(view.desc()[2] + 0.003f);
            view.setValue(view.getValue() + 1);
        } catch (Exception ignored) {
        }
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
                tile.getPaint().setColor(new Paint.Color(255 * r, 255 * r, 255 * r, 100));
                tile.setConsumer(CONSUMER.POINTER, false);

                group.add(tile);
            }
        }
    }
}
