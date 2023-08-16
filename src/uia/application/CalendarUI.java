package uia.application;

import uia.core.*;
import uia.core.architecture.ui.View;
import uia.physical.ui.wrapper.WrapperView;
import uia.core.architecture.Graphic;
import uia.core.architecture.Event;
import uia.core.architecture.ui.event.EventClick;
import uia.utils.TrigTable;
import uia.utils.Utils;
import uia.physical.Figure;
import uia.physical.ui.Component;
import uia.physical.ui.ComponentGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * UI Gregorian Calendar
 */

@Deprecated
public class CalendarUI extends WrapperView {
    public static final String[] WEEK = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    public static final String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar;

    private final ButtonListUI listUI;

    private Font font;

    private Geom geomCell;

    private final Paint paintCellOver;
    private final Paint paintCellSelected;

    private final Cell[] cells = new Cell[38];

    private int days;
    private int offset;
    private int sDay;
    private int sMonth;
    private int sYear;

    /**
     * Enable or disable the multiple day selection functionality
     */
    public boolean multipleSelection = false;

    public CalendarUI(View view) {
        super(new ComponentGroup(view));

        setGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, Figure.STD_ROUND, v.bounds()[2] / v.bounds()[3]), true);
        getPaint().setColor(45);
        addEvent((EventClick) (v, pointers) -> {
            int i = 0;
            while (i < days && !cells[7 + i].over) i++;

            if (i < days) {

                if (!multipleSelection && sDay != i + 1 && cells[7 + sDay - 1].selected)
                    cells[7 + sDay - 1].selected = false;

                sDay = i + 1;
                cells[7 + i].selected = !cells[7 + i].selected;

                updateEvent(EventSelection.class, getSelectedDays());
            }
        });

        listUI = new ButtonListUI(new Component("Button", 0.5f, 0.15f, 0.9f, 0.25f).setExpanseLimit(1f, 1f));
        listUI.setConsumer(CONSUMER.POINTER, false);
        listUI.getPaint().setColor(0, 0, 0, 1);
        listUI.getText().getTextPaint().setColor(255);
        listUI.getText().setPosition(0.35f, 0.5f);
        listUI.getViewRight().addEvent((EventClick) (v, pointers) -> setDate(sDay, sMonth + 1, sYear));

        View vLeft = listUI.getViewLeft();
        vLeft.setDimension(0.08f, 0.4f);
        vLeft.setPosition(0.7f, 0.5f);
        vLeft.addEvent((EventClick) (v, pointers) -> setDate(sDay, sMonth - 1, sYear));

        listUI.getViewRight().setDimension(0.08f, 0.4f);


        for (int i = 0; i < 7; i++) {
            Cell cell = new Cell(10, 100, 255);
            cell.text = WEEK[i];
            cells[i] = cell;
        }

        for (int i = 0; i < 31; i++) {
            Cell cell = new Cell(255, 255, 255);
            cell.text = String.valueOf(i + 1);
            cells[7 + i] = cell;
        }

        font = new Font("Arial", Font.STYLE.ITALIC, Font.FONT_SIZE_DESKTOP);

        geomCell = Figure.oval(new Geom(), Figure.STD_VERT);

        paintCellOver = new Paint().setColor(30);

        paintCellSelected = new Paint().setColor(10, 100, 255);

        calendar = GregorianCalendar.getInstance();

        int[] date = getCurrentDate();
        setDate(date[0], date[1], date[2]);


        ComponentGroup group = (ComponentGroup) getView();
        group.add(listUI);
        group.add(cells);
    }

    /**
     * Event Selection indicates that a day has been select.
     * It provides a View and the selected days as an array.
     */

    public interface EventSelection extends Event<View, Integer[]> {
    }

    /**
     * Event Change indicates a change in calendar's month and/or year.
     * It provides a View and the new date as an array made of: day, month, year.
     */

    public interface EventChange extends Event<View, Integer[]> {
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

        sDay = Utils.constrain(d, 1, days);

        offset = mapDay(calendar.get(Calendar.DAY_OF_WEEK));

        listUI.setText(MONTHS[m - 1] + " " + y);

        updateEvent(EventChange.class, toDate());
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
            cells[i].text = days[i];
        }
    }

    /**
     * Set a text's Font
     *
     * @param font a not null {@link Font}
     */

    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Set the Cell's geometry
     *
     * @param geom a not null {@link Geom}
     */

    public void setCellGeom(Geom geom) {
        geomCell = geom;
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (isVisible()) {
            float[] bounds = bounds();

            float dx;
            float dy;
            float dim = 0.7f / 6f;

            if (bounds[2] > bounds[3]) {
                dy = 0.475f * dim;
                dx = dy * bounds[3] / bounds[2];
            } else {
                dx = 0.475f * dim;
                dy = dx * bounds[2] / bounds[3];
            }

            float py1 = 1f / 3f;
            float py2 = py1 + dim;
            float gapY = (0.95f - py2) / 5f;

            for (int i = 0; i < 7; i++) {
                Cell cell = cells[i];
                cell.setPosition(0.15f + dim * i, py1);
                cell.setDimension(dx, dy);
                cell.update(this);
            }

            for (int i = 0; i < 31; i++) {
                Cell cell = cells[7 + i];
                cell.setPosition(0.15f + dim * ((i + offset) % 7), py2 + gapY * ((i + offset) / 7));
                cell.setDimension(dx, dy);
                cell.setVisible(i < days);
                cell.update(this);
            }

            // adjust font size
            int optFontSize = (int) Math.min(Math.min(dx * bounds[0], dy * bounds[1]), Font.FONT_SIZE_DESKTOP);
            if (optFontSize != font.getSize()) font.setSize(optFontSize);
        }
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {

            // highlight the selected or enabled days' cells
            for (int i = 0; i < days; i++) {
                Cell cell = cells[7 + i];

                if (cell.selected || cell.over) {
                    graphic.setPaint(cell.selected ? paintCellSelected : paintCellOver);
                    float scale = cell.over ? 1.1f : 1f;
                    float[] bounds = cell.bounds();

                    geomCell.x = bounds[0];
                    geomCell.y = bounds[1];
                    geomCell.scaleX = scale * bounds[2];
                    geomCell.scaleY = scale * bounds[3];
                    graphic.drawGeometry(geomCell);

                    // redraw the cell
                    cell.draw(graphic);
                }
            }
        }
    }

    /**
     * @return the selected days
     */

    public int[] getSelectedDays() {
        int count = 0;
        for (int i = 0; i < days; i++) {
            if (cells[7 + i].selected) count++;
        }

        int[] out = new int[count];
        count = 0;

        for (int i = 0; i < days; i++) {
            if (cells[7 + i].selected) out[count++] = i + 1;
        }

        return out;
    }

    /**
     * @return a new array filled with: day, month and year
     */

    public int[] toDate() {
        return new int[]{sDay, sMonth, sYear};
    }

    /**
     * @return the Calendar's {@link Font}
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the Cell's {@link Geom}
     */

    public Geom getCellGeom() {
        return geomCell;
    }

    /**
     * @return the {@link Paint} used when at least a {@link Pointer} is over a Calendar's Cell
     */

    public Paint getPaintOver() {
        return paintCellOver;
    }

    /**
     * @return the {@link Paint} used when a Calendar's Cell is selected
     */

    public Paint getPaintSelected() {
        return paintCellSelected;
    }

    /**
     * Check if the given string equals to the given value
     *
     * @param in    a not null String to compare to
     * @param value a value to compare to
     */

    private static boolean equals(String in, int value) {
        return in != null && Integer.parseInt(in) == value;
    }

    /**
     * Check if the given date contains the given day
     *
     * @param date a not null date to control
     * @param day  the day to look for
     * @return true if the date contains the given day
     */

    public static boolean isDay(String date, int day) {
        return date != null && equals(date.substring(0, (date.charAt(1) == '/') ? 1 : 2), day);
    }

    /**
     * Check if the given date contains the given month
     *
     * @param date  a not null date to control
     * @param month the month to look for
     * @return true if the date contains the given month
     */

    public static boolean isMonth(String date, int month) {
        if (date != null) {
            int i = date.indexOf("/") + 1;
            return equals(date.substring(i, i + (date.charAt(i + 1) == '/' ? 1 : 2)), month);
        }
        return false;
    }

    /**
     * Check if the given date contains the given year
     *
     * @param date a not null date to control
     * @param year the year to look for
     * @return true if the date contains the given year
     */

    public static boolean isYear(String date, int year) {
        return date != null && equals(date.substring(date.lastIndexOf("/") + 1), year);
    }

    /**
     * @return an array filled with: the current day between [1,31], the current month between [1,12] and the current year.
     */

    public static int[] getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new int[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
    }

    /**
     * Given an integer, returns the day of the week, where 0 is monday and 6 is sunday
     *
     * @param i an integer
     * @return the day of the week otherwise -1
     */

    public static int mapDay(int i) {
        switch (i) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return -1;
        }
    }

    /**
     * Calendar's cell.
     * <br>
     * A cell is a general item used to represent a day or a week's day
     */

    private class Cell implements View {
        private final float[] bounds = new float[4];
        private final float[] boundsAbs = new float[4];

        public final Paint paintText;

        public String text;

        private boolean over = false;
        public boolean selected = false;
        public boolean visible = true;

        public Cell(int r, int g, int b) {
            paintText = new Paint().setColor(r, g, b);
        }

        @Override
        public void dispatch(Key key) {
        }

        @Override
        public void dispatch(List<Pointer> pointers) {
            over = false;

            for (Pointer i : pointers) {
                over = !i.isConsumed() && TrigTable.intersects(
                        boundsAbs[0], boundsAbs[1], boundsAbs[2], boundsAbs[3], i.getX(), i.getY(), 1, 1
                );
                if (over) break;
            }
        }

        @Override
        public void update(View container) {
            float[] containerBounds = container.bounds();

            boundsAbs[0] = containerBounds[0] + containerBounds[2] * bounds[0];
            boundsAbs[1] = containerBounds[1] + containerBounds[3] * bounds[1];
            boundsAbs[2] = containerBounds[2] * bounds[2];
            boundsAbs[3] = containerBounds[3] * bounds[3];
        }

        @Override
        public void draw(Graphic graphic) {
            if (visible && text != null && text.length() > 0) {
                char[] array = text.toCharArray();

                graphic.setFont(font);
                graphic.setPaint(paintText);
                graphic.drawText(array, 0, array.length,
                        boundsAbs[0] - font.getWidth(array, 0, array.length) / 2f,
                        boundsAbs[1] + 0.5f * font.getSize(),
                        0f);
            }
        }

        @Override
        public void setGeom(BiConsumer<View, Geom> builder, boolean everyFrame) {

        }

        @Override
        public Geom getGeom() {
            return null;
        }

        @Override
        public void setPaint(Paint paint) {

        }

        @Override
        public Paint getPaint() {
            return null;
        }

        @Override
        public void addEvent(Event<View, ?> event) {
        }

        @Override
        public void removeEvent(Event<View, ?> event) {
        }

        @Override
        public void updateEvent(Class<? extends Event> type, Object o) {

        }

        @Override
        public void setPosition(float x, float y) {
            bounds[0] = x;
            bounds[1] = y;
        }

        @Override
        public void setDimension(float x, float y) {
            bounds[2] = x;
            bounds[3] = y;
        }

        @Override
        public void setRotation(float radians) {

        }

        @Override
        public void setFocus(boolean request) {

        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public float[] bounds() {
            return boundsAbs;
        }

        @Override
        public float[] desc() {
            return new float[0];
        }

        @Override
        public boolean contains(float x, float y) {
            return false;
        }

        @Override
        public String getID() {
            return null;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public void setConsumer(CONSUMER consumer, boolean enableConsumer) {

        }

        @Override
        public void setColliderPolicy(boolean aabb) {

        }
    }
}
