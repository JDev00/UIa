package uia.platform.swing;

import uia.physical.message.store.GlobalMessageStore;
import uia.physical.message.store.MessageStore;
import uia.physical.callbacks.CallbackStore;
import uia.physical.message.Messages;
import uia.core.basement.Callable;
import uia.core.basement.Callback;
import uia.core.ui.window.Window;
import uia.core.ScreenTouch;
import uia.core.ui.window.*;
import uia.core.Key;

import java.util.function.IntFunction;
import java.util.Collections;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * {@link Window} implementation based on Java Swing.
 */

public class WindowSwing implements Window {
    private final MessageStore globalMessageStore = GlobalMessageStore.getInstance();
    private final Callable callable;

    private final JFrame jFrame;
    private final int[] screenSize = new int[2];
    private boolean focus = false;

    public WindowSwing(int x, int y) {
        screenSize[0] = x;
        screenSize[1] = y;

        callable = new CallbackStore(10);

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.setTitle("UIa Swing Window");
        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                screenSize[0] = container.getWidth();
                screenSize[1] = container.getHeight();
                // notifies clients when window resizes
                notifyCallbacks(OnWindowResized.class, WindowSwing.this);
            }
        });
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                notifyCallbacks(OnWindowClosed.class, WindowSwing.this);
            }
        });
        jFrame.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                updateFocus(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateFocus(false);
            }
        });
        jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                Key key = new Key(Key.Action.TYPED, e.getModifiers(), e.getKeyChar(), e.getKeyCode());
                addKeyEvent(key);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Key key = new Key(Key.Action.PRESSED, e.getModifiers(), e.getKeyChar(), e.getKeyCode());
                addKeyEvent(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Key key = new Key(Key.Action.RELEASED, e.getModifiers(), e.getKeyChar(), e.getKeyCode());
                addKeyEvent(key);
            }
        });
        jFrame.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.PRESSED));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.RELEASED));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.CLICKED));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // useless
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.EXITED));
            }
        });
        jFrame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.DRAGGED));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                addScreenTouchEvent(getScreenTouches(e, 0, ScreenTouch.Action.MOVED));
            }
        });
        jFrame.addMouseWheelListener(event -> addScreenTouchEvent(
                getScreenTouches(event, event.getWheelRotation(), ScreenTouch.Action.WHEEL))
        );
    }

    protected void addUIComponent(Component component) {
        jFrame.add(component);
    }

    /**
     * Destroy this Window
     */

    protected void destroy() {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        jFrame.dispose();
    }

    /**
     * Helper function. Adds a new Key event to the global store.
     */

    private void addKeyEvent(Key key) {
        globalMessageStore.add(Messages.newKeyEventMessage(key, null));
    }

    /**
     * Helper function. Adds a new ScreenTouch event to the global store.
     */

    private void addScreenTouchEvent(List<ScreenTouch> screenTouches) {
        globalMessageStore.add(Messages.newScreenEventMessage(screenTouches, null));
    }

    /**
     * Helper function. Returns a new List of {@link ScreenTouch}s.
     */

    private List<ScreenTouch> getScreenTouches(MouseEvent mouseEvent, int wheelRotation, ScreenTouch.Action action) {
        IntFunction<ScreenTouch.Button> mapNativeMouseButton = button -> {
            switch (button) {
                case 1:
                    return ScreenTouch.Button.LEFT;
                case 2:
                    return ScreenTouch.Button.CENTER;
                case 3:
                    return ScreenTouch.Button.RIGHT;
                default:
                    return null;
            }
        };

        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        int[] insets = getInsets();
        int[] position = {x - insets[0], y - insets[1]};

        ScreenTouch screenTouch = new ScreenTouch(
                action,
                mapNativeMouseButton.apply(mouseEvent.getButton()),
                position[0],
                position[1],
                wheelRotation);
        return new ArrayList<>(Collections.singletonList(screenTouch));
    }

    /**
     * Helper function. Updates the window focus.
     *
     * @param focus true to set window on focus
     */

    private void updateFocus(boolean focus) {
        this.focus = focus;
        if (focus) {
            notifyCallbacks(OnWindowGainedFocus.class, WindowSwing.this);
        } else {
            notifyCallbacks(OnWindowLostFocus.class, WindowSwing.this);
        }
    }

    @Override
    public void registerCallback(Callback<?> callback) {
        callable.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        callable.unregisterCallback(callback);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        callable.notifyCallbacks(type, data);
    }

    @Override
    public Window setVisible(boolean visible) {
        jFrame.setVisible(visible);
        return this;
    }

    @Override
    public boolean isVisible() {
        return jFrame.isVisible();
    }

    @Override
    public Window setAlwaysOnTop(boolean alwaysOnTop) {
        jFrame.setAlwaysOnTop(alwaysOnTop);
        return this;
    }

    @Override
    public Window setResizable(boolean resizable) {
        jFrame.setResizable(resizable);
        return this;
    }

    @Override
    public Window setTitle(String title) {
        jFrame.setTitle(title);
        return this;
    }

    @Override
    public Window resize(int width, int height) {
        if (width < 200) {
            throw new IllegalArgumentException("width must be greater than 200 pixels");
        }
        if (height < 200) {
            throw new IllegalArgumentException("height must be greater than 200 pixels");
        }

        screenSize[0] = width;
        screenSize[1] = height;
        jFrame.setSize(width, height);
        return this;
    }

    @Override
    public int getViewportWidth() {
        return screenSize[0];
    }

    @Override
    public int getViewportHeight() {
        return screenSize[1];
    }

    @Override
    public boolean isFocused() {
        return focus;
    }

    @Override
    public int[] getInsets() {
        Insets insets = jFrame.getInsets();
        return new int[]{
                insets.left, insets.top,
                insets.right, insets.bottom
        };
    }
}
