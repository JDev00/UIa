package develop;

import uia.application.awt.ContextAWT;
import uia.core.basement.Callback;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnMouseHover;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.scroller.Scroller;
import uia.physical.scroller.WheelScroller;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.physical.wrapper.WrapperView;

import java.util.Iterator;

/**
 * UIListView has been designed to handle a set of views.
 */

public class UIListView extends WrapperView implements ViewGroup {
    /*private final UIScrollBar horBar;
    private final UIScrollBar verBar;*/

    private final ViewGroup containerGroup;
    private final ViewGroup containerList;

    private ViewPositioner viewPositioner;

    private Scroller scroller = new WheelScroller();

    public UIListView(View view) {
        super(new ComponentGroup(view));

        float[] sum = {0f};
        viewPositioner = (v, i) -> {
            float[] bounds = bounds();
            if (bounds[3] != 0) {
                float h = 1.1f * v.bounds()[3] / (2 * bounds[3]);
                if (i == 0) sum[0] = 0.2f;
                sum[0] += h;
                v.setPosition(0.5f, sum[0]);
                sum[0] += h;
            }
        };


        /*verBar = new UIScrollBar(new Component("VERBAR", 0.975f, 0.5f, 0.03f, 0.95f));
        verBar.setConsumer(CONSUMER.POINTER, false);


        horBar = new UIScrollBar(new Component("HORBAR", 0.5f, 0.975f, 0.03f, 0.95f));
        horBar.rotate(-TrigTable.HALF_PI);
        horBar.setConsumer(CONSUMER.POINTER, false);*/


        containerList = new ComponentGroup(new Component("SKELETON", 0.475f, 0.475f, 0.95f, 0.95f)
                .setExpanseLimit(1f, 1f));
        containerList.setClip(false);
        containerList.setConsumer(CONSUMER.POINTER, false);
        containerList.getPaint().setColor(Theme.RED);


        containerGroup = ((ViewGroup) getView());
        containerGroup.add(containerList);//, verBar, horBar);
        containerGroup.addCallback((OnMouseHover) sp -> scroller.update(sp));
    }

    /**
     * ViewPositioner has been designed to position a set of views
     */

    public interface ViewPositioner {

        /**
         * Place the specified View
         *
         * @param view a not null {@link View} to be placed
         * @param i    the View's position (index)
         */

        void place(View view, int i);
    }

    /**
     * OnAdd is called when a new View is added to a ListView
     */

    public interface OnAdd extends Callback<View> {
    }

    /**
     * OnRemove is called when a View is removed from a ListView
     */

    public interface OnRemove extends Callback<View> {
    }

    @Override
    public void setClip(boolean clipRegion) {
        containerGroup.setClip(clipRegion);
    }

    @Override
    public boolean hasClip() {
        return containerList.hasClip();
    }

    @Override
    public void add(View... views) {
        containerList.add(views);
    }

    @Override
    public void add(int i, View view) {
        containerList.add(i, view);

        if (containerList.indexOf(view) != -1)
            notifyCallbacks(OnAdd.class, view);
    }

    @Override
    public void remove(View view) {
        containerList.remove(view);
    }

    @Override
    public void removeAll() {
        containerList.removeAll();
    }

    @Override
    public int size() {
        return containerList.size();
    }

    @Override
    public View get(int i) {
        return containerList.get(i);
    }

    @Override
    public View get(String id) {
        return containerList.get(id);
    }

    @Override
    public int indexOf(View view) {
        return containerList.indexOf(view);
    }

    @Override
    public float[] boundsContent() {
        return containerList.boundsContent();
    }

    @Override
    public Iterator<View> iterator() {
        return containerList.iterator();
    }

    /**
     * Set a new ViewPositioner
     *
     * @param viewPositioner a {@link ViewPositioner}; it could be null
     */

    public void setViewPositioner(ViewPositioner viewPositioner) {
        this.viewPositioner = viewPositioner;
    }

    public void setScroll(float x, float y) {
        //horBar.setScroll(x);
        //verBar.setScroll(y);
    }

    @Override
    public void update(View parent) {
        if (viewPositioner != null) {

            for (int i = 0; i < size(); i++) {
                viewPositioner.place(get(i), i);
            }
        }

        super.update(parent);

        if (isVisible()) {
            float height = containerList.boundsContent()[3];
            scroller.setMax(height);
            scroller.setFactor(height / (2 * containerList.size() + 1));

            containerList.setPosition(0.475f, 0.475f - scroller.getValue() / bounds()[3]);
        }
    }

    //

    public static void main(String[] args) {
        ViewGroup view = new UIListView(new Component("TEST", 0.5f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1f, 1f));
        /*view.addCallback((OnClick) p -> {
            view.setVisible(!view.isVisible());
        });*/
        //view.setClip(false);

        for (int i = 0; i < 20; i++) {
            view.add(createView(i));
        }

        Context context = new ContextAWT(1800, 900);
        context.start();
        context.setView(view);
    }

    private static View createView(int number) {
        ViewText out = new ComponentText(new Component("TEXT", 0f, 0f, 0.9f, 0.2f)
                .setExpanseLimit(1.1f, 1.1f));
        out.setText(number + " HELLO!");
        out.setAlign(ViewText.AlignY.CENTER);
        out.getPaint().setColor(ThemeDarcula.W_FOREGROUND);
        out.setConsumer(CONSUMER.POINTER, false);
        return out;
    }
}
