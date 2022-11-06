package uia.core.widget;

import uia.core.event.Event;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.widget.text.TextView;

/**
 * UI button
 */

public class Button extends Widget {
    private final TextView textView;

    private Paint prevPaint;
    private Paint paintEnabled;

    private float xTranslate = 0f;
    private float yTranslate = 0f;

    private boolean enabled = false;
    private boolean locked = false;

    public Button(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.buildShape(s -> Figure.rect(s, Figure.STD_VERT, 0.2f), true);
        super.setExpansion(0.025f, 0.025f);
        super.setClipping(false);
        super.setPointersConstrain(false);
        getEventQueue().addEvent((v, s) -> {
            if (s == Event.POINTER_CLICK && !locked) enable(!enabled);
        });

        paintEnabled = new Paint(0, 255, 0);

        textView = new TextView(context, x, y, width, height);
        textView.getPaint().setColor(0, 0, 0, 1);
        textView.getPaintText().setColor(255, 255, 255);
        textView.setPointerConsumer(false);
        textView.setPositionAdjustment(false);
        textView.setDimensionAdjustment(false);
        textView.setAlignY(TextView.AlignY.CENTER);
        textView.getEventQueue().addEvent(textView.funSelectionReset());

        add(textView);
    }

    /**
     * Lock the button's state
     */

    public void lock() {
        locked = true;
    }

    /**
     * Unlock the button's state
     */

    public void unlock() {
        locked = false;
    }

    /**
     * Enable or disable this button
     *
     * @param enabled true to enable it
     */

    public void enable(boolean enabled) {
        this.enabled = enabled;
        setPaint(enabled ? paintEnabled : prevPaint);
    }

    /**
     * Set the {@link Paint} to use it when the button is enabled
     *
     * @param paint a not null Paint
     */

    public void setPaintEnabled(Paint paint) {
        if (paint != null) paintEnabled = paint;
    }

    /**
     * Translate the text around
     *
     * @param xTranslate a value, typically between [-1,1], used to move the text around x-axis
     * @param yTranslate a value, typically between [-1,1], used to move the text around y-axis
     */

    public void setTextTranslation(float xTranslate, float yTranslate) {
        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
    }

    /**
     * Set the button's text
     *
     * @param text a not null String
     */

    public void setText(String text) {
        textView.setText(text);
    }

    @Override
    protected void update() {
        super.update();

        float w = width();
        float h = height();

        if (!getPaint().equals(paintEnabled)) prevPaint = getPaint();

        textView.setDimension(Math.max(w, textView.getLongestLine()), Math.min(h, 1.2f * textView.getTextHeight()));
        textView.setPosition(xTranslate * (textView.width() + w), yTranslate * (textView.height() + h));
    }

    /**
     * @return the text translation factor along x-axis
     */

    public float getTranslateX() {
        return xTranslate;
    }

    /**
     * @return the text translation factor along y-axis
     */

    public float getTranslateY() {
        return yTranslate;
    }

    /**
     * @return true if this button is locked
     */

    public boolean isLocked() {
        return locked;
    }

    /**
     * @return true if this button is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the {@link Paint} used when the button is enabled
     */

    public Paint getPaintEnabled() {
        return paintEnabled;
    }

    /**
     * @return the {@link TextView} used to draw text
     */

    public TextView getTextView() {
        return textView;
    }

    /**
     * Enable the first button and disable the second one
     *
     * @param first  the first not null button
     * @param second the second not null button
     */

    public static void exclude(Button first, Button second) {
        if (first != null && second != null) {
            first.enable(true);
            second.enable(false);
        }
    }
}
