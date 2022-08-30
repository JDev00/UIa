package uia.core.widget;

import uia.core.View;
import uia.core.event.Event;
import uia.core.event.Mouse;
import uia.core.geometry.Figure;
import uia.core.geometry.Oval;
import uia.core.geometry.Rect;
import uia.core.policy.*;
import uia.utils.Utils;

import java.util.Calendar;

import static java.lang.Math.min;

/**
 * Calendar Widget.
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

    private final HorView month;

    private final Cell[] wee;
    private Cell[] day;

    private Paint paintCursor;
    private Figure cursor;

    private Path path;

    private final Font font;

    private int days;
    private int iday;
    private int imonth;
    private int iyear;
    private int offset;

    public CalendarView(Context context,
                        float px, float py,
                        float dx, float dy) {
        super(context, px, py, dx, dy);
        super.setPaint(context.createColor(getPaint(), 45, 45, 45));
        super.addEvent((Mouse) (v, s) -> {
            if (s == Mouse.CLICK) {
                int i = 0;
                while (i < days && !day[i].over) i++;

                if (i < days) {
                    iday = i + 1;
                    updateEvent(Date.class, 0);
                }
            }
        });


        month = new HorView(context, 0, 0, 0, 0);
        month.enableAutoAdjustment(false);
        month.enableOverlay(false);
        month.setLeftScale(0.15f, 0.3f);
        month.setRightScale(0.15f, 0.3f);
        month.setPaint(context.createColor(month.getPaint(), Context.COLOR.STD_NO_PAINT));
        month.set(MONTHS_IT);
        month.<TextView>modify(HorView.TEXT, v -> {
            v.setPaint(context.createColor(v.getPaint(), Context.COLOR.STD_NO_PAINT));
            v.setPaintText(context.createColor(v.getPaintText(), Context.COLOR.WHITE));
            //v.setFontSize(min(0.1f * month.dy(), v.getFontSize()));
        });
        month.modify(HorView.LEFT, v -> {
            v.setPaint(context.createColor(v.getPaint(), 255, 230, 220));
            v.addEvent((Mouse) (v1, s) -> {
                if (s == Mouse.CLICK) {
                    month.showPrev();
                    setDate(iday, imonth - 1, iyear);
                }
            });
        });
        month.modify(HorView.RIGHT, v -> {
            v.setPaint(context.createColor(v.getPaint(), 255, 230, 220));
            v.addEvent((Mouse) (v1, s) -> {
                if (s == Mouse.CLICK) {
                    month.showNext();
                    setDate(iday, imonth + 1, iyear);
                }
            });
        });


        Paint paintText = context.createColor(null, 10, 100, 255);
        Paint paintNote = context.createColor(null, 0, 255, 0);

        wee = new Cell[7];
        for (int i = 0; i < wee.length; i++) {
            wee[i] = new Cell(paintText, paintNote);
            wee[i].text = WEEK_IT[i];
        }


        Figure figureNote = new Rect();
        paintText = context.createColor(null, Context.COLOR.WHITE);

        day = new Cell[31];
        for (int i = 0; i < day.length; i++) {
            day[i] = new Cell(paintText, paintNote);
            day[i].text = String.valueOf(i + 1);
            day[i].figureNote = figureNote;
        }


        paintCursor = context.createColor(null, 100, 100, 255);
        cursor = Oval.create();

        path = context.createPath();

        font = context.copyFont(null, ((TextView) month.get(HorView.TEXT)).getFont());
        font.setSize(2 * font.getSize() / 3f);


        int[] date = getCurrentDate();
        setDate(date[0], date[1], date[2]);
    }

    /**
     * Event representing a date selection
     */

    public interface Date extends Event<View> {
    }

    /**
     * Set the calendar date
     *
     * @param d the day of the month between [1, 31]
     * @param m the month between [1, 12]
     * @param y the year
     */

    public void setDate(int d, int m, int y) {
        if (m > 12) {
            m = 1;
            y++;
        }

        if (m < 1) {
            m = 12;
            y--;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(y, m - 1, 1);


        imonth = m;
        iyear = y;
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        iday = Utils.constrain(d, 1, days);


        offset = mapDay(calendar.get(Calendar.DAY_OF_WEEK));

        month.setText(month.getString(m - 1) + " " + y);

        // Update event handler
        updateEvent(Date.class, 0);
    }

    /**
     * Remove all notes
     */

    public void clearNotes() {
        for (Cell cell : day) {
            cell.note = null;
        }
    }

    /**
     * Set a note to a specific day
     *
     * @param i    the day starting from 1
     * @param note a note to set to the specified day
     */

    public void setNote(int i, String note) {
        int k = i - 1;
        if (k >= 0 && k < day.length)
            day[k].note = note;
    }

    /**
     * Set the name of the week's days.
     * <br>
     * Note that the first day is monday and the last is sunday.
     *
     * @param days an array of strings of length 7
     */

    public void setWeek(String[] days) {
        if (days != null && days.length == 7) {

            for (int i = 0; i < days.length; i++) {
                wee[i].text = days[i];
            }
        }
    }

    /**
     * Set the week's color
     *
     * @param p a not null {@link Paint}
     */

    public void setWeekColor(Paint p) {
        if (p != null) {

            for (Cell cell : wee) {
                cell.paintText = p;
            }
        }
    }

    /**
     * Set the week's color
     *
     * @param p a not null array of {@link Paint}.
     *          If {@code array.length() < 7}, then the rest of the week will keep the old color
     */

    public void setWeekColor(Paint[] p) {
        if (p != null) {
            int i = 0;

            while (i < p.length && i < wee.length) {

                if (p[i] != null)
                    wee[i].paintText = p[i];

                i++;
            }
        }
    }

    /**
     * Set the days' color
     *
     * @param p a not null {@link Paint}
     */

    public void setDaysColor(Paint p) {
        if (p != null) {

            for (Cell cell : day) {
                cell.paintText = p;
            }
        }
    }

    /**
     * Set the days' color
     *
     * @param p a not null array of {@link Paint}.
     *          If {@code array.length() < 7}, then the rest of the days will keep the old color
     */

    public void setDaysColor(Paint[] p) {
        if (p != null) {
            int i = 0;

            while (i < p.length && i < day.length) {

                if (p[i] != null)
                    day[i].paintText = p[i];

                i++;
            }
        }
    }

    /**
     * Set a cursor's Paint
     *
     * @param p a not null {@link Paint}
     */

    public void setCursorPaint(Paint p) {
        if (p != null)
            paintCursor = p;
    }

    /**
     * Set a new cursor's Figure
     *
     * @param cursor a not null {@link Figure}
     */

    public void setCursor(Figure cursor) {
        this.cursor = cursor;
    }

    @Override
    public void setContext(Context c) {
        super.setContext(c);

        if (!c.equals(getContext()))
            path = c.createPath();
    }

    @Override
    protected void pointerDispatcher(boolean update) {
        Context context = getContext();
        for (int i = 0; i < days; i++) {
            day[i].pointerDispatcher(context, update);
        }

        View.updatePointerDispatcher(month, update);

        super.pointerDispatcher(update);
    }

    @Override
    public void postDraw(Render render) {
        float px = px();
        float py = py();


        month.setDim(0.9f * dx(), 0.33f * dy());
        month.setPos(px, py - dy() / 3f);
        month.draw(render);


        float cdx = month.dx() / 7f;
        float cdy = (2f * dy() / 3) / 7;
        float min = min(cdx, cdy);

        if (cursor != null) {
            render.setPaint(paintCursor);
            cursor.build(path, day[iday - 1].px, day[iday - 1].py, min, min, 0f);
            render.draw(path);
        }


        render.setFont(font);

        for (int i = 0; i < wee.length; i++) {
            float x = (i - wee.length / 2) / (float) wee.length;
            wee[i].set(px + month.dx() * x, py - dy() / 6f, cdx, cdy);
            wee[i].draw(render, path, font);
        }


        float dpy = wee[0].py + cdy;

        for (int i = 0; i < days; i++) {
            day[i].set(px + day[i].dx * ((i + offset) % 7 - 3), dpy + day[i].dy * ((i + offset) / 7), cdx, cdy);
            day[i].draw(render, path, font);
        }
    }

    /**
     * @return the days of the current month
     */

    public final int getDays() {
        return days;
    }

    /**
     * @return the selected day
     */

    public final int getSelectedDay() {
        return iday;
    }

    /**
     * @return the current calendar's month expressed between [1, 12]
     */

    public final int getMonth() {
        return imonth;
    }

    /**
     * @return the current calendar's year
     */

    public final int getYear() {
        return iyear;
    }

    /**
     * @return a new array filled with: calendar's day, calendar's month and calendar's year
     */

    public final int[] toDate() {
        return new int[]{iday, imonth, iyear};
    }

    /**
     * @param i the day starting from 0
     * @return true if the given day has a note attached
     */

    public final boolean hasNote(int i) {
        return day[i].note != null;
    }

    /**
     * @param i the day starting from 0
     * @return the note associated to the specified day
     */

    public final String getNote(int i) {
        return day[i].note;
    }

    /**
     * @return the cursor's Paint
     */

    public final Paint getPaintCursor() {
        return paintCursor;
    }

    /**
     * @return the cursor object
     */

    public final Figure getCursor() {
        return cursor;
    }

    /**
     * @return the Font used to render days
     */

    public final Font getFont() {
        return font;
    }

    /**
     * @return the header used to display month and year
     */

    public final HorView getHeader() {
        return month;
    }

    /**
     * Given an integer between [0,6] returns the day of the week, where 0 is monday and 6 is sunday.
     * <br>
     * Note that if {@code i < 0 or i > 6} it returns 0 (MONDAY)
     *
     * @param i an integer between [0,6]
     * @return the day of the week
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
        }

        return 0;
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
     * @param date  the date to format
     * @param regex the string used to concatenate day, month and year
     * @return if possible the date otherwise null
     */

    public static String toString(int[] date, String regex) {
        return (regex != null && date != null && date.length == 3)
                ? (date[0] + regex + date[1] + regex + date[2])
                : null;
    }

    /**
     * @return an array filled with:
     * <br>
     * 1) the current day between [1,31]
     * <br>
     * 2) the current month between [1,12]
     * <br>
     * 3) the current year</li>
     */

    public static int[] getCurrentDate() {
        Calendar c = Calendar.getInstance();
        return new int[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
    }

    /*
     *
     *
     */

    /**
     * Calendar cell
     */

    private final static class Cell {
        public Figure figure = null;
        public Figure figureNote = null;

        public Paint paintText;
        public Paint paintNote;

        public String text;
        public String note;

        public float px;
        public float py;
        public float dx;
        public float dy;

        private boolean over = false;

        public Cell(Paint paintText, Paint paintNote) {
            this.paintText = paintText;
            this.paintNote = paintNote;
        }

        public void set(float px, float py,
                        float dx, float dy) {
            this.px = px;
            this.py = py;
            this.dx = dx;
            this.dy = dy;
        }

        public void pointerDispatcher(Context context, boolean update) {
            Pointer p = context.pointers()[0];

            if (update && !p.isConsumed())
                over = Utils.AABB(p.getX(), p.getY(), 1, 1, px, py, dx, dy);
        }

        public void draw(Render render,
                         Path path, Font font) {
            if (text != null) {
                float textSize = font.getSize();
                boolean noNote = (note == null);

                if (figure != null) {
                    figure.build(path, px, py, dx, dy, 0f);
                    render.draw(path);
                }

                render.setPaint(noNote ? paintText : paintNote);
                render.drawText(text,
                        (int) (px - font.getWidth(text.toCharArray(), 0, text.length()) / 2f),
                        (int) (py + textSize / 2f));

                if (figureNote != null && !noNote) {
                    figureNote.build(path,
                            px, py + 0.55f * textSize,
                            font.getWidth(text.toCharArray(), 0, text.length()), 2f, 0f);
                    render.draw(path);
                }
            }
        }
    }
}
