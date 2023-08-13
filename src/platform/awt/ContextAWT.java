package platform.awt;

import uia.core.*;
import uia.core.Font;
import uia.core.Paint;
import uia.physical.ui.Component;
import uia.core.architecture.ui.View;
import uia.core.architecture.*;
import uia.physical.ui.wrapper.WrapperView;
import uia.utils.Timer;
import uia.physical.ui.RootView;

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
 * AWT {@link Context} implementation
 */

public abstract class ContextAWT implements Context {
    private ScheduledExecutorService mainThread;

    private final JFrame jFrame;
    private final CanvasAWT uiPanel;
    private final GraphicAWT graphic;

    public View loadingView;
    private View currentView;
    private final View rootView;

    private final List<HINT> hints = new ArrayList<>();

    private final int iWidth;
    private final int iHeight;
    private int width;
    private int height;

    private boolean focus = true;
    private boolean isLoading = true;
    private boolean isRunning = false;

    public ContextAWT(int x, int y) {
        graphic = new GraphicAWT();

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.add(uiPanel = new CanvasAWT());

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
                dispatch(e, Pointer.ACTION.PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dispatch(e, Pointer.ACTION.RELEASED);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispatch(e, Pointer.ACTION.CLICKED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dispatch(e, Pointer.ACTION.EXITED);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                dispatch(e, Pointer.ACTION.DRAGGED);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                dispatch(e, Pointer.ACTION.MOVED);
            }
        });

        jFrame.addMouseWheelListener(e -> dispatch(e, Pointer.ACTION.WHEEL));

        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                width = container.getWidth();
                height = container.getHeight();
            }
        });


        // store window dimension
        Container container = jFrame.getContentPane();
        iWidth = width = container.getWidth();
        iHeight = height = container.getHeight();


        rootView = new RootView();


        loadingView = new WrapperView(new Component("ViewLoad", 0.5f, 0.5f, 0.25f, 0.25f)) {
            private final char[] str = "Loading".toCharArray();
            private final Paint paint = new Paint().setColor(255, 0, 0);
            private final Font font = new Font("Arial", Font.STYLE.ITALIC, 2 * Font.FONT_SIZE_DESKTOP);

            @Override
            public void draw(Graphic graphic) {
                super.draw(graphic);

                graphic.setFont(font);
                graphic.setPaint(paint);
                graphic.drawText(str, 0, str.length,
                        width() / 2f - font.getWidth(str, 0, str.length) / 2f,
                        height() / 2f - font.getSize() / 2f, 0f);
            }
        };

        // start async setup
        new Thread(() -> {
            setup();
            isLoading = false;
        }).start();
    }

    /**
     * Build context in an async way
     */

    public abstract void setup();

    /**
     * Dispatch keys
     */

    private void dispatch(KeyEvent e, Key.ACTION action) {
        if (currentView != null)
            currentView.dispatch(new Key(action, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
    }

    private final List<Pointer> pointers = new ArrayList<>();

    /**
     * Dispatch pointers
     */

    private void dispatch(MouseEvent e, Pointer.ACTION action) {
        int ox = e.getX() - jFrame.getInsets().left;
        int oy = e.getY() - jFrame.getInsets().top;
        int wheel = (e instanceof MouseWheelEvent) ? ((MouseWheelEvent) e).getWheelRotation() : 0;

        Pointer.BUTTON button = null;
        switch (e.getButton()) {
            case 1:
                button = Pointer.BUTTON.LEFT;
                break;
            case 2:
                button = Pointer.BUTTON.CENTER;
                break;
            case 3:
                button = Pointer.BUTTON.RIGHT;
                break;
        }

        pointers.clear();
        pointers.add(new Pointer(action, button, ox, oy, wheel));

        if (currentView != null)
            currentView.dispatch(pointers);
    }

    @Override
    public Object getNative() {
        return jFrame;
    }

    @Override
    public void setHints(HINT... hint) {
        hints.clear();
        hints.addAll(Arrays.asList(hint));
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;

            // set the context visibility
            jFrame.setVisible(true);

            int period = 1000 / 60;

            mainThread = null;
            mainThread = Executors.newSingleThreadScheduledExecutor();
            mainThread.scheduleAtFixedRate(uiPanel::repaint, 0, period, TimeUnit.MILLISECONDS);
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
    public void setView(View view) {
        currentView = view;
    }

    @Override
    public int getFrameRate() {
        return uiPanel.frameRate;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int initialWidth() {
        return iWidth;
    }

    @Override
    public int initialHeight() {
        return iHeight;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String clipboard(String str, boolean toCopy) {
        if (toCopy) {
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

    private class CanvasAWT extends JPanel {
        private final Timer timer;

        private int frameRate;
        private int frameCount;
        private float lastFrameCount;

        public CanvasAWT() {
            timer = new Timer();
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
            rootView.setDimension(width, height);
            rootView.setFocus(focus);
            rootView.draw(graphic);

            if (isLoading) {
                loadingView.update(rootView);
                loadingView.draw(graphic);
            } else if (currentView != null) {
                currentView.update(rootView);
                currentView.draw(graphic);
            }
        }
    }
}
