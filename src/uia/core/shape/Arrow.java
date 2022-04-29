package uia.core.shape;

import java.awt.*;

public class Arrow extends Figure {
    private int direction;

    public Arrow(boolean right) {
        direction = right ? 1 : -1;
    }

    /**
     * Set the arrow direction
     *
     * @param right true to create a right arrow otherwise a left one
     */

    public void setRight(boolean right) {
        direction = right ? 1 : -1;
    }

    @Override
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * direction * this.dx;
        float dy = 0.5f * this.dy;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py);
        path.lineTo(px - dx, py + dy);
        path.lineTo(px - 2 * dx / 3f, py);
        path.closePath();

        canvas.fill(path);
    }
}
