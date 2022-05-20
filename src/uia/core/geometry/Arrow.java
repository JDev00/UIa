package uia.core.geometry;

import uia.core.policy.Path;

public class Arrow implements Figure {
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
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx *= 0.5f * direction;
        dy /= 2;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py);
        path.lineTo(px - dx, py + dy);
        path.lineTo(px - 2 * dx / 3f, py);
        path.close();
    }
}
