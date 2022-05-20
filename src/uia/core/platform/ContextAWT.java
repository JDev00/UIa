package uia.core.platform;

import uia.core.event.Keyboard;
import uia.core.geometry.Oval;
import uia.core.policy.*;
import uia.core.Page;
import uia.core.View;
import uia.core.animator.NodeEv;
import uia.core.policy.Font;
import uia.core.policy.Image;
import uia.core.policy.Paint;
import uia.core.widget.TextView;
import uia.utils.Timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * AWT Context implementation
 */

public class ContextAWT implements Context {

    private ScheduledExecutorService mainThread;

    private final JFrame jFrame;

    private final UIPanel uiPanel;

    private final RenderAWT render;

    private HINT hint = null;

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
     * Input attributes
     */

    private boolean pressing;

    private final Pointer[] pointers = {new PointerAWT()};

    private final Key key = new KeyAWT();

    /*
     * Page attributes
     */

    private final Stack<Page> store = new Stack<>();

    private final Stack<Page> pages = new Stack<>();

    private final Page loadPage;

    private Page curPage;

    public ContextAWT(int x, int y) {
        render = new RenderAWT();


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
                keyDispatch(e, Keyboard.TYPED);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyDispatch(e, Keyboard.PRESSED);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyDispatch(e, Keyboard.RELEASED);
            }
        });

        jFrame.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                touchDispatch(e, PointerAWT.PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                touchDispatch(e, PointerAWT.RELEASED);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                touchDispatch(e, PointerAWT.CLICKED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                touchDispatch(e, PointerAWT.EXITED);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                touchDispatch(e, PointerAWT.DRAGGED);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                touchDispatch(e, PointerAWT.MOVED);
            }
        });

        jFrame.addMouseWheelListener(e -> touchDispatch(e, PointerAWT.WHEEL));

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

    /**
     * Update mouse dispatcher of the current Page
     */

    private void touchDispatch(MouseEvent e, int action) {
        pressing = (action == PointerAWT.PRESSED || action == PointerAWT.DRAGGED);

        pointers[0].setNative(e, action, -jFrame.getInsets().left, -jFrame.getInsets().top);

        Page.updatePointerDispatcher(curPage);
    }

    /**
     * Update key dispatcher of the current Page
     */

    private void keyDispatch(KeyEvent e, int action) {
        key.set(e, action);
        Page.updateKeyDispatcher(curPage);
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
    public void setHint(HINT hint) {
        this.hint = hint;
    }

    @Override
    public HINT getHint() {
        return hint;
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
    public void setResizable(boolean resizable) {
        jFrame.setResizable(resizable);
    }

    @Override
    public void storePage(Page page) {
        if (page != null)
            store.add(page);
    }

    @Override
    public void removeStoredPage(int i) {
        store.remove(i);
    }

    @Override
    public int storedPages() {
        return store.size();
    }

    @Override
    public int indexOfStoredPage(Page page) {
        return store.indexOf(page);
    }

    @Override
    public Page getStoredPage(int i) {
        return store.get(i);
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
        return 1;// because of non touch-screen Context
    }

    @Override
    public Pointer[] pointers() {
        return pointers;
    }

    @Override
    public Key getKeyEnc() {
        return key;
    }

    @Override
    public final boolean isMousePressed() {
        return pressing;
    }

    @Override
    public Paint createColor(Paint p, float r, float g, float b) {
        return createColor(p, (int) r, (int) g, (int) b, 255);
    }

    @Override
    public Paint createColor(Paint p, float r, float g, float b, float a) {
        Paint out = (p == null) ? new PaintAWT() : p;
        out.setNative(new Color((int) r, (int) g, (int) b, (int) a));
        return out;
    }

    @Override
    public Paint createColor(Paint p, COLOR color) {
        switch (color) {
            case BLACK:
                return createColor(p, 0, 0, 0);
            case WHITE:
                return createColor(p, 255, 255, 255);
            case RED:
                return createColor(p, 255, 0, 0);
            case GREEN:
                return createColor(p, 0, 255, 0);
            case BLUE:
                return createColor(p, 0, 0, 255);
            case STD:
                return createColor(p, 30, 30, 30);
            case STD_NO_PAINT:
                return createColor(p, 0, 0, 0, 1);
            default:
                return null;
        }
    }

    @Override
    public Font createFont(Font f, String name, Font.STYLE style, int size) {
        if (name != null && size > 0) {
            Font out = (f == null) ? new FontAWT() : f;

            java.awt.Font fNative = null;

            switch (style) {

                case ITALIC:
                    fNative = new java.awt.Font(name, java.awt.Font.ITALIC, size);
                    break;

                case BOLD:
                    fNative = new java.awt.Font(name, java.awt.Font.BOLD, size);
                    break;

                case PLAIN:
                    fNative = new java.awt.Font(name, java.awt.Font.PLAIN, size);
                    break;
            }

            out.setNative(fNative);

            return out;
        }

        return null;
    }

    @Override
    public Font copyFont(Font f, Font toCopy) {
        if (toCopy == null)
            return null;

        Font out = (f == null) ? new FontAWT() : f;
        out.setNative(toCopy.getNative());
        return out;
    }

    @Override
    public Path createPath() {
        return new PathAWT();
    }

    @Override
    public final boolean copyIntoClipboard(String str) {
        if (str != null) {
            StringSelection selection = new StringSelection(str);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            return true;
        }
        return false;
    }

    @Override
    public final String pasteFromClipboard() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(null);

        if (t != null) {
            try {
                return (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception ignored) {
            }
        }

        return "";
    }

    @Override
    public final Object getNative() {
        return jFrame;
    }

    /*
     *
     * Page renderer
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

            // Compute frame rate
            if (timer.seconds() >= 1d) {
                frameRate = (int) (frameCount - lastFrameCount);
                lastFrameCount = frameCount;
                timer.reset();
            }

            if (hint != null) {
                Graphics2D g2d = (Graphics2D) g;

                switch (hint) {

                    case SMOOTH:
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        break;

                    case SMOOTH_TEXT:
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        break;
                }
            }

            render.setNative(g);

            if (isLoading) {
                loadPage.draw(render);
            } else if (curPage != null) {
                curPage.draw(render);
            }

            frameCount++;
        }
    }

    /*
     *
     * Page used while Context is loading
     *
     */

    private static class LPage extends Page {

        private LPage(Context context) {
            super(context);

            float dx = 0.2f * dx();
            float dy = 0.2f * dy();

            TextView view = new TextView(context, 0, 0, dx, dx);
            view.setFigure(Oval.create());
            view.setPaint(context.createColor(null, 150, 100, 255));
            view.setText("Loading");
            view.setAlignY(TextView.AlignY.CENTER);
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

    /*
     *
     * Utils
     *
     */

    /**
     * @return the screen size
     */

    public static int[] getScreenSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{dim.width, dim.height};
    }

    /**
     * Load an image
     *
     * @param path the image path
     * @return the loaded image otherwise null
     */

    public static Image loadImage(String path) {
        if (path != null) {
            try {
                Image out = new ImageAWT();
                out.setNative(ImageIO.read(new File(path)));
                return out;
            } catch (IOException ignored) {
            }
        }

        return null;
    }
}
