package uia.application;

import uia.core.paint.Paint;
import uia.core.basement.Drawable;
import uia.core.ui.ViewGroup;
import uia.physical.component.Component;
import uia.physical.component.ComponentText;
import uia.physical.component.WrapperView;
import uia.physical.component.WrapperViewText;
import uia.physical.group.ComponentGroup;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.core.*;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnMouseEnter;
import uia.core.ui.callbacks.OnMouseExit;
import uia.core.basement.Callback;
import uia.core.ui.callbacks.OnClick;
import uia.utility.CalendarUtility;
import uia.utility.MathUtility;
import uia.utility.Geometries;

import java.util.*;

/**
 * UIa built-in gregorian calendar.
 */

public final class UICalendar extends WrapperView {
    public static final String[] WEEK = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    public static final String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar;
    private final UIButtonList header;
    private final Cell[] cells = new Cell[38];
    private final Font font;
    private final Paint[] paintCell = {
            new Paint().setColor(Theme.TRANSPARENT),
            new Paint().setColor(ThemeDarcula.LIGHT_GRAY),
            new Paint().setColor(ThemeDarcula.BLUE)
    };

    private int days;
    private int offset;
    private final int[] currentDate = {1, 1, 2024};

    public UICalendar(View view) {
        super(new ComponentGroup(view));
        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), Geometries.STD_ROUND), true);
        getPaint().setColor(ThemeDarcula.DARK_GRAY);

        calendar = GregorianCalendar.getInstance();

        font = Font.createDesktopFont(Font.Style.ITALIC);

        header = createHeader(font);
        header.getViewRight().registerCallback((OnClick) touches -> setDate(
                currentDate[0],
                currentDate[1] + 1,
                currentDate[2])
        );
        header.getViewLeft().registerCallback((OnClick) touches -> setDate(
                currentDate[0],
                currentDate[1] - 1,
                currentDate[2])
        );

        for (int i = 0; i < 7; i++) {
            cells[i] = Cell.createWeekDay(WEEK[i]);
            cells[i].getTextPaint().setColor(Theme.BLUE);
        }

        for (int i = 0; i < 31; i++) {
            Cell cell = Cell.createDay(String.valueOf(i + 1));
            cell.registerCallback((OnClick) touches -> {
                Paint paint = cell.getPaint();
                if (paint.equals(paintCell[2])) {
                    paint.set(paintCell[1]);
                } else {
                    paint.set(paintCell[2]);
                    int day = getDate()[0];
                    notifyCallbacks(OnSelect.class, day);
                }
            });
            cell.registerCallback((OnMouseEnter) touches -> {
                Paint paint = cell.getPaint();
                if (!paint.equals(paintCell[2])) paint.set(paintCell[1]);
            });
            cell.registerCallback((OnMouseExit) touches -> {
                Paint paint = cell.getPaint();
                if (!paint.equals(paintCell[2])) paint.set(paintCell[0]);
            });
            cells[i + 7] = cell;
        }

        for (Cell i : cells) {
            i.setFont(font);
            i.getPaint().set(paintCell[0]);
        }

        int[] currentDate = CalendarUtility.getDate();
        setDate(currentDate[0], currentDate[1], currentDate[2]);

        ViewGroup group = getView();
        ViewGroup.insert(group, header);
        ViewGroup.insert(group, cells);
    }

    /**
     * Callback invoked when a day is selected.
     * <br>
     * It provides the selected day.
     */

    public interface OnSelect extends Callback<Integer> {
    }

    /**
     * Callback invoked when a change in calendar month or year is detected.
     * <br>
     * It provides the date as an array made of: day, month and year.
     */

    public interface OnChange extends Callback<Integer[]> {
    }

    /**
     * Sets the calendar date.
     *
     * @param day   the day of the month between [1, 31]
     * @param month the month between [1, 12]
     * @param year  the year
     */

    public void setDate(int day, int month, int year) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }

        calendar.clear();
        calendar.set(year, month - 1, 1);

        // update data
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        offset = CalendarUtility.getDay(calendar.get(Calendar.DAY_OF_WEEK));

        // update selection
        int prevCurrentDay = currentDate[0];
        if (prevCurrentDay > days && cells[7 + prevCurrentDay - 1].selected) {
            cells[7 + prevCurrentDay - 1].selected = false;
        }

        // update current date
        currentDate[0] = MathUtility.constrain(day, 1, days);
        currentDate[1] = month;
        currentDate[2] = year;

        // update month
        header.setText(MONTHS[month - 1] + " " + year);

        notifyCallbacks(OnChange.class, getDate());
    }

    /**
     * Sets the name of the week days.
     * <br>
     * Note that the first day is monday and the last is sunday.
     *
     * @param days the days of the week
     * @throws NullPointerException if {@code days == null}
     */

    public void setWeek(String... days) {
        Objects.requireNonNull(days);

        for (int i = 0; i < days.length; i++) {
            cells[i].setText(days[i]);
        }
    }

    /**
     * Adjusts the font size according to the View dimension.
     */
    private void updateFontSize(float width, float height) {
        float fontSize = Math.min(
                Math.min(width * getWidth(), height * getHeight()),
                Font.DESKTOP_SIZE
        );
        if (fontSize != font.getSize()) {
            font.setSize(fontSize);
        }
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (isVisible()) {
            float[] cellDim = {0.7f / 6f, 0.08f};

            float weekCellPosY = 1f / 3f;
            for (int i = 0; i < 7; i++) {
                cells[i].setPosition(0.15f + cellDim[0] * i, weekCellPosY);
            }

            float dayCellPosY = weekCellPosY + cellDim[0];
            float gap = (0.95f - dayCellPosY) / 5f;
            for (int i = 0; i < 31; i++) {
                Cell cell = cells[7 + i];
                cell.setPosition(
                        0.15f + cellDim[0] * ((i + offset) % 7),
                        dayCellPosY + gap * ((i + offset) / 7)
                );
                cell.setVisible(i < days);
            }

            for (Cell cell : cells) {
                cell.setDimension(cellDim[0], cellDim[1]);
                cell.update(this);
            }

            updateFontSize(cellDim[0], cellDim[1]);
        }
    }

    /**
     * @return the calendar text {@link Font}
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the {@link Paint} object used to color days
     */

    public Paint getPaintDay() {
        return paintCell[0];
    }

    /**
     * @return the {@link Paint} object used to color a cursor covered day
     */

    public Paint getPaintHover() {
        return paintCell[1];
    }

    /**
     * @return the {@link Paint} object used to highlight a selected day
     */

    public Paint getHighlightPaint() {
        return paintCell[2];
    }

    /**
     * @return the current calendar date, as new array, made of: day, month and year
     */

    public int[] getDate() {
        return new int[]{
                currentDate[0],
                currentDate[1],
                currentDate[2]
        };
    }

    /**
     * @return the selected days
     */

    public List<Integer> getSelectedDays() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            if (cells[7 + i].selected) {
                result.add(i + 1);
            }
        }
        return result;
    }

    /**
     * Creates the calendar header view.
     */

    private static UIButtonList createHeader(Font font) {
        UIButtonList view = new UIButtonList(new Component("HEADER", 0.5f, 0.15f, 0.9f, 0.2f));
        view.setConsumer(Consumer.SCREEN_TOUCH, false);
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
     * Cell is a generic item used to represent a day or a week day.
     */

    private static class Cell extends WrapperViewText {
        public boolean selected = false;

        public Cell(String id) {
            super(new ComponentText(
                    new Component("CALENDAR_CELL_" + id, 0f, 0f, 0f, 0f))
            );
            setConsumer(Consumer.SCREEN_TOUCH, false);
            setAlign(ViewText.AlignY.CENTER);
        }

        /**
         * Creates a new Cell used to represent a week day.
         */

        public static Cell createWeekDay(String weekDay) {
            Cell cell = new Cell(weekDay);
            cell.setText(weekDay);
            cell.getTextPaint().setColor(ThemeDarcula.BLUE);
            return cell;
        }

        /**
         * Creates a new Cell used to represent a day.
         */

        public static Cell createDay(String day) {
            Cell cell = new Cell(day);
            cell.setText(day);
            cell.getTextPaint().setColor(Theme.WHITE);
            return cell;
        }
    }
}
