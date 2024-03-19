package uia.application.calendar;

import test.__tests__.utility.TestUtility;
import uia.application.UIButtonList;
import uia.core.paint.Color;
import uia.core.paint.Paint;
import uia.core.basement.Drawable;
import uia.core.ui.ViewGroup;
import uia.core.ui.context.Context;
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
import uia.core.ui.callbacks.OnClick;
import uia.utility.CalendarUtility;
import uia.utility.MathUtility;
import uia.utility.Geometries;

import java.util.*;

/**
 * Standard UIa gregorian calendar.
 */

public final class UICalendar extends WrapperView {

    public enum SelectionType {SINGLE, RANGE}

    public static final String[] WEEK = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    public static final String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar;
    private final UIButtonList header;
    private final Cell[] cells = new Cell[38];
    private final View overlayCell;
    private final Font font;
    private final Paint[] paintCell = {
            new Paint().setColor(Theme.TRANSPARENT),
            new Paint().setColor(ThemeDarcula.BLUE)
    };
    private SelectionType selection = SelectionType.SINGLE;

    private int days;
    private int offset;
    private final int[] currentDate = {1, 1, 2024};

    public UICalendar(View view) {
        super(new ComponentGroup(view));
        setGeometry(g -> Drawable.buildRect(g, getWidth(), getHeight(), Geometries.STD_ROUND), true);
        getPaint().setColor(ThemeDarcula.DARK_GRAY);

        calendar = GregorianCalendar.getInstance();

        font = Font.createDesktopFont(Font.Style.ITALIC);

        header = createHeader(getID(), font);
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

        overlayCell = new Component("CALENDAR_OVERLAY_" + getID(), 0f, 0f, 0f, 0f);
        overlayCell.setConsumer(Consumer.SCREEN_TOUCH, false);
        overlayCell.setVisible(false);
        overlayCell.setGeometry(
                g -> Drawable.buildRect(g, overlayCell.getWidth(), overlayCell.getHeight(), 1f),
                true
        );
        overlayCell.getPaint().setColor(
                Color.createColor(150, 150, 150, 100)
        );

        for (int i = 0; i < 7; i++) {
            cells[i] = Cell.createWeekDay(WEEK[i]);
            cells[i].getTextPaint().setColor(Theme.BLUE);
        }

        for (int i = 0; i < 31; i++) {
            Cell cell = Cell.createDay(String.valueOf(i + 1));
            cell.registerCallback((OnClick) touches -> {
                int day = Integer.parseInt(cell.getText()) - 1;
                updateDaySelection(day);
                updateDayCellsHighlight();
            });
            cells[i + 7] = cell;
        }

        for (Cell cell : cells) {
            cell.getPaint().set(paintCell[0]);
            cell.setFont(font);
        }

        ViewGroup group = getView();
        ViewGroup.insert(group, cells);
        ViewGroup.insert(group, header, overlayCell);

        // sets the current date
        int[] currentDate = CalendarUtility.getDate();
        setDate(currentDate[0], currentDate[1], currentDate[2]);
    }

    /**
     * Helper function. Updates the day cells selection color.
     */

    private void updateDayCellsHighlight() {
        for (int j = 0; j < 31; j++) {
            Cell currentCell = cells[7 + j];
            Paint cellPaint = currentCell.getPaint();
            if (currentCell.selected) {
                cellPaint.set(paintCell[1]);
            } else {
                cellPaint.set(paintCell[0]);
            }
        }
    }

    private final int[] range = {-1, -1};

    /**
     * Selects a range of days.
     *
     * @param day the selected day; with -1 the range is cleared
     */

