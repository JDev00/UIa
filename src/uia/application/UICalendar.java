package uia.application;

import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.core.*;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.event.OnMouseEnter;
import uia.core.ui.event.OnMouseExit;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperView;
import uia.core.basement.Event;
import uia.core.ui.event.OnClick;
import uia.physical.wrapper.WrapperViewText;
import uia.utility.Utility;
import uia.utility.Figure;
import uia.physical.Component;
import uia.physical.ComponentGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * UI Gregorian Calendar
 */

// TODO: 20/08/2023 finire calendario 

public class UICalendar extends WrapperView {
    public static final String[] WEEK = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    public static final String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar;

    private final UIButtonList listUI;

    private final Font font;
    private final Paint[] paintCell = {
            new Paint().setColor(Theme.TRANSPARENT),
            new Paint().setColor(ThemeDarcula.W_BACKGROUND),
            new Paint().setColor(ThemeDarcula.W_FOREGROUND)
    };

    private final Cell[] cells = new Cell[38];

    private int days;
    private int offset;
    private int sDay;
    private int sMonth;
    private int sYear;

    public UICalendar(View view) {
        super(new ComponentGroup(view));

        buildGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, Figure.STD_ROUND, v.desc()[0] / v.desc()[1]), true);
        getPaint().setColor(ThemeDarcula.BACKGROUND);

        font = new Font("Arial", Font.STYLE.ITALIC, Font.FONT_SIZE_DESKTOP);


        listUI = (UIButtonList) createHeader(font);
        listUI.getViewRight().addEvent((OnClick) pointers -> setDate(sDay, sMonth + 1, sYear));
        listUI.getViewLeft().addEvent((OnClick) pointers -> setDate(sDay, sMonth - 1, sYear));


        for (int i = 0; i < 7; i++) {
            cells[i] = Cell.createWeekDay(WEEK[i]);
            cells[i].getTextPaint().setColor(Theme.BLUE);
        }

        for (int i = 0; i < 31; i++) {
            Cell cell = Cell.createDay(String.valueOf(i + 1));
            cell.addEvent((OnClick) pointers -> {
                Paint paint = cell.getPaint();
                if (paint.equals(paintCell[2])) {
                    paint.set(paintCell[1]);
                } else {
                    paint.set(paintCell[2]);
                    updateEvent(OnSelection.class, getDate()[0]);
                }
            });
            cell.addEvent((OnMouseEnter) pointers -> {
                Paint paint = cell.getPaint();
                if (!paint.equals(paintCell[2])) paint.set(paintCell[1]);
            });
            cell.addEvent((OnMouseExit) pointers -> {
                Paint paint = cell.getPaint();
                if (!paint.equals(paintCell[2])) paint.set(paintCell[0]);
            });
            cells[i + 7] = cell;
        }

        for (Cell i : cells) {
            i.setFont(font);
            i.getPaint().set(paintCell[0]);
        }

        calendar = GregorianCalendar.getInstance();

        int[] date = Utility.getDate();
        setDate(date[0], date[1], date[2]);


        ComponentGroup group = (ComponentGroup) getView();
        group.add(listUI);
        group.add(cells);
    }

    /**
     * Event called when a day has been select.
     * <br>
     * It provides a View and the selected day.
     */

    public interface OnSelection extends Event<Integer> {
    }

    /**
     * Event called when a change in calendar's month and/or year is detected.
     * <br>
     * It provides a View and the new date as an array made of: day, month and year.
     */

    public interface OnChange extends Event<Integer[]> {
    }

    /**
     * Create the calendar's header
     */

    private static View createHeader(Font font) {
        UIButtonList view = new UIButtonList(new Component("HEADER", 0.5f, 0.15f, 0.9f, 0.2f)
                .setExpanseLimit(1f, 1f));
        view.setConsumer(CONSUMER.POINTER, false);
        view.getPaint().setColor(Theme.TRANSPARENT);

        ViewText text = view.getViewText();
        text.setFont(font);
        text.getTextPaint().setColor(Theme.WHITE);
        text.setPosition(0.35f, 0.5f);

        View right = view.getViewRight();
        right.setDimension(0.05f, 0.4f);
        right.getPaint().setColor(Theme.WHITE);

        View left = view.getViewLeft();
        left.setDimension(0.05f, 0.4f);
        left.setPosition(0.7f, 0.5f);
        left.getPaint().setColor(Theme.WHITE);

        return view;
    }

    /**
     * Set the calendar's date
     *
     * @param d the day of the month between [1, 31]
     * @param m the month between [1, 12]
     * @param y the year
     */

    public void setDate(int d, int m, int y) {
        if (m > 12) {
            m = 1;
            y++;
        } else if (m < 1) {
            m = 12;
            y--;
        }

        calendar.clear();
        calendar.set(y, m - 1, 1);

        sMonth = m;
        sYear = y;
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // update selection
        if (sDay > days && cells[7 + sDay - 1].selected) cells[7 + sDay - 1].selected = false;

        sDay = Utility.constrain(d, 1, days);

        offset = Utility.getDay(calendar.get(Calendar.DAY_OF_WEEK));

        listUI.setText(MONTHS[m - 1] + " " + y);

        updateEvent(OnChange.class, getDate());
    }

    /**
     * Set the name of the week's days.
     * <br>
     * Note that the first day is monday and the last is sunday.
     *
     * @param days a not null array
     */

    public void setWeek(String... days) {
        for (int i = 0; i < days.length; i++) {
            cells[i].setText(days[i]);
        }
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (isVisible()) {
            float dx = 0.7f / 6f;
            float dy = 0.08f;
            float y1 = 1f / 3f;
            float y2 = y1 + dx;
            float gap = (0.95f - y2) / 5f;

            for (int i = 0; i < 7; i++) {
                cells[i].setPosition(0.15f + dx * i, y1);
            }

            for (int i = 0; i < 31; i++) {
                Cell cell = cells[7 + i];
                cell.setPosition(0.15f + dx * ((i + offset) % 7), y2 + gap * ((i + offset) / 7));
                cell.setVisible(i < days);
            }

            for (Cell i : cells) {
                i.setDimension(dx, dy);
                i.update(this);
            }

            // adjust font size
            float[] desc = desc();
            float fontSize = Math.min(Math.min(dx * desc[0], dy * desc[1]), Font.FONT_SIZE_DESKTOP);
            if (fontSize != font.getSize()) font.setSize(fontSize);
        }
    }

    /**
     * @return the calendar's {@link Font}
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the {@link Paint} used to color days
     */

    public Paint getPaintDay() {
        return paintCell[0];
    }

    /**
     * @return the {@link Paint} used to color a cursor covered day
     */

    public Paint getPaintOver() {
        return paintCell[1];
    }

    /**
     * @return the {@link Paint} used when a day is selected
     */

    public Paint getPaintSelected() {
        return paintCell[2];
    }

    /**
     * @return the calendar's date as new array made of: day, month and year
     */

    public int[] getDate() {
        return new int[]{sDay, sMonth, sYear};
    }

    /**
     * @return a new {@link List} filled with the selected days
     */

    public List<Integer> getSelectedDays() {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            if (cells[7 + i].selected) out.add(i + 1);
        }
        return out;
    }

    /**
     * Cell is a general item used to represent a day or a day of week
     */

    private static class Cell extends WrapperViewText {
        public boolean selected = false;

        public Cell(int number) {
            super(new ComponentText(new Component("Cell" + number, 0f, 0f, 0f, 0f)));

            setAlign(ViewText.AlignY.CENTER);
            setConsumer(CONSUMER.POINTER, false);
        }

        private static int NUMBER = 0;

        /**
         * Create a new Cell used to represent a day of weeks
         */

        public static Cell createWeekDay(String weekDay) {
            Cell cell = new Cell(NUMBER++);
            cell.setText(weekDay);
            cell.getTextPaint().setColor(ThemeDarcula.W_FOREGROUND);
            return cell;
        }

        /**
         * Create a new Cell used to represent a day
         */

        public static Cell createDay(String day) {
            Cell cell = new Cell(NUMBER);
            cell.setText(day);
            cell.getTextPaint().setColor(Theme.WHITE);
            return cell;
        }
    }
}
