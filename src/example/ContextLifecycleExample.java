package example;

import uia.physical.ui.component.Component;
import uia.physical.ui.theme.Theme;
import uia.platform.swing.ContextSwing;
import uia.core.ui.callbacks.OnClick;
import uia.core.context.Context;
import uia.core.ui.View;

public class ContextLifecycleExample {

    public static View createMockView() {
        return new Component("A", 0.5f, 0.5f, 0.5f, 0.5f).setExpanseLimit(1.1f, 1.1f);
    }

    public static Context createAWTContext() {
        int[] screenSize = ContextSwing.getScreenSize();
        return ContextSwing.createAndStart(4 * screenSize[0] / 5, 4 * screenSize[1] / 5);
    }

    public static void main(String[] args) {
        Context context = createAWTContext();

        View view = createMockView();
        view.getStyle()
                .setBackgroundColor(Theme.RED);

        view.registerCallback((OnClick) touches -> {
            System.out.println("Terminated!");
            context.setLifecycleStage(Context.LifecycleStage.TERMINATED);
        });

        context.setView(view);
    }
}
