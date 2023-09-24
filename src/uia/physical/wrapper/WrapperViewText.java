package uia.physical.wrapper;

import uia.core.Font;
import uia.core.Paint;
import uia.core.ui.ViewText;

/**
 * WrapperViewText enables the creation of complex artifacts, like every day graphical widgets, specifically
 * designed to handle texts.
 */

public abstract class WrapperViewText extends WrapperView implements ViewText {
    private final ViewText viewText;

    public WrapperViewText(ViewText viewText) {
        super(viewText);
        this.viewText = viewText;
    }

    @Override
    public void setAlign(AlignX alignX) {
        viewText.setAlign(alignX);
    }

    @Override
    public AlignX getAlignX() {
        return viewText.getAlignX();
    }

    @Override
    public void setAlign(AlignY alignY) {
        viewText.setAlign(alignY);
    }

    @Override
    public AlignY getAlignY() {
        return viewText.getAlignY();
    }

    @Override
    public Paint getTextPaint() {
        return viewText.getTextPaint();
    }

    @Override
    public void setFont(Font font) {
        viewText.setFont(font);
    }

    @Override
    public Font getFont() {
        return viewText.getFont();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        viewText.setSingleLine(singleLine);
    }

    @Override
    public boolean isSingleLine() {
        return viewText.isSingleLine();
    }

    @Override
    public void setDescription(String description) {
        viewText.setDescription(description);
    }

    @Override
    public void setText(String text) {
        viewText.setText(text);
    }

    @Override
    public String getText() {
        return viewText.getText();
    }

    @Override
    public void scrollText(float x, float y) {
        viewText.scrollText(x, y);
    }

    @Override
    public float getTextWidth() {
        return viewText.getTextWidth();
    }

    @Override
    public float getTextHeight() {
        return viewText.getTextHeight();
    }
}
