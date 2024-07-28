package api.swing;

import uia.application.message.store.GlobalMessageStore;
import uia.application.message.EventTouchScreenMessage;
import uia.application.message.MessageFactory;
import uia.core.basement.message.MessageStore;
import uia.application.input.EmulatedInput;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.context.window.Window;
import uia.core.context.InputEmulator;
import uia.core.context.Context;
import uia.core.ui.View;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.datatransfer.*;
import java.util.*;
import java.awt.*;

/**
 * UIa {@link Context} implementation based on Java Swing.
 * <br>
 * <br>
 * <b>Usage example:</b>
 * <br>
 * <br>
 * <code>
 * <p>
 * Context context = createAndStart(1000, 500);
 * </code>
 */

public class ContextSwing implements Context {
    private LifecycleStage lifecycleStage = LifecycleStage.PAUSED;
    private ScheduledExecutorService renderingThread;

    private final RenderingEngineSwing renderingEngine;
    private final InputEmulator inputEmulator;
    private final WindowSwing window;

    public ContextSwing(int x, int y) {
        renderingEngine = new RenderingEngineSwing();

        window = new WindowSwing(x, y);
        window.addUIComponent(renderingEngine);

        MessageStore globalMessageStore = GlobalMessageStore.getInstance();
        inputEmulator = new EmulatedInput(generatedInput -> {
            int[] insets = window.getInsets();
            if (generatedInput instanceof EventTouchScreenMessage) {
                ScreenTouch[] screenTouch = generatedInput.getPayload();
                // copies and translates the screenTouch
                ScreenTouch copiedScreenTouch = ScreenTouch.copy(screenTouch[0], insets[0], insets[1]);
                // recreates the message
                generatedInput = MessageFactory.create(copiedScreenTouch, generatedInput.getRecipient());
            }
            // adds the message to the message store
            globalMessageStore.add(generatedInput);
        });
    }

    @Override
    public void setView(View view) {
        renderingEngine.setView(view);
    }

    /**
     * Helper function. Kills the rendering process.
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
        if (this.lifecycleStage.equals(lifecycleStage)) {
            return;
        }
        this.lifecycleStage = Objects.requireNonNull(lifecycleStage);

        switch (lifecycleStage) {
            case RUNNING:
                int repaintPeriodMillis = 1000 / 60;
                renderingThread = Executors.newSingleThreadScheduledExecutor();
                renderingThread.scheduleAtFixedRate(() -> renderingEngine.draw(
                                window.getViewportWidth(),
                                window.getViewportHeight(),
                                window.isFocused()
                        ),
                        0,
                        repaintPeriodMillis,
                        TimeUnit.MILLISECONDS);
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
                //
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
