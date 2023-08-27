package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called by a View when at least one Pointer is inside its area.
 * <br>
 * This event provides all the {@link Pointer}s that are currently inside the View's area.
 */

public interface OnMouseHover extends Event<List<Pointer>> {
}
