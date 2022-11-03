package uia.core.animator;

import uia.core.animator.policy.Animator;

/**
 * Node event description
 */

public interface NodeEvent<T extends Animator> {

    /**
     * NEXT_NODE state occurs when an animation reaches the next node
     */
    int NEXT_NODE = 0;

    /**
     * LAST_NODE state occurs when an animation reaches the last node
     */
    int LAST_NODE = 1;

    /**
     * @param t     a not null object
     * @param state the event's state
     */

    void onEvent(T t, int state);
}
