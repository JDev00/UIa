package uia.core.geometry;

import uia.core.policy.Path;

/**
 * Figure ADT
 */

public interface Figure {

    /**
     * Build this figure
     *
     * @param path a not null {@link Path} used to create the figure
     * @param px   the center position of the figure along x-axis
     * @param py   the center position of the figure along y-axis
     * @param dx   the dimension of the figure along x-axis
     * @param dy   the dimension of the figure along y-axis
     * @param rot  the rotation of the figure
     */

    void build(Path path,
               float px, float py,
               float dx, float dy,
               float rot);
}
