package uia.application;

import uia.core.architecture.ui.View;
import uia.core.architecture.Event;
import uia.core.architecture.ui.ViewText;
import uia.core.architecture.ui.event.EventClick;
import uia.physical.ui.wrapper.WrapperView;
import uia.physical.Figure;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;
import uia.physical.ui.ComponentText;
import uia.utils.TrigTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Horizontal scrollable list
 */

public class ButtonListUI extends WrapperView {
    private final View viewLeft;
    private final View viewRight;
    private final ViewText viewText;

    private final List<String> list = new ArrayList<>();

    private int index;

    public ButtonListUI(View view) {
        super(new ComponentGroup(view));

        setGeom((v, s) -> Figure.rect(s, Figure.STD_VERT, 1f, v.bounds()[2] / v.bounds()[3]), true);

        viewText = new ComponentText(new Component("Text", 0.5f, 0.5f, 0.7f, 1f)
                .setExpanseLimit(1f, 1f));
        viewText.setConsumer(CONSUMER.POINTER, false);
        viewText.getPaint().setColor(0, 0, 0, 1);
        viewText.setAlign(ViewText.AlignY.CENTER);

        viewLeft = new Component("NextText", 0.1f, 0.5f, 0.11f, 0.4f).setExpanseLimit(1.2f, 1.2f);
        viewLeft.setColliderPolicy(true);
        viewLeft.setConsumer(CONSUMER.POINTER, false);
        viewLeft.setGeom((v, s) -> Figure.arrow2(s), false);
        viewLeft.getPaint().setColor(0);
        viewLeft.setRotation(TrigTable.PI);
        viewLeft.addEvent((EventClick) (v, pointers) -> show(index - 1));

        viewRight = new Component("PrevText", 0.9f, 0.5f, 0.11f, 0.4f).setExpanseLimit(1.2f, 1.2f);
        viewRight.setColliderPolicy(true);
        viewRight.setConsumer(CONSUMER.POINTER, false);
        viewRight.setGeom((v, s) -> Figure.arrow2(s), false);
        viewRight.getPaint().setColor(0);
        viewRight.addEvent((EventClick) (v, pointers) -> show(index + 1));

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
     * @return the left View
     */

    public View getViewLeft() {
        return viewLeft;
    }

    /**
     * @return the right View
     */

    public View getViewRight() {
        return viewRight;
    }

    /**
     * @return the associated {@link ViewText} object
     */

    public ViewText getText() {
        return viewText;
    }
}
