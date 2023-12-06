package uia.application;

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
import java.util.Objects;

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

        viewPositioner = createVerticalPositioner(this, 1.01f);

        verticalBar = new UIScrollbar(
                new Component("LISTVIEW_VERTICAL_BAR_" + getID(), 0.975f, 0.5f, 0.03f, 0.98f)
                        .setMaxWidth(10),
                true
        );
        verticalBar.getPaint().setColor(Theme.BLACK);
        verticalBar.setVisible(false);

        horizontalBar = new UIScrollbar(
                new Component("LISTVIEW_HORIZONTAL_BAR_" + getID(), 0.5f, 0.975f, 0.9f, 0.05f)
                        .setMaxHeight(10f),
                false
        );
        horizontalBar.getPaint().setColor(Theme.BLACK);
        horizontalBar.setVisible(false);

        viewsContainer = new ComponentGroup(
                new Component("LISTVIEW_SKELETON_" + getID(), 0.475f, 0.475f, 0.95f, 0.95f)
        );
        viewsContainer.setClip(false);
        viewsContainer.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewsContainer.getPaint().setColor(Theme.TRANSPARENT);

        containerGroup = getView();
        ViewGroup.insert(containerGroup, viewsContainer, horizontalBar, verticalBar);

        /*registerCallback((OnMouseHover) touches -> {
            ScreenTouch screenTouch = touches.get(0);
            //System.out.println(screenTouch.getWheelRotation());
            //verticalBar.scroll(0.1f * screenTouch.getWheelRotation());
        });*/
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
            float[] bounds = bounds();
            float[] boundsContent = viewsContainer.boundsContent();
            float width = Math.max(0f, boundsContent[2] - bounds[2]);
            float height = Math.max(0f, boundsContent[3] - bounds[3]);
            float offsetX = Math.max(0f, width / bounds[2]);
            float offsetY = Math.max(0f, height / bounds[3]);

            barWidth = 1f / (offsetX + 1f);
            barHeight = 1f / (offsetY + 1f);

            horizontalBar.setVisible(barWidth < 1f);
            horizontalBar.setMaxValue(width);

            verticalBar.setVisible(barHeight < 1f);
            verticalBar.setMaxValue(height);

            float vx = verticalBar.isVisible() ? 0.475f : 0.5f;

            viewsContainer.setPosition(
                    vx - horizontalBar.getValue() / bounds()[2],
                    0.475f - verticalBar.getValue() / bounds()[3]
            );
        }
    }

    /**
     * Creates a vertical {@link ViewPositioner}
     *
     * @param group the {@link ViewGroup} container
     * @param gap   a value (>= 1) used to space views
     * @return a new vertical {@link ViewPositioner}
     * @throws NullPointerException if {@code group == null}
     */

    public static ViewPositioner createVerticalPositioner(ViewGroup group, float gap) {
        Objects.requireNonNull(group);
        float[] sum = {0f};
        float[] bounds = group.bounds();
        return (v, i) -> {
            if (bounds[3] != 0) {
                float h = gap * v.bounds()[3] / (2 * bounds[3]);
                if (i == 0) sum[0] = 0f;
                sum[0] += h;
                v.setPosition(0.5f, sum[0]);
                sum[0] += h;
            }
        };
    }

    /**
     * Creates a horizontal {@link ViewPositioner}
     *
     * @param group the {@link ViewGroup} container
     * @param gap   a value (>= 1) used to space views
     * @return a new horizontal {@link ViewPositioner}
     * @throws NullPointerException if {@code group == null}
     */

    public static ViewPositioner createHorizontalPositioner(ViewGroup group, float gap) {
        Objects.requireNonNull(group);
        float[] sum = {0f};
        float[] bounds = group.bounds();
        return (v, i) -> {
            if (bounds[3] != 0) {
                float w = gap * v.bounds()[2] / (2 * bounds[2]);
                if (i == 0) sum[0] = 0f;
                sum[0] += w;
                v.setPosition(sum[0], 0.5f);
                sum[0] += w;
            }
        };
    }

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
