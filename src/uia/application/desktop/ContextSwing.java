package uia.application.desktop;

import uia.core.ScreenTouch;
import uia.core.basement.Message;
import uia.core.ui.context.InputEmulator;
import uia.core.ui.context.Window;
import uia.physical.input.ArtificialInput;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.physical.message.MessageStore;

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
    private final RendererEngineSwing rendererEngine;
    private final InputEmulator inputEmulator;

    public ContextSwing(int x, int y) {
        rendererEngine = new RendererEngineSwing();

        window = new WindowSwing(x, y);
        window.addUIComponent(rendererEngine);

        MessageStore messageStore = MessageStore.getInstance();
        inputEmulator = new ArtificialInput((message) -> {
            int[] insets = window.getInsets();
            if (message.getType().equals(Message.Type.EVENT_SCREEN_TOUCH)) {
                ScreenTouch screenTouch = message.<List<ScreenTouch>>getMessage().get(0);
                screenTouch.translate(insets[0], insets[1]);
            }
            messageStore.add(message);
        });
    }

    @Override
    public void setView(View view) {
        rendererEngine.setView(view);
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
                renderingThread.scheduleAtFixedRate(() -> rendererEngine.draw(
                                window.getWidth(),
                                window.getHeight(),
                                window.isFocused()
                        ),
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
    public void setRenderingHint(RenderingHint... hints) {
        rendererEngine.setHints(Arrays.asList(hints));
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
}
