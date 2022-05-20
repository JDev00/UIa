package uia.core.geometry;

import uia.core.policy.Path;

public class Rect implements Figure {

    @Override
    public void build(Path path,
                      float px, float py,
                      float dx, float dy,
                      float rot) {
        dx /= 2;
        dy /= 2;

        path.reset();
        path.moveTo(px - dx, py - dy);
        path.lineTo(px + dx, py - dy);
        path.lineTo(px + dx, py + dy);
        path.lineTo(px - dx, py + dy);
        path.close();
    }

    /**
     * Create a new rect pause
     */

    public static Rect createRectPause() {
        return new Rect() {

            @Override
            public void build(Path path,
                              float px, float py,
                              float dx, float dy,
                              float rot) {
                dx /= 3;
                dy /= 2;

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
                path.close();
            }
        };
    }
}
