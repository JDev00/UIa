package uia.application.desktop;

import uia.core.basement.Message;
import uia.core.ui.Graphic;
import uia.core.ui.View;
import uia.core.ui.context.Context;
import uia.physical.ComponentHiddenRoot;
import uia.physical.message.MessageStore;
import uia.utility.Timer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RendererEngine is responsible to render on the native Graphic a specified View.
 */

public class RendererEngineSwing extends JPanel {
    private static final int MAX_MESSAGES_PER_SECOND = 40000;

    private final MessageStore messageStore = MessageStore.getInstance();
    private final Timer timer;
    private Graphics2D nativeGraphics;
    private final Graphic graphic;
    private final List<Context.RenderingHint> hints;
    private final View rootView;
    private View view;

    private int frameRate;
    private int frameCount;
    private float lastFrameCount;

    public RendererEngineSwing() {
        graphic = new GraphicAWT(() -> nativeGraphics);

        rootView = new ComponentHiddenRoot();

        timer = new Timer();

        hints = new ArrayList<>();
        hints.add(Context.RenderingHint.ANTIALIASING_ON);
    }

    /**
     * Set a View to draw
     *
     * @param view a {@link View}; it could be null
     */

    protected void setView(View view) {
        this.view = view;
    }

    /**
     * @return the handled View
     */

    protected View getView() {
        return view;
    }

    /**
     * Set some {@link Context.RenderingHint}s
     *
     * @param renderingHints a not null List of RenderingHints
     * @throws NullPointerException if {@code renderingHints == null}
     */

    protected void setHints(List<Context.RenderingHint> renderingHints) {
        Objects.requireNonNull(renderingHints);
        hints.clear();
        hints.addAll(renderingHints);
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
        for (Context.RenderingHint i : hints) {
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
            while (counter < limit && (message = messageStore.pop()) != null) {
                view.dispatchMessage(message);
                counter++;
            }

            view.update(rootView);
        }
    }

    /**
     * Draw the specified View on the native Graphics.
     *
     * @param screenWidth  the window width
     * @param screenHeight the window height
     * @param screenFocus  true if window is on focus
     */

    protected void draw(int screenWidth, int screenHeight, boolean screenFocus) {
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