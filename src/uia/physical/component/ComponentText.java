package uia.physical.component;

import uia.core.Font;
import uia.core.paint.Paint;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.physical.scroller.Scroller;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnMouseHover;
import uia.physical.scroller.WheelScroller;
import uia.physical.component.text.MultilineTextRenderer;
import uia.physical.component.text.TextRenderer;
import uia.physical.component.text.InlineTextRenderer;
import uia.physical.theme.Theme;

/**
 * Implementation of {@link ViewText}
 * <br>
 * Note that <b>text properties</b> are calculated at rendering time.
 */

public final class ComponentText extends WrapperView implements ViewText {
    /**
     * Array of Scroller used to scroll text on x-axis and y-axis.
     */
    public Scroller[] scroller;

    private final TextRenderer[] textRenderer;

    private AlignX alignX = AlignX.CENTER;
    private AlignY alignY = AlignY.TOP;

    private Font font;
    private final Paint paintText;
    private final Shape clipShape;

    private String text = "";
    private String description = "";

    private final float[] textBounds = {0f, 0f, 0f, 0f, 0f};
    private int lines;
    private boolean singleLine = false;

    public ComponentText(View view) {
        super(view);

        registerCallback((OnMouseHover) touches -> scroller[singleLine ? 0 : 1].update(touches));

        scroller = new Scroller[]{new WheelScroller(), new WheelScroller()};

        textRenderer = new TextRenderer[]{new InlineTextRenderer(), new MultilineTextRenderer()};

        font = new Font("Arial", Font.STYLE.PLAIN, Font.FONT_SIZE_DESKTOP);

        paintText = new Paint().setColor(Theme.BLACK);

        clipShape = new Shape();
        clipShape.setGeometry(getGeometry());
    }

    @Override
    public Paint getTextPaint() {
        return paintText;
    }

    @Override
    public void setFont(Font font) {
        if (font != null) this.font = font;
    }

    @Override
    public Font getFont() {
        return font;
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
    public void setAlign(AlignX alignX) {
        if (alignX != null) this.alignX = alignX;
    }

    @Override
    public AlignX getAlignX() {
        return alignX;
    }

    @Override
    public void setAlign(AlignY alignY) {
        if (alignY != null) this.alignY = alignY;
    }

    @Override
    public AlignY getAlignY() {
        return alignY;
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
        float n_lines = singleLine ? 1 : lines;

        scroller[0].setMax(textBounds[2] - 0.975f * width);
        scroller[0].setFactor(font.getWidth('a'));

        scroller[1].setMax(textBounds[3] - height);
        scroller[1].setFactor(textBounds[3] / (n_lines + 1));
    }

    /**
     * Update text bounds
     */

    private void updateTextBounds(float[] bounds, float width, float height) {
        float xDist = -width / 2f + (TextRenderer.map(alignX) - 1f) * (scroller[0].getValue() - 4f);
        float yDist = -height / 2f - scroller[1].getValue();
        float rot = bounds[4];
        float n_lines = singleLine ? 1 : lines;

        textBounds[0] = View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot);
        textBounds[1] = View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot);
        textBounds[3] = n_lines * font.getLineHeight();
        textBounds[4] = rot;
    }

    /**
     * Update clip shape position, dimension and rotation
     */

    private void updateClipShape(float[] bounds, float width, float height) {
        clipShape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        clipShape.setDimension(width, height);
        clipShape.setRotation(bounds[4]);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        if (isVisible()) {
            float[] bounds = getBounds();
            float w = getWidth();
            float h = getHeight();

            calculateAndSetScrollerData(w, h);
            updateTextBounds(bounds, w, h);
            updateClipShape(bounds, w, h);
        }
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        if (isVisible()) {
            graphics.setFont(font);
            graphics.setPaint(paintText);
            graphics.setClip(clipShape);

            int index = singleLine ? 0 : 1;
            textBounds[2] = textRenderer[index].draw(this, graphics, !text.isEmpty() ? text : description,
                    textBounds[0], textBounds[1], textBounds[4]);

            graphics.restoreClip();
        }
    }
}
