package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.platform.independent.Font;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Pointer;
import uia.core.platform.policy.Graphic;
import uia.utils.Collider;
import uia.utils.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Gregorian's calendar Widget
 */

public class CalendarView extends View {
    public final static String[] WEEK_IT = new String[]{"L", "M", "M", "G", "V", "S", "D"};
    public final static String[] WEEK_EN = new String[]{"M", "T", "W", "T", "F", "S", "S"};

    public final static String[] MONTHS_IT = new String[]{
            "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
    };
    public final static String[] MONTHS_EN = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar;

    private final HorView info;

    private Font font;

    private Shape shapeCell;

    private final Paint paintCellOver;
    private final Paint paintCellSelected;

    private final Cell[] cellWeek;

    private Cell[] cellDay;

    private int days;
    private int offset;
    private int sDay;
    private int sMonth;
    private int sYear;

    private boolean multipleSelection = false;

    public CalendarView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
        getPaint().setColor(45, 45, 45);
        getEventQueue().addEvent((v, s) -> {
            if (s == Event.POINTER_CLICK) {
                int i = 0;
                while (i < days && !cellDay[i].over) i++;

                if (i < days) {

                    if (!multipleSelection && sDay != i + 1 && cellDay[sDay - 1].selected)
                        cellDay[sDay - 1].select(false);

                    sDay = i + 1;
                    cellDay[i].select(!cellDay[i].isSelected());
                    updateEvent(Date.class, Date.SELECT);
                }
            }
        });

        info = new HorView(context, 0, 0, 1, 1) {
            @Override
            public void showPrev() {
                super.showPrev();
                setDate(sDay, sMonth - 1, sYear);
            }

            @Override
            public void showNext() {
                super.showNext();
                setDate(sDay, sMonth + 1, sYear);
            }
        };
        info.getPaint().setColor(0, 0, 0, 1);
        info.setExpansion(0f, 0f);
        info.setPointerConsumer(false);
        info.setPositionAdjustment(false);
        info.setDimensionAdjustment(false);
        info.set(MONTHS_IT);
        info.getTextView().getPaintText().setColor(255, 255, 255);
        info.getView1().getPaint().setColor(255, 230, 220);
        info.getView2().getPaint().setColor(255, 230, 220);

        cellWeek = new Cell[7];
        for (int i = 0; i < cellWeek.length; i++) {
            Cell cell = new Cell(10, 100, 255);
            cell.setText(WEEK_IT[i]);
            cellWeek[i] = cell;
        }

        cellDay = new Cell[31];
        for (int i = 0; i < cellDay.length; i++) {
            Cell cell = new Cell(255, 255, 255);
            cell.setText(String.valueOf(i + 1));
            cellDay[i] = cell;
        }

        calendar = GregorianCalendar.getInstance();

        font = new Font("Arial", Font.STYLE.ITALIC, 18f);

        shapeCell = Figure.oval(null, Figure.STD_VERT);

        paintCellOver = new Paint(30, 30, 30);

        paintCellSelected = new Paint(10, 100, 255);

