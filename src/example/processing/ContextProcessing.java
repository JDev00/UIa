package example.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.basement.Message;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.Graphic;
import uia.core.ui.context.InputEmulator;
import uia.core.ui.context.Window;
import uia.physical.ComponentHiddenRoot;
import uia.physical.message.MessageFactory;
import uia.physical.message.MessageStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Processing based {@link Context} implementation. Just demonstrative.
 */

public class ContextProcessing implements Context {
    private final Window window;
    private final RendererEngine rendererEngine;
    private LifecycleStage lifecycleStage = LifecycleStage.STOP;

    private static final int[] initScreenDim = new int[2];
    private static final String RENDERER = PConstants.P3D;

    public ContextProcessing(int w, int h) {
        window = new WindowProcessing();

        initScreenDim[0] = w;
        initScreenDim[1] = h;

        rendererEngine = new RendererEngine();
    }

    @Override
    public void setView(View view) {
        RendererEngine.currentView = view;
    }

    @Override
    public void setLifecycleStage(LifecycleStage lifecycleStage) {
        if (this.lifecycleStage.equals(lifecycleStage)) {
            return;
        }

        this.lifecycleStage = lifecycleStage;
        if (Objects.requireNonNull(lifecycleStage) == LifecycleStage.RUN) {
            PApplet.runSketch(new String[]{"RendererEngine"}, rendererEngine);
        } else {
            rendererEngine.stop();
        }
    }

    @Override
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }

    @Override
    public void setRenderingHint(RenderingHint... hint) {
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public InputEmulator getInputEmulator() {
        return null;
    }

    @Override
    public String clipboard(ClipboardOperation operation, String str) {
        return null;
    }

    //

    public static class RendererEngine extends PApplet {
        private Graphic graphic;
        private PGraphics nativeGraphics;

        private static View currentView;
        private final View rootView = new ComponentHiddenRoot();

        private final List<ScreenTouch> screenTouches = new ArrayList<>();

        @Override
        public void settings() {
            size(initScreenDim[0], initScreenDim[1], RENDERER);
        }

        @Override
        public void setup() {
            graphic = new GraphicProcessing(
                    () -> this,
                    () -> nativeGraphics);
        }

        @Override
        public void draw() {
            nativeGraphics = g;

            g.clear();

            rootView.setPosition(0f, 0f);
            rootView.setDimension(width, height);
            rootView.requestFocus(focused);

            if (currentView != null) {
                int counter = 0;
                int limit = 30000 / (int) Math.max(1, frameRate);
                Message message;

                while ((message = MessageStore.getInstance().pop()) != null && counter < limit) {
                    currentView.dispatchMessage(message);
                    counter++;
                }

                currentView.update(rootView);
                currentView.draw(graphic);
            }
        }

        /**
         * Dispatch keys
         */

        private void dispatch(KeyEvent event, Key.Action action) {
            Key key = new Key(action, event.getModifiers(), event.getKey(), event.getKeyCode());
            if (currentView != null) {
                currentView.dispatchMessage(MessageFactory.createKeyEventMessage(key, null));
            }
        }

        /**
         * Dispatch screen touches
         */

        private void dispatch(MouseEvent event, ScreenTouch.Action action) {
            ScreenTouch.Button button = null;
            switch (event.getButton()) {
                case LEFT:
                    button = ScreenTouch.Button.LEFT;
                    break;
                case CENTER:
                    button = ScreenTouch.Button.CENTER;
                    break;
                case RIGHT:
                    button = ScreenTouch.Button.RIGHT;
                    break;
            }

            screenTouches.clear();
            screenTouches.add(new ScreenTouch(action, button, event.getX(), event.getY(), event.getCount()));
            if (currentView != null) {
                currentView.dispatchMessage(MessageFactory.createScreenEventMessage(screenTouches, null));
            }
        }

        @Override
        public void mousePressed(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.PRESSED);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.RELEASED);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.DRAGGED);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.MOVED);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.CLICKED);
        }

        @Override
        public void mouseWheel(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.WHEEL);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            dispatch(event, ScreenTouch.Action.EXITED);
        }

        @Override
        public void keyPressed(KeyEvent event) {
            dispatch(event, Key.Action.PRESSED);
        }

        @Override
        public void keyReleased(KeyEvent event) {
            dispatch(event, Key.Action.RELEASED);
        }

        @Override
        public void keyTyped(KeyEvent event) {
            dispatch(event, Key.Action.TYPED);
        }
    }

    //

    private static class WindowProcessing implements Window {

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
}
