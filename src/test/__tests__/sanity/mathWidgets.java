package test.__tests__.sanity;

import uia.application.math.DrawableDistribution;
import test.__tests__.utility.TestUtility;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.component.Component;
import uia.application.math.UIGraphic;
import uia.core.ui.context.Context;
import uia.physical.theme.Theme;
import uia.core.ui.ViewGroup;

/**
 * sanity test.
 */

public final class mathWidgets extends WrapperView {

    private mathWidgets() {
        super(new ComponentGroup(
                new Component("MAIN", 0.5f, 0.5f, 1f, 1f)
        ));

        UIGraphic graphic = new UIGraphic(
                new Component("GRAPHIC", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        graphic.getStyle()
                .setBackgroundColor(Theme.LIGHT_GRAY)
                .setBorderColor(Theme.LIGHT_CORAL)
                .setBorderWidth(5f);

        DrawableDistribution distribution = graphic.getDistribution(0);
        distribution
                .setLineColor(Theme.BLUE)
                .setPointColor(Theme.LIME)
                .setLineWidth(5)
                .setPointSize(8);
        distribution
                .add(0f, 0f)
                .add(1000f, 1000f)
                .add(10f, 1000f)
                .add(200f, 100f);

        ViewGroup.insert(getView(), graphic);
    }

    public static void main(String[] args) {
        Context context = TestUtility.createMockContext();
        context.setView(new mathWidgets());
    }
}
