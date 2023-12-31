package example.thirdparty;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.basement.Message;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.physical.ComponentHiddenRoot;
import uia.physical.message.MessageStore;
import uia.physical.message.Messages;

import java.util.ArrayList;
import java.util.List;

public class RendererEngineProcessing extends PApplet {
    private Graphic graphic;
    private PGraphics nativeGraphics;

    public static final String RENDERER = PConstants.P3D;
    public static final int[] initScreenDimension = new int[2];

    public static View currentView;
    private final View rootView = new ComponentHiddenRoot();

    private final List<ScreenTouch> screenTouches = new ArrayList<>();

    @Override
    public void settings() {
        size(initScreenDimension[0], initScreenDimension[1], RENDERER);
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
            currentView.dispatchMessage(Messages.newKeyEventMessage(key, null));
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
            currentView.dispatchMessage(Messages.newScreenEventMessage(screenTouches, null));
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
