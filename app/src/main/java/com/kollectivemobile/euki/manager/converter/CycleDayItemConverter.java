package com.kollectivemobile.euki.manager.converter;

import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.utils.Constants;
import com.kollectivemobile.euki.utils.DateUtils;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CycleDayItemConverter {
    public static List<CycleDayItem> convert(Map<Date, CalendarItem> dict, List<Range<Date>> predictionRanges, Boolean trackPeriodEnabled) {
        Map<String, CalendarItem> dateDict = new HashMap<>();
        for (Date date : dict.keySet()) {
            dateDict.put(DateUtils.toString(date, DateUtils.DateLongFormat), dict.get(date));
        }

        Date nextPredictionDate = null;
        if (predictionRanges.size() > 0) {
            nextPredictionDate = predictionRanges.get(0).getMinimum();
        }
        Date today = DateUtils.startDate(new Date());

        List<CycleDayItem> items = new ArrayList<>();

        Date endDate = DateUtils.startDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.YEAR, -3);
        Date startDate = calendar.getTime();

        Date currentDate = startDate;

        Integer currentDayCycle = 0;
        Integer emptyDays = 0;

        while (currentDate.before(endDate) || DateUtils.isSameDate(currentDate, endDate)) {
            CalendarItem calendarItem = dateDict.get(DateUtils.toString(currentDate, DateUtils.DateLongFormat));

            Boolean hasPeriod = false;
            if (calendarItem != null) {
                hasPeriod = calendarItem.hasPeriod();
            }

            Boolean isNextPeriod = emptyDays >= Constants.sMinDaysBetweenPeriods;
            Boolean showNextPeriod = emptyDays >= Constants.sMinDaysToShowNextPeriod;

            if (hasPeriod) {
                emptyDays = 0;
            } else {
                emptyDays++;
            }

            if (hasPeriod && isNextPeriod) {
                currentDayCycle = 1;
            } else if (currentDayCycle > 0) {
                currentDayCycle++;
            }

            Date dateNextCycle;

            if (DateUtils.isSameDate(today, currentDate) && showNextPeriod) {
                dateNextCycle = nextPredictionDate;
            } else {
                dateNextCycle = null;
            }

            Integer dayCycle = (!trackPeriodEnabled || currentDayCycle == 0) ? null : currentDayCycle;
            CycleDayItem item = new CycleDayItem(currentDate, dayCycle, dateNextCycle, calendarItem);
            items.add(item);

            calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR, 24);

            currentDate = calendar.getTime();
        }

        return items;
    }
}
