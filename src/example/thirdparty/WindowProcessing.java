package example.thirdparty;

import uia.core.ui.context.Window;

public class WindowProcessing implements Window {

    @Override
    public Window setAlwaysOnTop(boolean alwaysOnTop) {
        return null;
    }

    @Override
    public Window setResizable(boolean resizable) {
        return null;
    }

    @Override
    public Window setTitle(String title) {
        return null;
    }

    @Override
    public Window show() {
        return null;
    }

    @Override
    public Window hide() {
        return null;
    }

    @Override
    public Window resize(int width, int height) {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public int[] getInsets() {
        return null;
    }
}
