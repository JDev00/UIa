package uia.core.geometry;

import uia.core.policy.Path;

public class Delete implements Figure {
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
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

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
        path.close();
    }
}
