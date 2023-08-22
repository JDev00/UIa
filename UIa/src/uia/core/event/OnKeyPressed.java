package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Key;

/**
 * Event called when a key is pressed
 */

public interface OnKeyPressed extends Event<View, Key> {
}
