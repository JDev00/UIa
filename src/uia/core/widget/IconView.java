package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.core.widget.text.TextView;
import uia.utils.Timer;

/**
 * UI component designed to handle an Icon
 */

public class IconView extends ImageView {
    private Timer timer;

    private TextView textView;

    private Paint paintOver;
    private Paint prevPaint;

    private float xTrans;
    private float yTrans;
    private float waitTime = 1f;

    public IconView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        getEventQueue().addEvent((v, s) -> {
            switch (s) {
                case Event.POINTER_ENTER:
                    timer.reset();
                    enable(true);
                    break;

                case Event.POINTER_LEAVE:
                case Event.FOCUS_LOST:
                    enable(false);
                    break;

                case Event.POINTER_HOVER:
                    if (!textView.isVisible()) {
                        PointerEvent e = getPointerEvent();
                        xTrans = e.getX() - width() / 2f;
                        yTrans = e.getY() - height() / 2f;
                    }
                    break;
            }
        });

        timer = new Timer();

        paintOver = new Paint(255, 255, 255, 200);

        textView = new TextView(context, 0, 0, 1, 1);
        textView.getPaint().setColor(80, 80, 80);
        textView.getPaintText().setColor(255, 255, 255);
        textView.setAlignY(TextView.AlignY.CENTER);
        textView.setVisible(false);
        textView.setSingleLine(true);
        textView.setPointerConsumer(false);
        textView.setPositionAdjustment(false);
        textView.setDimensionAdjustment(false);
    }

    private void enable(boolean over) {
        if (over) {
            super.setPaint(paintOver);
        } else {
            super.setPaint(prevPaint);
        }
    }

    /**
     * Set a Paint to use when user is over the View's area
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaintOver(Paint paint) {
        if (paint != null) paintOver = paint;
    }

    /**
     * Set the time to wait before draw the information panel
     *
     * @param waitTime a value greater or equal to zero
     */

    public void setWaitTime(float waitTime) {
        if (waitTime >= 0) this.waitTime = waitTime;
    }

    /**
     * Set the information panel's text
     *
     * @param text a String; it could be null
     */

    public void setText(String text) {
        textView.setText(text);
    }

    @Override
    protected void update() {
        super.update();

        if (!getPaint().equals(paintOver)) prevPaint = getPaint();

        Context context = getContext();

        float w = 1.1f * textView.getLongestLine();
        float h = 1.33f * textView.getTextHeight();
        float x = x() + xTrans + w / 2f;
        float y = y() + yTrans + h / 2f + 25f;
        float dx = Math.max(0, x + w / 2f - context.width());
        float dy = Math.max(0, y + h / 2f - context.height());

        textView.setVisible(arePointersOver() && timer.isOver(waitTime));
        textView.setDimension(w, h);
        textView.setPosition(x - dx, y - dy);

        View.update(textView);
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        View.draw(textView, graphic);
    }

    /**
     * @return the {@link TextView} used as information panel
     */

    public TextView getTextView() {
        return textView;
    }
}
