package uia.application;

import uia.core.shape.Geometry;
import uia.core.Paint;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.physical.theme.Theme;
import uia.physical.ComponentGroup;
import uia.physical.WrapperView;
import uia.physical.Component;
import uia.utility.TrigTable;
import uia.utility.Utility;

import java.util.Objects;

/**
 * Standard UIa component.
 * <br>
 * Progressbar is a widget used to display the progress of a task.
 * <br>
 * It consists of two elements:
 * <ul>
 *     <li>an external bar that gives the progressbar its shape;</li>
 *     <li>an internal bar that displays the progress.</li>
 * </ul>
 * <br>
 *
 * @apiNote Designed to support rotation
 */

public final class UIProgressbar extends WrapperView {
    private final View internalBar;

    private float value;
    private float min;
    private float max;

    public UIProgressbar(View view) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(Theme.DARK_GRAY);

        internalBar = new Component("PROGRESSBAR_INTERNAL_BAR_" + getID(), 0.5f, 0.5f, 1f, 1f);
        internalBar.getPaint().setColor(Theme.LIME);

        ViewGroup group = getView();
        group.setClip(true);
        ViewGroup.insert(group, internalBar);

        setRange(0f, 1f);
        setValue(value);
    }

    /**
     * @return the internal bar {@link Paint}
     */

    public Paint getInternalBarPaint() {
        return internalBar.getPaint();
    }

    /**
     * Set the progressbar value range
     *
     * @param min the minimum value
     * @param max the maximum value
     */

    public void setRange(float min, float max) {
        if (min < max) {
            this.min = min;
            this.max = max;
            setValue(value);
        }
    }

    /**
     * Set the progressbar value
     *
     * @param value the progressbar value between [min, max]
     */

    public void setValue(float value) {
        this.value = Utility.constrain(value, min, max);
    }

    /**
     * Helper function. Updates the internal bar geometry.
     */

    private void updateInternalBarGeometry() {
        float xVertex = value - 0.5f;
        Geometry internalBarGeometry = internalBar.getGeometry().removeAllVertices();
        internalBarGeometry.addVertices(
                -0.5f, -0.5f,
                xVertex, -0.5f,
                xVertex, 0.5f,
                -0.5f, 0.5f);
    }

    @Override
    public void update(View parent) {
        updateInternalBarGeometry();
        super.update(parent);
    }

    /**
     * @return the minimum value
     */

    public float getMin() {
        return min;
    }

    /**
     * @return the maximum value
     */

    public float getMax() {
        return max;
    }

    /**
     * @return the current value
     */

    public float getValue() {
        return value;
    }

    /**
     * Creates a new horizontal Progressbar based of the specified View
     *
     * @param view a not null {@link View}
     * @throws NullPointerException if {@code view == null}
     */

    public static UIProgressbar createHorizontal(View view) {
        Objects.requireNonNull(view);
        return new UIProgressbar(view);
    }

    /**
     * Creates a new vertical Progressbar based of the specified View
     *
     * @param view a not null {@link View}
     * @throws NullPointerException if {@code view == null}
     */

    public static UIProgressbar createVertical(View view) {
        Objects.requireNonNull(view);
        UIProgressbar result = new UIProgressbar(view);
        result.setRotation(-TrigTable.HALF_PI);
        return result;
    }
}
