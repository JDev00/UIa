package uia.core.context.window;

import uia.core.basement.Callback;

/**
 * Callback invoked by a Window when the Window itself has lost focus.
 * <br>
 * It returns the Window that lost the focus.
 *
 * @since 1.5.6
 */

public interface OnWindowLostFocus extends Callback<Window> {
}
