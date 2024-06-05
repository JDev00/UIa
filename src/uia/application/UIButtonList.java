package uia.application;

import uia.core.ui.style.TextHorizontalAlignment;
import uia.core.ui.style.TextVerticalAlignment;
import uia.physical.component.ComponentText;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.component.Component;
import uia.core.ui.style.StyleFunction;
import uia.core.ui.callbacks.OnClick;
import uia.core.basement.Drawable;
import uia.core.basement.Callback;
import uia.physical.theme.Theme;
import uia.core.ui.style.Style;
import uia.utility.MathUtility;
import uia.utility.Geometries;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;

/**
 * Standard UIa component.
 * <br>
 * UIButtonList is a horizontally scrollable list of values.
 */

public final class UIButtonList extends WrapperView {

    /**
     * Graphical elements that make up the UIButtonList component.
     */

    public enum Element {
        LEFT_ARROW, RIGHT_ARROW, TEXT;

        static int mapToIndex(Element element) {
            switch (element) {
                case LEFT_ARROW:
                    return 1;
                case RIGHT_ARROW:
                    return 2;
                case TEXT:
                    return 0;
                default:
                    return -1;
            }
        }
    }

    private final ViewText viewText;
    private final StyleFunction[] styleFunctions = {null, null, null};
    private final List<String> valuesToDisplay = new ArrayList<>();

    private int currentValueIndex;

    public UIButtonList(View view) {
        super(new ComponentGroup(view));
        getStyle()
                .setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true)
                .setTextAlignment(TextHorizontalAlignment.CENTER)
                .setTextAlignment(TextVerticalAlignment.CENTER);

        viewText = new ComponentText(
                new Component("BUTTON_LIST_TEXT_" + getID(), 0.5f, 0.5f, 0.7f, 1f)
        );
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.getStyle()
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setBackgroundColor(Theme.TRANSPARENT);

        View leftArrow = new Component("BUTTON_LIST_LEFT_" + getID(), 0.1f, 0.5f, 0.1f, 0.5f)
                .setExpanseLimit(1.2f, 1.2f);
        leftArrow.registerCallback((OnClick) touches -> showValue(currentValueIndex - 1));
        leftArrow.setConsumer(Consumer.SCREEN_TOUCH, false);
        leftArrow.setColliderPolicy(ColliderPolicy.AABB);
        leftArrow.setRotation(MathUtility.PI);
        leftArrow.getStyle()
                .setGeometry(Geometries::arrow, false)
                .setBackgroundColor(Theme.BLACK);

        View rightArrow = new Component("BUTTON_LIST_RIGHT_" + getID(), 0.9f, 0.5f, 0.1f, 0.5f)
                .setExpanseLimit(1.2f, 1.2f);
        rightArrow.registerCallback((OnClick) touches -> showValue(currentValueIndex + 1));
        rightArrow.setConsumer(Consumer.SCREEN_TOUCH, false);
        rightArrow.setColliderPolicy(ColliderPolicy.AABB);
        rightArrow.getStyle()
                .setGeometry(Geometries::arrow, false)
                .setBackgroundColor(Theme.BLACK);

        ViewGroup.insert(getView(), viewText, leftArrow, rightArrow);
    }

    /**
     * Callback invoked when the next value is requested.
     */

    public interface OnNextValue extends Callback<String> {
    }

    /**
     * Callback invoked when the previous value is requested.
     */

    public interface OnPreviousValue extends Callback<String> {
    }

    /**
     * Sets a style function for the given element.
     *
     * @param element        the element on which the style function is to be set
     * @param dynamicBinding true to allow this component to respond to style function changes
     * @param styleFunction  the style function to be used
     * @throws NullPointerException if {@code element == null || styleFunction == null}
     * @since 1.6.0
     */

    public void setStyleFunction(Element element, boolean dynamicBinding, StyleFunction styleFunction) {
        Objects.requireNonNull(styleFunction);
        Objects.requireNonNull(element);

        // registers the given style function
        int elementIndex = Element.mapToIndex(element);
        styleFunctions[elementIndex] = styleFunction;

        // updates style
        updateElementStyle(getView(), elementIndex);
        if (!dynamicBinding) {
            styleFunctions[elementIndex] = null;
        }
    }

    /**
     * Updates the element style.
     *
     * @since 1.6.0
     */

    private void updateElementStyle(ViewGroup container, int elementIndex) {
        StyleFunction styleFunction = styleFunctions[elementIndex];
        if (styleFunction != null) {
            Style elementStyle = container.get(elementIndex).getStyle();
            styleFunction.apply(elementStyle);
        }
    }

    /**
     * Fills this button with the values to display and displays the first item by default.
     *
     * @param values the values to be displayed
     */

    public void setValues(String... values) {
        valuesToDisplay.clear();
        valuesToDisplay.addAll(Arrays.asList(values));

        currentValueIndex = 0;
        showValue(currentValueIndex);
    }

    /**
     * Displays the specified String.
     *
     * @param index the index of the String to display
     */

    public void showValue(int index) {
        if (!valuesToDisplay.isEmpty()) {

            if (index >= valuesToDisplay.size()) {
                index = 0;
            } else if (index < 0) {
                index = valuesToDisplay.size() - 1;
            }

            String element = valuesToDisplay.get(index);
            if (index < this.currentValueIndex) {
                notifyCallbacks(OnPreviousValue.class, element);
            }
            if (index > this.currentValueIndex) {
                notifyCallbacks(OnNextValue.class, element);
            }
            this.currentValueIndex = index;
            viewText.setText(element);
        }
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        // updates the text style
        Style containerStyle = getStyle();
        Style viewTextStyle = viewText.getStyle();
        viewTextStyle
                .setTextAlignment(containerStyle.getHorizontalTextAlignment())
                .setTextAlignment(containerStyle.getVerticalTextAlignment())
                .setTextColor(containerStyle.getTextColor())
                .setFont(containerStyle.getFont());

        // updates all the components style
        int numberOfElements = 3;
        ViewGroup container = getView();
        for (int i = 0; i < numberOfElements; i++) {
            updateElementStyle(container, i);
        }
    }

    /**
     * @return the number of managed values
     */

    public int numberOfValues() {
        return valuesToDisplay.size();
    }
}
