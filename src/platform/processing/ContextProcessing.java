package platform.processing;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uia.core.Key;
import uia.core.Pointer;
import uia.core.architecture.Context;
import uia.core.architecture.ui.View;
import uia.core.architecture.Graphic;
import uia.physical.ui.RootView;

import java.util.ArrayList;
import java.util.List;

/**
 * Context implementation to support Processing behind the scene
 */

public abstract class ContextProcessing implements Context {
    private final Render render;

    private static final List<View> VIEWS = new ArrayList<>();

    private static int screenWidth;
    private static int screenHeight;

    public ContextProcessing(int w, int h) {
        screenWidth = w;
        screenHeight = h;

        render = new Render();

        new Thread(this::setup).start();
    }

    // Renderer

    public static class Render extends PApplet {
        private Graphic graphic;

        private final RootView rootView = new RootView();

        private final List<Pointer> pointers = new ArrayList<>();

        @Override
        public void settings() {
            size(screenWidth, screenHeight, P3D);
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

            for (View i : VIEWS) {
                i.update(rootView);
                i.draw(graphic);
            }
        }

        /**
         * Dispatch keys
         */

        private void dispatch(KeyEvent event, Key.ACTION action) {
            Key key = new Key(action, event.getModifiers(), event.getKey(), event.getKeyCode());

            for (View i : VIEWS) {
                i.dispatch(key);
            }
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

            for (View i : VIEWS) {
                i.dispatch(pointers);
            }
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

    public abstract void setup();

    @Override
    public Object getNative() {
        return render;
    }

    @Override
    public void setHints(HINT... hint) {

    }

    @Override
    public void start() {
        PApplet.main(render.getClass());
    }

    @Override
    public void stop() {
        render.stop();
    }

    @Override
    public void setWindowParams(boolean alwaysOnTop, boolean resizable, String title) {
        /*render.getSurface().setAlwaysOnTop(alwaysOnTop);
        render.getSurface().setResizable(resizable);
        render.getSurface().setTitle(title);*/
    }

    @Override
    public void setView(View view) {
        VIEWS.clear();
        VIEWS.add(view);
    }

    @Override
    public int getFrameRate() {
        return (int) render.frameRate;
    }

    @Override
    public int width() {
        return render.width;
    }

    @Override
    public int height() {
        return render.height;
    }

    @Override
    public int initialWidth() {
        return screenWidth;
    }

    @Override
    public int initialHeight() {
        return screenHeight;
    }

    @Override
    public boolean isRunning() {
        return render.isLooping();
    }

    @Override
    public String clipboard(String str, boolean toCopy) {
        return null;
    }
}
