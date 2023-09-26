package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.manager.converter.CycleDayItemConverter;
import com.kollectivemobile.euki.manager.converter.CyclePeriodDataConverter;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.utils.DateUtils;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CycleManagerImpl implements CycleManager {
    private AppSettingsManager mAppSettingsManager;
    private CalendarManager mCalendarManager;

    public CycleManagerImpl(AppSettingsManager appSettingsManager, CalendarManager calendarManager) {
        mAppSettingsManager = appSettingsManager;
        mCalendarManager = calendarManager;
    }

    @Override
    public void requestCycleItems(final EukiCallback<List<CycleDayItem>> callback) {
        final Boolean trackPeriodEnabled = mAppSettingsManager.trackPeriodEnabled();

        mCalendarManager.getDayscalendarItems(new EukiCallback<List<CalendarItem>>() {
            @Override
            public void onSuccess(List<CalendarItem> calendarItems) {
                final Map<Date, CalendarItem> dict = new HashMap<>();

                for (CalendarItem item : calendarItems) {
                    dict.put(DateUtils.startDate(item.getDate()), item);
                }

                mCalendarManager.getPredictionRange(new EukiCallback<List<Range<Date>>>() {
                    @Override
                    public void onSuccess(List<Range<Date>> ranges) {
                        callback.onSuccess(CycleDayItemConverter.convert(dict, ranges, trackPeriodEnabled));
                    }

                    @Override
                    public void onError(ServerError serverError) {
                        callback.onError(serverError);
                    }
                });
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    @Override
    public void requestCyclePeriodData(final EukiCallback<CyclePeriodData> callback) {
        if (!mAppSettingsManager.trackPeriodEnabled()) {
            callback.onSuccess(new CyclePeriodData(null, null, null, null, null, new ArrayList<CyclePeriodItem>()));
            return;
        }

        mCalendarManager.getDayscalendarItems(new EukiCallback<List<CalendarItem>>() {
            @Override
            public void onSuccess(List<CalendarItem> calendarItems) {
                List<Range<Date>> hiddenPeriods = mAppSettingsManager.hiddenCyclePeriods();
                Collections.sort(calendarItems, new Comparator<CalendarItem>() {
                    @Override
                    public int compare(CalendarItem item0, CalendarItem item1) {
                        return item0.getDate().compareTo(item1.getDate());
                    }
                });
                callback.onSuccess(CyclePeriodDataConverter.convert(calendarItems, hiddenPeriods));
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    @Override
    public void deletePeriod(CyclePeriodItem item, EukiCallback<Boolean> callback) {
        List<Range<Date>> hiddenPeriods = mAppSettingsManager.hiddenCyclePeriods();
        hiddenPeriods.add(Range.between(item.getInitialDate(), item.getEndDate()));
        mAppSettingsManager.saveHiddenCyclePeriods(hiddenPeriods);
        callback.onSuccess(true);
    }
}
