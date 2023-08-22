package uia.application;

import uia.core.ui.View;
import uia.core.basement.Event;
import uia.core.ui.ViewText;
import uia.core.event.OnClick;
import uia.application.theme.Theme;
import uia.physical.wrapper.WrapperView;
import uia.utils.Figure;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.utils.TrigTable;

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

        buildGeom((v, s) -> Figure.rect(s, Figure.STD_VERT, 1f, v.desc()[0] / v.desc()[1]), true);

        viewText = new ComponentText(new Component("TEXT", 0.5f, 0.5f, 0.7f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(CONSUMER.POINTER, false);
        viewText.setAlign(ViewText.AlignY.CENTER);
        viewText.getPaint().setColor(Theme.TRANSPARENT);

        viewLeft = new Component("LEFT", 0.1f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewLeft.buildGeom((v, s) -> Figure.arrow(s), false);
        viewLeft.setColliderPolicy(true);
        viewLeft.setConsumer(CONSUMER.POINTER, false);
        viewLeft.getPaint().setColor(Theme.BLACK);
        viewLeft.setRotation(TrigTable.PI);
        viewLeft.addEvent((OnClick) (v, pointers) -> show(index - 1));

        viewRight = new Component("RIGHT", 0.9f, 0.5f, 0.1f, 0.5f).setExpanseLimit(1.2f, 1.2f);
        viewRight.buildGeom((v, s) -> Figure.arrow(s), false);
        viewRight.setColliderPolicy(true);
        viewRight.setConsumer(CONSUMER.POINTER, false);
        viewRight.getPaint().setColor(Theme.BLACK);
        viewRight.addEvent((OnClick) (v, pointers) -> show(index + 1));

        ((ComponentGroup) getView()).add(viewText, viewLeft, viewRight);
    }

    /**
     * Event representing the action of move to the next String
     */

    public interface EventNext extends Event<View, String> {
    }

    /**
     * Event representing the action of move to the previous String
     */

    public interface EventPrevious extends Event<View, String> {
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

            if (i < index) updateEvent(EventPrevious.class, str);
            if (i > index) updateEvent(EventNext.class, str);

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
