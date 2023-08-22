package uia.core.event;

import uia.core.basement.Event;
import uia.core.ui.View;
import uia.core.Pointer;

import java.util.List;

/**
 * Event called when a click happened
 */

public interface OnClick extends Event<View, List<Pointer>> {
}
