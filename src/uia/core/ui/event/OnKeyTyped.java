package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Key;

/**
 * Event called when a key is typed.
 * <br>
 * This event provides the typed {@link Key}.
 */

public interface OnKeyTyped extends Event<Key> {
}