    private void dayRangeSelection(int day) {
        // updates range selection
        if (range[0] == -1) {
            range[0] = day;
        } else if (range[1] == -1) {
            range[1] = day;
        } else {
            range[0] = day;
            range[1] = -1;
        }

        // clears the selection range
        for (int i = 0; i < 31; i++) {
            cells[7 + i].selected = false;
        }

        // update cell selection
        int minValue = Math.min(range[0], range[1]);
        int maxValue = Math.max(range[0], range[1]);
        for (int j = 0; j < 31; j++) {
            Cell currentCell = cells[7 + j];
            currentCell.selected = j == day || range[1] >= 0 && j >= minValue && j <= maxValue;
            currentCell.setGeometry(Geometries::rect, false);
        }

        // update cells geometry
        if (range[1] != -1) {
            Cell startCell = cells[7 + minValue];
            startCell.setGeometry(
                    g -> Geometries.rect(g, Geometries.STD_VERT, 1f, 0f, 0f, 1f, startCell.getWidth() / startCell.getHeight()),
                    true);

            Cell endCell = cells[7 + maxValue];
            endCell.setGeometry(
                    g -> Geometries.rect(g, Geometries.STD_VERT, 0f, 1f, 1f, 0f, endCell.getWidth() / endCell.getHeight()),
                    true);

            if (range[0] == range[1]) {
                startCell.setGeometry(
                        g -> Geometries.rect(g, Geometries.STD_VERT, 1f, startCell.getWidth() / startCell.getHeight()),
                        true);
            }
        }
    }

    /**
     * Selects one day and deselects all others.
     *
     * @param day the day to select between [0, 30]
     */

    private void selectSingleDay(int day) {
        range[0] = range[1] = day;

        for (int j = 0; j < 31; j++) {
            Cell currentCell = cells[7 + j];
            currentCell.setGeometry(Geometries::rect, false);
            currentCell.selected = false;
        }

        Cell selectedCell = cells[7 + day];
        selectedCell.selected = true;
        selectedCell.setGeometry(
                g -> Geometries.rect(g, Geometries.STD_VERT, 1f, selectedCell.getWidth() / selectedCell.getHeight()),
                true);
    }

    /**
     * Helper function. Updates the day selection.
     *
     * @param day the selected day
     */

    private void updateDaySelection(int day) {
        if (selection.equals(SelectionType.SINGLE)) {
            selectSingleDay(day);
        } else {
            dayRangeSelection(day);
        }

        int[] rangeCopy = {range[0], range[1]};
        notifyCallbacks(OnDaySelect.class, rangeCopy);
    }

    /**
     * Helper function. Marks the current date cell.
     *
     * @param day the day between [1, 31]
     */

