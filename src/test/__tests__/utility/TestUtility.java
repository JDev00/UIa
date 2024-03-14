package test.__tests__.utility;

import uia.application.desktop.ContextSwing;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.context.Context;
import uia.physical.component.Component;
import uia.physical.group.ComponentGroup;
import uia.physical.component.ComponentText;
import uia.physical.theme.Theme;

/**
 * Collections of test utilities
 */

public class TestUtility {

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
        result.getPaint().setColor(Theme.DARK_GRAY);
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
