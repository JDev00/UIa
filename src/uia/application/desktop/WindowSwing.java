package uia.application.desktop;

import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.basement.Message;
import uia.core.ui.context.Window;
import uia.physical.message.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Built-in {@link Window} implementation based on Java Swing.
 */

public class WindowSwing implements Window {
    private final JFrame jFrame;
    private final LinkedList<Message> eventMessage = new LinkedList<>();
    private final int[] screenSize = new int[2];
    private boolean focus = false;

    public WindowSwing(int x, int y) {
        screenSize[0] = x;
        screenSize[1] = y;

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.setTitle("UIa Standard Window");
        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                screenSize[0] = container.getWidth();
                screenSize[1] = container.getHeight();
            }
        });
        jFrame.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                focus = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                focus = false;
            }
        });
        jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                addKeyEvent(new Key(Key.Action.TYPED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                addKeyEvent(new Key(Key.Action.PRESSED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                addKeyEvent(new Key(Key.Action.RELEASED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
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
        jFrame.addMouseWheelListener(e -> {
            addScreenTouchEvent(getScreenTouches(e, e.getWheelRotation(), ScreenTouch.Action.WHEEL));
        });
    }

    private void addKeyEvent(Key key) {
        synchronized (eventMessage) {
            eventMessage.push(Messages.newKeyEventMessage(key, null));
        }
    }

    private void addScreenTouchEvent(List<ScreenTouch> screenTouches) {
        synchronized (eventMessage) {
            eventMessage.push(Messages.newScreenEventMessage(screenTouches, null));
        }
    }

    protected void addUIComponent(Component component) {
        jFrame.add(component);
    }

    /**
     * Get and remove the first generated Event as a {@link Message}.
     */

    protected Message popEventMessage() {
        synchronized (eventMessage) {
            return eventMessage.poll();
        }
    }

    /**
     * Destroy this Window
     */

    protected void destroy() {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        jFrame.dispose();
    }

    /**
     * @return the corresponding {@link ScreenTouch.Button} or null
     */

    private static ScreenTouch.Button mapNativeMouseButton(int button) {
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
    }

    private final java.util.List<ScreenTouch> screenTouches = new ArrayList<>();

    /**
     * Helper function. Returns a List of {@link ScreenTouch}s.
     */

    private List<ScreenTouch> getScreenTouches(MouseEvent mouseEvent, int wheelRotation, ScreenTouch.Action action) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        int[] insets = getInsets();
        int[] position = {x - insets[0], y - insets[1]};

        screenTouches.clear();
        screenTouches.add(new ScreenTouch(
                action,
                mapNativeMouseButton(mouseEvent.getButton()),
                position[0],
                position[1],
                wheelRotation));
        return screenTouches;
    }

    @Override
    public Window show() {
        jFrame.setVisible(true);
        return this;
    }

    @Override
    public Window hide() {
        jFrame.setVisible(false);
        return this;
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

    /**
     * @throws IllegalArgumentException {@code if width < 200 or height < 200}
     */

    @Override
    public Window resize(int width, int height) {
        if (width < 200) throw new IllegalArgumentException("width must be greater than 200 pixels");
        if (height < 200) throw new IllegalArgumentException("height must be greater than 200 pixels");
        screenSize[0] = width;
        screenSize[1] = height;
        jFrame.setSize(width, height);
        return this;
    }

    @Override
    public int getWidth() {
        return screenSize[0];
    }

    @Override
    public int getHeight() {
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
