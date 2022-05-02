package uia.core.figure;

import java.awt.*;

public class Delete extends Rect {
    private int thickness;

    public Delete(int thickness) {
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

        path.reset();
        path.moveTo(px - dx + thickness, py - dy);
        path.lineTo(px - dx, py - dy);
        path.lineTo(px - thickness / 2f, py);
        path.lineTo(px - dx, py + dy);
        path.lineTo(px - dx + thickness, py + dy);
        path.lineTo(px, py + thickness / 2f);
        path.lineTo(px + dx - thickness, py + dy);
        path.lineTo(px + dx, py + dy);
        path.lineTo(px + thickness / 2f, py);
        path.lineTo(px + dx, py - dy);
        path.lineTo(px + dx - thickness, py - dy);
        path.lineTo(px, py - thickness / 2f);
        path.closePath();

        canvas.fill(path);
    }
}
