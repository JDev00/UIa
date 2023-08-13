package uia.core.architecture.ui.event;

import uia.core.architecture.Event;
import uia.core.architecture.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event representing a Click
 */

public interface EventClick extends Event<View, List<Pointer>> {
}
