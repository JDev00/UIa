package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Key;

/**
 * Event called when a key is typed
 */

public interface OnKeyTyped extends Event<View, Key> {
}
