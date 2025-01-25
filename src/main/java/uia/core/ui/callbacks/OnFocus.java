package uia.core.ui.callbacks;

import uia.core.basement.Callback;

/**
 * Callback called when focus is gained or lost by a View.
 * <br>
 * It provides a Boolean whose value is True when focus is gained.
 */

@FunctionalInterface
public interface OnFocus extends Callback<Boolean> {
}
