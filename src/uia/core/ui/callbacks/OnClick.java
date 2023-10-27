package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.ScreenPointer;

import java.util.List;

/**
 * Callback called by a View when a click, occurred inside its area, is detected.
 * <br>
 * It provides the {@link ScreenPointer}s, as a List, that are within the View's area.
 */

public interface OnClick extends Callback<List<ScreenPointer>> {
}
