package com.kollectivemobile.euki.manager;

import android.util.Log;

import com.kollectivemobile.euki.manager.converter.CyclePeriodDataConverter;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.model.database.dao.CalendarItemDAO;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.utils.Constants;
import com.kollectivemobile.euki.utils.DateUtils;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarManagerImpl implements CalendarManager {
    private CalendarFilter mCalendarFilter;
    private CalendarItemDAO mCalendarItemDAO;
    private LocalNotificationManager mLocalNotificationManager;

    private AppSettingsManager mAppSettingsManager;

    public CalendarManagerImpl(CalendarItemDAO calendarItemDAO, LocalNotificationManager localNotificationManager, AppSettingsManager appSettingsManager) {
        this.mCalendarFilter = new CalendarFilter();
        this.mCalendarItemDAO = calendarItemDAO;
        this.mLocalNotificationManager = localNotificationManager;
        this.mAppSettingsManager = appSettingsManager;
    }

    @Override
    public CalendarFilter getCalendarFilter() {
        return mCalendarFilter;
    }

    @Override
    public void updateCalendarFilter(CalendarFilter calendarFilter) {
        mCalendarFilter = calendarFilter;
    }

    @Override
    public void todayCalendarItem(EukiCallback<CalendarItem> callback) {
        getCalendarItem(new Date(), callback);
    }

    @Override
    public void getCalendarItem(Date date, EukiCallback<CalendarItem> callback) {
        List<CalendarItem> items = mCalendarItemDAO.getCalendarItems();

        for (CalendarItem item : items) {
            if (DateUtils.isSameDate(date, item.getDate())) {
                callback.onSuccess(item);
                return;
            }
        }

        callback.onSuccess(new CalendarItem(date));
    }

    @Override
    public CalendarItem getCalendarItem(Date date) {
        List<CalendarItem> items = mCalendarItemDAO.getCalendarItems();

        for (CalendarItem item : items) {
            if (DateUtils.isSameDate(date, item.getDate())) {
                return item;
            }
        }

        return new CalendarItem(date);
    }

    @Override
    public void getCalendarItems(EukiCallback<Map<String, CalendarItem>> callback) {
        Map<String, CalendarItem> mapItems = new HashMap<>();
        List<CalendarItem> items = mCalendarItemDAO.getCalendarItems();

        for (CalendarItem item : items) {
            String dateString = DateUtils.toString(item.getDate(), DateUtils.DateLongFormat);
            if (dateString != null) {
                mapItems.put(dateString, item);
            }
        }

        callback.onSuccess(mapItems);
    }

    @Override
    public void getDayscalendarItems(EukiCallback<List<CalendarItem>> callback) {
        List<CalendarItem> filterItems = new ArrayList<>();
        List<CalendarItem> items = mCalendarItemDAO.getCalendarItems();

        for (CalendarItem item : items) {
            if (item.hasData()) {
                filterItems.add(item);
            }
        }

        Collections.sort(filterItems, new Comparator<CalendarItem>() {
            @Override
            public int compare(CalendarItem item1, CalendarItem item2) {
                return item2.getDate().compareTo(item1.getDate());
            }
        });

        callback.onSuccess(filterItems);
    }

    @Override
    public void save(final Appointment appointment, final EukiCallback<Boolean> callback) {
        final Date date = appointment.getDate();
        if (date == null) {
            callback.onError(new ServerError("Appointment without Date"));
            return;
        }

        getCalendarItem(date, new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem foundItem) {
                CalendarItem calendarItem;
                if (foundItem == null) {
                    calendarItem = new CalendarItem();
                    calendarItem.setDate(date);
                } else {
                    calendarItem = foundItem;
                }

                calendarItem.getAppointments().add(appointment);
                saveItem(calendarItem, callback);
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    @Override
    public void saveItem(CalendarItem calendarItem, EukiCallback<Boolean> callback) {
        List<Appointment> appointments = new ArrayList<>();
        List<Appointment> otherAppointments = new ArrayList<>();

        for (Appointment appointment : calendarItem.getAppointments()) {
            if (appointment.getDate() == null) {
                continue;
            }
            if (DateUtils.isSameDate(appointment.getDate(), calendarItem.getDate())) {
                appointments.add(appointment);
            } else {
                otherAppointments.add(appointment);
            }
        }

        for (Appointment appointment : otherAppointments) {
            Date otherDate = appointment.getDate();
            if (otherDate == null) {
                continue;
            }

            CalendarItem toSaveCalendarItem = getCalendarItem(otherDate);
            if (toSaveCalendarItem == null) {
                toSaveCalendarItem = new CalendarItem(otherDate);
            }

            toSaveCalendarItem.getAppointments().add(appointment);
            saveItem(toSaveCalendarItem, new EukiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Log.d(getClass().getName(), "Other date appointment saved");
                }

                @Override
                public void onError(ServerError serverError) {
                    Log.d(getClass().getName(), "Other date appointment saving error!");
                }
            });
        }

        calendarItem.setAppointments(appointments);

        if (calendarItem.getId() == null) {
            mCalendarItemDAO.insert(calendarItem);
        } else {
            if (calendarItem.hasData()) {
                mCalendarItemDAO.update(calendarItem);
            } else {
                mCalendarItemDAO.delete(calendarItem);
            }
        }

        for (Appointment appointment : calendarItem.getAppointments()) {
            updateLocalNotifications(appointment);
        }
        callback.onSuccess(true);
    }

    @Override
    public void getPendingNotify(final EukiCallback<Appointment> callback) {
        getCalendarItem(new Date(), new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem calendarItem) {
                if (calendarItem == null) {
                    callback.onError(new ServerError("No calendarItem found"));
                    return;
                }

                for (int i=1; i<calendarItem.getAppointments().size(); i++) {
                    final Appointment appointment = calendarItem.getAppointments().get(i);

                    if (appointment.getAlertShown()) {
                        continue;
                    }

                    Date currentDate = new Date();
                    Date appointmentDate = appointment.getDate();
                    Date alertDate = appointment.getAlertDate();
                    if (appointmentDate != null && alertDate != null && alertDate.before(currentDate) && currentDate.before(appointmentDate)) {
                        appointment.setAlertShown(true);
                        saveItem(calendarItem, new EukiCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                callback.onSuccess(appointment);
                            }

                            @Override
                            public void onError(ServerError serverError) {
                                callback.onError(serverError);
                            }
                        });
                        return;
                    }
                }

                callback.onSuccess(null);
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    @Override
    public Boolean shouldShowIncludeCycleAlert(Date date) {
        Date latestBleedingTracking = mAppSettingsManager.latestBleedingTracking();

        if (latestBleedingTracking == null) {
            return true;
        }

        return DateUtils.daysDiff(latestBleedingTracking, date) >= Constants.sDaysBleedingTrackingAlert;
    }

    @Override
    public void updateLatestBleedingTracking(Date date) {
        Date checkDate = DateUtils.startDate(date);
        Date currentDate = mAppSettingsManager.latestBleedingTracking();

        if (currentDate == null || currentDate.before(checkDate)) {
            mAppSettingsManager.saveLatestBleedingTracking(checkDate);
        }
    }

    @Override
    public void getPredictionRange(final EukiCallback<List<Range<Date>>> callback) {
        if (!mAppSettingsManager.periodPredictionEnabled()) {
            callback.onSuccess(new ArrayList<Range<Date>>());
            return;
        }

        requestCyclePeriodData(new EukiCallback<CyclePeriodData>() {
            @Override
            public void onSuccess(CyclePeriodData data) {
                List<Range<Date>> ranges = new ArrayList<>();

                Double averageCycleLength = data.getAverageCycleLength();
                Double averagePeriodLength = data.getAveragePeriodLength();
                List <CyclePeriodItem> items = data.getItems();

                if (averageCycleLength != null && averagePeriodLength != null && items.size() > 0) {
                    Date startDate = items.get(0).getInitialDate();

                    for (int index = 1; index <= 4; index++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        calendar.add(Calendar.HOUR, 24 * (index * averageCycleLength.intValue()));
                        Date start = calendar.getTime();

                        calendar.setTime(start);
                        calendar.add(Calendar.HOUR, 24 * (averagePeriodLength.intValue() - 1));
                        calendar.set(Calendar.HOUR, 23);
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.SECOND, 59);
                        Date end = calendar.getTime();

                        ranges.add(Range.between(start, end));
                    }
                }

                callback.onSuccess(ranges);
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    private void requestCyclePeriodData(final EukiCallback<CyclePeriodData> callback) {
        if (!mAppSettingsManager.trackPeriodEnabled()) {
            callback.onSuccess(new CyclePeriodData(null, null, null, null, null, new ArrayList<CyclePeriodItem>()));
            return;
        }

        getDayscalendarItems(new EukiCallback<List<CalendarItem>>() {
            @Override
            public void onSuccess(List<CalendarItem> calendarItems) {
                callback.onSuccess(convert(calendarItems));
            }

            @Override
            public void onError(ServerError serverError) {
                callback.onError(serverError);
            }
        });
    }

    private CyclePeriodData convert(List<CalendarItem> calendarItems) {
        Collections.sort(calendarItems, new Comparator<CalendarItem>() {
            @Override
            public int compare(CalendarItem item0, CalendarItem item1) {
                return item0.getDate().compareTo(item1.getDate());
            }
        });
        List<Range<Date>> hiddenRanges = mAppSettingsManager.hiddenCyclePeriods();
        return CyclePeriodDataConverter.convert(calendarItems, hiddenRanges);
    }

    @Override
    public void removeAll() {
        mCalendarItemDAO.deleteAll();
    }

    private void updateLocalNotifications(Appointment appointment) {
        String appointmentId = appointment.getId();
        if (appointmentId == null) {
            return;
        }

        mLocalNotificationManager.deleteNotification(appointmentId);

        Date alertDate = appointment.getAlertDate();
        if (alertDate == null && new Date().after(alertDate)) {
            return;
        }

        mLocalNotificationManager.createNotification(appointmentId, alertDate, null);
    }
}
