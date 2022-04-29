package uia.core.shape;

import java.awt.*;
import java.awt.geom.*;

/**
 * Generic shape implementation
 */

public abstract class Figure implements Shape {
    protected float px;
    protected float py;
    protected float dx;
    protected float dy;

    protected GeneralPath path = new GeneralPath();

    /**
     * Set the position of this shape
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    public void setPos(float x, float y) {
        px = x;
        py = y;
    }

    /**
     * Set the dimension of this shape
     *
     * @param x the dimension along x-axis
     * @param y the dimension along y-axis
     */

    public void setDim(float x, float y) {
        dx = x;
        dy = y;
    }

    /**
     * Draw this shape on screen
     *
     * @param canvas a non-null {@link Graphics2D} used to render this shape
     */

    public abstract void draw(Graphics2D canvas);

    @Override
    public Rectangle getBounds() {
        return path.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return path.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return path.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return path.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return path.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return path.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return path.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return path.getPathIterator(at, flatness);
    }
}
