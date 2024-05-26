package example.thirdparty;

import uia.core.ui.context.window.Window;

public class WindowProcessing implements Window {

    @Override
    public Window setVisible(boolean visible) {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

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
    public Window resize(int width, int height) {
        return null;
    }

    @Override
    public int getViewportWidth() {
        return 0;
    }

    @Override
    public int getViewportHeight() {
        return 0;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public int[] getInsets() {
        return new int[0];
    }
}
