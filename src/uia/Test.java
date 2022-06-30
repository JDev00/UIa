package uia;

import uia.core.*;
import uia.core.event.Mouse;
import uia.core.policy.*;
import uia.core.platform.ContextAWT;
import uia.core.widget.Button;
import uia.core.widget.CalendarView;
import uia.core.widget.HorView;
import uia.core.widget.TextView;
import uia.core.widget.math.BoardView;
import uia.core.widget.math.GraphicView;
import uia.core.widget.math.struct.Curve;
import uia.core.widget.math.struct.VecS;
import uia.utils.Utils;

import java.io.File;
import java.util.Stack;

public class Test extends ContextAWT {

    public Test(int x, int y) {
        super(x, y);
    }

    @Override
    public void asyncSetup(int x, int y) {
        storePage(new Page1(this));

        open(getStoredPage(0));
    }

    public static void main(String[] args) {
        int[] dim = getScreenSize();
        Test test = new Test((int) (0.8f * dim[0]), (int) (0.8f * dim[1]));
        test.start();

        //System.out.println(countLines(new File("src\\")));
    }

    /**
     * Count the number of lines for the given path
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

            String in = Utils.read("src\\uia\\core\\Page.java");

            //in = Utils.read("C:\\Users\\Manu\\Downloads\\Mozilla.txt");


            /*Font[] f = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

            for (Font i : f) {
                System.out.println(i.getFontName());
            }*/


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

            /*
            Image img = loadImage("C:\\Users\\Manu\\Documents\\Personale\\Dolci\\Torrone al cioccolato bianco.jpg");
            int dx = (int) dx();
                    int dy = (int) dy();
                    int px = (int) px() - dx / 2;
                    int py = (int) py() - dy / 2;
                    render.drawImage(img, px, py, dx, dy);
            */

            /*test1.addEvent((State) (v, s) -> {
                if (s == State.FOCUS_GAINED)
                    System.out.println("Gained!");

                if (s == State.FOCUS_LOST)
                    System.out.println("Lost!");
            });*/


            Curve curve = new Curve(context);
            curve.addPlot(100, i -> new VecS(i, i));

            GraphicView g = new BoardView(context, 0, 0, 500, 500);
            g.add(curve);


            TextView test1 = TextView.createFriendly(context, -dx / 2f + 150, 0, 200, 200);
            test1.setFont(context.createFont(test1.getFont(), "Arial", Font.STYLE.BOLD, 30));
            test1.setPaint(context.createColor(test1.getPaint(), 255, 255, 0));
            test1.setAlignX(TextView.AlignX.LEFT);
            test1.setText(in);


            Button button = new Button(context, dx / 2f - 200, 0, 150, 150);
            button.addEvent((Mouse) (v, s) -> {
                if (s == Mouse.CLICK) {
                    boolean b = HINT.SMOOTH.equals(context.getHint());
                    context.setHint(b ? null : HINT.SMOOTH);
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

            // Tracker
            add(new TextView(context, -dx / 2f + 150, -dy / 2f + 50, 300, 100) {
                final Runtime runtime = Runtime.getRuntime();
                final long MB = 1024 * 1024;

                @Override
                protected void update() {
                    long memory = (runtime.totalMemory() - runtime.freeMemory()) / MB;

                    enableAutoJustification(false);
                    setText(getContext().getFrameRate() + " FPS\nUsed memory: " + memory + " MB");
                }
            });
        }
    }
}