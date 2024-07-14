package example;

import uia.physical.ui.component.text.ComponentText;
import uia.physical.ui.component.Component;
import uia.core.ui.callbacks.OnClick;
import uia.core.context.Context;
import uia.physical.ui.Theme;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import api.swing.ContextSwing;

/**
 * Demonstrative example. Closes the application programmatically
 * by clicking on the red view.
 */

public class ContextLifecycleExample {

    /**
     * Creates a custom View.
     */

    public static View createView() {
        ViewText result = new ComponentText(
                new Component("", 0.5f, 0.5f, 0.5f, 0.5f).setExpanseLimit(1.1f, 1.1f)
        );
        result.getStyle().setBackgroundColor(Theme.RED);
        return result;
    }

    /**
     * Creates and starts the application Context.
     */

    public static Context createContext() {
        int[] screenSize = ContextSwing.getScreenSize();
        int width = 4 * screenSize[0] / 5;
        int height = 4 * screenSize[1] / 5;
        return ContextSwing.createAndStart(width, height);
    }

    private static void closeApplication(Context context) {
        context.setLifecycleStage(Context.LifecycleStage.TERMINATED);
    }

    public static void main(String[] args) {
        Context context = createContext();

        View view = createView();
        // when the view is clicked, the application is closed
        view.registerCallback((OnClick) touches -> closeApplication(context));

        context.setView(view);
    }
}
