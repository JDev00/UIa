package uia.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * Date and Time representation
 */

public class Date {
    public static final int NULL = -1;

    private static final String REGEX1 = Pattern.quote("/");
    private static final String REGEX2 = Pattern.quote(":");

    private final int[] data;

    public Date(int day, int month, int year, int hour, int minute, int second) {
        data = new int[]{day, month, year, hour, minute, second};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date1 = (Date) o;
        return Arrays.equals(data, date1.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
        if (data[3] != NULL) {
            return getDate("-") + " at " + getTime(":");
        } else {
            return getDate("-");
        }
    }

    /**
     * @return the date's day
     */

    public int getDay() {
        return data[0];
    }

    /**
     * @return the date's month
     */

    public int getMonth() {
        return data[1];
    }

    /**
     * @return the date's year
     */

    public int getYear() {
        return data[2];
    }

    /**
     * @return the date's hour
     */

    public int getHour() {
        return data[3];
    }

    /**
     * @return the date's minute
     */

    public int getMinute() {
        return data[4];
    }

    /**
     * @return the date's second
     */

    public int getSecond() {
        return data[5];
    }

    private static String norm(int d) {
        return d < 10 ? ("0" + d) : String.valueOf(d);
    }

    /**
     * @param regex a not null String used to concatenate day, month and year
     * @return the formatted date
     */

    public String getDate(String regex) {
        return norm(data[0]) + regex + norm(data[1]) + regex + norm(data[2]);
    }

    /**
     * @param regex a not null String used to concatenate hour, minute and second
     * @return the formatted time
     */

    public String getTime(String regex) {
        return norm(data[3]) + regex + norm(data[4]) + regex + norm(data[5]);
    }

    /**
     * Compare this Date with a given one.
     * <br>
     * <b>This method only compares dates and not time.</b>
     *
     * @param date a not null {@link Date}
     * @return 0 if the Dates are equal; -1 if this Date is less than the given one;
     * 1 if this Date is greater than the given one.
     */

    public int compareDate(Date date) {
        int i = 2;
        int o = 0;
        while (i >= 0 && o == 0) {
            o = this.data[i] - date.data[i];
            i--;
        }
        return Integer.compare(o, 0);
    }

    /**
     * Compare this Date with a given one.
     * <br>
     * <b>This method only compares time.</b>
     *
     * @param date a not null {@link Date}
     * @return 0 if the times are equal; -1 if this time is less than the given one;
     * 1 if this time is greater than the given one.
     */

    public int compareTime(Date date) {
        int i = 3;
        int o = 0;
        while (i < data.length && o == 0) {
            o = this.data[i] - date.data[i];
            i++;
        }
        return Integer.compare(o, 0);
    }

    /**
     * Create a new Date instance
     *
     * @param day    the date day
     * @param month  the date month
     * @param year   the date year
     * @param hour   the date hour
     * @param minute the date minute
     * @param second the date second
     * @return a new Date instance
     */

    public static Date create(int day, int month, int year,
                              int hour, int minute, int second) {
        if (year > 0
                && month >= 1 && month <= 12
                && day >= 1 && day <= days(month, year)
                && (hour >= 0 && hour <= 23 || hour == NULL)
                && (minute >= 0 && minute <= 59 || minute == NULL)
                && (second >= 0 && second <= 59 || second == NULL))
            return new Date(day, month, year, hour, minute, second);

        return new Date(NULL, NULL, NULL, NULL, NULL, NULL);
    }

    /**
     * Create a new Date instance
     *
     * @param day   the date day
     * @param month the date month
     * @param year  the date year
     * @return a new Date instance if the given params are correct otherwise null
     */

    public static Date create(int day, int month, int year) {
        return create(day, month, year, NULL, NULL, NULL);
    }

    /**
     * Create a new Date instance
     *
     * @param date a not null textual date
     * @return a new Date instance if the given params are correct otherwise null
     */

    public static Date create(String date) {
        int[] toDate = toDate(date);
        if (toDate != null) return create(toDate[0], toDate[1], toDate[2], NULL, NULL, NULL);
        return null;
    }

    /**
     * Create a new Date instance
     *
     * @param date a not null textual date
     * @param time a not null textual time expressed as x:y:z
     * @return a new Date instance if the given params are correct otherwise null
     */

    public static Date create(String date, String time) {
        int[] toDate = toDate(date);
        int[] toTime = toTime(time);
        if (toDate != null && toTime != null) {
            return create(toDate[0], toDate[1], toDate[2], toTime[0], toTime[1], toTime[2]);
        }
        return null;
    }

    /**
     * @param month the date month between [1,12]
     * @param year  the date year
     * @return the number of days for the given month
     */

    public static int days(int month, int year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return (year % 4 == 0) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 0;
        }
    }

    /**
     * @param date a not null String formatted as follows: d/m/y
     * @return a new array filled with day, month and year or null
     */

    public static int[] toDate(String date) {
        if (date != null) {
            String[] split = date.split(REGEX1);

            if (split.length == 3
                    && Utils.isNumber(split[0])
                    && Utils.isNumber(split[1])
                    && Utils.isNumber(split[2])) {
                int day = parseInt(split[0]);
                int month = parseInt(split[1]);
                int year = parseInt(split[2]);

                if (year > 0
                        && month >= 1 && month <= 12
                        && day >= 1 && day <= days(month, year)) return new int[]{day, month, year};
            }
        }

        return null;
    }

    /**
     * @param time a not null String formatted as follows: h:m:s
     * @return a new array filled with hour, minute and second or null
     */

    public static int[] toTime(String time) {
        if (time != null) {
            String[] split = time.split(REGEX2);

            if (split.length == 3
                    && Utils.isNumber(split[0])
                    && Utils.isNumber(split[1])
                    && Utils.isNumber(split[2])) {
                int hour = parseInt(split[0]);
                int minute = parseInt(split[1]);
                int second = parseInt(split[2]);

                if (hour >= 0 && hour <= 23
                        && minute >= 0 && minute <= 59
                        && second >= 0 && second <= 59) return new int[]{hour, minute, second};
            }
        }

        return null;
    }
}
