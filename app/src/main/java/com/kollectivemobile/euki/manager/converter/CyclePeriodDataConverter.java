package com.kollectivemobile.euki.manager.converter;

import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.Range;

public class CyclePeriodDataConverter {
    public static CyclePeriodData convert(List<CalendarItem> calendarItems, List<Range<Date>> hiddenPeriods) {
        if (hiddenPeriods == null) {
            hiddenPeriods = new ArrayList<>();
        }

        List<CalendarItem> items = new ArrayList<>();
        for (CalendarItem item : calendarItems) {
            if (item.getIncludeCycleSummary()) {
                items.add(item);
            }
        }

        List<CyclePeriodItem> periodItems = new ArrayList<>();

        Date currentStartDate = DateUtils.startDate(items.size() > 0 ? items.get(0).getDate() : new Date());
        Integer currentDuration = 0;

        for (CalendarItem calendarItem : items) {
            Integer daysDiff = DateUtils.daysDiff(currentStartDate, calendarItem.getDate()).intValue();

            if (daysDiff > 14) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentStartDate);
                calendar.add(Calendar.HOUR, 24 * (daysDiff - 1));
                Date endDate = calendar.getTime();

                CyclePeriodItem periodItem = new CyclePeriodItem(currentStartDate, endDate, currentDuration + 1);
                periodItems.add(periodItem);

                currentStartDate = calendarItem.getDate();
                currentDuration = 0;
            } else {
                currentDuration = daysDiff;
            }
        }

        // Remove hidden periods

        List<CyclePeriodItem> filter = new ArrayList<>();
        for (CyclePeriodItem item : periodItems) {
            Range<Date> itemRange = Range.between(item.getInitialDate(), item.getEndDate());
            Boolean overlaps = false;

            for (Range<Date> hiddenPeriod : hiddenPeriods) {
                if (hiddenPeriod.isOverlappedBy(itemRange)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                filter.add(item);
            }
        }
        periodItems = filter;

        // Average Cycle Length calculation

        List<CyclePeriodItem> averageItems = new ArrayList<>();
        if (periodItems.size() > 4) {
            for (int index = periodItems.size() - 5; index <= periodItems.size() - 1; index++) {
                averageItems.add(periodItems.get(index));
            }
        } else {
            averageItems.addAll(periodItems);
        }

        Double averageCycleLength = null;
        if (!averageItems.isEmpty()) {
            Integer sum = 0;
            for (CyclePeriodItem item : averageItems) {
                sum += DateUtils.daysDiff(item.getInitialDate(), item.getEndDate()).intValue();
            }
            Integer length = averageItems.size();
            averageCycleLength = sum.doubleValue() / length.doubleValue();
        }

        // Variation calculation

        Integer variation = null, max = null, min = null;
        for (CyclePeriodItem item : periodItems) {
            Integer diff = DateUtils.daysDiff(item.getInitialDate(), item.getEndDate()).intValue();

            if (min == null || min > diff) {
                min = diff;
            }
            if (max == null || max < diff) {
                max = diff;
            }
        }
        if (max != null && min != null) {
            variation = max - min;
        }

        // Average Period Length calculation

        Double averagePeriodLength = null;
        if (!periodItems.isEmpty()) {
            Integer sum = 0;
            for (CyclePeriodItem item : averageItems) {
                sum += item.getDuration();
            }
            Integer length = averageItems.size();
            averagePeriodLength = sum.doubleValue() / length.doubleValue();
        }

        // Max Length calculation

        Integer maxCycleLength = null;
        for (CyclePeriodItem item : periodItems) {
            Integer diff = DateUtils.daysDiff(item.getInitialDate(), item.getEndDate()).intValue();
            if (maxCycleLength == null || maxCycleLength < diff) {
                maxCycleLength = diff;
            }
        }

        // Current Day Cycle calculation

        Integer currentDayCycle = null;
        if (!items.isEmpty()) {
            Date endDate = new Date();
            Integer daysDiff = DateUtils.daysDiff(currentStartDate, endDate).intValue() + 1;

            CyclePeriodItem periodItem = new CyclePeriodItem(currentStartDate, endDate, currentDuration + 1);
            periodItems.add(periodItem);
            currentDayCycle = daysDiff;
        }

        Collections.sort(periodItems, new Comparator<CyclePeriodItem>() {
            @Override
            public int compare(CyclePeriodItem item0, CyclePeriodItem item1) {
                return item1.getInitialDate().compareTo(item0.getInitialDate());
            }
        });

        return new CyclePeriodData(averageCycleLength, variation, averagePeriodLength, currentDayCycle, maxCycleLength, periodItems);
    }
}
