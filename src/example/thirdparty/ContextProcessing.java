package example.thirdparty;

import processing.core.PApplet;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.context.InputEmulator;
import uia.core.ui.context.Window;

import java.util.Objects;

/**
 * {@link Context} implementation based on Processing API.
 * <br>
 * Created for demonstrative purpose.
 */

public class ContextProcessing implements Context {
    private final Window window;
    private final RendererEngineProcessing rendererEngineProcessing;
    private LifecycleStage lifecycleStage = LifecycleStage.STOP;

    public ContextProcessing(int w, int h) {
        window = new WindowProcessing();

        RendererEngineProcessing.initScreenDimension[0] = w;
        RendererEngineProcessing.initScreenDimension[1] = h;

        rendererEngineProcessing = new RendererEngineProcessing();
    }

    @Override
    public void setView(View view) {
        RendererEngineProcessing.currentView = view;
    }

    @Override
    public void setLifecycleStage(LifecycleStage lifecycleStage) {
        Objects.requireNonNull(lifecycleStage);

        if (this.lifecycleStage.equals(lifecycleStage)) {
            return;
        }

        this.lifecycleStage = lifecycleStage;
        if (lifecycleStage == LifecycleStage.RUN) {
            PApplet.runSketch(new String[]{"RendererEngine"}, rendererEngineProcessing);
        } else {
            rendererEngineProcessing.stop();
        }
    }

    @Override
    public LifecycleStage getLifecycleStage() {
        return lifecycleStage;
    }

    @Override
    public void setRenderingHint(RenderingHint... hint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public InputEmulator getInputEmulator() {
        return null;
    }

    @Override
    public String clipboard(ClipboardOperation operation, String str) {
        return null;
    }

    /**
     * @return the processing PApplet object
     */

    public PApplet getPApplet() {
        return rendererEngineProcessing;
    }

    /**
     * Creates a new ContextProcessing and start it
     *
     * @param width  the window width
     * @param height the window height
     * @return a new ContextProcessing
     */

    public static Context createAndStart(int width, int height) {
        Context context = new ContextProcessing(width, height);
        context.getWindow().setVisible(true);
        context.setLifecycleStage(LifecycleStage.RUN);
        return context;
    }
}
