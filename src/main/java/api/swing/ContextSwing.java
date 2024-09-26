package api.swing;

import uia.application.message.store.GlobalMessageStore;
import uia.core.basement.message.MessageStore;
import uia.application.input.EmulatedInput;
import uia.core.context.window.Window;
import uia.core.context.InputEmulator;
import uia.core.context.Context;
import uia.core.ui.View;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferStrategy;
import java.awt.datatransfer.*;
import java.util.*;
import java.awt.*;

/**
 * UIa {@link Context} implementation based on Java Swing.
 * <br>
 * <br>
 * <b>Implementation choices</b>
 * <ul>
 *  <li>rendering is scheduled to happen 30 times per second (aka 30 FPS) </li>
 * </ul>
 * <b>Usage example:</b>
 * <br>
 * <code>
 * Context context = ContextSwing.createAndStart(1000, 500);
 * </code>
 */

public class ContextSwing implements Context {
    private static final int FPS_30 = 1_000 / 30;

    private LifecycleStage lifecycleStage = LifecycleStage.PAUSED;
    private ScheduledExecutorService renderingThread;

    private final RenderingEngineSwing renderingEngine;
    private final InputEmulator inputEmulator;
    private final WindowSwing window;

    public ContextSwing(int x, int y) {
        renderingEngine = new RenderingEngineSwing();

        window = new WindowSwing(x, y);

        MessageStore globalMessageStore = GlobalMessageStore.getInstance();
        inputEmulator = new EmulatedInput(globalMessageStore::add);
    }

    @Override
    public void setView(View view) {
        renderingEngine.setView(view);
    }

    /**
     * Helper method. Kills the rendering process.
     */

    private void killRenderingThread() {
        try {
            renderingThread.shutdownNow();
        } catch (Exception ignored) {
            //
        }
    }

    @Override
    public void setLifecycleStage(LifecycleStage lifecycleStage) {
        if (lifecycleStage == null) {
            throw new NullPointerException("'lifecycleStage' can't be null");
        }
        if (lifecycleStage.equals(this.lifecycleStage)) {
            return;
        }

        // updates the lifecycle stage
        this.lifecycleStage = lifecycleStage;
        switch (lifecycleStage) {
            case RUNNING:
                //BufferStrategy bufferStrategy = window.getBufferStrategy();
                final float[] drawableBounds = new float[4];

                // creates the rendering thread
                renderingThread = Executors.newSingleThreadScheduledExecutor();
                renderingThread.scheduleAtFixedRate(() -> {
                            //int[] insets = window.getInsets();
                            drawableBounds[0] = 0;
                            drawableBounds[1] = 0;
                            drawableBounds[2] = window.getViewportWidth();
                            drawableBounds[3] = window.getViewportHeight();

                            /*Graphics graphics = bufferStrategy.getDrawGraphics();
                            renderingEngine.draw(graphics, drawableBounds, window.isFocused());
                            graphics.dispose();
                            bufferStrategy.show();*/

                            window.refresh(graphics -> {
                                renderingEngine.draw(graphics, drawableBounds, window.isFocused());
                            });
                        },
                        0, FPS_30, TimeUnit.MILLISECONDS);
                break;
            case PAUSED:
                killRenderingThread();
                break;
            case TERMINATED:
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
    public void setRenderingHint(RenderingHint... hints) {
        renderingEngine.setHints(Arrays.asList(hints));
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
    public String clipboard(ClipboardOperation operation, String stringToBeCopied) {
        if (ClipboardOperation.COPY.equals(operation)) {
            StringSelection selection = new StringSelection(stringToBeCopied);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } else {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);
            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception ignored) {
                //
            }
        }
        return null;
    }

    /**
     * @return the physical screen dimension
     */

    public static int[] getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{screenSize.width, screenSize.height};
    }

    /**
     * Create a new ContextSwing, make it visible and start it
     *
     * @param width  the window width
     * @param height the window height
     * @return a new {@link ContextSwing}
     */

    public static Context createAndStart(int width, int height) {
        Context context = new ContextSwing(width, height);
        context.getWindow().setVisible(true);
        context.setLifecycleStage(LifecycleStage.RUNNING);
        return context;
    }
}
