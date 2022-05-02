package uia.core.figure;

import java.awt.*;

public class Plus extends Rect {
    private int thickness;

    public Plus(int thickness) {
        this.thickness = thickness;
    }

    /**
     * Set the line thickness
     *
     * @param thickness the line thickness
     */

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * this.dx;
        float dy = 0.5f * this.dy;
        float t = 0.5f * thickness;

        path.reset();
        path.moveTo(px - t, py - dy);
        path.lineTo(px + t, py - dy);
        path.lineTo(px + t, py - t);
        path.lineTo(px + dx, py - t);
        path.lineTo(px + dx, py + t);
        path.lineTo(px + t, py + t);
        path.lineTo(px + t, py + dy);
        path.lineTo(px - t, py + dy);
        path.lineTo(px - t, py + t);
        path.lineTo(px - dx, py + t);
        path.lineTo(px - dx, py - t);
        path.lineTo(px - t, py - t);
        path.closePath();

        canvas.fill(path);
    }
}
