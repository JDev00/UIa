package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.ui.primitives.Key;

/**
 * Callback called when a key is pressed.
 * <br>
 * It provides the pressed {@link Key}.
 */

@FunctionalInterface
public interface OnKeyPressed extends Callback<Key> {
}
