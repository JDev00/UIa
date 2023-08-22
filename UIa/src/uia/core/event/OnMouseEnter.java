package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called when mouse enters the View's area
 */

public interface OnMouseEnter extends Event<View, List<Pointer>> {
}
