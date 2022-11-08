package uia.core.widget.math;

import uia.core.View;
import uia.core.event.Event;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.core.widget.text.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DistributionView}'s implementation used to draw point's information
 */

public class BoardView extends DistributionView {
    private Shape coveredPoint;

    private Paint paintCovered;

    private TextView panel;

    private final String[] axisName = {"x", "y"};

    private final List<float[]> nearest = new ArrayList<>();

    public BoardView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        super.setGraphicScale(0.67f, 0.95f);
        super.setGraphicOffset(-0.15f, 0f);

        final StringBuilder builder = new StringBuilder(1000);

        getEventQueue().addEvent((v, s) -> {
            if (s == Event.POINTER_HOVER) {
                nearest(v.getPointerEvent().getX(), nearest);

                builder.delete(0, builder.length());
                builder.append("(").append(axisName[0]).append(", ").append(axisName[1]).append(") = {");

                for (float[] i : nearest) {
                    if (i.length != 0) builder.append("\n  (").append(i[0]).append(", ").append(i[1]).append("), ");
                }

                builder.append("\n}");

                // Test
                synchronized (panel) {
                    panel.setText(builder.toString());
                }
            }
        });

        panel = new TextView(context, 0, 0, width / 3f, 1);
        panel.getFont().setSize(15f);
        panel.getPaint().setColor(28, 28, 28, 200);
        panel.getPaintText().setColor(255, 255, 255);
        panel.setAlignX(TextView.AlignX.LEFT);
        panel.setPositionAdjustment(false);
        panel.setDimensionAdjustment(false);

        coveredPoint = Figure.rect(null);

        paintCovered = new Paint(0, 255, 0);
    }

    /**
     * Set the abscissa and ordinate names
     *
     * @param x the abscissa's name
     * @param y the ordinate's name
     */

    public void setAxisName(String x, String y) {
        if (x != null) axisName[0] = x;
        if (y != null) axisName[1] = y;
    }

    /**
     * Set the covered point's shape
     *
     * @param point a not null {@link Shape}
     */

    public void setCoveredShape(Shape point) {
        if (point != null) coveredPoint = point;
    }

    /**
     * Set the Paint used to color covered points
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaintCovered(Paint paint) {
        if (paint != null) paintCovered = paint;
    }

    /**
     * Set a new Panel to display data points
     *
     * @param panel a not null {@link TextView}
     */

    public void setPanel(TextView panel) {
        this.panel = panel;
    }

    @Override
    protected void updatePointers(boolean update) {
        View.updatePointers(panel, update);
        super.updatePointers(update);
    }

    @Override
    protected void updateKeys(boolean update) {
        View.updateKeys(panel, update);
        super.updateKeys(update);
    }

    @Override
    protected void update() {
        super.update();

        if (panel != null) {

            if (!getContext().equals(panel.getContext())) panel.setContext(getContext());

            panel.setCenter(x() + 0.35f * width(), y());
            panel.setDimension(0.25f * width(), 0.95f * height());

            View.update(panel);
        }
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        // draw covered shape
        graphic.setPaint(paintCovered);
        for (float[] i : nearest) {
            coveredPoint.setPosition(getCenterX() + getScaleX() * i[0], getCenterY() - getScaleY() * i[1]);
            coveredPoint.setDimension(10, 10);
            coveredPoint.draw(graphic);
        }

        // draw panel
        if (panel != null) View.draw(panel, graphic);
    }

    /**
     * @return the {@link Paint} used to color covered points
     */

    public Paint getPaintCovered() {
        return paintCovered;
    }

    /**
     * @return the covered {@link Shape}'s point
     */

    public Shape getShapeCovered() {
        return coveredPoint;
    }

    /**
     * @return the associated panel
     */

    public final TextView getPanel() {
        return panel;
    }
}
