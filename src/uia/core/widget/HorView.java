package uia.core.widget;

import uia.core.View;
import uia.core.Widget;
import uia.core.event.Mouse;
import uia.core.geometry.Arrow;
import uia.core.policy.Context;
import uia.core.policy.Render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uia.utils.Utils.*;

/**
 * Static horizontal list.
 * <br>
 * The Widget is composed of three elements (ordered from the first added):
 * <ul>
 * <li>A TextView used to draw the current String</li>
 * <li>A left View used to navigate to the previous string</li>
 * <li>A right View used to navigate to the next string</li>
 * </ul>
 */

public class HorView extends Widget {
    public static final int TEXT = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private final View left;
    private final View right;
    private final TextView textView;

    private final List<String> list;

    private int index;

    private float xScaleLeft = 0.3f;
    private float yScaleLeft = 0.8f;
    private float xScaleRight = 0.3f;
    private float yScaleRight = 0.8f;

    public HorView(Context context,
                   float px, float py,
                   float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setExpansion(0f, 0f);

        left = new View(context, 0, 0, 0, 0);
        left.enableAutoAdjustment(false);
        left.setExpansion(0.1f, 0.1f);
        left.setFigure(new Arrow(false));
        left.setPaint(context.createColor(left.getPaint(), Context.COLOR.BLACK));
        left.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.CLICK)
                showPrev();
        });

        right = new View(context, 0, 0, 0, 0);
        right.enableAutoAdjustment(false);
        right.setExpansion(0.1f, 0.1f);
        right.setFigure(new Arrow(true));
        right.setPaint(context.createColor(right.getPaint(), Context.COLOR.BLACK));
        right.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.CLICK)
                showNext();
        });

        textView = new TextView(context, 0, 0, 1, 1);
        textView.enableAutoAdjustment(false);
        textView.setAlignY(TextView.AlignY.CENTER);
        textView.setPaint(context.createColor(textView.getPaint(), Context.COLOR.STD_NO_PAINT));
        textView.setText("No data!");

        list = new ArrayList<>();

        add(textView);
        add(left);
        add(right);
    }

    /**
     * Fills this view with a given array of strings and displays, by default, the first element
     *
     * @param strings a non-null array of strings
     */

    public void set(String... strings) {
        if (strings != null) {
            index = 0;

            list.clear();

            if (strings.length == 0) {
                list.add("Empty");
            } else {
                list.addAll(Arrays.asList(strings));
            }

            textView.setText(list.get(0));
        }
    }

    /**
     * Draw the specified String
     *
     * @param i the position of the String to draw
     */

    public void show(int i) {
        if (i >= 0 && i < list.size()) {
            index = i;
            textView.setText(list.get(i));
        }
    }

    /**
     * Draw the next String
     */

    public void showNext() {
        if (list.size() > 0) {
            index++;

            if (index >= list.size())
                index = 0;

            textView.setText(list.get(index));
        }
    }

    /**
     * Draw the previous String
     */

    public void showPrev() {
        if (list.size() > 0) {
            index--;

            if (index < 0)
                index = list.size() - 1;

            textView.setText(list.get(index));
        }
    }

    /**
     * Set a String to draw.
     * <br>
     * Note that this method will not affect the stored strings.
     *
     * @param text a not null String
     */

    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * Scale the left View
     *
     * @param x a value between [0, 1] used to scale along x-axis
     * @param y a value between [0, 1] used to scale along y-axis
     */

    public void setLeftScale(float x, float y) {
        xScaleLeft = constrain(x, 0, 1);
        yScaleLeft = constrain(y, 0, 1);
    }

    /**
     * Scale the right View
     *
     * @param x a value between [0, 1] used to scale along x-axis
     * @param y a value between [0, 1] used to scale along y-axis
     */

    public void setRightScale(float x, float y) {
        xScaleRight = constrain(x, 0, 1);
        yScaleRight = constrain(y, 0, 1);
    }

    /**
     * Show or hide the left View and/or the right one
     *
     * @param left  true if the left view has to be visible
     * @param right true if the right view has to be visible
     */

    public void setVisible(boolean left, boolean right) {
        this.left.setVisible(left);
        this.right.setVisible(right);
    }

    @Override
    public void enableOverlay(boolean touchConsumer) {
        super.enableOverlay(touchConsumer);
        left.enableOverlay(touchConsumer);
        right.enableOverlay(touchConsumer);
        textView.enableOverlay(touchConsumer);
    }

    @Override
    protected void postDraw(Render render) {
        float px = px();
        float py = py();
        float dx = dx();
        float dy = dy();

        //System.out.println(index);

        left.setDim(xScaleLeft * dy, yScaleLeft * dy);
        left.setPos(px - dx / 2 + left.dx(), py);

        right.setDim(xScaleRight * dy, yScaleRight * dy);
        right.setPos(px + dx / 2 - right.dx(), py);

        textView.setDim(dx - 2f * dy / 5, dy);
        textView.setPos(px, py);

        super.postDraw(render);
    }

    /**
     * @return the amount of stored strings
     */

    public final int sizeStrings() {
        return list.size();
    }

    /**
     * @return the position of the current rendered String
     */

    public final int getIndex() {
        return index;
    }

    /**
     * @return the stored element specified by the given position
     */

    public final String getString(int i) {
        return list.get(i);
    }
}
