package uia.application.platform.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uia.core.Key;
import uia.core.Pointer;
import uia.core.basement.Context;
import uia.core.ui.View;
import uia.core.basement.Graphic;
import uia.physical.ComponentRoot;
import uia.physical.message.MessageStore;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Context} implementation based on Processing
 */

public class ContextProcessing implements Context {
    private final Renderer renderer;

    private static final int[] initScreenDim = new int[2];
    private static final String RENDERER = PConstants.P3D;

    public ContextProcessing(int w, int h) {
        initScreenDim[0] = w;
        initScreenDim[1] = h;

        renderer = new Renderer();
    }

    // Renderer

    public static class Renderer extends PApplet {
        private Graphic graphic;

        private static View currentView;

        private final View rootView = new ComponentRoot();

        private final List<Pointer> pointers = new ArrayList<>();

        @Override
        public void settings() {
            size(initScreenDim[0], initScreenDim[1], RENDERER);
        }

        @Override
        public void setup() {
            graphic = new GraphicProcessing();
        }

        @Override
        public void draw() {
            g.clear();

            graphic.setNative(g, this);

            rootView.setPosition(0f, 0f);
            rootView.setDimension(width, height);
            rootView.setFocus(focused);

            if (currentView != null) {
                int counter = 0;
                int limit = 30000 / (int) Math.max(1, frameRate);
                Object[] message;

                while ((message = MessageStore.getInstance().pop()) != null && counter < limit) {
                    currentView.dispatch(message[0], (String) message[1], (String) message[2]);
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
                currentView.dispatch(key);
        }

        /**
         * Dispatch pointers
         */

        private void dispatch(MouseEvent event, Pointer.ACTION action) {
            Pointer.BUTTON button = null;
            switch (event.getButton()) {
                case LEFT:
                    button = Pointer.BUTTON.LEFT;
                    break;
                case CENTER:
                    button = Pointer.BUTTON.CENTER;
                    break;
                case RIGHT:
                    button = Pointer.BUTTON.RIGHT;
                    break;
            }

            pointers.clear();
            pointers.add(new Pointer(action, button, event.getX(), event.getY(), event.getCount()));

            if (currentView != null)
                currentView.dispatch(pointers);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            dispatch(event, Pointer.ACTION.PRESSED);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            dispatch(event, Pointer.ACTION.RELEASED);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            dispatch(event, Pointer.ACTION.DRAGGED);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            dispatch(event, Pointer.ACTION.MOVED);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            dispatch(event, Pointer.ACTION.CLICKED);
        }

        @Override
        public void mouseWheel(MouseEvent event) {
            dispatch(event, Pointer.ACTION.WHEEL);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            dispatch(event, Pointer.ACTION.EXITED);
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
    public Object getNative() {
        return renderer;
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
    public void setWindowParams(boolean alwaysOnTop, boolean resizable, String title) {
        /*render.getSurface().setAlwaysOnTop(alwaysOnTop);
        render.getSurface().setResizable(resizable);
        render.getSurface().setTitle(title);*/
    }

    @Override
    public int getFrameRate() {
        return (int) renderer.frameRate;
    }

    @Override
    public int width() {
        return renderer.width;
    }

    @Override
    public int height() {
        return renderer.height;
    }

    @Override
    public boolean isRunning() {
        return renderer.isLooping();
    }

    @Override
    public String clipboard(String str, boolean toCopy) {
        return null;
    }
}
