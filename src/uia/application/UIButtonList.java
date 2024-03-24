package uia.application;

import uia.core.basement.Drawable;
import uia.core.ui.View;
import uia.core.basement.Callback;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.theme.Theme;
import uia.physical.component.WrapperView;
import uia.utility.Geometries;
import uia.physical.component.Component;
import uia.physical.group.ComponentGroup;
import uia.physical.component.ComponentText;
import uia.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Standard UIa component.
 * <br>
 * UIButtonList is a horizontal scrollable list.
 */

public final class UIButtonList extends WrapperView {
    private final View viewLeft;
    private final View viewRight;
    private final ViewText viewText;

    private final List<String> list = new ArrayList<>();

    private int index;

    public UIButtonList(View view) {
        super(new ComponentGroup(view));
        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), 1f), true);

        viewText = new ComponentText(
                new Component("BUTTON_LIST_" + getID(), 0.5f, 0.5f, 0.7f, 1f)
        );
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(ViewText.AlignY.CENTER);
        viewText.getPaint()
                .setColor(Theme.TRANSPARENT)
                .setTextColor(null);

        viewLeft = new Component("BUTTON_LIST_LEFT_" + getID(), 0.1f, 0.5f, 0.1f, 0.5f)
                .setExpanseLimit(1.2f, 1.2f);
        viewLeft.registerCallback((OnClick) touches -> show(index - 1));
        viewLeft.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewLeft.setGeometry(Geometries::arrow, false);
        viewLeft.setColliderPolicy(ColliderPolicy.AABB);
        viewLeft.getPaint().setColor(Theme.BLACK);
        viewLeft.setRotation(MathUtility.PI);

        viewRight = new Component("BUTTON_LIST_RIGHT_" + getID(), 0.9f, 0.5f, 0.1f, 0.5f)
                .setExpanseLimit(1.2f, 1.2f);
        viewRight.registerCallback((OnClick) touches -> show(index + 1));
        viewRight.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewRight.setGeometry(Geometries::arrow, false);
        viewRight.setColliderPolicy(ColliderPolicy.AABB);
        viewRight.getPaint().setColor(Theme.BLACK);

        ViewGroup.insert(getView(), viewText, viewLeft, viewRight);
    }

    /**
     * Callback invoked when the next value is requested.
     */

    public interface OnNext extends Callback<String> {
    }

    /**
     * Callback invoked when the previous value is requested.
     */

    public interface OnPrevious extends Callback<String> {
    }

    /**
     * Fills this button with an array of strings and displays, by default, the first element.
     *
     * @param strings the strings to be displayed
     */

    public void setText(String... strings) {
        list.clear();
        list.addAll(Arrays.asList(strings));
        show((index = 0));
    }

    /**
     * Displays the specified String.
     *
     * @param index the index of the String to display
     */

    public void show(int index) {
        if (!list.isEmpty()) {

            if (index >= list.size()) {
                index = 0;
            } else if (index < 0) {
                index = list.size() - 1;
            }

            String element = list.get(index);
            if (index < this.index) {
                notifyCallbacks(OnPrevious.class, element);
            }
            if (index > this.index) {
                notifyCallbacks(OnNext.class, element);
            }
            this.index = index;
            viewText.setText(element);
        }
    }

    /**
     * @return the number of managed strings
     */

    public int size() {
        return list.size();
    }

    /**
     * @return the {@link View} displayed on the left
     */

    public View getViewLeft() {
        return viewLeft;
    }

    /**
     * @return the {@link View} displayed on the right
     */

    public View getViewRight() {
        return viewRight;
    }

    /**
     * @return the {@link ViewText} object
     */

    public ViewText getViewText() {
        return viewText;
    }
}
