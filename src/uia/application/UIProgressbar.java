package uia.application;

import uia.physical.ui.component.utility.ComponentUtility;
import uia.physical.ui.component.WrapperView;
import uia.physical.ui.group.ComponentGroup;
import uia.physical.ui.component.Component;
import uia.physical.ui.theme.Theme;
import uia.utility.MathUtility;
import uia.core.ui.style.Style;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

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
        getStyle()
                .setBackgroundColor(Theme.DARK_GRAY)
                .setGeometry(
                        geometry -> ComponentUtility.buildRect(geometry, getWidth(), getHeight(), 1f),
                        true
                );

        internalBar = new Component("PROGRESSBAR_INTERNAL_BAR_" + getID(), 0.5f, 0.5f, 1f, 1f);
        internalBar.getStyle()
                .setBackgroundColor(Theme.LIME)
                .setGeometry(geometry -> {
                    float xVertex = value - 0.5f;
                    geometry.removeAllVertices().addVertices(
                            -0.5f, -0.5f,
                            xVertex, -0.5f,
                            xVertex, 0.5f,
                            -0.5f, 0.5f
                    );
                }, true);

        ViewGroup.insert(getView(), internalBar);

        setRange(0f, 1f);
        setValue(value);
    }

    /**
     * @return the internal bar {@link Style}
     */

    public Style getInternalBarStyle() {
        return internalBar.getStyle();
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
        this.value = MathUtility.constrain(value, min, max);
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
        result.getStyle().setRotation(-MathUtility.HALF_PI);
        return result;
    }
}
