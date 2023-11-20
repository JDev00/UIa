package uia.develop;

import uia.application.UIScrollbar;
import uia.application.desktop.ContextSwing;
import uia.core.ScreenTouch;
import uia.core.basement.Callback;
import uia.core.ui.callbacks.OnMouseHover;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.physical.WrapperView;

import java.util.Iterator;

/**
 * UIListView has been designed to handle a set of views.
 * It automatically provides a vertical and a horizontal scrollbar.
 */

public class UIListView extends WrapperView implements ViewGroup {
    /*private final UIScrollBar horBar;*/
    private final UIScrollbar verticalBar;
    private final ViewGroup containerGroup;
    private final ViewGroup viewsContainer;
    private ViewPositioner viewPositioner;

    public UIListView(View view) {
        super(new ComponentGroup(view));

        float[] sum = {0f};
        viewPositioner = (v, i) -> {
            float[] bounds = bounds();
            if (bounds[3] != 0) {
                float h = 1.0f * v.bounds()[3] / (2 * bounds[3]);
                if (i == 0) sum[0] = 0f;
                sum[0] += h;
                v.setPosition(0.5f, sum[0]);
                sum[0] += h;
            }
        };

        verticalBar = new UIScrollbar(
                new Component("LISTVIEW_VERTICAL_BAR_" + getID(), 0.975f, 0.5f, 0.03f, 0.95f)
                        .setMaxWidth(10),
                true
        );
        verticalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        verticalBar.getPaint().setColor(Theme.BLACK);

        registerCallback((OnMouseHover) touches -> {
            ScreenTouch screenTouch = touches.get(0);
            verticalBar.scroll(0.1f * screenTouch.getWheelRotation());
        });

        /*horBar = new UIScrollBar(new Component("HORBAR", 0.5f, 0.975f, 0.03f, 0.95f));
        horBar.rotate(-TrigTable.HALF_PI);
        horBar.setConsumer(CONSUMER.POINTER, false);*/

        viewsContainer = new ComponentGroup(
                new Component("LISTVIEW_SKELETON_" + getID(), 0.475f, 0.475f, 0.95f, 0.95f)
        );
        viewsContainer.setClip(false);
        viewsContainer.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewsContainer.getPaint().setColor(Theme.TRANSPARENT);

        containerGroup = getView();
        containerGroup.add(viewsContainer, verticalBar);
    }

    /**
     * ViewPositioner has been designed to position a set of views
     */

    public interface ViewPositioner {

        /**
         * Place the specified View
         *
         * @param view a not null {@link View} to be placed
         * @param i    the View position (index)
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
        return viewsContainer.hasClip();
    }

    @Override
    public void add(View... views) {
        viewsContainer.add(views);
    }

    @Override
    public void add(int i, View view) {
        viewsContainer.add(i, view);
        if (viewsContainer.indexOf(view) != -1) {
            notifyCallbacks(OnAdd.class, view);
        }
    }

    @Override
    public void remove(View view) {
        viewsContainer.remove(view);
    }

    @Override
    public void removeAll() {
        viewsContainer.removeAll();
    }

    @Override
    public int size() {
        return viewsContainer.size();
    }

    @Override
    public View get(int i) {
        return viewsContainer.get(i);
    }

    @Override
    public View get(String id) {
        return viewsContainer.get(id);
    }

    @Override
    public int indexOf(View view) {
        return viewsContainer.indexOf(view);
    }

    @Override
    public float[] boundsContent() {
        return viewsContainer.boundsContent();
    }

    @Override
    public Iterator<View> iterator() {
        return viewsContainer.iterator();
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
            float height = viewsContainer.boundsContent()[3];
            //scroller.setMax(height);
            //scroller.setFactor(height / (2 * containerList.size() + 1));

            viewsContainer.setPosition(0.475f, 0.475f - verticalBar.getValue());
            //scroller.getValue() / bounds()[3]);
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

        Context context = new ContextSwing(1800, 900);
        context.setLifecycleStage(Context.LifecycleStage.RUN);
        context.setView(view);
    }

    private static View createView(int number) {
        ViewText out = new ComponentText(new Component("TEXT", 0f, 0f, 0.9f, 0.2f)
                .setExpanseLimit(1.1f, 1.1f));
        out.setText(number + " HELLO!");
        out.setAlign(ViewText.AlignY.CENTER);
        out.getPaint().setColor(ThemeDarcula.W_FOREGROUND);
        out.setConsumer(Consumer.SCREEN_TOUCH, false);
        return out;
    }
}
