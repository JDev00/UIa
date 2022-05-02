package uia.core.event;

import uia.core.View;

/**
 * Event used to track View changes
 */

public interface State extends Event<View> {
    /**
     * FOCUS_GAINED state is invoked when view gains focus
     */
    int FOCUS_GAINED = 0;

    /**
     * FOCUS_LOST state is invoked when view lost focus
     */
    int FOCUS_LOST = 1;
}
