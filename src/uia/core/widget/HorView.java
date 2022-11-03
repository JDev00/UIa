package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.policy.Context;
import uia.core.widget.text.TextView;
import uia.utils.TrigTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UI horizontal list with scroller function already attached.
 * <br>
 * HorView is composed of three elements:
 * <ul>
 * <li>A TextView used to draw the current string</li>
 * <li>A View, by default located on the left, used to navigate to the previous string</li>
 * <li>A View, by default located on the right, used to navigate to the next string</li>
 * </ul>
 */

public class HorView extends Widget {
    private final IconView view1;
    private final IconView view2;
    private final TextView tView;

    private final List<String> list;

    private int index;

    public HorView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setClipping(false);
        super.setPointersConstrain(false);
        getEventQueue().addEvent((v, s) -> {// built-in scroller
            if (s == Event.POINTER_HOVER) {
                PointerEvent e = v.getPointerEvent();

                if (e.isMouseWheeling()) {
                    int c = e.getWheelRotation();
                    if (c > 0) {
                        showNext();
                    } else if (c < 0) showPrev();
                }
            }
        });

        view1 = new IconView(context, 0, 0, 0, 0);
        view1.buildShape(Figure::buildArrow2, false);
        view1.getPaint().setColor(0, 0, 0);
        view1.setExpansion(0.1f, 0.1f);
        view1.setText("Previous");
        view1.setAABBContainer(true);
        view1.setPointerConsumer(false);
        view1.setPositionAdjustment(false);
        view1.setDimensionAdjustment(false);
        view1.setRotation(TrigTable.PI);
        view1.getEventQueue().addEvent((v, s) -> {
            if (s == Event.POINTER_CLICK) showPrev();
        });

        view2 = new IconView(context, 0, 0, 0, 0);
        view2.buildShape(Figure::buildArrow2, false);
        view2.getPaint().setColor(0, 0, 0);
        view2.setExpansion(0.1f, 0.1f);
        view2.setText("Next");
        view2.setAABBContainer(true);
        view2.setPointerConsumer(false);
        view2.setPositionAdjustment(false);
        view2.setDimensionAdjustment(false);
        view2.getEventQueue().addEvent((v, s) -> {
            if (s == Event.POINTER_CLICK) showNext();
        });

        tView = new TextView(context, 0, 0, 1, 1);
        tView.getPaint().setColor(0, 0, 0, 1);
        tView.setAlignY(TextView.AlignY.CENTER);
        tView.setPositionAdjustment(false);
        tView.setDimensionAdjustment(false);

        list = new ArrayList<>();

        set();

        add(view1);
        add(view2);
        add(tView);
    }

    /**
     * Fill this view with an array of strings and displays, by default, the first element
     *
     * @param strings a not null array of strings
     */

    public void set(String... strings) {
        if (strings != null) {
            index = 0;
            list.clear();

            if (strings.length == 0) {
                list.add("Empty!");
            } else {
                list.addAll(Arrays.asList(strings));
            }

            tView.setText(list.get(0));
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
            tView.setText(list.get(i));
        }
    }

    /**
     * Draw the next String
     */

    public void showNext() {
        if (list.size() > 0) {
            index++;
            if (index >= list.size()) index = 0;
            tView.setText(list.get(index));
        }
    }

    /**
     * Draw the previous String
     */

    public void showPrev() {
        if (list.size() > 0) {
            index--;
            if (index < 0) index = list.size() - 1;
            tView.setText(list.get(index));
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
        tView.setText(text);
    }

    /**
     * Show or hide the left View and/or the right one
     *
     * @param show1 true if the (default) left view has to be visible
     * @param show2 true if the (default) right view has to be visible
     */

    public void setVisible(boolean show1, boolean show2) {
        view1.setVisible(show1);
        view2.setVisible(show2);
    }

    @Override
    protected void update() {
        super.update();

        float width = width();
        float height = height();
        float vHeight = Math.min(0.75f * height, 1.225f * tView.getFont().getSize());
        float vWidth = Math.min(0.1f * width, vHeight);

        view1.setDimension(vWidth, vHeight);
        view1.setPosition(width / 6f + view1.boundsStatic()[2], 0f);

        view2.setDimension(vWidth, vHeight);
        view2.setPosition(width / 2f - view1.boundsStatic()[2], 0f);

        tView.setDimension(2 * width / 3f, height);
        tView.setPosition(-width / 6f, 0f);
    }

    /**
     * @return the amount of stored strings
     */

    public int strings() {
        return list.size();
    }

    /**
     * @return the position of the current rendered String
     */

    public int getIndex() {
        return index;
    }

    /**
     * @return the stored element specified by the given position
     */

    public String getString(int i) {
        return list.get(i);
    }

    /**
     * @return the (default) left {@link View}
     */

    public View getView1() {
        return view1;
    }

    /**
     * @return the (default) right {@link View}
     */

    public View getView2() {
        return view2;
    }

    /**
     * @return the associated {@link TextView}
     */

    public TextView getTextView() {
        return tView;
    }
}
