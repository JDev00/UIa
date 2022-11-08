package uia.test.example;

import uia.core.platform.implementation.ContextAWT;

public class ContextDesk extends ContextAWT {

    public ContextDesk(int x, int y) {
        super(x, y);

        addHint(HINT.ANTIALIASING_TEXT);
        addHint(HINT.RENDER_QUALITY_HIGH);
    }

    @Override
    public void asyncSetup(int x, int y) {
        storePage(new Page3(this));
        open(getStoredPage(0));
    }

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();
        ContextDesk context = new ContextDesk((int) (0.8f * dim[0]), (int) (0.8f * dim[1]));
        context.setTitle("UIa");
        context.start();
    }
}