        int[] date = getCurrentDate();
        setDate(date[0], date[1], date[2]);
    }

    /**
     * Date event
     */

    public interface Date extends Event<View> {
        /**
         * SELECT state indicates that a day has been select
         */
        int SELECT = 0;

        /**
         * CHANGE state indicates a change in calendar's month or year
         */
        int CHANGE = 1;
    }

    /**
     * Color sundays
     *
     * @param r  the sunday's red channel
     * @param g  the sunday's green channel
     * @param b  the sunday's blue channel
     * @param ro the other days red channel
     * @param go the other days green channel
     * @param bo the other days blue channel
     */

    public Date colorSundays(int r, int g, int b,
                             int ro, int go, int bo) {
        Date date = (v, s) -> {
            if (s == Date.CHANGE) {
                for (int i = 0; i < days; i++) {
                    Paint paint = cellDay[i].getPaintText();

                    if ((offset + i + 1) % 7 == 0) {
                        paint.setColor(r, g, b);
                    } else {
                        paint.setColor(ro, go, bo);
                    }
                }
            }
        };
        date.onEvent(this, Date.CHANGE);
        return date;
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
        if (sDay > days && cellDay[sDay - 1].isSelected()) cellDay[sDay - 1].select(false);

        sDay = Utils.constrain(d, 1, days);

        offset = mapDay(calendar.get(Calendar.DAY_OF_WEEK));

        info.setText(info.getString(m - 1) + " " + y);

        // update events
        updateEvent(Date.class, Date.CHANGE);
    }

    /**
     * Set the name of the week's days.
     * <br>
     * Note that the first day is monday and the last is sunday.
     *
     * @param days a not null array
     */

    public void setWeek(String[] days) {
        if (days != null && days.length == 7) {

            for (int i = 0; i < days.length; i++) {
                cellWeek[i].text = days[i];
            }
        }
    }

    /**
     * Enable or disable the multiple day selection functionality
     *
     * @param multipleSelection true to enable multiple day selection
     */

    public void setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }

    /**
     * Set a text's Font
     *
     * @param font a not null {@link Font}
     */

    public void setFont(Font font) {
        if (font != null) this.font = font;
    }

    /**
     * Set the Cell's Shape
     *
     * @param shapeCell a not null {@link Shape}
     */

    public void setShapeCell(Shape shapeCell) {
        if (shapeCell != null) this.shapeCell = shapeCell;
    }

    @Override
    protected void updatePointers(boolean update) {
        for (int i = 0; i < days; i++) {
            cellDay[i].updatePointers(update);
        }
        View.updatePointers(info, update);
        super.updatePointers(update);
    }

    @Override
    protected void update() {
        super.update();
        View.update(info);
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        float x = x();
        float y = y();
        float width = width();
        float height = height();

        float wCell = 0.9f * width / 7f;
        float hCell = (2f * height / 3) / 7;
        float dim = 0.975f * Math.min(wCell, hCell);

        float weePosY = y - height / 6f;
        float dayPosY = weePosY + hCell;

        graphic.setFont(font);

        // draw week's cells
        for (int i = 0; i < cellWeek.length; i++) {
            cellWeek[i].setPosition(x + wCell * (i - cellWeek.length / 2), weePosY);
            cellWeek[i].setDimension(dim, dim);
            cellWeek[i].draw(graphic);
        }

        // highlight the selected or enabled days' cells
        for (int i = 0; i < days; i++) {
            Cell cell = cellDay[i];

            if (cell.isSelected() || cell.over) {
                graphic.setPaint(cell.isSelected() ? paintCellSelected : paintCellOver);
                float scale = cell.over ? 1.1f : 1f;
                shapeCell.setPosition(cell.x, cell.y);
                shapeCell.setDimension(scale * cell.width, scale * cell.height);
                shapeCell.draw(graphic);
            }
        }

        // draw days' cells
        for (int i = 0; i < days; i++) {
            cellDay[i].setPosition(x + wCell * ((i + offset) % 7 - 3), dayPosY + hCell * ((i + offset) / 7));
            cellDay[i].setDimension(dim, dim);
            cellDay[i].draw(graphic);
        }

        // draw header
        info.setDimension(0.9f * width, 0.33f * height);
        info.setCenter(x, y - height / 3f);
        View.draw(info, graphic);
    }

    /**
     * @return the first day offset
     */

    public float getDayOffset() {
        return offset;
    }

    /**
     * @return the selected days
     */

    public int[] getSelectedDays() {
        int count = 0;
        for (int i = 0; i < days; i++) {
            if (cellDay[i].isSelected()) count++;
        }

        int[] out = new int[count];
        count = 0;

        for (int i = 0; i < days; i++) {
            if (cellDay[i].isSelected()) out[count++] = i + 1;
        }

        return out;
    }

    /**
     * @return the number of days of the current month
     */

    public int getDays() {
        return days;
    }

    /**
     * @return the selected day or -1 if no day has been selected yet
     */

    public int getDay() {
        return cellDay[sDay - 1].isSelected() ? sDay : -1;
    }

    /**
     * @return the calendar's month expressed between [1, 12]
     */

    public int getMonth() {
        return sMonth;
    }

    /**
     * @return the calendar's year
     */

    public int getYear() {
        return sYear;
    }

    /**
     * @return a new array filled with: {@link #getDay()}, {@link #getMonth()} and {@link #getYear()}
     */

    public int[] toDate() {
        return new int[]{sDay, sMonth, sYear};
    }

    /**
     * @return true if multiple selection functionality is enabled
     */

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    /**
     * @return the Calendar's {@link Font}
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the Cell's {@link Shape}
     */

    public Shape getShapeCell() {
        return shapeCell;
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
     * @return the {@link HorView} used to display Calendar's information
     */

    public HorView getHorView() {
        return info;
    }

    /**
     * @return the {@link Cell}s used to represent week's days
     */

    public Cell[] getWeekCells() {
        return cellWeek;
    }

    /**
     * @return the {@link Cell}s used to represent days
     */

    public Cell[] getDayCells() {
        return cellDay;
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
     * Return the given date formatted
     *
     * @param date  an array made of: day, month and year
     * @param regex a String used to concatenate day, month and year
     * @return if possible the date otherwise null
     */

    public static String toString(int[] date, String regex) {
        return (regex != null && date != null && date.length == 3) ? (date[0] + regex + date[1] + regex + date[2]) : null;
    }

    /**
     * @return an array filled with: the current day between [1,31], the current month between [1,12] and the current year.
     */

    public static int[] getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new int[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
    }

    /**
     * Calendar's cell.
     * <br>
     * A cell is a general UI item used to represent a day or a week's day
     */

    public class Cell {
        private final Paint paintText;

        private String text;

        private float x;
        private float y;
        private float width;
        private float height;

        private boolean over = false;
        private boolean selected = false;

        public Cell(int r, int g, int b) {
            paintText = new Paint(r, g, b);
        }

        /**
         * Set the Cell's position
         */

        protected void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Set the Cell's dimension
         */

        protected void setDimension(float width, float height) {
            this.width = Math.max(0, width);
            this.height = Math.max(0, height);
        }

        /**
         * Set a new text to display
         *
         * @param text a String; it could be null
         */

        public void setText(String text) {
            this.text = text;
        }

        /**
         * Select or deselect this Cell
         *
         * @param selected true to select it
         */

        public void select(boolean selected) {
            this.selected = selected;
        }

        /**
         * Update Cell's pointers
         */

        protected void updatePointers(boolean update) {
            Pointer p = getContext().pointers()[0];
            over = update && !p.isConsumed() && Collider.AABB(x, y, width, height, p.getX(), p.getY(), 1, 1);
        }

        /**
         * Draw this Cell on the given Graphic
         *
         * @param graphic a not null {@link Graphic}
         */

        protected void draw(Graphic graphic) {
            if (text != null && text.length() > 0) {
                graphic.setPaint(paintText);
                graphic.drawText(text, x - font.getWidth(text) / 2f, y + font.getSize() / 2f);
            }
        }

        /**
         * @return true if this Cell is currently selected
         */

        public boolean isSelected() {
            return selected;
        }

        /**
         * @return the {@link Paint} used to manipulate text color
         */

        public Paint getPaintText() {
            return paintText;
        }
    }
}
