package uia.application.awt;

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
 * Framework built-in {@link Context} implementation based on Java AWT and SWING
 */

public class ContextAWT implements Context {
    private ScheduledExecutorService mainThread;
    private final WindowAWT window;
    private final ArtificialInput artificialInput;
    private boolean isRunning = false;

    public ContextAWT(int x, int y) {
        window = new WindowAWT(x, y);

        artificialInput = new ArtificialInput() {
            @Override
            public ArtificialInput click(int x, int y) {
                window.dispatchMousePointer(x, y, 0, null, ScreenPointer.ACTION.CLICKED);
                return this;
            }

            @Override
            public ArtificialInput moveOnScreen(int xStart, int yStart, int xEnd, int yEnd, float duration) {
                throw new UnsupportedOperationException("Not implemented yet");
            }

            @Override
            public ArtificialInput dragOnScreen(int xStart, int yStart, int xEnd, int yEnd, float duration) {
                throw new UnsupportedOperationException("Not implemented yet");
            }

            @Override
            public ArtificialInput sendKey(char key) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(key);
                window.dispatchKey(key, keyCode, 0, Key.ACTION.RELEASED);
                return this;
            }

            @Override
            public ArtificialInput sendKeyCode(int keyCode) {
                // TODO: check if '(char) keyCode' is correct
                window.dispatchKey((char) keyCode, keyCode, 0, Key.ACTION.RELEASED);
                return this;
            }
        };
    }

    @Override
    public void setView(View view) {
        window.view = view;
    }

    @Override
    public void setHints(HINT... hint) {
        List<HINT> hints = window.hints;
        hints.clear();
        hints.addAll(Arrays.asList(hint));
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;

            window.show();

            int period = 1000 / 60;

            mainThread = Executors.newSingleThreadScheduledExecutor();
            mainThread.scheduleAtFixedRate(window::draw, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void stop() {
        if (isRunning) {
            mainThread.shutdownNow();
            isRunning = false;
        }
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public ArtificialInput getArtificialInput() {
        return artificialInput;
    }

    @Override
    public int getFrameRate() {
        return window.frameRate;
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
     * Window
     */

    private static class WindowAWT implements Window {
        private final JFrame jFrame;
        private final Renderer renderer;
        private View view;

        private final List<ScreenPointer> screenPointers = new ArrayList<>();
        private final List<HINT> hints;

        private final int[] screenSize = new int[2];
        private int frameRate;
        private boolean focus = true;

        public WindowAWT(int x, int y) {
            renderer = new Renderer();


            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.getContentPane().setPreferredSize(new Dimension(x, y));
            jFrame.setTitle("UIa AWT");
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
            hints.add(HINT.ANTIALIASING_ON);
        }

        /**
         * Helper function. Dispatch mouse attributes to the handled View.
         */

        protected void dispatchMousePointer(int x, int y, int wheelRotation,
                                            ScreenPointer.BUTTON button, ScreenPointer.ACTION action) {
            int[] position = {x - jFrame.getInsets().left, y - jFrame.getInsets().top};

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

        private void show() {
            jFrame.setVisible(true);
        }

        private void draw() {
            renderer.repaint();
        }

        @Override
        public Object getNative() {
            return jFrame;
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

        /**
         * Renderer engine
         */

        private class Renderer extends JPanel {
            private static final int MAX_MESSAGES_PER_SECOND = 30000;

            private final MessageStore messageStore = MessageStore.getInstance();
            private final Timer timer;
            private final Graphic graphic;
            private final View rootView;

            private int frameCount;
            private float lastFrameCount;

            public Renderer() {
                graphic = new GraphicAWT();

                rootView = new ComponentRoot();

                timer = new Timer();
            }

            /**
             * Helper function
             */

            private void updateFrameRateAndFrameCount() {
                frameCount++;

                if (timer.seconds() >= 1d) {
                    frameRate = (int) (frameCount - lastFrameCount);
                    lastFrameCount = frameCount;
                    timer.reset();
                }
            }

            /**
             * Helper function
             */

            private void applyHints(Graphics2D g2d) {
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
            }

            /**
             * Helper function
             */

            private void updateAndDrawView() {
                if (view != null) {
                    int counter = 0;
                    int limit = MAX_MESSAGES_PER_SECOND / Math.max(1, frameRate);
                    Object[] message;

                    while ((message = messageStore.pop()) != null && counter < limit) {
                        view.dispatch(View.DISPATCHER.MESSAGE, message);
                        counter++;
                    }

                    view.update(rootView);
                    view.draw(graphic);
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                updateFrameRateAndFrameCount();

                Graphics2D g2d = (Graphics2D) g;

                applyHints(g2d);

                graphic.setNative(g2d);

                rootView.setPosition(0f, 0f);
                rootView.setDimension(screenSize[0], screenSize[1]);
                rootView.requestFocus(focus);

                updateAndDrawView();
            }
        }
    }
}
