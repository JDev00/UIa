package uia.application.ui.component.text;

import uia.application.ui.component.WrapperView;
import uia.core.ui.ViewText;

/**
 * WrapperViewText wraps a ViewText implementation.
 */

public abstract class WrapperViewText extends WrapperView implements ViewText {
    private final ViewText viewText;

    protected WrapperViewText(ViewText viewText) {
        super(viewText);
        this.viewText = viewText;
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
    public void setPlaceholder(String placeholder) {
        viewText.setPlaceholder(placeholder);
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
    public float[] getScrollValue() {
        return viewText.getScrollValue();
    }

    @Override
    public float[] getTextBounds() {
        return viewText.getTextBounds();
    }
}
