package uia.core.ui.window;

import uia.core.basement.Callback;

/**
 * Callback invoked by a Window when the Window itself has lost focus.
 * <br>
 * It returns the Window that lost the focus.
 */

public interface OnWindowLostFocus extends Callback<Window> {
}
