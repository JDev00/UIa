package uia.core.event;

import uia.core.View;
import uia.core.event.Event;

/**
 * Event used to track mouse actions inside a {@link View}
 */

public interface Mouse extends Event<View> {
    /**
     * ENTER state represents the mouse entering the view area
     */
    int ENTER = 0;

    /**
     * HOVER state represents mouse over the view area
     */
    int HOVER = 1;

    /**
     * LEAVE state represents the mouse leaving the view area
     */
    int LEAVE = 2;

    /**
     * CLICK state represents a mouse click inside the view area
     */
    int CLICK = 3;
}
