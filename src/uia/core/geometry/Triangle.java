package uia.core.geometry;

import uia.core.policy.Path;

public class Triangle implements Figure {

    @Override
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py);
        path.lineTo(px - dx, py + dy);
        path.close();
    }
}
