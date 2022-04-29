package uia.core.event;

import uia.core.View;
import uia.core.event.Event;

/**
 * Event used to track key actions inside a {@link View}
 */

public interface Key extends Event<View> {
    /**
     * PRESSED state represents a key pressed
     */
    int PRESSED = java.awt.event.KeyEvent.KEY_PRESSED;

    /**
     * RELEASED state represents a key released
     */
    int RELEASED = java.awt.event.KeyEvent.KEY_RELEASED;

    /**
     * TYPED state represents a key typed
     */
    int TYPED = java.awt.event.KeyEvent.KEY_TYPED;
}
