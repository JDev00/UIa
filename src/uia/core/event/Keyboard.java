package uia.core.event;

import uia.core.View;

/**
 * Event used to track key actions inside a {@link View}
 */

public interface Keyboard extends Event<View> {
    /**
     * PRESSED state represents a key pressed
     */
    int PRESSED = 0;

    /**
     * RELEASED state represents a key released
     */
    int RELEASED = 1;

    /**
     * TYPED state represents a key typed
     */
    int TYPED = 2;
}
