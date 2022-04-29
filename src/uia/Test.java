package uia;

import uia.core.*;
import uia.core.event.Mouse;
import uia.core.event.State;
import uia.core.shape.Arrow;
import uia.core.utility.TextSuite;
import uia.core.widget.TextView;
import uia.core.ViewGroup;
import uia.core.widget.math.BoardView;
import uia.core.widget.math.GraphicView;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.utils.PageSaver;
import uia.utils.Utils;

import java.awt.*;
import java.io.File;
import java.util.Stack;

public class Test extends StdContext {

    public Test(int x, int y) {
        super(x, y);
    }

    @Override
    public void asyncSetup(int x, int y) {
        PageSaver.push(new Page1(this));
        PageSaver.push(new Page2(this));

        open(PageSaver.get(0));
    }

    public static void main(String[] args) {
        Dimension dim = Context.getScreenSize();
        Test test = new Test((int) (0.8f * dim.width), (int) (0.8f * dim.height));
        test.start();

        //System.out.println(countLines(new File("src\\")));
    }

    /**
     * Counts the number of lines for the given path
     */

    private static int countLines(File file) {
        Stack<File> stack = new Stack<>();
        stack.push(file);

        int out = 0;
        while (!stack.empty()) {
            File pop = stack.pop();
            File[] list = pop.listFiles();

            if (list != null) {

                for (File i : list) {
                    stack.push(i);
                }
            } else {
                out += Utils.readLines(pop.getAbsolutePath()).size();
            }
        }

        return out;
    }

    /*
     *
     *
     */

    public static class Page1 extends Page {

        public Page1(Context context) {
            super(context);

            float dx = dx();
            float dy = dy();

            String in = Utils.read("src\\uia\\core\\Context.java");

            /*Menu test = new Menu(0, 0, 100, 40);
            test.setVisible(true);
            test.setColor(Color.GREEN);
            test.addEvent((Menu.Element) (v, s) -> {
                if (s == Mouse.CLICK)
                    System.out.format("Clicked! %s\n", test.getUserOver().getTextView().getText());
            });

            for (int i = 0; i < 10; i++) {
                Button b = test.add(100, 100, "ciao");
                b.enableTouchConsumer(false);
                b.setText(String.valueOf(i));
                b.addEvent((Mouse) (v, s) -> {
                    if (s == Mouse.CLICK)
                        System.out.format("Clicked! %s\n", b.getTextView().getText());
                });

                //test.add(ViewGroup.LAST, textView);
                //test.addToDraw(0);
            }*/


            TextView test2 = new TextView(context, -90, 0, 200, 200);
            test2.addEvent((State) (v, s) -> {
                if (s == State.FOCUS_GAINED)
                    System.out.println("Gained!");

                if (s == State.FOCUS_LOST)
                    System.out.println("Lost!");

                //v.setColor(v.isFocused() ? Color.BLUE : Color.WHITE);
            });

            test2.addText(in);

            TextSuite textSuite = test2.getTextSuite();
            textSuite.setAlignX(TextSuite.AlignX.LEFT);
            /*test2.addEvent((Mouse) (v, s) -> {
                View.MotionEvent e = v.getMotionEvent();

                if (s == Mouse.CLICK)
                    System.out.println("CLICK!");

                if (s == Mouse.ENTER)
                    System.out.println("ENTER!");

                if (s == Mouse.HOVER)
                    System.out.println("HOVER!");

                if (s == Mouse.LEAVE)
                    System.out.println("LEAVE!");
            });*/

            View test3 = new View(context, 0, 0, 100, 100);
            test3.setExpansion(0f, 0f);
            test3.setFigure(new Arrow(true));
            test3.addEvent((Mouse) (v, s) -> {
                if (s == Mouse.CLICK)
                    context.open(PageSaver.peek());
            });


            ViewGroup<View> viewGroup1 = new ViewGroup<View>(context, -200, 0, 300, 300) {
            };
            viewGroup1.setColor(Color.RED);
            viewGroup1.add(ViewGroup.LAST, test2);

            ViewGroup<View> viewGroup2 = new ViewGroup<View>(context, 200, 0, 300, 300) {
            };
            viewGroup2.setColor(Color.GREEN);
            viewGroup2.add(ViewGroup.LAST, test3);

            /*add(viewGroup2);
            add(viewGroup1);*/

            Curve curve = Curve.create(100, i -> new VecS(i, (float) Math.sqrt(i), null));
            curve.analyse();
            curve.drawVariance = true;
            curve.drawPoints = false;
            curve.colorLine = Color.GREEN;

            Curve curve2 = Curve.create(100, i -> new VecS(i, -(float) Math.sqrt(i), null));
            curve2.analyse();
            curve2.colorVariance = new Color(255, 0, 0, 200);
            curve2.drawVariance = true;
            curve2.drawPoints = false;
            curve2.colorLine = Color.GREEN;

            GraphicView test4 = new BoardView(context, 0, 0, 400, 400);
            test4.add(curve);
            test4.add(curve2);

            add(viewGroup1);
            add(viewGroup2);
        }
    }

    /*
     *
     */

    private static class Page2 extends Page {

        public Page2(StdContext context) {
            super(context);
            super.setColor(Color.BLUE);

            View view = new View(context, -150, 0, 300, 300) {

                @Override
                protected void updateState() {
                    System.out.println(isMouseOver());
                }
            };
            view.setExpansion(1f, 1f);
            view.setFigure(new Arrow(false));
            view.addEvent((Mouse) (v, s) -> {
                if (s == Mouse.CLICK)
                    context.open(PageSaver.get(0));
            });

            add(view);
        }
    }
}
