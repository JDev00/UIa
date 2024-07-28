package utility;

import uia.application.ui.component.text.ComponentText;
import uia.core.rendering.color.ColorCollection;
import uia.application.ui.group.ComponentGroup;
import uia.application.ui.component.Component;
import uia.core.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import api.swing.ContextSwing;

/**
 * Collections of utilities for tests.
 */

public final class TestUtility {

    private TestUtility() {
    }

    /**
     * Freezes the thread that calls this function for the specified amount of milliseconds.
     *
     * @param millis the freeze time (> 0) in milliseconds
     */

    public static void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException error) {
            error.printStackTrace();
        }
    }

    /**
     * Creates a new View.
     *
     * @return a new {@link View}
     */

    public static View createView(String id, float x, float y, float width, float height) {
        return new Component(id, x, y, width, height);
    }

    /**
     * Creates a new ViewText.
     *
     * @return a new {@link ViewText}
     */

    public static ViewText createViewText(String id, float x, float y, float width, float height) {
        return new ComponentText(new Component(id, x, y, width, height));
    }

    /**
     * Creates a new ViewGroup.
     *
     * @return a new {@link ViewGroup}
     */

    public static ViewGroup createViewGroup(String id, float x, float y, float width, float height) {
        return new ComponentGroup(new Component(id, x, y, width, height));
    }

    /**
     * Creates a new ViewGroup that fills the entire screen.
     *
     * @return a new {@link ViewGroup}
     */

    public static ViewGroup createRoot() {
        ViewGroup result = createViewGroup("ROOT_GROUP", 0.5f, 0.5f, 1f, 1f);
        result.getStyle().setBackgroundColor(ColorCollection.DARK_GRAY);
        return result;
    }

    /**
     * Creates a new ComponentTracker.
     */

    public static View createTracker() {
        return new ComponentTracker(0.95f, 0.95f, 0.1f, 0.1f);
    }

    /**
     * Create a new ContextAWT and start it. The window dimensions are:
     * <ul>
     *     <li>
     *         width: 720;
     *     </li>
     *     <li>
     *         height: 540
     *     </li>
     * </ul>
     *
     * @return a new {@link ContextSwing}
     */

    public static Context createMockContext() {
        return ContextSwing.createAndStart(720, 540);
    }
}
