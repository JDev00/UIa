package uia.application;

import uia.core.ui.View;
import uia.core.basement.Callback;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.utility.Figure;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.utility.TrigTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Horizontal scrollable list
 */

public class UIButtonList extends WrapperView {
    private final View viewLeft;
    private final View viewRight;
    private final ViewText viewText;

    private final List<String> list = new ArrayList<>();

    private int index;

    public UIButtonList(View view) {
        super(new ComponentGroup(view));

        buildGeometry(g -> Component.buildRect(g, getWidth(), getHeight(), 1f), true);

        viewText = new ComponentText(new Component("TEXT", 0.5f, 0.5f, 0.7f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(CONSUMER.POINTER, false);
        viewText.setAlign(ViewText.AlignY.CENTER);
        viewText.getPaint().setColor(Theme.TRANSPARENT);

        viewLeft = new Component("LEFT", 0.1f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewLeft.buildGeometry(Figure::arrow, false);
        viewLeft.setColliderPolicy(COLLIDER_POLICY.AABB);
        viewLeft.setConsumer(CONSUMER.POINTER, false);
        viewLeft.getPaint().setColor(Theme.BLACK);
        viewLeft.setRotation(TrigTable.PI);
        viewLeft.addCallback((OnClick) pointers -> show(index - 1));

        viewRight = new Component("RIGHT", 0.9f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewRight.buildGeometry(Figure::arrow, false);
        viewRight.setColliderPolicy(COLLIDER_POLICY.AABB);
        viewRight.setConsumer(CONSUMER.POINTER, false);
        viewRight.getPaint().setColor(Theme.BLACK);
        viewRight.addCallback((OnClick) pointers -> show(index + 1));

        ((ComponentGroup) getView()).add(viewText, viewLeft, viewRight);
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
