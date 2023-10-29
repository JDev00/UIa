package uia.application.awt;

import uia.core.ui.context.InputEmulator;
import uia.core.ui.context.Window;
import uia.physical.input.ArtificialInput;
import uia.physical.message.MessageStore;
import uia.core.*;
import uia.core.ui.Graphic;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.utility.Timer;
import uia.physical.ComponentHiddenRoot;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Framework built-in {@link Context} implementation based on Java AWT and SWING.
 * <br><br>
 * <b>Usage example:</b>
 * <br><br>
 * <code>
 * <p>
 * Context context = new ContextAWT(1000, 500);
 * <br>
 * context.getWindow().show();
 * <br>
 * context.setLifecycleStage(LifecycleStage.RUN);
 * </code>
 */

public class ContextAWT implements Context {
    private ScheduledExecutorService renderingThread;
    private LifecycleStage lifecycleStage = LifecycleStage.STOP;
    private final WindowAWT window;
    private final Renderer renderer;
    private final InputEmulator inputEmulator;

    public ContextAWT(int x, int y) {
        renderer = new Renderer();

        window = new WindowAWT(x, y, this::dispatchScreenPointers, this::dispatchKey);
        window.registerNativeComponent(renderer);

        inputEmulator = new ArtificialInput(
                screenPointer -> {
                    int[] insets = window.getInsets();
                    screenPointer.translate(insets[0], insets[1]);
                    dispatchSingleScreenPointer(screenPointer);
                },
                this::dispatchKey
        );
    }

    private void dispatchScreenPointers(List<ScreenPointer> screenPointers) {
        View view = renderer.view;
        if (view != null && lifecycleStage.equals(LifecycleStage.RUN)) {
            view.dispatch(View.Dispatcher.SCREEN_POINTER, screenPointers);
        }
    }

    private void dispatchSingleScreenPointer(ScreenPointer screenPointer) {
        dispatchScreenPointers(Collections.singletonList(screenPointer));
    }

    private void dispatchKey(Key key) {
        View view = renderer.view;
        if (view != null && lifecycleStage.equals(LifecycleStage.RUN)) {
            view.dispatch(View.Dispatcher.KEY, key);
        }
    }

    @Override
    public void setView(View view) {
        renderer.view = view;
    }

    /**
     * Helper function. Kill the rendering process.
     */

