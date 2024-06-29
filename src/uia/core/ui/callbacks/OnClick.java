package uia.core.ui.callbacks;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.Callback;

/**
 * Callback called by a View when a click, occurred inside its area, is detected.
 * <br>
 * It provides the {@link ScreenTouch}s that are within the View's area.
 */

public interface OnClick extends Callback<ScreenTouch[]> {
}
