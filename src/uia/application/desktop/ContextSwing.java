package uia.application.desktop;

import uia.core.basement.Message;
import uia.core.ui.context.InputEmulator;
import uia.core.ui.context.Window;
import uia.physical.input.ArtificialInput;
import uia.physical.message.Messages;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Framework built-in {@link Context} implementation based on Java Swing.
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

public class ContextSwing implements Context {
    private ScheduledExecutorService renderingThread;
    private LifecycleStage lifecycleStage = LifecycleStage.STOP;
    private final WindowSwing window;
    private final RendererEngine rendererEngine;
    private final InputEmulator inputEmulator;

    public ContextSwing(int x, int y) {
        rendererEngine = new RendererEngine();

        window = new WindowSwing(x, y);
        window.addUIComponent(rendererEngine);

        inputEmulator = new ArtificialInput(
                (screenTouch) -> {
                    int[] insets = window.getInsets();
                    screenTouch.translate(insets[0], insets[1]);
                    dispatchSingleScreenTouch(screenTouch);
                },
                this::dispatchKey
        );
    }

    private void dispatchScreenTouches(List<ScreenTouch> screenTouches) {
        View view = rendererEngine.view;
        if (view != null && lifecycleStage.equals(LifecycleStage.RUN)) {
            view.dispatchMessage(Messages.newScreenEventMessage(screenTouches, null));
        }
    }

    private void dispatchSingleScreenTouch(ScreenTouch screenTouch) {
        dispatchScreenTouches(Collections.singletonList(screenTouch));
    }

    private void dispatchKey(Key key) {
        View view = rendererEngine.view;
        if (view != null && lifecycleStage.equals(LifecycleStage.RUN)) {
            view.dispatchMessage(Messages.newKeyEventMessage(key, null));
        }
    }

    @Override
    public void setView(View view) {
        rendererEngine.view = view;
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
                renderingThread.scheduleAtFixedRate(() -> {
                            Message eventMessage = window.popEventMessage();

                            if (eventMessage != null) {
                                System.out.println(eventMessage);
                            }

                            rendererEngine.draw(window.getWidth(), window.getHeight(), window.isFocused());
                        },
                        0,
                        repaintPeriodMillis,
                        TimeUnit.MILLISECONDS);
                break;
            case STOP:
                killRenderingThread();
                break;
            case TERMINATE:
                killRenderingThread();
                window.destroy();
                break;
        }
    }

    @Override
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }

    @Override
    public void setRenderingHint(RenderingHint... hint) {
        List<RenderingHint> hints = rendererEngine.hints;
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
     * RendererEngine is responsible to render on the native Graphic a specified View.
     */

    private static class RendererEngine extends JPanel {
        private static final int MAX_MESSAGES_PER_SECOND = 30000;

        private final MessageStore messageStore = MessageStore.getInstance();
        private final Timer timer;
        private Graphics2D nativeGraphics;
        private final Graphic graphic;
        private final List<RenderingHint> hints;
        private final View rootView;
        private View view;

        private int frameRate;
        private int frameCount;
        private float lastFrameCount;

        public RendererEngine() {
            graphic = new GraphicAWT(() -> nativeGraphics);

            rootView = new ComponentHiddenRoot();

            timer = new Timer();

            hints = new ArrayList<>();
            hints.add(RenderingHint.ANTIALIASING_ON);
        }

        /**
         * Helper function. Calculate rendering metrics.
         */

        private void calculateMetrics() {
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

        /**
         * Helper function. Update root View.
         */

        private void updateRootView(int x, int y, boolean focus) {
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

                Message message;
                while ((message = messageStore.pop()) != null && counter < limit) {
                    view.dispatchMessage(message);
                    counter++;
                }

                view.update(rootView);
            }
        }

        /**
         * Draw the specified View on the native Graphics.
         */

        private void draw(int screenWidth, int screenHeight, boolean screenFocus) {
            calculateMetrics();
            updateRootView(screenWidth, screenHeight, screenFocus);
            updateView();
            repaint();
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
