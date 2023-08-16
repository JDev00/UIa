package platform.test;

import platform.awt.ContextAWT;
import uia.core.architecture.Context;

public class TestContext extends ContextAWT {

    public TestContext(int x, int y) {
        super(x, y);
    }

    @Override
    public void setup() {
        setHints(HINT.ANTIALIASING_TEXT_ON);

        setView(new Test1(this));
    }

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();

        Context context = new TestContext((int) (0.8f * dim[0]), (int) (0.8f * dim[1]));
        context.setWindowParams(false, true, "UIa_Test");
        context.start();
    }
}