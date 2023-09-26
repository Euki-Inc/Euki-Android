package com.kollectivemobile.euki.ui.calendar.weeklycalendar;

import android.util.Pair;

import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DayItem {
    private Integer mIndex;
    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;
    private Date mDate;
    private Integer mDayOfWeek;
    private String mDayName;

    public DayItem() {
        mIndex = -1;
        mYear = 0;
        mMonth = 0;
        mDay = 0;
        mDate = new Date();
        mDayOfWeek = 0;
        mDayName = "";
    }

    public DayItem(Integer index, Integer year, Integer month, Integer day) {
        this();
        mIndex = index;
        mYear = year;
        mMonth = month;
        mDay = day;

        Calendar cal = new GregorianCalendar(year, month, day);
        mDate = cal.getTime();
        mDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        mDayName = StringUtils.capitalize(DateUtils.toString(mDate, DateUtils.eee));
    }

    public static Pair<List<DayItem>, Pair<DayItem, DayItem>> getItems(Date startDate) {
        List<DayItem> items = new ArrayList<>();
        DayItem todayItem = null;
        DayItem startItem = null;

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            calendar.add(Calendar.HOUR, 24*3);
        }

        calendar.set(Calendar.DAY_OF_WEEK, 7);
        Date endDate = calendar.getTime();
        Date today = new Date();

        Boolean shouldStop = false;

        for (int year = 2012; year <= 2030; year++) {
            if (shouldStop) {
                break;
            }
            for (int month = 0; month <= 11; month++) {
                if (shouldStop) {
                    break;
                }

                Integer numdays = DateUtils.numDaysMonth(year, month);
                for (int day = 1; day <= numdays; day++) {
                    if (shouldStop) {
                        break;
                    }

                    DayItem item = new DayItem(items.size(), year, month, day);

                    if (DateUtils.isSameDate(item.getDate(), endDate)) {
                        shouldStop = true;
                    }

                    items.add(item);

                    if (DateUtils.isSameDate(today, item.getDate())) {
                        todayItem = item;
                    }

                    if (startDate != null && DateUtils.isSameDate(startDate, item.getDate())) {
                        startItem = item;
                    }
                }
            }
        }

        return new Pair<>(items, new Pair<>(todayItem, startItem));
    }

    public Integer getIndex() {
        return mIndex;
    }

    public void setIndex(Integer index) {
        mIndex = index;
    }

    public Integer getYear() {
        return mYear;
    }

    public void setYear(Integer year) {
        mYear = year;
    }

    public Integer getMonth() {
        return mMonth;
    }

    public void setMonth(Integer month) {
        mMonth = month;
    }

    public Integer getDay() {
        return mDay;
    }

    public void setDay(Integer day) {
        mDay = day;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Integer getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }

    public String getDayName() {
        return mDayName;
    }

    public void setDayName(String dayName) {
        mDayName = dayName;
    }
}
