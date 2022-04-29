package uia.core.shape;

import java.awt.*;

public class Triangle extends Figure {

    @Override
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * this.dx;
        float dy = 0.5f * this.dy;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py);
        path.lineTo(px - dx, py + dy);
        path.closePath();

        canvas.fill(path);
    }
}
