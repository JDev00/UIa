package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.ui.primitives.Key;

/**
 * Callback called when a key is released.
 * <br>
 * It provides the released {@link Key}.
 */

@FunctionalInterface
public interface OnKeyReleased extends Callback<Key> {
}
