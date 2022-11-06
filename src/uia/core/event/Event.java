package uia.core.event;

/**
 * Event description
 */

public interface Event<T> {

    /**
     * ENTER state represents the mouse entering the view area
     */
    int POINTER_ENTER = 0;

    /**
     * HOVER state represents mouse over the view area
     */
    int POINTER_HOVER = 1;

    /**
     * LEAVE state represents the mouse leaving the view area
     */
    int POINTER_LEAVE = 2;

    /**
     * CLICK state represents a mouse click inside the view area
     */
    int POINTER_CLICK = 3;

    /**
     * PRESSED state represents a key pressed
     */
    int KEY_PRESSED = 4;

    /**
     * RELEASED state represents a key released
     */
    int KEY_RELEASED = 5;

    /**
     * TYPED state represents a key typed
     */
    int KEY_TYPED = 6;

    /**
     * FOCUS_GAINED state is invoked when view gains focus
     */
    int FOCUS_GAINED = 7;

    /**
     * FOCUS_LOST state is invoked when view lost focus
     */
    int FOCUS_LOST = 8;

    /**
     * @param t     a not null object
     * @param state the event's state
     */

    void onEvent(T t, int state);
}
