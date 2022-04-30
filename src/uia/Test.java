package uia;

import uia.core.*;
import uia.core.utility.TextSuite;
import uia.core.widget.TextView;
import uia.core.ViewGroup;
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
        storePage(new Page1(this));

        open(getStoredPage(0));
    }

    public static void main(String[] args) {
        Dimension dim = Context.getScreenSize();
        Test test = new Test((int) (0.8f * dim.width), (int) (0.8f * dim.height));
        test.start();

        Test test1 = new Test((int) (0.8f * dim.width), (int) (0.8f * dim.height));
        test1.start();

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
            /*test2.addEvent((State) (v, s) -> {
                if (s == State.FOCUS_GAINED)
                    System.out.println("Gained!");

                if (s == State.FOCUS_LOST)
                    System.out.println("Lost!");
            });*/

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


            ViewGroup<View> viewGroup1 = new ViewGroup<View>(context, -200, 0, 300, 300) {
            };
            viewGroup1.setColor(Color.RED);
            viewGroup1.add(ViewGroup.LAST, test2);

            /*add(viewGroup2);*/

            add(test2);
        }
    }
}
