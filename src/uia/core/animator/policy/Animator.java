package uia.core.animator.policy;

import uia.core.animator.NodeEvent;
import uia.core.event.EventQueue;

/**
 * Animator ADT
 */

public interface Animator {

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
     * Set an animator path
     *
     * @param animatorPath a not null animatorPath instance
     */

    Animator setPath(AnimatorPath animatorPath);

    /**
     * Set a new {@link EventQueue} and keep the attached events
     *
     * @param eventQueue a new not null EventQueue
     */

    Animator setEventQueue(EventQueue<NodeEvent<Animator>> eventQueue);

    /**
     * Clear all nodes
     */

    Animator clear();

    /**
     * Add a new node
     *
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    Animator addNode(float x, float y, float seconds);

    /**
     * Add a new node
     *
     * @param i       the position where add the node
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    Animator addNode(int i, float x, float y, float seconds);

    /**
     * Update the node params
     *
     * @param i       the position of the node inside this animator
     * @param x       the node position along x-axis
     * @param y       the node position along y-axis
     * @param seconds the seconds required to move towards this node
     */

    Animator setNode(int i, float x, float y, float seconds);

    /**
     * Copy the nodes of the given animator to this one
     *
     * @param animator a not null animator instance
     */

    Animator setNodes(Animator animator);

    /**
     * Remove the specified node
     *
     * @param i the position of the node inside this animator
     */

    Animator removeNode(int i);

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
     * @return the attached {@link EventQueue}
     */

    EventQueue<NodeEvent<Animator>> getEventQueue();

    /**
     * @return the {@link AnimatorPath}
     */

    AnimatorPath getPath();
}
