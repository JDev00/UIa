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
import uia.physical.ComponentGroup;
import uia.physical.component.ComponentText;
import uia.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Horizontal scrollable list
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

        viewText = new ComponentText(new Component("TEXT", 0.5f, 0.5f, 0.7f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewText.setAlign(ViewText.AlignY.CENTER);
        viewText.getPaint().setColor(Theme.TRANSPARENT);

        viewLeft = new Component("LEFT", 0.1f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewLeft.setGeometry(Geometries::arrow, false);
        viewLeft.setColliderPolicy(ColliderPolicy.AABB);
        viewLeft.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewLeft.getPaint().setColor(Theme.BLACK);
        viewLeft.setRotation(MathUtility.PI);
        viewLeft.registerCallback((OnClick) touches -> show(index - 1));

        viewRight = new Component("RIGHT", 0.9f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewRight.setGeometry(Geometries::arrow, false);
        viewRight.setColliderPolicy(ColliderPolicy.AABB);
        viewRight.setConsumer(Consumer.SCREEN_TOUCH, false);
        viewRight.getPaint().setColor(Theme.BLACK);
        viewRight.registerCallback((OnClick) touches -> show(index + 1));

        ViewGroup.insert(getView(), viewText, viewLeft, viewRight);
    }

    /**
     * Event called when the next value is requested
     */

    public interface OnNext extends Callback<String> {
    }

    /**
     * Event called when the previous value is requested
     */

    public interface OnPrevious extends Callback<String> {
    }

    /**
     * Fill this view with an array of strings and displays, by default, the first element
     *
     * @param strings a not null array of strings
     */

    public void setText(String... strings) {
        list.clear();
        list.addAll(Arrays.asList(strings));
        show((index = 0));
    }

    /**
     * Display the specified String
     *
     * @param i the index of the String to display
     */

    public void show(int i) {
        if (!list.isEmpty()) {

            if (i >= list.size()) {
                i = 0;
            } else if (i < 0) {
                i = list.size() - 1;
            }

            String str = list.get(i);

            if (i < index) notifyCallbacks(OnPrevious.class, str);
            if (i > index) notifyCallbacks(OnNext.class, str);

            index = i;
            viewText.setText(str);
        }
    }

    /**
     * @return the number of handled strings
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
