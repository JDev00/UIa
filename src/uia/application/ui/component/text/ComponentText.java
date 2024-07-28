package uia.application.ui.component.text;

import uia.application.ui.component.text.renderer.MultilineTextRenderer;
import uia.application.ui.component.text.renderer.InlineTextRenderer;
import uia.application.ui.component.text.renderer.TextRenderer;
import uia.application.ui.component.utility.ComponentUtility;
import uia.core.ui.style.TextHorizontalAlignment;
import uia.application.ui.scroller.WheelScroller;
import uia.application.ui.component.WrapperView;
import uia.application.ui.scroller.Scroller;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.callbacks.OnMouseHover;
import uia.core.rendering.Transform;
import uia.core.rendering.font.Font;
import uia.core.rendering.Graphics;
import uia.core.ui.style.Style;
import uia.core.ui.ViewText;
import uia.core.ui.View;

/**
 * Implementation of {@link ViewText}.
 * <br>
 * Note that <b>text properties</b> are calculated at rendering time.
 */

public final class ComponentText extends WrapperView implements ViewText {
    /**
     * Array of Scroller used to scroll text on x-axis and y-axis.
     */
    public Scroller[] scroller;

    private final TextRenderer[] textRenderer;

    private final Transform clipTransform;

    private String text = "";
    private String description = "";

    private final float[] textBounds = {0f, 0f, 0f, 0f, 0f};
    private int lines = 1;
    private boolean singleLine = false;

    public ComponentText(View view) {
        super(view);

        registerCallback((OnMouseHover) touches -> scroller[singleLine ? 0 : 1].update(touches));

        scroller = new Scroller[]{new WheelScroller(), new WheelScroller()};

        textRenderer = new TextRenderer[]{
                new InlineTextRenderer(),
                new MultilineTextRenderer()
        };

        clipTransform = new Transform();
    }

    @Override
    public void setText(String text) {
        this.text = (text == null) ? "" : text;

        if (this.text.isEmpty()) {
            textBounds[2] = 0f;
            textBounds[3] = 0f;

            scroller[0].reset();
            scroller[1].reset();
        }

        lines = ViewText.countLines(text) + 1;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setDescription(String description) {
        this.description = (description == null) ? "" : description;
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    @Override
    public boolean isSingleLine() {
        return singleLine;
    }

    /**
     * Helper method
     */

    private void updateScrollerValue(Scroller scroller, float value) {
        scroller.setValue(scroller.getValue() + value * scroller.getFactor());
    }

    @Override
    public void scrollText(float x, float y) {
        updateScrollerValue(scroller[0], x);
        updateScrollerValue(scroller[1], y);
    }

    private final float[] scrollValue = new float[2];

    @Override
    public float[] getScrollValue() {
        scrollValue[0] = scroller[0].getValue();
        scrollValue[1] = scroller[1].getValue();
        return scrollValue;
    }

    private final float[] textBoundsCopy = new float[5];

    @Override
    public float[] getTextBounds() {
        System.arraycopy(textBounds, 0, textBoundsCopy, 0, textBoundsCopy.length);
        return textBoundsCopy;
    }

    /**
     * Set the scroller scroll factor and maximum value
     */

    private void calculateAndSetScrollerData(float width, float height) {
        float numberOfLines = singleLine ? 1 : lines;

        scroller[0].setMax(textBounds[2] - 0.975f * width);
        Font font = getFont();
        scroller[0].setFactor(font.getWidth('a'));

        scroller[1].setMax(textBounds[3] - height);
        scroller[1].setFactor(textBounds[3] / (numberOfLines + 1));
    }

    /**
     * Updates text bounds.
     */

    private void updateTextBounds(float[] bounds, float width, float height) {
        TextHorizontalAlignment textAlignment = getStyle().getHorizontalTextAlignment();
        float xDist = -width / 2f + (TextHorizontalAlignment.map(textAlignment) - 1f) * (scroller[0].getValue() - 4f);
        float yDist = -height / 2f - scroller[1].getValue();
        float rot = bounds[4];
        float numberOfLines = singleLine ? 1 : lines;
        Font font = getFont();

        textBounds[0] = ComponentUtility.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot);
        textBounds[1] = ComponentUtility.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot);
        textBounds[3] = numberOfLines * font.getLineHeight();
        textBounds[4] = rot;
    }

    /**
     * Updates clip shape position, dimension and rotation.
     */

    private void updateClipShape(float[] bounds, float width, float height) {
        clipTransform
                .setTranslation(
                        bounds[0] + bounds[2] / 2f,
                        bounds[1] + bounds[3] / 2f
                )
                .setScale(width, height)
                .setRotation(bounds[4]);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        if (isVisible()) {
            float[] bounds = getBounds();
            float width = getWidth();
            float height = getHeight();

            calculateAndSetScrollerData(width, height);
            updateTextBounds(bounds, width, height);
            updateClipShape(bounds, width, height);
        }
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        if (isVisible()) {
            Geometry geometry = getGeometry();
            Style style = getStyle();
            graphics
                    .setTextColor(style.getTextColor())
                    .setFont(style.getFont())
                    .setClip(clipTransform, geometry.vertices(), geometry.toArray());

            String textToDisplay = !text.isEmpty() ? text : description;
            int renderer = singleLine ? 0 : 1;
            textBounds[2] = textRenderer[renderer].draw(this, graphics,
                    textToDisplay,
                    textBounds[0],
                    textBounds[1],
                    textBounds[4]
            );

            graphics.restoreClip();
        }
    }

    /**
     * Helper function.
     *
     * @return the text font used
     */

    private Font getFont() {
        return getStyle().getFont();
    }
}
