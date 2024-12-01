package adaptor.swing;

import uia.application.message.messagingSystem.MessagingSystem;
import uia.application.ui.component.ComponentHiddenRoot;
import adaptor.swing.graphics.GraphicsAWT;
import uia.core.rendering.Graphics;
import uia.core.context.Context;
import uia.utility.Timer;
import uia.core.ui.View;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.awt.*;

/**
 * The RenderingEngineSwing is responsible for rendering a single view
 * on the AWT graphics.
 */

public class RenderingEngineSwing {
    private final List<Context.RenderingHint> renderingHints;
    private final MessagingSystem messagingSystem;
    private final Timer timer;

    private final Graphics graphics;
    private final View rootView;
    private View view;

    private Graphics2D thirdPartyGraphics;

    private int frameRate;
    private int frameCount;
    private float lastFrameCount;

    public RenderingEngineSwing() {
        messagingSystem = new MessagingSystem();

        graphics = new GraphicsAWT(() -> thirdPartyGraphics);

        rootView = new ComponentHiddenRoot();

        timer = new Timer();

        renderingHints = new ArrayList<>();
        renderingHints.add(Context.RenderingHint.ANTIALIASING_ON);
    }

    /**
     * Sets a View to be drawn.
     *
     * @param view the view to be drawn; it could be null
     */

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Sets some rendering hints.
     *
     * @param renderingHints a not null list of {@link Context.RenderingHint}
     * @throws NullPointerException if {@code renderingHints == null}
     */

    public void setHints(List<Context.RenderingHint> renderingHints) {
        Objects.requireNonNull(renderingHints);
        this.renderingHints.clear();
        this.renderingHints.addAll(renderingHints);
    }

    /**
     * Helper method. Calculates the rendering metrics.
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
     * Helper method. Applies the rendering hints.
     */

    private void applyHints() {
        for (Context.RenderingHint renderingHint : renderingHints) {
            switch (renderingHint) {
                case ANTIALIASING_ON:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );
                    break;
                case ANTIALIASING_OFF:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF
                    );
                    break;
                case TEXT_ANTIALIASING_ON:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                    );
                    break;
                case TEXT_ANTIALIASING_OFF:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
                    );
                    break;
                case COLOR_QUALITY_HIGH:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_QUALITY
                    );
                    break;
                case COLOR_QUALITY_LOW:
                    thirdPartyGraphics.setRenderingHint(
                            RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_SPEED
                    );
                    break;
            }
        }
    }

    /**
     * Helper method. Updates the root view.
     *
     * @param bounds    the root view bounds as an array of four elements:
     *                  <ul>
     *                     <li>the absolute position on the x-axis</li>
     *                     <li>the absolute position on the y-axis</li>
     *                     <li>the absolute width</li>
     *                     <li>the absolute height</li>
     *                  </ul>
     * @param isFocused true to set the root view to focus; false otherwise
     */

    private void updateRootView(float[] bounds, boolean isFocused) {
        // removes focus when the root view loses it
        if (view != null && !isFocused && rootView.isOnFocus()) {
            view.requestFocus(false);
        }
        rootView.requestFocus(isFocused);
        rootView.getStyle()
                .setDimension(bounds[2], bounds[3])
                .setPosition(bounds[0], bounds[1]);
    }

    /**
     * Helper method. Updates the View.
     */

    private void updateView() {
        int maxMessagesToProcess = MessagingSystem.MAX_MESSAGES_TO_PROCESS / Math.max(1, frameRate);
        messagingSystem.setMaxMessagesToProcess(maxMessagesToProcess);
        messagingSystem.sendMessagesTo(view);
        view.update(rootView);
    }

    /**
     * Helper method. Draws the View on screen.
     */

    private void drawView() {
        view.draw(graphics);
    }

    /**
     * Draws the View on the given Graphics.
     *
     * @param drawableBounds the boundaries of the screen drawable area as an array of four elements:
     *                       <ul>
     *                          <li>the absolute left-top position on the x-axis</li>
     *                          <li>the absolute left-top position on the y-axis</li>
     *                          <li>the absolute width</li>
     *                          <li>the absolute height</li>
     *                       </ul>
     * @param isFocused      true if the window is focused
     */

    public void draw(java.awt.Graphics graphics,
                     float[] drawableBounds, boolean isFocused) {
        updateRootView(drawableBounds, isFocused);

        thirdPartyGraphics = (Graphics2D) graphics;
        applyHints();
        try {
            calculateMetrics();
            if (view != null) {
                updateView();
                drawView();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
