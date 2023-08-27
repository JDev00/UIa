package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called by a View when mouse enters its area.
 * <br>
 * This event provides all the {@link Pointer}s that are currently inside the View's area.
 */

public interface OnMouseEnter extends Event<List<Pointer>> {
}
