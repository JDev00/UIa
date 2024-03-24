package test.__tests__.sanity;

import test.__tests__.utility.TestUtility;
import uia.application.math.DrawableDistribution;
import uia.application.math.UIGraphic;
import uia.core.ui.ViewGroup;
import uia.core.ui.context.Context;
import uia.physical.component.Component;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.theme.Theme;

/**
 * sanity test.
 */

public class mathWidgets extends WrapperView {

    public mathWidgets() {
        super(new ComponentGroup(new Component("MAIN", 0.5f, 0.5f, 1f, 1f)));

        UIGraphic graphic = new UIGraphic(
                new Component("GRAPHIC", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        graphic.getPaint()
                .setStrokeColor(Theme.LIGHT_CORAL)
                .setStrokeWidth(1);

        DrawableDistribution distribution = graphic.getDistribution(0);
        distribution.add(0f, 0f)
                .add(1000f, 1000f)
                .add(10f, 1000f)
                .add(200f, 100f);
        distribution.getLinePaint()
                .setColor(Theme.BLUE);

        ViewGroup.insert(getView(), graphic);
    }

    public static void main(String[] args) {
        Context context = TestUtility.createMockContext();
        context.setView(new mathWidgets());
    }
}
