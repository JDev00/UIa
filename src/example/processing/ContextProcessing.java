package example.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uia.core.Key;
import uia.core.ScreenPointer;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.Graphic;
import uia.physical.ComponentRoot;
import uia.physical.message.MessageStore;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Context} implementation based on Processing
 */

// TODO: implement context correctly

public class ContextProcessing implements Context {
    private final Window window;
    private final Renderer renderer;

    private static final int[] initScreenDim = new int[2];
    private static final String RENDERER = PConstants.P3D;

    public ContextProcessing(int w, int h) {
        window = new WindowPro();

        initScreenDim[0] = w;
        initScreenDim[1] = h;

        renderer = new Renderer();
    }

    // Renderer

    public static class Renderer extends PApplet {
        private Graphic graphic;

        private static View currentView;

        private final View rootView = new ComponentRoot();

        private final List<ScreenPointer> screenPointers = new ArrayList<>();

        @Override
        public void settings() {
            size(initScreenDim[0], initScreenDim[1], RENDERER);
        }

        @Override
        public void setup() {
            graphic = new GraphicProcessing();
        }

        private final Object[] nativeData = new Object[2];

        @Override
        public void draw() {
            g.clear();

            nativeData[0] = g;
            nativeData[1] = this;
            graphic.setNative(nativeData);

            rootView.setPosition(0f, 0f);
            rootView.setDimension(width, height);
            rootView.requestFocus(focused);

            if (currentView != null) {
                int counter = 0;
                int limit = 30000 / (int) Math.max(1, frameRate);
                Object[] message;

                while ((message = MessageStore.getInstance().pop()) != null && counter < limit) {
                    currentView.dispatch(View.DISPATCHER.MESSAGE, message);
                    counter++;
                }

                currentView.update(rootView);
                currentView.draw(graphic);
            }
        }

        /**
         * Dispatch keys
         */

        private void dispatch(KeyEvent event, Key.ACTION action) {
            Key key = new Key(action, event.getModifiers(), event.getKey(), event.getKeyCode());
            if (currentView != null)
                currentView.dispatch(View.DISPATCHER.KEY, key);
        }

        /**
         * Dispatch pointers
         */

        private void dispatch(MouseEvent event, ScreenPointer.ACTION action) {
            ScreenPointer.BUTTON button = null;
            switch (event.getButton()) {
                case LEFT:
                    button = ScreenPointer.BUTTON.LEFT;
                    break;
                case CENTER:
                    button = ScreenPointer.BUTTON.CENTER;
                    break;
                case RIGHT:
                    button = ScreenPointer.BUTTON.RIGHT;
                    break;
            }

            screenPointers.clear();
            screenPointers.add(new ScreenPointer(action, button, event.getX(), event.getY(), event.getCount()));

            if (currentView != null)
                currentView.dispatch(View.DISPATCHER.POINTERS, screenPointers);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.PRESSED);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.RELEASED);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.DRAGGED);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.MOVED);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.CLICKED);
        }

        @Override
        public void mouseWheel(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.WHEEL);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            dispatch(event, ScreenPointer.ACTION.EXITED);
        }

        @Override
        public void keyPressed(KeyEvent event) {
            dispatch(event, Key.ACTION.PRESSED);
        }

        @Override
        public void keyReleased(KeyEvent event) {
            dispatch(event, Key.ACTION.RELEASED);
        }

        @Override
        public void keyTyped(KeyEvent event) {
            dispatch(event, Key.ACTION.TYPED);
        }
    }

    @Override
    public void setView(View view) {
        Renderer.currentView = view;
    }

    @Override
    public void setHints(HINT... hint) {

    }

    @Override
    public void start() {
        PApplet.main(renderer.getClass());
    }

    @Override
    public void stop() {
        renderer.stop();
    }

    @Override
    public int getFrameRate() {
        return (int) renderer.frameRate;
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public ArtificialInput getArtificialInput() {
        return null;
    }

    @Override
    public boolean isRunning() {
        return renderer.isLooping();
    }

    @Override
    public String clipboard(CLIPBOARD_OPERATION operation, String str) {
        return null;
    }

    /*
     *
     */

    private static class WindowPro implements Window {

        @Override
        public Object getNative() {
            return null;
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
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }
    }
}
