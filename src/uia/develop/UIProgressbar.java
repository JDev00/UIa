package uia.develop;

import uia.application.desktop.ContextSwing;
import uia.core.Paint;
import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.context.Context;
import uia.physical.theme.ThemeDarcula;
import uia.physical.ComponentGroup;
import uia.physical.WrapperView;
import uia.physical.Component;
import uia.physical.ComponentText;
import uia.utility.TrigTable;
import uia.utility.Utility;

import java.util.function.Function;

/**
 * Standard UIa component.
 * <br>s
 * Progressbar to describe.
 */

public class UIProgressbar extends WrapperView {
    private final View internalBar;
    private final ViewText viewText;
    private Function<String, String> textFunction;

    private float value;
    private float min;
    private float max;

    public UIProgressbar(View view) {
        super(new ComponentGroup(view));

        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);
        getPaint().setColor(ThemeDarcula.W_BACKGROUND);

        internalBar = new Component("PROGRESSBAR_INTERNAL_BAR_" + getID(), 0.5f, 0.5f, 1f, 1f);
        internalBar.getPaint().setColor(ThemeDarcula.W_FOREGROUND);

        viewText = new ComponentText(
                new Component("TEXT", 0.5f, 1.275f, 0.225f, 0.5f)
        );
        viewText.setGeometry(g -> Drawable.buildRect(g, viewText.getWidth(), viewText.getHeight(), 0.5f),
                true
        );
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(ComponentText.AlignY.CENTER);
        viewText.getPaint().setColor(ThemeDarcula.BACKGROUND);
        viewText.getTextPaint().setColor(ThemeDarcula.TEXT);
        viewText.getFont().setSize(17f);

        ViewGroup group = getView();
        group.setClip(false);
        ViewGroup.insert(group, viewText, internalBar);

        textFunction = s -> Utility.limitDecimals(s, 1);

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
     * @return the information text
     */

    public ViewText getText() {
        return viewText;
    }

    /**
     * Set the progressbar information text
     *
     * @param fun a not null {@link Function}
     */

    public void setText(Function<String, String> fun) {
        if (fun != null) {
            this.textFunction = fun;
            setValue(getValue());
        }
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
        viewText.setText(textFunction.apply(String.valueOf(this.value)));
    }

    /**
     * Helper function. Updates the internal bar geometry.
     */

    private void updateInternalBarGeometry() {
        float xVertex = value - 0.5f;
        internalBar.getGeometry()
                .clear()
                .addVertices(
                        -0.5f, -0.5f,
                        xVertex, -0.5f,
                        xVertex, 0.5f,
                        -0.5f, 0.5f
                );
    }

    private void updateTextBounds() {
        /*float h = 1.33f * viewText.getTextHeight() / desc[1];
        float yh = 0.75f * h + abs(rotX(0, 0.5f * viewText.getTextWidth() / desc[1], cos(-desc[2]), sin(-desc[2])));

        viewText.setDimension(
                1.33f * viewText.getTextWidth() / getWidth(),
                1.33f * viewText.getTextHeight() / desc[1]
        );
        viewText.setPosition(mp, 1f + yh);
        viewText.setRotation(-desc[2]);*/
    }

    @Override
    public void update(View parent) {
        updateInternalBarGeometry();
        super.update(parent);
        updateTextBounds();
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

    public static void main(String[] args) {
        UIProgressbar progressbar = new UIProgressbar(
                new Component("", 0.5f, 0.5f, 0.2f, 0.1f)
        );
        progressbar.setValue(0.778f);
        progressbar.setRotation(TrigTable.HALF_PI);

        ViewGroup group = new ComponentGroup(
                new Component("", 0.5f, 0.5f, 1f, 1f)
        );
        ViewGroup.insert(group, progressbar);

        Context context = ContextSwing.createAndStart(1000, 500);
        context.setView(group);
    }
}
