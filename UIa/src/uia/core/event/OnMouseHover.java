package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called when mouse lays over the View's area
 */

public interface OnMouseHover extends Event<View, List<Pointer>> {
}
