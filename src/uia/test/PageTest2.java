package uia.test;

import uia.core.Page;
import uia.core.policy.Context;
import uia.core.widget.math.GraphicView;
import uia.core.widget.math.GraphicView2;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.utils.Utils;

import java.util.function.Function;

public class PageTest2 extends Page {

    public PageTest2(Context context) {
        super(context);

        Curve curve = new Curve(context);
        curve.addPlot(100, i -> new VecS(i, (float) Math.log(i + 1), null));
        curve.addPoint(101, 1000, null);
        curve.addPoint(102, 200, null);
        curve.analyse();
        curve.pointFactor = 2;
        curve.drawVariance = true;

        Curve curve1 = new Curve(context);
        curve1.addPlot(100, i -> new VecS(i, i / 10f, null));

        GraphicView view = new GraphicView(context, 0, 0, 0.5f * dx(), 0.5f * dy());
        view.add(curve);
        view.add(curve1);


        Function<Integer, double[]> pi_half = i -> new double[]{
                20 + i, Utils.constrain(Math.tan(Utils.PI * i / 20), -50, 50)
        };


        GraphicView2 view2 = new GraphicView2(context, 0, 0, 0.5f * dx(), 0.5f * dy());
        view2.plot(0, new double[]{0, 0});
        view2.plot(0, new double[]{11, 20});
        view2.plot(0, new double[]{20, 30});
        view2.plot(0, 100, pi_half);

        view2.plot(1, new double[]{0, 30});
        view2.plot(1, new double[]{0, 30});

        add(view2);
    }
}
