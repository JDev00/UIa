package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.ScreenPointer;

import java.util.List;

/**
 * Event called by a View when at least one Pointer is inside its area.
 * <br>
 * This event provides all the {@link ScreenPointer}s that are currently inside the View's area.
 */

public interface OnMouseHover extends Callback<List<ScreenPointer>> {
}
