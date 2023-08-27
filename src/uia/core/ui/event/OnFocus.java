package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;

/**
 * Event called when focus is gained or lost by a View.
 * <br>
 * This event provides a boolean whose value is True is focus is gained.
 */

public interface OnFocus extends Event<Boolean> {
}
