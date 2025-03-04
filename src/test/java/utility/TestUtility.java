package utility;

import uia.application.ui.component.ComponentHiddenRoot;
import uia.application.ui.component.text.ComponentText;
import uia.core.rendering.color.ColorCollection;
import uia.application.ui.group.ComponentGroup;
import uia.application.ui.component.Component;
import uia.core.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import adaptor.swing.ContextSwing;

import java.util.function.Supplier;

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
     * Waits until a specific condition is satisfied.
     *
     * @param condition   the condition to be satisfied
     * @param retryPeriod the time between each evaluation
     * @param timeout     the maximum time in milliseconds the condition is evaluated
     * @throws RuntimeException if the condition is not met within the provided timeout
     */

    public static void waitUntil(Supplier<Boolean> condition, int retryPeriod, int timeout) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeout) {
            if (condition.get()) {
                return;
            }
            waitFor(retryPeriod);
        }

        throw new RuntimeException("Timeout exceeded while waiting for condition.");
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
        ViewGroup result = createViewGroup("root_group", 0.5f, 0.5f, 1f, 1f);
        result.getStyle().setBackgroundColor(ColorCollection.NAVY);
        return result;
    }

    /**
     * Creates a graphical tree with a root and two children (id1 = view1, id2 = view2).
     *
     * @return the graphical tree
     */

    public static ViewGroup createSimpleTree() {
        ViewGroup root = createRoot();
        ViewGroup.insert(root,
                createView("view1", 0.25f, 0.5f, 0.5f, 1f),
                createView("view2", 0.25f, 0.5f, 0.5f, 1f)
        );
        return root;
    }

    /**
     * Updates the provided view.
     */

    public static void updateView(int windowWidth, int windowHeight, View root) {
        View hiddenRoot = new ComponentHiddenRoot();
        hiddenRoot.getStyle()
                .setPosition(0, 0)
                .setDimension(windowWidth, windowHeight);
        root.update(hiddenRoot);
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
