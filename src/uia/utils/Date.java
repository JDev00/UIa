package uia.utils;

import com.sun.istack.internal.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * Object used to represent a date
 */

public final class Date {
    public static final int NULL = -1;

    private static final String REGEX1 = Pattern.quote("/");
    private static final String REGEX2 = Pattern.quote(":");

    private final int day;
    private final int month;
    private final int year;

    private final int hour;
    private final int minute;
    private final int second;

    private Date(int day, int month, int year,
                 int hour, int minute, int second) {
        this.day = day;
        this.month = month;
        this.year = year;

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year
                && hour == date.hour && minute == date.minute && second == date.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year, hour, minute, second);
    }

    private static String norm(int d) {
        return d < 10 ? ("0" + d) : String.valueOf(d);
    }

    @Override
    public String toString() {
        if (hour != NULL) {
            return getDate("-") + " at " + getTime();
        } else {
            return getDate("-");
        }
    }

    /**
     * @param regex a String used to concatenate day, month and year
     * @return the date
     */

    public String getDate(String regex) {
        return norm(day) + regex + norm(month) + regex + norm(year);
    }

    /**
     * @return the time
     */

    public String getTime() {
        return norm(hour) + ":" + norm(minute) + ":" + norm(second);
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
     * @return a new Date instance if the given params are correct otherwise null
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

        return null;
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

    public static Date create(@NotNull String date) {
        String[] split = date.split(REGEX1);
        return create(parseInt(split[0]), parseInt(split[1]), parseInt(split[2]));
    }

    /**
     * Create a new Date instance
     *
     * @param date a non-null textual date
     * @param time a non-null textual time expressed as x:y:z
     * @return a new Date instance if the given params are correct otherwise null
     */

    public static Date create(@NotNull String date, @NotNull String time) {
        String[] d = date.split(REGEX1);
        String[] t = time.split(REGEX2);
        return create(
                parseInt(d[0]), parseInt(d[1]), parseInt(d[2]),
                parseInt(t[0]), parseInt(t[1]), parseInt(t[2]));
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
     * @return true if the given String is a date
     */

    public static boolean validateDate(String date) {
        if (date != null) {
            String[] split = date.split(REGEX1);

            if (split.length == 3
                    && Utils.isNumber(split[0])
                    && Utils.isNumber(split[1])
                    && Utils.isNumber(split[2])) {
                int day = parseInt(split[0]);
                int month = parseInt(split[1]);
                int year = parseInt(split[2]);

                return year > 0
                        && month >= 1 && month <= 12
                        && day >= 1 && day <= days(month, year);
            }
        }

        return false;
    }

    /**
     * @return true if the given String is a time of the type x:y:z
     */

    public static boolean validateTime(String date) {
        if (date != null) {
            String[] split = date.split(REGEX2);

            if (split.length == 3
                    && Utils.isNumber(split[0])
                    && Utils.isNumber(split[1])
                    && Utils.isNumber(split[2])) {
                int hour = parseInt(split[0]);
                int minute = parseInt(split[1]);
                int second = parseInt(split[2]);

                return hour >= 0 && hour <= 23
                        && minute >= 0 && minute <= 59
                        && second >= 0 && second <= 59;
            }
        }

        return false;
    }
}
