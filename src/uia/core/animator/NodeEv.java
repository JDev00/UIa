package uia.core.animator;

import uia.core.event.Event;

/**
 * Event called by an {@link Animator} when the animation reaches the next node
 */

public interface NodeEv extends Event<Animator> {
    /**
     * NEXT state occurs when an animation reaches the next node
     */
    int NEXT = 0;

    /**
     * LAST state occurs when an animation reaches the last node
     */
    int LAST = 1;
}
