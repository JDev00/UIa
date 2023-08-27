package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Key;

/**
 * Event called when a key is pressed.
 * <br>
 * This event provides the {@link Key} pressed.
 */

public interface OnKeyPressed extends Event<Key> {
}
