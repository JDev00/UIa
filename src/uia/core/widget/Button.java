package uia.core.widget;

import uia.core.Widget;
import uia.core.event.Mouse;
import uia.core.geometry.Oval;
import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Render;

import static java.lang.Math.*;

/**
 * Standard UI Button
 */

public class Button extends Widget {
    private final TextView textView;

    private Paint outColor;
    private Paint overColor;
    private Paint outEnabledColor;
    private Paint overEnabledColor;

    private float xTranslate = 0;
    private float yTranslate = 1;

    private boolean enabled = false;
    private boolean lockState = false;

    public Button(Context context,
                  float px, float py,
                  float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setExpansion(0.1f, 0.1f);
        super.setFigure(Oval.create());
        super.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.ENTER || s == Mouse.LEAVE) {
                enable(enabled);
            } else if (s == Mouse.CLICK && !lockState) {
                enable(!enabled);
            }
        });

        outColor = context.createColor(null, Context.COLOR.WHITE);
        overColor = context.createColor(null, 255, 255, 255, 100);
        outEnabledColor = context.createColor(null, Context.COLOR.GREEN);
        overEnabledColor = context.createColor(null, 0, 255, 0, 100);

        textView = new TextView(context, px, py, dx, dy);
        textView.enableOverlay(false);
        textView.enableSingleLine(true);
        textView.enableAutoAdjustment(false);
        textView.setAlignY(TextView.AlignY.CENTER);
        textView.setPaint(context.createColor(textView.getPaint(), Context.COLOR.STD_NO_PAINT));

        add(textView);
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
     * Lock the button's state
     */

    public void lock() {
        lockState = true;
    }

    /**
     * Unlock the button's state
     */

    public void unlock() {
        lockState = false;
    }

    /**
     * Enable or disable this button
     *
     * @param enabled true to enable it
     */

    public void enable(boolean enabled) {
        this.enabled = enabled;

        if (super.arePointersOver()) {
            super.setPaint(enabled ? overEnabledColor : overColor);
        } else {
            super.setPaint(enabled ? outEnabledColor : outColor);
        }
    }

    /**
     * Set the button's text
     */

    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * Set a Paint object to display when this button is enabled
     *
     * @param paint a not null {@link Paint} object
     */

    public void setPaintEnabled(Paint paint) {
        if (paint != null && paint.getColors().size() > 0) {
            float[] c = paint.getColors().get(0);
            outEnabledColor = paint;
            overEnabledColor = getContext().createColor(overEnabledColor, c[0], c[1], c[2], abs(c[3] - 50));
        }
    }

    @Override
    public void setPaint(Paint paint) {
        super.setPaint(paint);

        if (paint != null && paint.getColors().size() > 0) {
            float[] c = paint.getColors().get(0);
            outColor = paint;
            overColor = getContext().createColor(overColor, c[0], c[1], c[2], abs(c[3] - 50));
        }
    }

    @Override
    protected void postDraw(Render render) {
        textView.setPos(
                xTranslate * 0.5f * (textView.dx() + dx()),
                yTranslate * 0.5f * (textView.dy() + dy()));

        super.postDraw(render);

        // keep here because of text attributes are calculated after text rendering
        textView.setDim(
                max(textView.getLongestLine(), dx()),
                min(textView.getTextHeight(), dy()));
    }

    /**
     * @return the text translation factor along x-axis
     */

    public final float getTranslateX() {
        return xTranslate;
    }

    /**
     * @return the text translation factor along y-axis
     */

    public final float getTranslateY() {
        return yTranslate;
    }

    /**
     * @return true if this button is locked
     */

    public boolean isLocked() {
        return lockState;
    }

    /**
     * @return true if this button is enabled
     */

    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the view used to render text
     */

    public final TextView getTextView() {
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
