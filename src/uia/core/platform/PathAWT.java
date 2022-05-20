package uia.core.platform;

import uia.core.policy.Path;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

/**
 * AWT path implementation
 */

public class PathAWT implements Path {
    private Path2D path;

    public PathAWT() {
        path = new GeneralPath();
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof Path2D || o == null)
            path = (Path2D) o;
    }

    @Override
    public Path2D getNative() {
        return path;
    }

    @Override
    public void reset() {
        path.reset();
    }

    @Override
    public void rewind() {
        path.reset();
    }

    @Override
    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y) {
        path.lineTo(x, y);
    }

    @Override
    public void close() {
        path.closePath();
    }

    @Override
    public void addPath(Path path, boolean connect) {
        if (path != null)
            this.path.append((Shape) path.getNative(), connect);
    }

    @Override
    public void addPath(Object path, boolean connect) {
        if (path != null)
            this.path.append((Shape) path, connect);
    }

    @Override
    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return path.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Path path) {
        if (path != null
                && path.getNative() instanceof Shape) {
            Area area1 = new Area(this.path);
            Area area2 = new Area((Shape) path.getNative());
            area1.intersect(area2);
            return area1.equals(area2) || area1.equals(new Area(this.path));
        }

        return false;
    }

    @Override
    public boolean contains(Object path) {
        if (path instanceof Shape) {
            Area area1 = new Area(this.path);
            Area area2 = new Area((Shape) path);
            area1.intersect(area2);
            return area1.equals(area2) || area1.equals(new Area(this.path));
        }

        return false;
    }

    @Override
    public boolean intersect(double x, double y, double w, double h) {
        return path.intersects(x, y, w, h);
    }

    @Override
    public boolean intersect(Path path) {
        if (path != null
                && path.getNative() instanceof Shape) {
            Area area1 = new Area(this.path);
            Area area2 = new Area((Shape) path.getNative());
            area1.intersect(area2);
            return !area1.isEmpty();
        }

        return false;
    }

    @Override
    public boolean intersect(Object path) {
        if (path instanceof Shape) {
            Area area1 = new Area(this.path);
            Area area2 = new Area((Shape) path);
            area1.intersect(area2);
            return !area1.isEmpty();
        }

        return false;
    }
}
