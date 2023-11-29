package uia.develop;

import uia.application.UIScrollbar;
import uia.application.desktop.ContextSwing;
import uia.core.basement.Callback;
import uia.core.ui.callbacks.OnClick;
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
    private final UIScrollbar horizontalBar;
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
                new Component("LISTVIEW_VERTICAL_BAR_" + getID(), 0.975f, 0.5f, 0.03f, 0.98f)
                        .setMaxWidth(10),
                true
        );
        verticalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        verticalBar.getPaint().setColor(Theme.BLACK);
        verticalBar.setVisible(false);

        /*registerCallback((OnMouseHover) touches -> {
            ScreenTouch screenTouch = touches.get(0);
            //System.out.println(screenTouch.getWheelRotation());
            //verticalBar.scroll(0.1f * screenTouch.getWheelRotation());
        });*/

        horizontalBar = new UIScrollbar(
                new Component("HORBAR", 0.5f, 0.975f, 0.9f, 0.05f)
                        .setMaxHeight(10f),
                false
        );
        horizontalBar.setConsumer(Consumer.SCREEN_TOUCH, false);
        horizontalBar.setVisible(false);

        viewsContainer = new ComponentGroup(
                new Component("LISTVIEW_SKELETON_" + getID(), 0.475f, 0.475f, 0.95f, 0.95f)
        );
        viewsContainer.setClip(false);
        viewsContainer.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewsContainer.getPaint().setColor(Theme.TRANSPARENT);

        containerGroup = getView();
        ViewGroup.insert(containerGroup, viewsContainer, horizontalBar, verticalBar);
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
    public boolean insert(int i, View view) {
        boolean result = viewsContainer.insert(i, view);
        if (result) {
            notifyCallbacks(OnAdd.class, view);
        }
        return result;
    }

    @Override
    public boolean remove(View view) {
        boolean result = viewsContainer.remove(view);
        if (result) {
            notifyCallbacks(OnRemove.class, view);
        }
        return result;
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

    /**
     * Define me!
     */

    public void setScrollValue(float x, float y) {
        horizontalBar.setValue(x);
        verticalBar.setValue(y);
    }

    /**
     * Helper function. Updates the view positioner.
     */

    private void updatePositioner() {
        if (viewPositioner != null) {
            for (int i = 0; i < size(); i++) {
                viewPositioner.place(get(i), i);
            }
        }
    }

    float barWidth = 0f;
    float barHeight = 0f;

    @Override
    public void update(View parent) {
        updatePositioner();

        horizontalBar.setInternalBarSize(barWidth);
        verticalBar.setInternalBarSize(barHeight);
        super.update(parent);

        if (isVisible()) {
            horizontalBar.setVisible(barWidth < 1f);
            verticalBar.setVisible(barHeight < 1f);

            float[] bounds = viewsContainer.boundsContent();
            float width = bounds[2];
            float height = bounds[3];
            float offsetX = Math.max(0f, width / bounds()[2] - 1f);
            float offsetY = Math.max(0f, height / bounds()[3] - 1f);
            barWidth = 1f / (offsetX + 1f);
            barHeight = 1f / (offsetY + 1f);

            horizontalBar.setMaxValue(width);
            verticalBar.setMaxValue(height);

            float vx = verticalBar.isVisible() ? 0.475f : 0.5f;

            viewsContainer.setPosition(
                    vx - horizontalBar.getValue() / bounds()[2],
                    0.475f - verticalBar.getValue() / bounds()[3]
            );
        }
    }

    /*public static ViewPositioner createVerticalPositioner() {
        float[] sum = {0f};
        return (v, i) -> {
            float[] bounds = bounds();
            if (bounds[3] != 0) {
                float h = 1.0f * v.bounds()[3] / (2 * bounds[3]);
                if (i == 0) sum[0] = 0f;
                sum[0] += h;
                v.setPosition(0.5f, sum[0]);
                sum[0] += h;
            }
        };
    }*/

    public static void main(String[] args) {
        UIListView group = new UIListView(
                new Component("TEST", 0.5f, 0.5f, 0.5f, 0.5f).setMaxHeight(300)
        );
        group.registerCallback((OnClick) touches -> {
            ViewGroup.insert(group, createView(-1));
        });

        for (int i = 0; i < 10; i++) {
            ViewGroup.insert(group, createView(i));
        }

        Context context = ContextSwing.createAndStart(1000, 700);
        context.setView(group);
    }

    private static View createView(int number) {
        ViewText out = new ComponentText(
                new Component("" + number, 0f, 0f, 0.9f, 0.1f)
        );
        out.setText(number + " HELLO!");
        out.setAlign(ViewText.AlignY.CENTER);
        out.getPaint().setColor(ThemeDarcula.W_FOREGROUND);
        out.setConsumer(Consumer.SCREEN_TOUCH, false);
        return out;
    }
}
