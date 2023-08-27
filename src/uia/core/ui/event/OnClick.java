package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called by a View when a click, that happens inside its area, is detected.
 * <br>
 * This event provides all the {@link Pointer}s that are currently inside the View's area.
 */

public interface OnClick extends Event<List<Pointer>> {
}
