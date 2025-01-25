package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.ui.primitives.Key;

/**
 * Callback called when a key is typed.
 * <br>
 * It provides the typed {@link Key}.
 */

@FunctionalInterface
public interface OnKeyTyped extends Callback<Key> {
}
