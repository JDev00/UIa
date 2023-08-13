package uia.physical.ui;

import uia.core.Font;
import uia.core.Paint;
import uia.core.architecture.Graphic;
import uia.core.architecture.Scroller;
import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewText;
import uia.core.architecture.ui.event.EventHover;
import uia.physical.WheelScroller;
import uia.physical.ui.text.TextRenderer;
import uia.physical.ui.text.TextRendererV1;
import uia.physical.ui.wrapper.WrapperView;

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
    private Paint paintText;

    private String text = "";
    private String description;

    private int lines;

    // represents the text bounds as a rectangle
    private final float[] textBounds = {0f, 0f, 0f, 0f, 0f};

    private boolean singleLine = false;

    public ComponentText(View view) {
        super(view);

        addEvent((EventHover) (v, pointers) -> scroller[singleLine ? 0 : 1].update(pointers));

        textRenderer = new TextRendererV1();

        font = new Font("Arial", Font.STYLE.PLAIN, Font.FONT_SIZE_DESKTOP);

        paintText = new Paint().setColor(0);

        scroller = new Scroller[]{new WheelScroller(), new WheelScroller()};
    }

    /**
     * Helper method. Count the number of break lines inside the given String.
     * If the given String is null, then return 0.
     *
     * @return the number of break lines inside the given array
     */

    private int countLines(String string) {
        int lines = 0;
        if (string != null) {
            char[] chars = string.toCharArray();
            for (char i : chars) {
                if (i == '\n') lines++;
            }
        }
        return lines;
    }

    @Override
    public void setTextPaint(Paint paint) {
        paintText = paint;
    }

    @Override
    public Paint getTextPaint() {
        return paintText;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setText(String text) {
        this.text = (text == null) ? "" : text;

        if (text == null) {
            textBounds[2] = 0f;
            textBounds[3] = 0f;

            scroller[0].reset();
            scroller[1].reset();
        }

        lines = countLines(text) + 1;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setAlign(AlignX alignX) {
        if (alignX != null) this.alignX = alignX;
    }

    @Override
    public void setAlign(AlignY alignY) {
        if (alignY != null) this.alignY = alignY;
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
            float[] bounds_s = boundsShape();
            float n_lines = isSingleLine() ? 1 : lines;

            // update scroller
            scroller[0].setMax(textBounds[2] - 0.975f * bounds_s[2]);
            scroller[0].setFactor(font.getWidth('a'));

            scroller[1].setMax(textBounds[3] - bounds_s[3]);
            scroller[1].setFactor(textBounds[3] / (n_lines + 1));

            // TODO: 23/07/2023 implement text pivot rotation 
            // update text boundaries
            textBounds[0] = bounds[0] + bounds[2] / 2f + (TextRenderer.map(alignX) - 1f) * scroller[0].getValue();
            textBounds[1] = bounds[1] - (alignY.equals(AlignY.TOP) ? 0f : (textBounds[3] - bounds[3])) / 2f - scroller[1].getValue();
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

            String data = text.length() > 0 ? text : description;
            if (singleLine) {
                textBounds[2] = textRenderer.drawInline(this, graphic, font, alignX, data,
                        textBounds[0], textBounds[1], textBounds[4]);
            } else {
                textBounds[2] = textRenderer.draw(this, graphic, font, alignX, data,
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
