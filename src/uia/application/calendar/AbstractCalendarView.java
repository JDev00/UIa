package uia.application.calendar;

import uia.application.UIButtonList;
import uia.core.Font;
import uia.core.basement.Drawable;
import uia.core.paint.Color;
import uia.core.paint.Paint;
import uia.core.shape.Geometry;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.physical.component.Component;
import uia.physical.component.WrapperView;
import uia.physical.group.ComponentGroup;
import uia.physical.theme.Theme;
import uia.physical.theme.ThemeDarcula;
import uia.utility.CalendarUtility;
import uia.utility.Geometries;
import uia.utility.MathUtility;

import java.util.*;

/**
 * Abstract representation of the Gregorian calendar.
 * It provides all the basement operations expect day selection.
 */

public abstract class AbstractCalendarView extends WrapperView implements CalendarView {
    private final Calendar calendar = GregorianCalendar.getInstance();

    private final CalendarCell[] cells = new CalendarCell[38];
    private final UIButtonList header;
    private final View overlayCell;
    private Color currentDayColor;
    private final String[] months;
    private final Font font;


    private int days;
    private int offset;
    private final int[] currentDate = {1, 1, 2024};

    protected AbstractCalendarView(View view, String[] weekdays, String[] months) {
        super(new ComponentGroup(view));
        getPaint().setColor(ThemeDarcula.DARK_GRAY);

        this.months = months;

        font = Font.createDesktopFont(Font.Style.ITALIC);

        currentDayColor = Theme.PINK;

        header = createHeader("CALENDAR_HEADER_" + getID(), font);
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
            cells[i] = CalendarCell.createWeekDay(weekdays[i]);
            cells[i].getPaint().setTextColor(Theme.SILVER);
        }

        for (int i = 0; i < 31; i++) {
            CalendarCell cell = CalendarCell.createDay(String.valueOf(i + 1));
            cell.getPaint().setTextColor(Theme.WHITE);
            cell.registerCallback((OnClick) touches -> {
                int day = Integer.parseInt(cell.getText());
                notifyCallbacks(OnDaySelect.class, day);
            });
            cells[i + 7] = cell;
        }

        for (CalendarCell cell : cells) {
            cell.getPaint().setColor(Theme.TRANSPARENT);
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
     * Helper function. Creates the calendar header.
     */

    private static UIButtonList createHeader(String id, Font font) {
        UIButtonList result = new UIButtonList(
                new Component(id, 0.5f, 0.15f, 0.75f, 0.2f)
        );
        result.setConsumer(Consumer.SCREEN_TOUCH, false);
        result.setGeometry(Geometries::rect, false);
        result.getPaint().setColor(Theme.TRANSPARENT);

        ViewText text = result.getViewText();
        text.getPaint().setTextColor(Theme.WHITE);
        text.setAlign(ViewText.AlignX.LEFT);
        text.setPosition(0.35f, 0.5f);
        text.setFont(font);

        View right = result.getViewRight();
        right.setDimension(0.05f, 0.4f);
        right.setPosition(0.965f, 0.5f);
        right.getPaint().setColor(Theme.WHITE);

        View left = result.getViewLeft();
        left.setDimension(0.05f, 0.4f);
        left.setPosition(0.75f, 0.5f);
        left.getPaint().setColor(Theme.WHITE);

        return result;
    }

    //

    /**
     * Marks the specified as selected.
     *
     * @param day      the day to be marked between [1, 31]
     * @param selected true to mark the day as selected
     * @throws NullPointerException if {@code day < 1 || day > 31}
     */

    protected void markDayAsSelected(int day, boolean selected) {
        if (day < 1 || day > 31) {
            throw new IndexOutOfBoundsException("the day must be between [1, 31]. You gave " + day);
        }
        cells[7 + day - 1].selected = selected;
    }

    /**
     * @return true if the specified day is marked as selected
     * @throws NullPointerException if {@code day < 1 || day > 31}
     */

    protected boolean isDayMarkedAsSelected(int day) {
        if (day < 1 || day > 31) {
            throw new IndexOutOfBoundsException("the day must be between [1, 31]. You gave " + day);
        }
        return cells[7 + day - 1].selected;
    }

    /**
     * Sets the day geometry.
     */

    protected void setDayCellGeometry(int day,
                                      java.util.function.Consumer<Geometry> builder, boolean inTimeBuilding) {
        cells[7 + day - 1].setGeometry(builder, inTimeBuilding);
    }

    /**
     * @return the Paint associated to the specified day
     */

    protected Paint getDayCellPaint(int day) {
        return cells[7 + day - 1].getPaint();
    }

    protected float getDayCelWidth() {
        CalendarCell cell = cells[7];
        return cell.getWidth();
    }

    protected float getDayCelHeight() {
        CalendarCell cell = cells[7];
        return cell.getHeight();
    }

    //

    /**
     * @return the calendar {@link Font}
     */

    public Font getFont() {
        return font;
    }

    /**
     * Sets the color used to paint the current day.
     *
     * @param color the current day color
     * @throws NullPointerException if {@code color == null}
     */

    public void setCurrentDayColor(Color color) {
        Objects.requireNonNull(color);
        currentDayColor = color;
    }

    /**
     * Helper function. Adjusts the font size according to the calendar View dimension.
     */

    private void updateFontSize(float width, float height) {
        int fontSize = (int) Math.min(
                Math.min(0.75f * width * getWidth(), 0.75f * height * getHeight()),
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

            CalendarCell cell = cells[7 + i];
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
            // highlights the current day
            cells[7 + currentDate[0] - 1].getPaint().setTextColor(currentDayColor);

            float[] cellDim = {0.7f / 6f, 0.08f};

            updateFontSize(cellDim[0], cellDim[1]);

            overlayCell.setDimension(cellDim[0], cellDim[1]);

            float weekCellPosY = 1f / 3f;
            for (int i = 0; i < 7; i++) {
                cells[i].setPosition(0.15f + cellDim[0] * i, weekCellPosY);
            }

            float dayCellPosY = weekCellPosY + cellDim[0];
            updateDayCells(dayCellPosY, cellDim);

            for (CalendarCell cell : cells) {
                cell.setDimension(cellDim[0], cellDim[1]);
                cell.update(this);
            }
        }
    }

    /**
     * Helper function. Marks the current date cell.
     *
     * @param day the day between [1, 31]
     */

    private void markCurrentDateCell(int day) {
        for (CalendarCell cell : cells) {
            cell.current = false;
        }
        // update current cell
        cells[7 + day - 1].current = true;
    }

    @Override
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
        header.setText(months[month - 1] + " " + year);

        markCurrentDateCell(currentDate[0]);

        notifyCallbacks(OnDateChange.class, getDate());
    }

    @Override
    public int[] getDate() {
        return new int[]{currentDate[0], currentDate[1], currentDate[2]};
    }

    @Override
    public List<Integer> getSelectedDays() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            if (cells[7 + i].selected) {
                result.add(i + 1);
            }
        }
        return result;
    }
}
