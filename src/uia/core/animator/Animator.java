package uia.core.animator;

import uia.core.event.Event;

/**
 * Animator ADT
 */

public interface Animator {

    /**
     * Add a new event to this animator
     *
     * @param event an animator event
     */

    void addEvent(Event<Animator> event);

    /**
     * Set an animator path
     *
     * @param animatorPath a non-null animatorPath instance
     * @throws NullPointerException if {@code animatorPath == null}
     */

    void setPath(AnimatorPath animatorPath);

    /**
     * Copy the nodes of the given animator to this one
     *
     * @param animator a non-null animator instance
     */

    void setNodes(Animator animator);

    /**
     * Pause this animator
     */

    void pause();

    /**
     * Resume this animator
     */

    void resume();

    /**
     * Restart this animator
     */

    void restart();

    /**
     * Clear all nodes
     */

    void clear();

    /**
     * Add a new node
     *
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    void addNode(float x, float y, float seconds);

    /**
     * Add a new node
     *
     * @param i       the position where add the node
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    void addNode(int i, float x, float y, float seconds);

    /**
     * Update the node params
     *
     * @param i       the position of the node inside this animator
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    void setNode(int i, float x, float y, float seconds);

    /**
     * Remove the specified node
     *
     * @param i the position of the node inside this animator
     */

    void removeNode(int i);

    /**
     * Update this animator
     */

    void update();

    /**
     * @return the number of nodes
     */

    int size();

    /**
     * @return the current node
     */

    int current();

    /**
     * @return the x-position of the object moving across the nodes
     */

    float x();

    /**
     * @return the y-position of the object moving across the nodes
     */

    float y();

    /**
     * @param i the position of the node inside this animator
     * @return the node position along x-axis
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    float getX(int i);

    /**
     * @param i the position of the node inside this animator
     * @return the node position along y-axis
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    float getY(int i);

    /**
     * @param i the position of the node inside this animator
     * @return the node seconds required to move to
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= size()}
     */

    float getSeconds(int i);

    /**
     * @return true if this animator is paused
     */

    boolean isPaused();

    /**
     * @return true if this animator has reached the last node
     */

    boolean isFinished();

    /**
     * @return the animator path
     */

    AnimatorPath getPath();
}
