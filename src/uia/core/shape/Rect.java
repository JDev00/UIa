package uia.core.shape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.*;

public class Rect extends Figure {

    @Override
    public void draw(Graphics2D canvas) {
        float dx = 0.5f * this.dx;
        float dy = 0.5f * this.dy;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py - dy);
        path.lineTo(px + dx, py + dy);
        path.lineTo(px - dx, py + dy);
        path.closePath();

        canvas.fill(path);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return abs(px - x) <= 0.5f * (dx + w) && abs(py - y) <= 0.5f * (dy + h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return contains(r.getCenterX(), r.getCenterY(), r.getWidth(), r.getHeight());
    }

    @Override
    public boolean contains(double x, double y) {
        return contains(x, y, 1, 1);
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Create a new rect pause
     */

    public static Rect createRectPause() {
        return new Rect() {

            @Override
            public void draw(Graphics2D canvas) {
                float dx = this.dx / 3f;
                float dy = this.dy / 2f;

                float pxa = px - dx;
                float pxb = px + dx;

                path.reset();
                path.moveTo(pxa - dx / 2f, py - dy);
                path.lineTo(pxa + dx / 2f, py - dy);
                path.lineTo(pxa + dx / 2f, py + dy);
                path.lineTo(pxa - dx / 2f, py + dy);

                path.moveTo(pxb - dx / 2f, py - dy);
                path.lineTo(pxb + dx / 2f, py - dy);
                path.lineTo(pxb + dx / 2f, py + dy);
                path.lineTo(pxb - dx / 2f, py + dy);
                path.closePath();

                canvas.fill(path);
            }
        };
    }
}