    private void killRenderingThread() {
        try {
            renderingThread.shutdownNow();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setLifecycleStage(LifecycleStage lifecycleStage) {
        if (this.lifecycleStage.equals(lifecycleStage)) {
            return;
        }
        this.lifecycleStage = Objects.requireNonNull(lifecycleStage);

        switch (lifecycleStage) {
            case RUN:
                int repaintPeriodMillis = 1000 / 60;
                renderingThread = Executors.newSingleThreadScheduledExecutor();
                renderingThread.scheduleAtFixedRate(() -> renderer.updateAndDrawView(
                                window.getWidth(),
                                window.getHeight(),
                                window.isFocused()
                        ),
                        0,
                        repaintPeriodMillis,
                        TimeUnit.MILLISECONDS);
                return;

            case STOP:
                killRenderingThread();
                return;

            case TERMINATE:
                killRenderingThread();
                window.destroy();
        }
    }

    @Override
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }

    @Override
    public void setRenderingHint(RenderingHint... hint) {
        List<RenderingHint> hints = renderer.hints;
        hints.clear();
        hints.addAll(Arrays.asList(hint));
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public InputEmulator getInputEmulator() {
        return inputEmulator;
    }

    @Override
    public String clipboard(ClipboardOperation operation, String str) {
        if (ClipboardOperation.COPY.equals(operation)) {
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
     * {@link Window} implementation
     */

    private static class WindowAWT implements Window {
        private final JFrame jFrame;
        private final int[] screenSize = new int[2];
        private boolean focus = false;

        public WindowAWT(int x, int y,
                         Consumer<List<ScreenPointer>> screenPointersListener,
                         Consumer<Key> keyListener) {
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
                    keyListener.accept(new Key(Key.ACTION.TYPED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    keyListener.accept(new Key(Key.ACTION.PRESSED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    keyListener.accept(new Key(Key.ACTION.RELEASED, e.getModifiers(), e.getKeyChar(), e.getKeyCode()));
                }
            });
            jFrame.addMouseListener(new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.PRESSED));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.RELEASED));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.CLICKED));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.EXITED));
                }
            });
            jFrame.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.DRAGGED));
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    screenPointersListener.accept(getScreenPointers(e, 0, ScreenPointer.ACTION.MOVED));
                }
            });
            jFrame.addMouseWheelListener(e -> screenPointersListener.accept(
                    getScreenPointers(e, e.getWheelRotation(), ScreenPointer.ACTION.WHEEL))
            );
        }

        private void registerNativeComponent(Component component) {
            jFrame.add(component);
        }

        /**
         * Destroy this Window
         */

        private void destroy() {
            jFrame.dispose();
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        }

        /**
         * @return the corresponding {@link uia.core.ScreenPointer.BUTTON} or null
         */

        private static ScreenPointer.BUTTON mapNativeMouseButton(int button) {
            switch (button) {
                case 1:
                    return ScreenPointer.BUTTON.LEFT;
                case 2:
                    return ScreenPointer.BUTTON.CENTER;
                case 3:
                    return ScreenPointer.BUTTON.RIGHT;
                default:
                    return null;
            }
        }

        private final List<ScreenPointer> screenPointers = new ArrayList<>();

        /**
         * Helper function. Returns a List of ScreenPointers.
         */

        private List<ScreenPointer> getScreenPointers(MouseEvent mouseEvent, int wheelRotation, ScreenPointer.ACTION action) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            int[] insets = getInsets();
            int[] position = {x - insets[0], y - insets[1]};

            screenPointers.clear();
            screenPointers.add(new ScreenPointer(
                    action,
                    mapNativeMouseButton(mouseEvent.getButton()),
                    position[0],
                    position[1],
                    wheelRotation));
            return screenPointers;
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

        @Override
        public Window resize(int width, int height) {
            assert width >= 200 : "window width can't be lower than 200 px";
            assert height >= 200 : "window height can't be lower than 200 px";
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

    /**
     * Renderer engine
     */

    private static class Renderer extends JPanel {
        private static final int MAX_MESSAGES_PER_SECOND = 30000;

        private final MessageStore messageStore = MessageStore.getInstance();
        private final Timer timer;
        private final Graphic graphic;
        private Graphics2D nativeGraphics;
        private final View rootView;
        private View view;

        private final List<RenderingHint> hints;

        private int frameRate;
        private int frameCount;
        private float lastFrameCount;

        public Renderer() {
            graphic = new GraphicAWT(() -> nativeGraphics);

            rootView = new ComponentHiddenRoot();

            timer = new Timer();

            hints = new ArrayList<>();
            hints.add(RenderingHint.ANTIALIASING_ON);
        }

        /**
         * Helper function. Calculate frame rate.
         */

        private void calculateFrameRate() {
            frameCount++;

            if (timer.seconds() >= 1d) {
                frameRate = (int) (frameCount - lastFrameCount);
                lastFrameCount = frameCount;
                timer.reset();
            }
        }

        /**
         * Helper function. Apply the rendering hints.
         */

        private void applyHints() {
            for (RenderingHint i : hints) {
                switch (i) {
                    case ANTIALIASING_ON:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        break;
                    case ANTIALIASING_OFF:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        break;
                    case TEXT_ANTIALIASING_ON:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        break;
                    case TEXT_ANTIALIASING_OFF:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                        break;
                    case COLOR_QUALITY_HIGH:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                        break;
                    case COLOR_QUALITY_LOW:
                        nativeGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                        break;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            nativeGraphics = (Graphics2D) graphics;
            applyHints();
            if (view != null) {
                view.draw(graphic);
            }
        }

        /**
         * Helper function. Update root View.
         */

        private void updateRoot(int x, int y, boolean focus) {
            rootView.setPosition(0f, 0f);
            rootView.setDimension(x, y);
            rootView.requestFocus(focus);
        }

        /**
         * Helper function. Update the handled View.
         */

        private void updateView() {
            if (view != null) {
                int counter = 0;
                int limit = MAX_MESSAGES_PER_SECOND / Math.max(1, frameRate);
                Object[] message;

                while ((message = messageStore.pop()) != null && counter < limit) {
                    view.dispatch(View.Dispatcher.MESSAGE, message);
                    counter++;
                }

                view.update(rootView);
            }
        }

        private void updateAndDrawView(int screenWidth, int screenHeight, boolean screenFocus) {
            calculateFrameRate();
            updateRoot(screenWidth, screenHeight, screenFocus);
            updateView();
            repaint();
        }
    }
}
