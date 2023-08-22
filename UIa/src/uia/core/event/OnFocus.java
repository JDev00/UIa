package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;

/**
 * Event called when focus is gained or lost by a View
 */

public interface OnFocus extends Event<View, Boolean> {
}
