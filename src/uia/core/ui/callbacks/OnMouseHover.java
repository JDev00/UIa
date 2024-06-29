package uia.core.ui.callbacks;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.Callback;

/**
 * Callback called by a View when at least one ScreenPointer is within its area.
 * <br>
 * It provides the {@link ScreenTouch}s that are within the View's area.
 */

public interface OnMouseHover extends Callback<ScreenTouch[]> {
}
