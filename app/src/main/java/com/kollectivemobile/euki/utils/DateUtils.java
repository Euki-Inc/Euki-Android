package com.kollectivemobile.euki.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static final String DateLongFormat = "MMMM dd, yyyy";
    public static final String DateTimeLongFormat = "yyyyMMddhhmmss";
    public static final String TimeFormat = "h:mma";
    public static final String CalendarFormat = "MMMM yyyy";
    public static final String todayMMMMdd = "'Today', MMMM dd";
    public static final String eeeMMMMdd = "EEE, MMMM dd";
    public static final String eeeMMMdd = "EEE, MMM dd";
    public static final String eeeMMMdyyyyhmma = "EEE, MMM d, yyyy h:mm a";
    public static final String eee = "EEE";
    public static final String eeedd = "EEE dd";
    public static final String MMMdd = "MMM dd";
    public static final DateFormat mDateFormatDateLong = new SimpleDateFormat(DateLongFormat);
    public static final DateFormat mDateFormatDateTimeLongFormat = new SimpleDateFormat(DateTimeLongFormat);
    public static final DateFormat mDateFormatTimeFormat = new SimpleDateFormat(TimeFormat);
    public static final DateFormat mDateFormatCalendarFormat = new SimpleDateFormat(CalendarFormat);
    public static final DateFormat mDateFormateeeMMMMdd = new SimpleDateFormat(eeeMMMMdd);
    public static final DateFormat mDateFormateeeMMMdd = new SimpleDateFormat(eeeMMMdd);
    public static final DateFormat mDateFormatTodayMMMMdd = new SimpleDateFormat(todayMMMMdd);
    public static final DateFormat mDateFormateeeMMMdyyyyhmma = new SimpleDateFormat(eeeMMMdyyyyhmma);
    public static final DateFormat mDateFormateee = new SimpleDateFormat(eee);
    public static final DateFormat mDateFormateeedd = new SimpleDateFormat(eeedd);
    public static final DateFormat mDateFormatMMMdd = new SimpleDateFormat(MMMdd);

    public static Boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }

    public static Long daysDiff(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar1.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
        calendar1.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        calendar1.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH));
        date2 = calendar1.getTime();

        long msDiff = date2.getTime() - date1.getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        return daysDiff;
    }

    public static Long weeksDiff(Date date1, Date date2) {
        long msDiff = date2.getTime() - date1.getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        return daysDiff / 7;
    }

    public static Integer numDaysMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Boolean isFuture(Date date) {
        return (date).after(new Date());
    }

    public static String toString(Date date, String format) {
        DateFormat dateFormat = null;
        switch (format) {
            case DateLongFormat:
                dateFormat = mDateFormatDateLong;
                break;
            case DateTimeLongFormat:
                dateFormat = mDateFormatDateTimeLongFormat;
                break;
            case TimeFormat:
                dateFormat = mDateFormatTimeFormat;
                break;
            case CalendarFormat:
                dateFormat = mDateFormatCalendarFormat;
                break;
            case todayMMMMdd:
                dateFormat = mDateFormatTodayMMMMdd;
                break;
            case eeeMMMMdd:
                dateFormat = mDateFormateeeMMMMdd;
                break;
            case eeeMMMdd:
                dateFormat = mDateFormateeeMMMdd;
                break;
            case eeeMMMdyyyyhmma:
                dateFormat = mDateFormateeeMMMdyyyyhmma;
                break;
            case eee:
                dateFormat = mDateFormateee;
                break;
            case eeedd:
                dateFormat = mDateFormateeedd;
                break;
            case MMMdd:
                dateFormat = mDateFormatMMMdd;
                break;
        }

        if (dateFormat != null) {
            return dateFormat.format(date);
        }

        return null;
    }

    public static Date toDate(String string, String format) {
        DateFormat dateFormat = null;
        switch (format) {
            case DateLongFormat:
                dateFormat = mDateFormatDateLong;
                break;
            case DateTimeLongFormat:
                dateFormat = mDateFormatDateTimeLongFormat;
                break;
            case TimeFormat:
                dateFormat = mDateFormatTimeFormat;
                break;
            case CalendarFormat:
                dateFormat = mDateFormatCalendarFormat;
                break;
            case todayMMMMdd:
                dateFormat = mDateFormatTodayMMMMdd;
                break;
            case eeeMMMMdd:
                dateFormat = mDateFormateeeMMMMdd;
                break;
            case eeeMMMdd:
                dateFormat = mDateFormateeeMMMdd;
                break;
            case eeeMMMdyyyyhmma:
                dateFormat = mDateFormateeeMMMdyyyyhmma;
                break;
            case eee:
                dateFormat = mDateFormateee;
                break;
            case eeedd:
                dateFormat = mDateFormateeedd;
                break;
            case MMMdd:
                dateFormat = mDateFormatMMMdd;
                break;
        }

        if (dateFormat != null) {
            try {
                return dateFormat.parse(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Integer getDaysCount(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static Integer getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Integer getDayOfWeek(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Boolean isTodayMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month);
    }

    public static Date startDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
