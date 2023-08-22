package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called when mouse exits the View's area
 */

public interface OnMouseExit extends Event<View, List<Pointer>> {
}
