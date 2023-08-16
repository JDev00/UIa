package uia.core.architecture.ui.event;

import uia.core.architecture.Event;
import uia.core.architecture.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event representing a mouse enter event
 */

public interface EventEnter extends Event<View, List<Pointer>> {
}
