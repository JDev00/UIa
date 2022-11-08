package uia.core.platform.implementation;

import uia.core.animator.policy.Animator;
import uia.core.animator.NodeEvent;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.policy.*;
import uia.core.Page;
import uia.core.View;
import uia.utils.Timer;
import uia.utils.TrigTable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * AWT Context implementation
 */

public class ContextAWT implements Context {

    private ScheduledExecutorService mainThread;

    private final JFrame jFrame;

    private final Canvas uiPanel;

    private final GraphicAWT render;

    private final List<HINT> hints = new ArrayList<>();

    private boolean isLoading = true;
    private boolean isRunning = false;

    private boolean focus = true;

    private int frameCount = 0;

    /* Screen */

    private final int iWidth;
    private final int iHeight;
    private int width;
    private int height;
    private float screenRatioX = 1f;
    private float screenRatioY = 1f;

    /* Input */

    private boolean pressing;

    private final List<Pointer> pointers = Collections.singletonList(new PointerAWT());

    private final Key key = new KeyAWT();

    /* Page */

    private final Stack<Page> store = new Stack<>();

    private final Stack<Page> pages = new Stack<>();

    private final Page loadPage;

    private Page curPage;

    public ContextAWT(int x, int y) {
        render = new GraphicAWT();

        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
        jFrame.add(uiPanel = new Canvas());

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
                updateKeys(e, Key.TYPED);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                updateKeys(e, Key.PRESSED);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateKeys(e, Key.RELEASED);
            }
        });

        jFrame.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                updatePointers(e, Pointer.PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                updatePointers(e, Pointer.RELEASED);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                updatePointers(e, Pointer.CLICKED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                updatePointers(e, Pointer.EXITED);
            }
        });

        jFrame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                updatePointers(e, Pointer.DRAGGED);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updatePointers(e, Pointer.MOVED);
            }
        });

        jFrame.addMouseWheelListener(e -> updatePointers(e, Pointer.WHEEL));

        jFrame.pack();
        jFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Container container = jFrame.getContentPane();
                width = container.getWidth();
                height = container.getHeight();
                screenRatioX = (float) width / iWidth;
                screenRatioY = (float) height / iHeight;
            }
        });

        // store window dimension
        Container container = jFrame.getContentPane();
        iWidth = width = container.getWidth();
        iHeight = height = container.getHeight();

        loadPage = new LPage(this);

        // start an async setup
        Context.runTask(() -> {
            asyncSetup(width, height);
            isLoading = false;
        });
    }

    /**
     * Update page's keys
     */

    private void updateKeys(KeyEvent e, int action) {
        key.setNative(e, action);
        if (curPage != null) curPage.updateKeys();
    }

    /**
     * Update page's pointers
     */

    private void updatePointers(MouseEvent e, int action) {
        pressing = (action == PointerAWT.PRESSED || action == PointerAWT.DRAGGED);

        pointers.get(0).setNative(e, action, -jFrame.getInsets().left, -jFrame.getInsets().top);

        if (curPage != null) curPage.updatePointers();
    }

    @Override
    public void clearHint() {
        hints.clear();
    }

    @Override
    public void addHint(HINT hint) {
        if (hint != null) hints.add(hint);
    }

    @Override
    public void removeHint(HINT hint) {
        try {
            hints.remove(hint);
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public List<HINT> getHints() {
        return hints;
    }

    @Override
    public void asyncSetup(int w, int h) {
    }

    @Override
    public boolean start() {
        if (!isRunning) {
            isRunning = true;

            // set the context visibility
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
    public boolean stop() {
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
    public void setTitle(String title) {
        jFrame.setTitle(title);
    }

    @Override
    public void storePage(Page page) {
        if (page != null) store.add(page);
    }

    @Override
    public void removeStoredPage(int i) {
        try {
            store.remove(i);
        } catch (IndexOutOfBoundsException ignored) {
        }
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
    public void clear() {
        // close the page on top of the stack
        if (pages.size() > 0) pages.peek().close();
        pages.clear();
    }

    @Override
    public void open(final Page page) {
        if (page != null) {
            pages.remove(page);
            pages.add(page);

            // close the previous page.
            // Because of its induction nature, all stacked pages except the one on top are closed
            if (pages.size() > 1) pages.get(pages.size() - 2).close();

            curPage = page;
            curPage.open();
        }
    }

    @Override
    public Page close() {
        if (pages.size() > 0) {
            Page page = pages.pop();
            page.close();

            if (pages.size() > 0) {
                curPage = pages.peek();
                curPage.open();
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
    public int indexOf(Page page) {
        return pages.indexOf(page);
    }

    @Override
    public Page get(int i) {
        try {
            return pages.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Page getLoadingPage() {
        return loadPage;
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
    public float screenRatioX() {
        return screenRatioX;
    }

    @Override
    public float screenRatioY() {
        return screenRatioY;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int pointerSize() {
        return 1;// because of non multi-pointer context
    }

    @Override
    public List<Pointer> pointers() {
        return pointers;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public boolean isMousePressed() {
        return pressing;
    }

    @Override
    public boolean isOnFocus() {
        return focus;
    }

    @Override
    public boolean copyIntoClipboard(String str) {
        try {
            StringSelection selection = new StringSelection(str);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            return true;
        } catch (HeadlessException | AWTError | IllegalStateException e) {
            return false;
        }
    }

    @Override
    public String pasteFromClipboard() {
        try {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = c.getContents(null);
            return (String) t.getTransferData(DataFlavor.stringFlavor);
        } catch (IOException | UnsupportedFlavorException | IllegalStateException e) {
            return "";
        }
    }

    @Override
    public List<String> fontList() {
        return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
    }

    @Override
    public Object getNative() {
        return jFrame;
    }

    /**
     * @return the physical screen dimension
     */

    public static int[] getScreenSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{dim.width, dim.height};
    }

    /**
     * Page renderer
     */

    private class Canvas extends JPanel {
        private final Timer timer;

        private int frameRate;
        private int frameCounter;
        private float lastFrameCount;

        public Canvas() {
            frameRate = 0;
            frameCounter = 0;

            lastFrameCount = 0;

            timer = new Timer();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            frameCounter++;

            // calculate frame rate
            if (timer.seconds() >= 1d) {
                frameRate = (int) (frameCount - lastFrameCount);
                lastFrameCount = frameCount;
                timer.reset();
            }

            Graphics2D g2d = (Graphics2D) g;

            for (HINT i : hints) {
                switch (i) {
                    case ANTIALIASING:
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        break;
                    case ANTIALIASING_TEXT:
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        break;
                    case RENDER_QUALITY_HIGH:
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                        break;
                    case RENDER_QUALITY_LOW:
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                        break;
                    case RENDER_QUALITY_COLOR_HIGH:
                        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                        break;
                    case RENDER_QUALITY_COLOR_LOW:
                        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
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

    /**
     * Page used while Context is loading
     */

    private static class LPage extends Page {

        private LPage(Context context) {
            super(context);

            float w = 0.2f * width();
            float h = 0.2f * height();

            // event executed when the last animation's node is reached
            NodeEvent<Animator> onLastNode = (a, s) -> {
                if (s == NodeEvent.LAST_NODE) {
                    a.getPath().reverse();
                    a.restart();
                }
            };

            View view = new View(context, 0, 0, w, w);
            view.setShape(s -> Figure.oval(s, Figure.STD_VERT), false);
            view.getPaint().setColor(50, 100, 255);
            view.getAnimatorDim()
                    .addNode(0, 0, 1)
                    .addNode(0.8f * w, 0.8f * h, 1)
                    .addNode(0.3f * w, 0.3f * h, 1.2f)
                    .addNode(0, 0, 1.8f)
                    .getEventQueue().addEvent(onLastNode);
            view.getAnimatorRot()
                    .addNode(0, 0, 1f)
                    .addNode(2 * TrigTable.PI / 3f, 0, 2f)
                    .addNode(0, 0, 2.5f)
                    .getEventQueue().addEvent(onLastNode);

            add(view);
        }
    }
}
