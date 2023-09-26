package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;

import org.apache.commons.lang3.Range;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CalendarManager {
    CalendarFilter getCalendarFilter();
    void updateCalendarFilter(CalendarFilter calendarFilter);
    void todayCalendarItem(EukiCallback<CalendarItem> callback);
    void getCalendarItem(Date date, EukiCallback<CalendarItem> callback);
    CalendarItem getCalendarItem(Date date);
    void getCalendarItems(EukiCallback<Map<String, CalendarItem>> callback);
    void getDayscalendarItems(EukiCallback<List<CalendarItem>> callback);
    void save(Appointment appointment, EukiCallback<Boolean> callback);
    void saveItem(CalendarItem calendarItem, EukiCallback<Boolean> callback);
    void getPendingNotify(EukiCallback<Appointment> callback);
    Boolean shouldShowIncludeCycleAlert(Date date);
    void updateLatestBleedingTracking(Date date);
    void getPredictionRange(EukiCallback<List<Range<Date>>> callback);
    void removeAll();
}
