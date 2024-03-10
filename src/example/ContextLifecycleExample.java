package example;

import uia.application.desktop.ContextSwing;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.context.Context;
import uia.physical.Component;

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
        view.registerCallback((OnClick) touches -> {
            System.out.println("Terminated!");
            context.setLifecycleStage(Context.LifecycleStage.TERMINATE);
        });

        context.setView(view);
    }
}
