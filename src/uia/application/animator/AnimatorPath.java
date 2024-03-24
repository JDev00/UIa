package uia.application.animator;

/**
 * AnimatorPath ADT
 * <br>
 * Describe me!
 */

public interface AnimatorPath {

    /**
     * Adds a new node.
     *
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    AnimatorPath addNode(float x, float y, float seconds);

    /**
     * Adds a new node at the specified position.
     *
     * @param i       the position where add the node
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    AnimatorPath addNode(int i, float x, float y, float seconds);

    /**
     * Updates the specified node.
     *
     * @param i       the position of the node inside this animator
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     * @throws IndexOutOfBoundsException if {@code i < 0 || i>= size()}
     */

    AnimatorPath setNode(int i, float x, float y, float seconds);

    /**
     * Clears all nodes.
     */

    AnimatorPath clear();

    /**
     * Removes the specified node.
     *
     * @param i the position of the node inside this animator
     * @throws IndexOutOfBoundsException if {@code i < 0 || i>= size()}
     */

    AnimatorPath removeNode(int i);

    /**
     * @return the number of nodes
     */

    int size();

    /**
     * @param index the node index
     * @return a new array made of node's position (along x,y) and seconds
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= size()}
     */

    float[] get(int index);
}
