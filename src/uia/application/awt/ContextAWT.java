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
 * Framework built-in {@link Context} implementation based on Java AWT and SWING.
 * <br><br>
 * <b>Usage example:</b>
 * <br><br>
 * <code>
 * <p>
 * Context context = new ContextAWT(1000, 500);
 * context.getWindow().show();
 * context.start();
 * </code>
 */

public class ContextAWT implements Context {
    private ScheduledExecutorService renderingThread;
    private LifecycleStage lifecycleStage = LifecycleStage.STOP;
    private final WindowAWT window;
    private final InputEmulator inputEmulator;

    public ContextAWT(int x, int y) {
        window = new WindowAWT(x, y);

        inputEmulator = new ArtificialInput(
                screenPointer -> {
                    int[] insets = window.getInsets();
                    window.dispatchMousePointer(
                            screenPointer.getX() + insets[0],
                            screenPointer.getY() + insets[1],
                            screenPointer.getWheelRotation(),
                            screenPointer.getButton(),
                            screenPointer.getAction());
                },
                key -> window.dispatchKey(
                        key.getKeyChar(),
                        key.getKeyCode(),
                        key.getModifiers(),
                        key.getAction())
        );
    }

    @Override
    public void setView(View view) {
        window.view = view;
    }

    /**
     * Helper function. Kill the rendering process.
     */

    private void killRenderingThread() {
        try {
            renderingThread.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
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
                renderingThread.scheduleAtFixedRate(window::updateAndDrawComponents,
                        0,
                        repaintPeriodMillis,
                        TimeUnit.MILLISECONDS);
                return;

            case STOP:
                killRenderingThread();
                return;

            default:
                killRenderingThread();
                System.exit(1);
        }
    }

    @Override
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }

    @Override
    public void setRenderingHint(RenderingHint... hint) {
        List<RenderingHint> hints = window.hints;
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
        private final Renderer renderer;
        private View view;

        private final List<ScreenPointer> screenPointers = new ArrayList<>();
        private final List<RenderingHint> hints;

        private final int[] screenSize = new int[2];
        private int frameRate;
        private boolean focus = true;

        public WindowAWT(int x, int y) {
            screenSize[0] = x;
            screenSize[1] = y;

            renderer = new Renderer();

            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
            jFrame.setTitle("UIa SWING");
            jFrame.add(renderer);
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
                    dispatchKey(e.getKeyChar(), e.getKeyCode(), e.getModifiers(), Key.ACTION.TYPED);
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    dispatchKey(e.getKeyChar(), e.getKeyCode(), e.getModifiers(), Key.ACTION.PRESSED);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    dispatchKey(e.getKeyChar(), e.getKeyCode(), e.getModifiers(), Key.ACTION.RELEASED);
                }
            });
            jFrame.addMouseListener(new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.PRESSED);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.RELEASED);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.CLICKED);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.EXITED);
                }
            });
            jFrame.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.DRAGGED);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    dispatchMousePointer(e, 0, ScreenPointer.ACTION.MOVED);
                }
            });
            jFrame.addMouseWheelListener(e -> dispatchMousePointer(e, e.getWheelRotation(), ScreenPointer.ACTION.WHEEL));


            hints = new ArrayList<>();
            hints.add(RenderingHint.ANTIALIASING_ON);
        }

        /**
         * Helper function. Dispatch mouse attributes to the handled View.
         */

        protected void dispatchMousePointer(int x, int y, int wheelRotation,
                                            ScreenPointer.BUTTON button, ScreenPointer.ACTION action) {
            int[] insets = getInsets();
            int[] position = {x - insets[0], y - insets[1]};

            screenPointers.clear();
            screenPointers.add(new ScreenPointer(action, button, position[0], position[1], wheelRotation));

            if (view != null) {
                view.dispatch(View.DISPATCHER.POINTERS, screenPointers);
            }
        }

        /**
         * @return the corresponding {@link uia.core.ScreenPointer.BUTTON} or null
         */

        private static ScreenPointer.BUTTON convertNativeMouseButton(int button) {
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

        /**
         * Helper function. Dispatch mouse attributes to the handled View.
         */

        private void dispatchMousePointer(MouseEvent mouseEvent, int wheelRotation, ScreenPointer.ACTION action) {
            dispatchMousePointer(
                    mouseEvent.getX(),
                    mouseEvent.getY(),
                    wheelRotation,
                    convertNativeMouseButton(mouseEvent.getButton()),
                    action
            );
        }

        /**
         * Helper function. Dispatch a key to the handled View.
         */

        protected void dispatchKey(char keyChar, int keyCode, int modifiers, Key.ACTION action) {
            if (view != null) {
                view.dispatch(View.DISPATCHER.KEY, new Key(action, modifiers, keyChar, keyCode));
            }
        }

        private void updateAndDrawComponents() {
            renderer.update();
            renderer.repaint();
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
        public int[] getInsets() {
            Insets insets = jFrame.getInsets();
            return new int[]{
                    insets.left, insets.top,
                    insets.right, insets.bottom
            };
        }

        /**
         * Renderer engine
         */

        private class Renderer extends JPanel {
            private static final int MAX_MESSAGES_PER_SECOND = 30000;

            private final MessageStore messageStore = MessageStore.getInstance();
            private final Timer timer;
            private final Graphic graphic;
            private Graphics2D nativeGraphics;
            private final View rootView;

            private int frameCount;
            private float lastFrameCount;

            public Renderer() {
                graphic = new GraphicAWT(() -> nativeGraphics);

                rootView = new ComponentRoot();

                timer = new Timer();
            }

            /**
             * Helper function. Update root component.
             */

            private void updateRoot() {
                rootView.setPosition(0f, 0f);
                rootView.setDimension(screenSize[0], screenSize[1]);
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
                        view.dispatch(View.DISPATCHER.MESSAGE, message);
                        counter++;
                    }

                    view.update(rootView);
                }
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
             * Update the Renderer state
             */

            protected void update() {
                calculateFrameRate();
                updateRoot();
                updateView();
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
        }
    }
}
