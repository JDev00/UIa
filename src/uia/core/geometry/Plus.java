package uia.core.geometry;

import uia.core.policy.Path;

public class Plus implements Figure {
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
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

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
        path.close();
    }
}
