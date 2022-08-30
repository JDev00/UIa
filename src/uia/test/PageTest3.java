package uia.test;

import uia.core.Page;
import uia.core.event.Mouse;
import uia.core.geometry.Pause;
import uia.core.policy.Context;
import uia.core.policy.Font;
import uia.core.widget.Button;
import uia.core.widget.CalendarView;
import uia.core.widget.HorView;
import uia.core.widget.TextView;
import uia.core.widget.math.BoardView;
import uia.core.widget.math.GraphicView;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.utils.Utils;

public class PageTest3 extends Page {

    public PageTest3(Context context) {
        super(context);

        float dx = dx();
        float dy = dy();


        Curve curve = new Curve(context);
        curve.addPlot(100, i -> new VecS(i, i));

        GraphicView g = new BoardView(context, 0, 0, 500, 500);
        g.add(curve);


        TextView test1 = TextView.createFriendly(context, -dx / 2f + 150, 0, 200, 200);
        test1.setExpansion(0.5f, 0.5f);
        test1.setFigure(new Pause());
        test1.setFont(context.createFont(test1.getFont(), "Arial", Font.STYLE.BOLD, 30));
        test1.setPaint(context.createColor(test1.getPaint(), 255, 255, 0));
        test1.setAlignX(TextView.AlignX.LEFT);
        test1.setText(Utils.read("src\\uia\\core\\Page.java"));
            /*test1.setRot(Utils.HALF_PI);
            test1.setupAnimator(View.ANIMATOR_TYPE.ROT, a -> {
                a.addNode(0, 0, 1f);
                a.addNode(6.28f, 6.28f, 10f);
                a.addEvent((NodeEv) (v, s) -> {
                    if (s == NodeEv.LAST) v.restart();
                });
            });*/


        Button button = new Button(context, dx / 2f - 200, 0, 150, 150);
        button.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.CLICK) {
                boolean b = Context.HINT.SMOOTH.equals(context.getHint());
                context.setHint(b ? null : Context.HINT.SMOOTH);
                button.setText(!b ? "enabled!" : "disabled!");
            }
        });


        HorView horView = new HorView(context, 0, 0, 200, 50);
        horView.set("Ostia!", "ciao!", "ao!", "come!");
        horView.setPaint(context.createColor(horView.getPaint(), 255, 0, 0));

        add(test1);
        add(g);
        add(horView);
        add(new CalendarView(context, 0, 200, 400, 400));
        add(button);

        // Upper left fps tracker
        add(new TextView(context, -dx / 2f + 150, -dy / 2f + 50, 300, 100) {
            final Runtime runtime = Runtime.getRuntime();
            final long MB = 1024 * 1024;

            @Override
            protected void update() {
                long memory = (runtime.totalMemory() - runtime.freeMemory()) / MB;

                enableAutoJustification(false);
                setText(getContext().getFrameRate() + " FPS\nMemory usage: " + memory + " MB");
            }
        });
    }
}
