package com.kollectivemobile.euki.model;

import android.text.format.DateUtils;

import com.kollectivemobile.euki.model.database.entity.CalendarItem;

import java.util.Date;

public class CycleDayItem {
    private Date mDate;
    private Integer mDayCycle;
    private Date mDateNextCycle;
    private CalendarItem mCalendarItem;

    public CycleDayItem(Date date, Integer dayCycle, Date dateNextCycle, CalendarItem calendarItem) {
        mDate = date;
        mDayCycle = dayCycle;
        mDateNextCycle = dateNextCycle;
        mCalendarItem = calendarItem;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Integer getDayCycle() {
        return mDayCycle;
    }

    public void setDayCycle(Integer dayCycle) {
        mDayCycle = dayCycle;
    }

    public Date getDateNextCycle() {
        return mDateNextCycle;
    }

    public void setDateNextCycle(Date dateNextCycle) {
        mDateNextCycle = dateNextCycle;
    }

    public CalendarItem getCalendarItem() {
        return mCalendarItem;
    }

    public void setCalendarItem(CalendarItem calendarItem) {
        mCalendarItem = calendarItem;
    }

    public Boolean isToday() {
        if (mDate == null) {
            return false;
        }
        return DateUtils.isToday(mDate.getTime());
    }
}
