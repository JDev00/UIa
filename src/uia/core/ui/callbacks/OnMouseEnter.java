package uia.core.ui.callbacks;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.Callback;

import java.util.List;

/**
 * Callback called by a View when mouse enters its area.
 * <br>
 * It provides the {@link ScreenTouch}s, as a List, that are within the View's area.
 */

public interface OnMouseEnter extends Callback<List<ScreenTouch>> {
}
