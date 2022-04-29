package uia.core;

import uia.core.animator.NodeEv;
import uia.core.utility.KeyEnc;
import uia.core.utility.Pointer;
import uia.utils.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Standard Context implementation
 */

// @Test new Page handler engine
// @See LPage and fix it
public class StdContext implements Context {

    /*
     * Attributes
     */

    private ScheduledExecutorService mainThread;

    private final JFrame jFrame;

    private final UIPanel uiPanel;

    private boolean isLoading = true;
    private boolean isRunning = false;

    private int frameCount = 0;

    /*
     * Screen attributes
     */

    private final int xStartScreen;
    private final int yStartScreen;
    private int xScreen;
    private int yScreen;
    private float xScreenFactor = 1f;
    private float yScreenFactor = 1f;

    /*
     * Mouse attributes
     */

    private final int[] xPointer = {0};
    private final int[] yPointer = {0};
    private boolean pressing;

    /*
     * Page attributes
     */

    private final Stack<Page> pages = new Stack<>();

    private final Page loadPage;

    private Page curPage;

    public StdContext(int x, int y) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.add(uiPanel = new UIPanel());

        jFrame.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                focusDispatcher(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                focusDispatcher(false);
            }
        });

        jFrame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                keyDispatch(e, KeyEvent.KEY_TYPED);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyDispatch(e, KeyEvent.KEY_PRESSED);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyDispatch(e, KeyEvent.KEY_RELEASED);
            }
        });

        jFrame.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_RELEASED);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_CLICKED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_EXITED);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_DRAGGED);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                touchDispatch(e, MouseEvent.MOUSE_MOVED);
            }
        });

        // @Test
        jFrame.addMouseWheelListener(e -> touchDispatch(e, MouseEvent.MOUSE_WHEEL));

        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                xScreen = container.getWidth();
                yScreen = container.getHeight();
                xScreenFactor = (float) xScreen / xStartScreen;
                yScreenFactor = (float) yScreen / yStartScreen;
            }
        });


        // Save window dimension
        Container container = jFrame.getContentPane();
        xStartScreen = xScreen = container.getWidth();
        yStartScreen = yScreen = container.getHeight();


        loadPage = new LPage(this);


        // Create an async setup
        Context.runTask(() -> {
            asyncSetup(xScreen, yScreen);
            isLoading = false;
        });
    }

    /*
     *
     * Dispatchers
     *
     */

    /**
     * Update focus dispatcher of the current Page
     */

    private void focusDispatcher(boolean gained) {
        Page.updateFocusDispatcher(curPage, gained);
    }

    private final Pointer pointer = new Pointer();

    /**
     * Update mouse dispatcher of the current Page
     */

    private void touchDispatch(MouseEvent e, int action) {
        pressing = (action == MouseEvent.MOUSE_PRESSED || action == MouseEvent.MOUSE_DRAGGED);

        xPointer[0] = e.getX() - jFrame.getInsets().left;
        yPointer[0] = e.getY() - jFrame.getInsets().top;

        pointer.set(e, action, -jFrame.getInsets().left, -jFrame.getInsets().top);

        Page.updateTouchDispatcher(curPage, pointer);
    }

    private final KeyEnc keyEnc = new KeyEnc();

    /**
     * Update key dispatcher of the current Page
     */

    private void keyDispatch(KeyEvent e, int action) {
        keyEnc.set(e, action);
        Page.updateKeyDispatcher(curPage, keyEnc);
    }

    /*
     *
     * Implementations
     *
     */

    @Override
    public void asyncSetup(int w, int h) {
    }

    @Override
    public final boolean start() {
        if (!isRunning) {
            isRunning = true;

            // Set this context visible
            jFrame.setVisible(true);

            int period = 1000 / 60;

            mainThread = null;
            mainThread = Executors.newSingleThreadScheduledExecutor();
            mainThread.scheduleAtFixedRate(uiPanel::repaint, 0, period, TimeUnit.MILLISECONDS);

            return true;
        }

        return false;
    }

    @Override
    public final boolean stop() {
        if (isRunning) {
            mainThread.shutdownNow();
            isRunning = false;
            return true;
        }

        return false;
    }

    @Override
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        jFrame.setAlwaysOnTop(alwaysOnTop);
    }

    @Override
    public final void clear() {
        // Close the page on top of the stack
        if (pages.size() > 0)
            Page.close(pages.peek());

        pages.clear();
    }

    @Override
    public final void open(final Page page) {
        if (page != null) {
            pages.remove(page);
            pages.add(page);

            // Close the previous page
            // Because of its induction nature, all stacked pages except the one on top are closed
            if (pages.size() > 1)
                Page.close(pages.get(pages.size() - 2));
            //pages.get(pages.size() - 2).close();

            //(curPage = page).open();

            curPage = page;
            Page.open(curPage);
        }
    }

    @Override
    public final Page close() {
        if (pages.size() > 0) {
            Page page = pages.pop();
            Page.close(page);
            //page.close();

            if (pages.size() > 0) {
                //(curPage = pages.peek()).open();
                curPage = pages.peek();
                Page.open(curPage);
            } else {
                curPage = null;
            }

            return page;
        }

        return null;
    }

    @Override
    public final int size() {
        return pages.size();
    }

    @Override
    public final int indexOf(Page page) {
        return pages.indexOf(page);
    }

    @Override
    public final Page get(int i) {
        return (i >= 0 && i < pages.size()) ? pages.get(i) : null;
    }

    @Override
    public final Page getLoadPage() {
        return loadPage;
    }

    @Override
    public final int getFrameRate() {
        return uiPanel.frameRate;
    }

    @Override
    public final int initialDx() {
        return xStartScreen;
    }

    @Override
    public final int initialDy() {
        return yStartScreen;
    }

    @Override
    public final int dx() {
        return xScreen;
    }

    @Override
    public final int dy() {
        return yScreen;
    }

    @Override
    public final float dxFactor() {
        return xScreenFactor;
    }

    @Override
    public final float dyFactor() {
        return yScreenFactor;
    }

    @Override
    public final boolean isLoading() {
        return isLoading;
    }

    @Override
    public final boolean isRunning() {
        return isRunning;
    }

    @Override
    public int pointerSize() {
        return 1;// 1 because of non touch-screen context.
    }

    @Override
    public final int[] xPointer() {
        return xPointer;
    }

    @Override
    public final int[] yPointer() {
        return yPointer;
    }

    @Override
    public final boolean isMousePressed() {
        return pressing;
    }

    @Override
    public final FontMetrics getFontMetrics(Font font) {
        return jFrame.getFontMetrics(font);
    }

    @Override
    public final Object getNative() {
        return jFrame;
    }

    /*
     *
     * Behind the scene page rendering
     *
     */

    private final class UIPanel extends JPanel {
        private final Timer timer;

        private int frameRate;

        private float lastFrameCount;

        public UIPanel() {
            frameRate = 0;

            lastFrameCount = 0;

            timer = new Timer();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Calculates frame rate
            if (timer.seconds() >= 1d) {
                frameRate = (int) (frameCount - lastFrameCount);
                lastFrameCount = frameCount;
                timer.reset();
            }

            Graphics2D g2 = (Graphics2D) g;

            if (isLoading) {
                loadPage.draw(g2);
            } else if (curPage != null) {
                curPage.draw(g2);
            }

            frameCount++;
        }
    }

    /*
     *
     * Page used while Context loading
     *
     */

    private static class LPage extends Page {

        private LPage(Context context) {
            super(context);

            float dx = 0.2f * dx();
            float dy = 0.2f * dy();

            View view = new View(context, 0, 0, dx, dy);
            view.setColor(new Color(150, 120, 255));
            //view.setText("Loading");
            //view.getTextSuite().setAlignY(TextSuite.AlignY.CENTER);
            view.setupAnimator(View.ANIMATOR_TYPE.DIM, a -> {
                a.addEvent((NodeEv) (v, s) -> {
                    if (s == NodeEv.LAST)
                        a.restart();
                });
                a.addNode(0, 0, 1);
                a.addNode(0.8f * dx, 0.8f * dy, 1);
                a.addNode(0.3f * dx, 0.3f * dy, 0.8f);
                a.addNode(0, 0, 1);
            });
            view.setupAnimator(View.ANIMATOR_TYPE.ROT, a -> {
                a.addEvent((NodeEv) (v, s) -> {
                    if (s == NodeEv.LAST)
                        a.restart();
                });
                a.addNode(0, 0, 1.5f);
                a.addNode(180, 180, 2f);
                a.addNode(0, 0, 1.5f);
            });

            add(view);
        }
    }
}
