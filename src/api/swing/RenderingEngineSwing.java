package api.swing;

import uia.physical.message.messagingSystem.MessagingSystem;
import uia.physical.ui.component.ComponentHiddenRoot;
import api.swing.graphics.GraphicsAWT;
import uia.core.rendering.Graphics;
import uia.core.context.Context;
import uia.utility.Timer;
import uia.core.ui.View;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.awt.*;

import javax.swing.*;

/**
 * The RenderingEngineSwing is responsible for rendering a single view
 * on the AWT graphics.
 */

public class RenderingEngineSwing extends JPanel {
    private final MessagingSystem messagingSystem;
    private final Timer timer;
    private Graphics2D graphics2D;
    private final Graphics graphics;
    private final List<Context.RenderingHint> hints;
    private final View rootView;
    private View view;

    private int frameRate;
    private int frameCount;
    private float lastFrameCount;

    public RenderingEngineSwing() {
        messagingSystem = new MessagingSystem();

        graphics = new GraphicsAWT(() -> graphics2D);

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

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Set some {@link Context.RenderingHint}s
     *
     * @param renderingHints a not null List of RenderingHints
     * @throws NullPointerException if {@code renderingHints == null}
     */

    public void setHints(List<Context.RenderingHint> renderingHints) {
        Objects.requireNonNull(renderingHints);
        hints.clear();
        hints.addAll(renderingHints);
    }

    /**
     * Helper function. Updates the root View.
     */

    private void updateRootView(int x, int y, boolean focus) {
        // remove focus when rootView loses it
        if (view != null && !focus && rootView.isOnFocus()) {
            view.requestFocus(false);
        }
        rootView.setPosition(0f, 0f);
        rootView.setDimension(x, y);
        rootView.requestFocus(focus);
    }

    /**
     * Draws the specified View on the AWT Graphics.
     *
     * @param screenWidth  the window width
     * @param screenHeight the window height
     * @param screenFocus  true if window is on focus
     */

    public void draw(int screenWidth, int screenHeight, boolean screenFocus) {
        updateRootView(screenWidth, screenHeight, screenFocus);
        repaint();
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
                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    break;
                case ANTIALIASING_OFF:
                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    break;
                case TEXT_ANTIALIASING_ON:
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    break;
                case TEXT_ANTIALIASING_OFF:
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    break;
                case COLOR_QUALITY_HIGH:
                    graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    break;
                case COLOR_QUALITY_LOW:
                    graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                    break;
            }
        }
    }

    /**
     * Helper function. Updates the View.
     */

    private void updateView() {
        if (view != null) {
            int maxMessagesToProcess = MessagingSystem.MAX_MESSAGES_TO_PROCESS / Math.max(1, frameRate);
            messagingSystem.setMaxMessagesToProcess(maxMessagesToProcess);
            messagingSystem.sendMessagesTo(view);
            view.update(rootView);
        }
    }

    /**
     * Helper function. Draws the View on screen.
     */

    private void drawView() {
        if (view != null) {
            view.draw(this.graphics);
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics graphics) {
        super.paintComponent(graphics);

        graphics2D = (Graphics2D) graphics;
        applyHints();
        try {
            calculateMetrics();
            updateView();
            drawView();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
