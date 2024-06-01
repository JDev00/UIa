package uia.application.list;

import uia.application.list.positioner.ViewPositionerFactory;
import uia.application.list.positioner.ViewPositioner;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.component.Component;
import uia.application.UIScrollbar;
import uia.physical.theme.Theme;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

import java.util.Iterator;

/**
 * Standard UIa component.
 * <br>
 * UIListView has been designed to handle a list of views.
 * <br>
 * Specifically, it is a layout with vertical and horizontal scrollbars.
 */

public final class UIListView extends WrapperView implements ViewGroup {
    private final UIScrollbar horizontalBar;
    private final UIScrollbar verticalBar;
    private final ViewGroup containerGroup;
    private final ViewGroup viewsContainer;
    private ViewPositioner viewPositioner;

    private float barWidth = 0f;
    private float barHeight = 0f;

    public UIListView(View view) {
        super(new ComponentGroup(view));

        viewPositioner = ViewPositionerFactory.create(this, 1.01f, true);

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
            notifyCallbacks(OnViewAdded.class, view);
        }
        return result;
    }

    @Override
    public boolean remove(View view) {
        boolean result = viewsContainer.remove(view);
        if (result) {
            notifyCallbacks(OnViewRemoved.class, view);
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
     * Sets a new ViewPositioner.
     *
     * @param viewPositioner a {@link ViewPositioner}; it could be null
     */

    public void setViewPositioner(ViewPositioner viewPositioner) {
        this.viewPositioner = viewPositioner;
    }

    /**
     * Sets the current scroll value.
     *
     * @param x the scroll value on the x-axis between [0, group content width]
     * @param y the scroll value on the y-axis between [0, group content height]
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

    @Override
    public void update(View parent) {
        horizontalBar.setInternalBarSize(barWidth);
        verticalBar.setInternalBarSize(barHeight);
        super.update(parent);

        // TODO: quick fix. It needs to be studied further.
        updatePositioner();
        super.update(parent);

        if (isVisible()) {
            float[] bounds = getBounds();
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
                    vx - horizontalBar.getValue() / getBounds()[2],
                    0.475f - verticalBar.getValue() / getBounds()[3]
            );
        }
    }
}