package uia.core.example;

import uia.core.Page;
import uia.core.platform.independent.Font;
import uia.core.platform.independent.Image;
import uia.core.platform.policy.Context;
import uia.core.widget.*;
import uia.core.widget.math.BoardView;
import uia.core.widget.math.DistributionView;
import uia.core.widget.math.data.DistributionDrawable;
import uia.core.widget.text.EditView;
import uia.core.widget.text.TextView;

public class Page2 extends Page {

    public Page2(Context context) {
        super(context);

        context.setHint(Context.HINT.SMOOTH_TEXT);

        float width = width();
        float height = height();

        TextView view1 = new EditView(context, 0f, 0f, 600f, 600f);
        //view1.setSingleLine(true);
        view1.setAlignY(TextView.AlignY.TOP);
        view1.setAlignX(TextView.AlignX.LEFT);
        view1.setDescription("Ciao!");
        view1.setText("abcde come");
        view1.getFont().setSize(40);


        Button view2 = new Button(context, 0f, 0f, 0.1f * width, 0.1f * width);
        view2.getPaint()
                .setColor(255, 0, 0)
                .enableStrokes(true);
        view2.getPaintEnabled()
                .setColor(0, 255, 0)
                .enableStrokes(true)
                .setStrokesWidth(5);
        view2.setText("Ciao! come va a0\n ritlu rtrut iurtiut\n yiuryt uiry itry iutyruity ui");


        HorView view3 = new HorView(context, 0f, 0f, 250f, 100f);
        view3.set("1", "2", "3", "4");


        BarView view4 = new BarView(context, 0.33f * width, -0.25f * height, 125f, 300f);
        view4.setVertical(false);


        CalendarView view5 = new CalendarView(context, 0f, 0f, 0.2f * width, 0.2f * width);
        view5.setMultipleSelection(true);
        view5.getEventQueue()
                .addEvent(view5.colorSundays(255, 0, 0, 255, 255, 255));


        DistributionView view6 = new BoardView(context, 0, 0, 0.5f * width, 0.5f * height);
        view6.getDistribution(0)
                .add(new float[]{0, 0})
                .add(i -> new float[]{100 + i, 30 + (float) Math.log(i + 1) * i}, 1, 30);
        view6.getDistribution(1)
                .add(new float[]{0, 0})
                .add(i -> new float[]{100 + i, 30 + (float) Math.sqrt(i + 1) * i}, 2, 30);
        view6.setDrawable(0, new DistributionDrawable()
                .setColorLine(255, 100, 255)
                .setLineWidth(2));


        String a = "9338 8974";//Utils.read("src\\uia\\core\\Page.java");

        ListView view7 = new ListView(context, 0f, 0f, 0.25f * width, 0.5f * height);
        view7.populate(i -> {
            TextView view = new EditView(context, 0f, 0f, 0.25f * width, 50f);
            view.setPositionAdjustment(false);
            view.setDimensionAdjustment(false);
            view.setText("(" + (i + 1) + ") " + a);
            view.getPaint().setColor((int) (255 * Math.random()), 255, (int) (255 * Math.random()));
            return view;
        }, 40000);


        IconView view8 = new IconView(context, 0.5f * width - 100f, 0f, 200f, 200f);
        view8.setText("Ciao, come va!");
        view8.setImage(new Image("sample\\ImgTest1.jpg"));


        BarScrollView view9 = new BarScrollView(context, 0f, 0f, 100f, 200f);
        view9.setRange(0f, 100f);


        BarView view10 = new BarView(context, 0f, 0f, 100f, 200f);
        view10.setRange(0f, 10f);


        TextView tracker = new TextView(context, -0.45f * width, -0.46f * height, 0.1f * width, 0.08f * height) {
            @Override
            public void update() {
                super.update();
                long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                setText("FPS: " + getContext().getFrameRate() + "\n Mem: " + mem + " MB");
            }
        };
        tracker.getFont()
                .setName(context.fontList()[0])
                .setStyle(Font.STYLE.BOLD)
                .setSize(20f);


        add(view7);
        add(tracker);
    }
}
