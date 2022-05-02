package uia.core.widget.math;

import uia.core.Context;
import uia.core.utility.Pointer;
import uia.core.utility.TextSuite;
import uia.core.widget.TextView;
import uia.core.View;
import uia.core.widget.math.struct.VecS;
import uia.utils.Utils;

import java.awt.*;
import java.util.List;

/**
 * Widget used to draw {@link uia.core.widget.math.struct.Curve}s and more
 */

public class BoardView extends GraphicView {
    private final TextView panel;

    private final StringBuilder builder;

    private final String[] axisName = {"x", "y"};

    public Color colorCoveredPoint = Color.GREEN;

    private float xScale = 2f / 5f;
    private float yScale = 1f / 4f;
    private float xTranslate = 0f;
    private float yTranslate = -0.475f;

    public BoardView(Context context,
                     float px, float py,
                     float dx, float dy) {
        super(context, px, py, dx, dy);

        panel = new TextView(context, 0, 0, dx / 3f, 1);
        panel.setColor(new Color(28, 28, 28, 200));
        panel.enableOverlay(false);
        panel.enableAutoAdjustment(false);

        TextSuite textSuite = panel.getTextSuite();
        textSuite.setFontSize(14);
        textSuite.setColor(Color.WHITE);

        builder = new StringBuilder(1000);
    }

    /**
     * Set the abscissa and ordinate names
     *
     * @param a0 the abscissa name
     * @param a1 the ordinate name
     */

    public void setCouple(String a0, String a1) {
        if (a0 != null)
            axisName[0] = a0;

        if (a1 != null)
            axisName[1] = a1;
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

    @Override
    protected void focusDispatcher(boolean gained) {
        if (!gained)
            panel.removeFocus();

        super.focusDispatcher(gained);
    }

    @Override
    protected void touchDispatcher(Pointer p, boolean update) {
        View.updateTouchDispatcher(panel, p, update);
        super.touchDispatcher(p, update);
    }

    @Override
    public void postDraw(Graphics2D canvas) {
        super.postDraw(canvas);

        if (arePointersOver()) {
            List<VecS> covered = getCovered();

            builder.delete(0, builder.length());
            builder.append("(").append(axisName[0]).append(", ").append(axisName[1]).append(") = {");

            canvas.setColor(colorCoveredPoint);
            for (VecS i : covered) {

                if (i.data == null) {
                    builder.append("\n(").append(i.x).append(", ").append(i.y).append("), ");
                } else {
                    builder.append("\n(").append(i.x).append(", ").append(i.y).append(", ").append(i.data).append("), ");
                }

                canvas.fillOval(
                        (int) (getCenterX() + getScaleX() * i.x) - 5,
                        (int) (getCenterY() - getScaleY() * i.y) - 5,
                        10, 10);
            }

            panel.setPos(
                    px() - dx() / 2f + xTranslate * panel.dx() + cmx,
                    py() - dy() / 2f + yTranslate * panel.dy() + cmy);
            panel.setDim(xScale * dx(), yScale * dy());
            panel.setText(builder.append("}").toString());
            panel.draw(canvas);
        }
    }

    /**
     * @return the associated panel
     */

    public final TextView getPanel() {
        return panel;
    }
}
