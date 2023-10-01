package uia.application.platform.awt;

import uia.physical.message.MessageStore;
import uia.core.*;
import uia.core.ui.Graphic;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.utility.Timer;
import uia.physical.ComponentRoot;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * {@link Context} implementation based on Java AWT/SWING
 */

public class ContextAWT implements Context {
    private ScheduledExecutorService mainThread;

    private final JFrame jFrame;
    private final Renderer renderer;

    private final View rootView;
    private View currentView;

    private final int[] screen_size = new int[2];

    private boolean focus = true;
    private boolean isRunning = false;

    public ContextAWT(int x, int y) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.setTitle("UIa AWT");
        jFrame.add(renderer = new Renderer());

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
                dispatch(e, Key.ACTION.TYPED);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                dispatch(e, Key.ACTION.PRESSED);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                dispatch(e, Key.ACTION.RELEASED);
            }
        });

        jFrame.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.RELEASED);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.CLICKED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.EXITED);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.DRAGGED);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                dispatch(e, ScreenPointer.ACTION.MOVED);
            }
        });

        jFrame.addMouseWheelListener(e -> dispatch(e, ScreenPointer.ACTION.WHEEL));

        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                screen_size[0] = container.getWidth();
                screen_size[1] = container.getHeight();
            }
        });


        rootView = new ComponentRoot();
    }

    /**
     * Dispatch keys
     */

    private void dispatch(KeyEvent e, Key.ACTION action) {
        if (currentView != null)
            currentView.dispatch(View.DISPATCHER.KEY, new Key(action, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
    }

    private final List<ScreenPointer> screenPointers = new ArrayList<>();

    /**
     * Dispatch pointers
     */

    private void dispatch(MouseEvent e, ScreenPointer.ACTION action) {
        int ox = e.getX() - jFrame.getInsets().left;
        int oy = e.getY() - jFrame.getInsets().top;
        int wheel = (e instanceof MouseWheelEvent) ? ((MouseWheelEvent) e).getWheelRotation() : 0;

        ScreenPointer.BUTTON button = null;
        switch (e.getButton()) {
            case 1:
                button = ScreenPointer.BUTTON.LEFT;
                break;
            case 2:
                button = ScreenPointer.BUTTON.CENTER;
                break;
            case 3:
                button = ScreenPointer.BUTTON.RIGHT;
                break;
        }

        screenPointers.clear();
        screenPointers.add(new ScreenPointer(action, button, ox, oy, wheel));

        if (currentView != null) currentView.dispatch(View.DISPATCHER.POINTERS, screenPointers);
    }

    @Override
    public Object getNative() {
        return jFrame;
    }

    @Override
    public void setView(View view) {
        currentView = view;
    }

    @Override
    public void setHints(HINT... hint) {
        List<HINT> hints = renderer.hints;
        hints.clear();
        hints.addAll(Arrays.asList(hint));
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;

            jFrame.setVisible(true);

            int period = 1000 / 60;

            mainThread = Executors.newSingleThreadScheduledExecutor();
            mainThread.scheduleAtFixedRate(renderer::repaint, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stop() {
        if (isRunning) {
            mainThread.shutdownNow();
            isRunning = false;
        }
    }

    @Override
    public void setWindowParams(boolean alwaysOnTop, boolean resizable, String title) {
        jFrame.setAlwaysOnTop(alwaysOnTop);
        jFrame.setResizable(resizable);
        jFrame.setTitle(title);
    }

    @Override
    public int getFrameRate() {
        return renderer.frameRate;
    }

    @Override
    public int getWidth() {
        return screen_size[0];
    }

    @Override
    public int getHeight() {
        return screen_size[1];
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String clipboard(CLIPBOARD_OPERATION operation, String str) {
        if (CLIPBOARD_OPERATION.COPY.equals(operation)) {
            StringSelection selection = new StringSelection(str);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } else {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = c.getContents(null);
            try {
                return (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * @return the physical screen dimension
     */

    public static int[] getScreenSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{dim.width, dim.height};
    }

    /**
     * Render engine
     */

    private class Renderer extends JPanel {
        private final MessageStore messageStore = MessageStore.getInstance();
        private final Graphic graphic;
        private final Timer timer;
        private final List<HINT> hints;

        private int frameRate;
        private int frameCount;
        private float lastFrameCount;

        public Renderer() {
            graphic = new GraphicAWT();

            timer = new Timer();

            hints = new ArrayList<>();
            hints.add(HINT.ANTIALIASING_ON);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            frameCount++;

            // calculate frame rate
            if (timer.seconds() >= 1d) {
                frameRate = (int) (frameCount - lastFrameCount);
                lastFrameCount = frameCount;
                timer.reset();
            }

            Graphics2D g2d = (Graphics2D) g;

            for (HINT i : hints) {
                switch (i) {
                    case ANTIALIASING_ON:
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        break;
                    case ANTIALIASING_OFF:
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        break;
                    case ANTIALIASING_TEXT_ON:
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        break;
                    case ANTIALIASING_TEXT_OFF:
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                        break;
                    case RENDER_QUALITY_COLOR_HIGH:
                        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                        break;
                    case RENDER_QUALITY_COLOR_LOW:
                        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                        break;
                }
            }

            graphic.setNative(g2d);

            rootView.setPosition(0f, 0f);
            rootView.setDimension(screen_size[0], screen_size[1]);
            rootView.requestFocus(focus);

            if (currentView != null) {
                int counter = 0;
                int limit = 30000 / Math.max(1, frameRate);
                Object[] message;

                while ((message = messageStore.pop()) != null && counter < limit) {
                    currentView.dispatch(View.DISPATCHER.MESSAGE, message);
                    counter++;
                }

                currentView.update(rootView);
                currentView.draw(graphic);
            }
        }
    }
}