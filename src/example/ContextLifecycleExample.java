package example;

import uia.application.awt.ContextAWT;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.context.Context;
import uia.physical.Component;

public class ContextLifecycleExample {

    public static View createMockView() {
        return new Component("A", 0.5f, 0.5f, 0.5f, 0.5f, 1.1f, 1.1f);
    }

    public static Context createAWTContext() {
        int[] screenSize = ContextAWT.getScreenSize();
        Context result = new ContextAWT(4 * screenSize[0] / 5, 4 * screenSize[1] / 5);
        result.getWindow().show();
        result.setLifecycleStage(Context.LifecycleStage.RUN);
        return result;
    }

    public static void main(String[] args) {
        Context context = createAWTContext();

        View view = createMockView();
        view.registerCallback((OnClick) pointers -> {
            System.out.println("Clicked!");
            context.setLifecycleStage(Context.LifecycleStage.TERMINATE);
        });

        context.setView(view);
    }
}
