package uia.application.list.positioner;

import uia.core.ui.View;

/**
 * ViewPositioner positions is responsible for positioning a View within a view list.
 */

public interface ViewPositioner {

    /**
     * Positions the specified View.
     *
     * @param view  a not null {@link View} to be placed
     * @param index the View position
     */

    void place(View view, int index);
}