    private void markCurrentDateCell(int day) {
        for (Cell cell : cells) {
            cell.current = false;
        }
        // update current cell
        cells[7 + day - 1].current = true;
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

        markCurrentDateCell(currentDate[0]);

        notifyCallbacks(OnDateChange.class, getDate());
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
     * Sets the day selection interaction, between single or range.
     *
     * @param selectionType the selectionType
     * @throws NullPointerException if {@code selectionType == null}
     */

    public void setSelectionType(SelectionType selectionType) {
        Objects.requireNonNull(selectionType);
        selection = selectionType;

        // updates day selection only when a day has been selected
        if (range[0] > 0) {
            setDaySelectionInterval(range[0] + 1, range[1] + 1);
        }
    }

    /**
     * Sets the selected days.
     * <br>
     * If {@link SelectionType#RANGE} is set, as a result,
     * all the days in the specified range will be selected.
     * <br>
     * If {@link SelectionType#SINGLE} is set, as a result, the 'startDay' is selected.
     *
     * @param startDay the first day, between [1, days], to be selected
     * @param stopDay  the last day, between [1, days], to be selected
     * @throws IndexOutOfBoundsException if:
     *                                   <ul>
     *                                       <li>startDay < 0</li>
     *                                       <li>startDay >= days</li>
     *                                       <li>stopDay < 0</li>
     *                                       <li>stopDay >= days</li>
     *                                   </ul>
     */

    public void setDaySelectionInterval(int startDay, int stopDay) {
        if (startDay != 0 && (startDay < 1 || startDay > days)) {
            throw new IndexOutOfBoundsException("startDay day is out of range!");
        }
        if (stopDay != 0 && (stopDay < 1 || stopDay > days)) {
            throw new IndexOutOfBoundsException("stopDay day is out of range!");
        }

        if (selection.equals(SelectionType.SINGLE)) {
            updateDaySelection(startDay - 1);
        } else {
            if (startDay > 0) {
                updateDaySelection(startDay - 1);
            }
            if (stopDay > 0) {
                updateDaySelection(stopDay - 1);
            }
            if (startDay == 0 && stopDay == 0) {
                updateDaySelection(-1);
            }
        }
        updateDayCellsHighlight();
    }

    /**
     * Helper function. Adjusts the font size according to the calendar View dimension.
     */

    private void updateFontSize(float width, float height) {
        int fontSize = (int) Math.min(
                Math.min(width * getWidth(), height * getHeight()),
                Font.DESKTOP_SIZE
        );
        if (fontSize != (int) font.getSize()) {
            font.setSize(fontSize);
        }
    }

    /**
     * Helper function. Updates the day cells.
     */

    private void updateDayCells(float posY, float[] cellDim) {
        int inactiveCells = 0;
        float gap = (0.95f - posY) / 5f;
        for (int i = 0; i < 31; i++) {
            float px = 0.15f + cellDim[0] * ((i + offset) % 7);
            float py = posY + gap * ((i + offset) / 7);

            Cell cell = cells[7 + i];
            cell.setPosition(px, py);
            cell.setVisible(i < days);

            if (cell.active) {
                overlayCell.setPosition(px, py);
                overlayCell.setVisible(true);
                overlayCell.update(this);
            } else {
                inactiveCells++;
            }
        }

        if (inactiveCells == 31) {
            overlayCell.setVisible(false);
        }
    }

    @Override
    public void update(View container) {
        super.update(container);

        if (isVisible()) {
            float[] cellDim = {0.7f / 6f, 0.08f};

            updateFontSize(cellDim[0], cellDim[1]);

            overlayCell.setDimension(cellDim[0], cellDim[1]);

            float weekCellPosY = 1f / 3f;
            for (int i = 0; i < 7; i++) {
                cells[i].setPosition(0.15f + cellDim[0] * i, weekCellPosY);
            }

            float dayCellPosY = weekCellPosY + cellDim[0];
            updateDayCells(dayCellPosY, cellDim);

            for (Cell cell : cells) {
                cell.setDimension(cellDim[0], cellDim[1]);
                cell.update(this);
            }
        }
    }

    /**
     * @return the calendar {@link Font}
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
     * @return the {@link Paint} object used to highlight a selected day
     */

    public Paint getHighlightPaint() {
        return paintCell[1];
    }

    /**
     * @return the {@link Paint} object used to color a cursor covered day
     */

    public Paint getPaintHover() {
        return overlayCell.getPaint();
    }

    /**
     * @return the current calendar date, as new array, made of: day, month and year
     */

    public int[] getDate() {
        return new int[]{currentDate[0], currentDate[1], currentDate[2]};
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
     * Helper function. Creates the calendar header.
     */

    private static UIButtonList createHeader(String id, Font font) {
        UIButtonList result = new UIButtonList(
                new Component("CALENDAR_HEADER_" + id, 0.5f, 0.15f, 0.9f, 0.2f)
        );
        result.setConsumer(Consumer.SCREEN_TOUCH, false);
        result.getPaint().setColor(Theme.TRANSPARENT);

        ViewText text = result.getViewText();
        text.getTextPaint().setColor(Theme.WHITE);
        text.setPosition(0.35f, 0.5f);
        text.setFont(font);

        View right = result.getViewRight();
        right.setDimension(0.05f, 0.4f);
        right.getPaint().setColor(Theme.WHITE);

        View left = result.getViewLeft();
        left.setDimension(0.05f, 0.4f);
        left.setPosition(0.7f, 0.5f);
        left.getPaint().setColor(Theme.WHITE);

        return result;
    }

    /**
     * Cell is a generic item used to represent a day or a week day.
     */

    private static class Cell extends WrapperViewText {
        public boolean selected = false;
        public boolean current = false;
        public boolean active = false;

        public Cell(String id) {
            super(new ComponentText(
                    new Component("CALENDAR_CELL_" + id, 0f, 0f, 0f, 0f))
            );
            registerCallback((OnMouseEnter) touches -> active = true);
            registerCallback((OnMouseExit) touches -> active = false);
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

    public static void main(String[] args) {
        UICalendar calendar = new UICalendar(
                new Component("CALENDAR", 0.5f, 0.5f, 0.5f, 0.5f)
        );
        calendar.setSelectionType(SelectionType.RANGE);
        calendar.setDaySelectionInterval(10, 20);
        //calendar.setSelectionType(SelectionType.SINGLE);
        //calendar.setDaySelectionInterval(0, 0);

        Context context = TestUtility.createMockContext();
        context.setView(calendar);
    }
}
