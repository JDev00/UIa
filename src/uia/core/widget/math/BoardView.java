package uia.core.widget.math;

import uia.core.policy.Context;
import uia.core.policy.Paint;
import uia.core.policy.Render;
import uia.core.widget.TextView;
import uia.core.View;
import uia.core.widget.math.struct.VecS;
import uia.core.widget.math.struct.Curve;
import uia.utils.Utils;

import java.util.List;

/**
 * Widget used to draw {@link Curve}s and display data point with a built-in panel
 */

public class BoardView extends GraphicView {
    private TextView panel;

    private final StringBuilder builder;

    private final String[] axisName = {"x", "y"};

    public Paint colorCoveredPoint;

    private float xScale = 2f / 5f;
    private float yScale = 1f / 4f;
    private float xTranslate = 0f;
    private float yTranslate = -0.475f;

    public BoardView(Context context,
                     float px, float py,
                     float dx, float dy) {
        super(context, px, py, dx, dy);

        colorCoveredPoint = context.createColor(null, 0, 255, 0);

        panel = new TextView(context, 0, 0, dx / 3f, 1);
        panel.enableOverlay(false);
        panel.enableAutoAdjustment(false);
        panel.setFontSize(14);
        panel.setPaint(context.createColor(null, 28, 28, 28, 200));
        panel.setPaintText(context.createColor(null, Context.COLOR.WHITE));

        builder = new StringBuilder(1000);
    }

    /**
     * Set the abscissa and ordinate names
     *
     * @param x the abscissa name
     * @param y the ordinate name
     */

    public void setAxisName(String x, String y) {
        if (x != null)
            axisName[0] = x;

        if (y != null)
            axisName[1] = y;
    }

    /**
     * Translate panel up/down and left/right according to its dimension
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void setPanelTranslation(float x, float y) {
        xTranslate = x;
        yTranslate = y;
    }

    /**
     * Set the panel scale relative to this board view
     *
     * @param x the scale along x-axis
     * @param y the scale along y-axis
     */

    public void setPanelScale(float x, float y) {
        xScale = Utils.constrain(x, 0, 1);
        yScale = Utils.constrain(y, 0, 1);
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
    protected void focusDispatcher(boolean gained) {
        if (!gained && panel != null)
            panel.removeFocus();

        super.focusDispatcher(gained);
    }

    @Override
    protected void pointerDispatcher(boolean update) {
        View.updatePointerDispatcher(panel, update);
        super.pointerDispatcher(update);
    }

    @Override
    public void postDraw(Render render) {
        super.postDraw(render);

        if (arePointersOver()) {
            List<VecS> covered = getCovered();

            builder.delete(0, builder.length());
            builder.append("(").append(axisName[0]).append(", ").append(axisName[1]).append(") = {");

            render.setPaint(colorCoveredPoint);
            for (VecS i : covered) {

                if (i.data == null) {
                    builder.append("\n(").append(i.x).append(", ").append(i.y).append("), ");
                } else {
                    builder.append("\n(").append(i.x).append(", ").append(i.y).append(", ").append(i.data).append("), ");
                }

                render.drawOval(
                        (int) (getCenterX() + getScaleX() * i.x) - 5,
                        (int) (getCenterY() - getScaleY() * i.y) - 5,
                        10, 10);
            }

            // Draw Panel
            if (panel != null) {

                if (!getContext().equals(panel.getContext()))
                    panel.setContext(getContext());

                panel.setPos(
                        px() - dx() / 2f + xTranslate * panel.dx() + cmx,
                        py() - dy() / 2f + yTranslate * panel.dy() + cmy);
                panel.setDim(xScale * dx(), yScale * dy());
                panel.setText(builder.append("}").toString());
                panel.draw(render);
            }
        }
    }

    /**
     * @return the associated panel
     */

    public final TextView getPanel() {
        return panel;
    }
}
