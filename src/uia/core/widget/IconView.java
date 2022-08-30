package uia.core.widget;

import uia.core.View;
import uia.core.event.Mouse;
import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Render;
import uia.utils.Timer;

/**
 * UI component specialized in icon/image drawing
 */

public class IconView extends View {
    private final Timer timer;

    private final TextView textView;

    private Paint color;
    private Paint oColor;

    private float waitTime = 0.5f;
    private float xPanel;
    private float yPanel;
    private float staticGapY = 15f;

    public IconView(Context context,
                    float px, float py,
                    float dx, float dy) {
        super(context, px, py, dx, dy);
        //super.setImageScale(0.8f, 0.8f);
        super.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.ENTER) {
                enable(true);
            } else if (s == Mouse.LEAVE) {
                enable(false);
            }
        });

        timer = new Timer();

        textView = new TextView(context, 0, 0, 1, 1);
        textView.enableSingleLine(true);
        textView.setAlignY(TextView.AlignY.CENTER);

        color = context.createColor(null, Context.COLOR.WHITE);
        oColor = context.createColor(null, 255, 150, 150);
    }

    private void enable(boolean over) {
        if (over) {
            timer.reset();
            super.setPaint(oColor);
        } else {
            super.setPaint(color);
        }
    }

    @Override
    public void setPaint(Paint paint) {
        super.setPaint(paint);

        if (paint != null)
            this.color = paint;
    }

    /**
     * Sets a color when user is over the icon
     *
     * @param paint a not null color
     */

    public void setPaintOver(Paint paint) {
        if (paint != null)
            oColor = paint;
    }

    /**
     * Sets the time to wait before display the panel
     *
     * @param waitTime a value greater or equal to zero
     */

    public void setWaitTime(float waitTime) {
        if (waitTime >= 0)
            this.waitTime = waitTime;
    }

    /**
     * Sets a static gap between description box and icon view
     *
     * @param yGap a value {@code > 0}
     */

    public void setYGap(float yGap) {
        if (yGap > 0)
            staticGapY = yGap;
    }

    /**
     * Sets a description that will be shown after a certain period of time set with {@link IconView#setWaitTime(float)}
     *
     * @param desc a string; it could be null
     */

    public void setText(String desc) {
        textView.setText(desc);
    }

    /*@Override
    protected void touchDispatch(int[] x, int[] y, int action, int button, boolean update) {
        super.touchDispatch(x, y, action, button, update);

        if (super.isMouseOver()) {
            xPanel = (context.mouseX() < 0.5f * context.dx() ? 1 : -1) * (-0.5f * dx() + 0.5f * textView.dx());
            yPanel = (context.mouseY() < 0.5f * context.dy() ? 1 : -1) * (staticGapY + 0.5f * dy() + 0.5f * textView.dy());
        }
    }*/

    @Override
    protected void postDraw(Render render) {
        if (arePointersOver() && timer.secondsFloat() >= waitTime) {
            // modify!
            //textView.setDim(textView.getWidth(0, textView.getCharCount()), 1.2f * textView.getFontSize());
            textView.setPos(px() + xPanel, py() + yPanel);
            textView.draw(render);
        }
    }

    /**
     * @return the {@link TextView} used to draw description
     */

    public final TextView getTextView() {
        return textView;
    }
}
