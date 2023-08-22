package uia.physical;

import uia.core.Font;
import uia.core.Paint;
import uia.core.basement.Graphic;
import uia.physical.scroller.Scroller;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.event.OnMouseHover;
import uia.physical.scroller.WheelScroller;
import uia.physical.text.TextRenderer;
import uia.physical.text.TextRendererV1;
import uia.physical.wrapper.WrapperView;

import static uia.utils.TrigTable.*;

/**
 * Implementation of {@link ViewText}
 * <br>
 * Note that <b>Text properties</b> are calculated at rendering time.
 */

public final class ComponentText extends WrapperView implements ViewText {

    /**
     * Not null array used to scroll text along x-axis and y-axis.
     */
    public Scroller[] scroller;

    public TextRenderer textRenderer;

    private AlignX alignX = AlignX.CENTER;
    private AlignY alignY = AlignY.TOP;

    private Font font;
    private final Paint paintText;

    private String text = "";
    private String description = "";

    private int lines;

    // represents the text bounds as a rectangle
    private final float[] textBounds = {0f, 0f, 0f, 0f, 0f};

    private boolean singleLine = false;

    public ComponentText(View view) {
        super(view);

        addEvent((OnMouseHover) (v, pointers) -> scroller[singleLine ? 0 : 1].update(pointers));

        textRenderer = new TextRendererV1();

        font = new Font("Arial", Font.STYLE.PLAIN, Font.FONT_SIZE_DESKTOP);

        paintText = new Paint().setColor(new Paint.Color(0));

        scroller = new Scroller[]{new WheelScroller(), new WheelScroller()};
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

    @Override
    public void update(View parent) {
        super.update(parent);

        if (isVisible()) {
            float[] bounds = bounds();
            float[] desc = desc();
            float n_lines = isSingleLine() ? 1 : lines;

            // update scroller
            scroller[0].setMax(textBounds[2] - 0.975f * desc[0]);
            scroller[0].setFactor(font.getWidth('a'));

            scroller[1].setMax(textBounds[3] - desc[1]);
            scroller[1].setFactor(textBounds[3] / (n_lines + 1));

            // update text boundaries
            float d_x = -desc[0] / 2f + (TextRenderer.map(alignX) - 1f) * (scroller[0].getValue() - 4f);
            float d_y = -desc[1] / 2f - scroller[1].getValue();

            textBounds[0] = bounds[0] + bounds[2] / 2f + rotX(d_x, d_y, cos(bounds[4]), sin(bounds[4]));
            textBounds[1] = bounds[1] + bounds[3] / 2f + rotY(d_x, d_y, cos(bounds[4]), sin(bounds[4]));
            textBounds[3] = n_lines * font.getLineHeight();
            textBounds[4] = bounds[4];
        }
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            graphic.setFont(font);
            graphic.setPaint(paintText);
            graphic.setClip(getGeom());

            String data = !text.isEmpty() ? text : description;
            if (singleLine) {
                textBounds[2] = textRenderer.drawInline(this, graphic, data,
                        textBounds[0], textBounds[1], textBounds[4]);
            } else {
                textBounds[2] = textRenderer.draw(this, graphic, data,
                        textBounds[0], textBounds[1], textBounds[4]);
            }

            graphic.restoreClip();
        }
    }

    @Override
    public float getTextWidth() {
        return textBounds[2];
    }

    @Override
    public float getTextHeight() {
        return textBounds[3];
    }
}
