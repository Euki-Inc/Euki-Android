package com.kollectivemobile.euki.ui.calendar.weeklycalendar;

import com.kollectivemobile.euki.model.database.entity.CalendarItem;

import java.util.Date;

public interface WeeklyCalendarListener {
    void selectedDate(CalendarItem calendarItem);
}
