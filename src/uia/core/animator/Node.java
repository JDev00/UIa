package uia.core.animator;

import uia.structure.vector.Vec;

import static java.lang.Math.*;

public class Node extends Vec {
    public float seconds;

    private Node(float x, float y, float seconds) {
        super(x, y);
        this.seconds = abs(seconds);
    }

    public void set(float x, float y, float seconds) {
        this.set(x, y);
        this.seconds = abs(seconds);
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

    @Override
    public String toString() {
        return "Node{x=" + x +
                ", y=" + y +
                ", seconds=" + seconds +
                '}';
    }

    /**
     * Create a new node
     */

    public static Node create() {
        return create(0, 0, 0);
    }

    /**
     * Create a new node
     *
     * @param v       a value used to fill node.x and node.y
     * @param seconds the seconds required to move towards this node
     */

    public static Node create(float v, float seconds) {
        return create(v, v, seconds);
    }

    /**
     * Create a new node
     *
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    public static Node create(float x, float y, float seconds) {
        return new Node(x, y, seconds);
    }

    /**
     * Creates a new node
     *
     * @param node a node to copy
     */

    public static Node create(Node node) {
        return new Node(node.x, node.y, node.seconds);
    }
}
