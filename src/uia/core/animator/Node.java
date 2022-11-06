package uia.core.animator;

import uia.structure.Vec;

import static java.lang.Math.*;

/**
 * Animator's node.
 * A Node is a piece of the animation flow.
 */

public class Node extends Vec {
    public float seconds;

    public Node(float x, float y, float seconds) {
        super(x, y);
        this.seconds = abs(seconds);
    }

    public Node set(float x, float y, float seconds) {
        set(x, y);
        this.seconds = abs(seconds);
        return this;
    }

    @Override
    public String toString() {
        return "Node{x=" + x + ", y=" + y + ", seconds=" + seconds + '}';
    }

    /**
     * Check if the given point intersects with this node
     *
     * @param x   the point's x-position
     * @param y   the point's y-position
     * @param dim the sum of the two points dimension
     */

    public boolean nearTo(float x, float y, float dim) {
        return abs(x - this.x) <= dim && abs(y - this.y) <= dim;
    }

    /**
     * Check if the given node intersects with this node
     *
     * @param node the node to control
     * @param dim  the sum of the two points dimension
     */

    public boolean nearTo(Vec node, float dim) {
        return nearTo(node.x, node.y, dim);
    }

    public static float getDistX(float x, float nextX) {
        return nextX - x;
    }

    public static float getDistY(float y, float nextY) {
        return nextY - y;
    }

    public static float getSteps(float value, float seconds) {
        return value / (60f * abs(seconds));
    }
}
