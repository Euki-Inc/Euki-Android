package com.kollectivemobile.euki.model;

import android.util.Pair;

import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthItem {
    private Integer mYear;
    private Integer mMonth;
    private Integer mNumDays;
    private Integer mFirstDayWeek;
    private Integer mMinIndex;

    public MonthItem() {
        this.mYear = 0;
        this.mMonth = 0;
        this.mNumDays = 0;
        this.mFirstDayWeek = 0;
        this.mMinIndex = 0;
    }

    public MonthItem(Integer year, Integer month) {
        this();
        this.mYear = year;
        this.mMonth = month;

        mNumDays = DateUtils.getDaysCount(year, month);
        mFirstDayWeek = DateUtils.getDayOfWeek(year, month, 1);
    }

    public static Pair<List<MonthItem>, MonthItem> getItems() {
        List<MonthItem> items = new ArrayList<>();
        MonthItem todayItem = new MonthItem();
        for (int year = 1959; year <= 2050; year++) {
            for (int month = 1; month <= 12; month++) {
                MonthItem monthItem = new MonthItem(year, month);
                items.add(monthItem);

                if (DateUtils.isTodayMonth(year, month)) {
                    todayItem = monthItem;
                }
            }
        }
        return new Pair<>(items, todayItem);
    }

    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        return calendar.getTime();
    }

    public Date getDate(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public String getTitle() {
        String str = DateUtils.toString(getDate(), DateUtils.CalendarFormat);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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

    public Integer getNumDays() {
        return mNumDays;
    }

    public void setNumDays(Integer numDays) {
        mNumDays = numDays;
    }

    public Integer getFirstDayWeek() {
        return mFirstDayWeek;
    }

    public void setFirstDayWeek(Integer firstDayWeek) {
        mFirstDayWeek = firstDayWeek;
    }

    public Integer getMinIndex() {
        return mMinIndex;
    }

    public void setMinIndex(Integer minIndex) {
        mMinIndex = minIndex;
    }
}
