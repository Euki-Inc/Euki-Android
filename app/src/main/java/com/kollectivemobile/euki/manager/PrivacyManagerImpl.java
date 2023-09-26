package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.utils.Constants.*;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrivacyManagerImpl implements PrivacyManager {
    private CalendarManager mCalendarManager;
    private LocalNotificationManager mLocalNotificationManager;
    private ReminderManager mReminderManager;
    private AppSettingsManager mAppSettingsManager;
    private BookmarkManager mBookmarkManager;

    public PrivacyManagerImpl(CalendarManager calendarManager, LocalNotificationManager localNotificationManager,
                              ReminderManager reminderManager, AppSettingsManager appSettingsManager,
                              BookmarkManager bookmarkManager) {
        this.mCalendarManager = calendarManager;
        this.mLocalNotificationManager = localNotificationManager;
        this.mReminderManager = reminderManager;
        this.mAppSettingsManager = appSettingsManager;
        this.mBookmarkManager = bookmarkManager;
    }

    @Override
    public void removeAllData() {
        final Date now = new Date();
        mCalendarManager.getDayscalendarItems(new EukiCallback<List<CalendarItem>>() {
            @Override
            public void onSuccess(List<CalendarItem> calendarItems) {
                List<String> removeIds = new ArrayList<>();
                for (CalendarItem calendarItem : calendarItems) {
                    for (Appointment appointment : calendarItem.getAppointments()) {
                        if (appointment.getAlertDate().after(now)) {
                            removeIds.add(appointment.getId());
                        }
                    }
                }
                mLocalNotificationManager.deleteNotifications(removeIds);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });

        mReminderManager.getReminders(new EukiCallback<List<ReminderItem>>() {
            @Override
            public void onSuccess(List<ReminderItem> reminderItems) {
                List<String> removeIds = new ArrayList<>();
                for (ReminderItem item : reminderItems) {
                    String alertId = item.getAlertId();
                    if (alertId != null) {
                        removeIds.add(alertId);
                    }
                }
                mLocalNotificationManager.deleteNotifications(removeIds);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });

        mReminderManager.removeAll();
        mAppSettingsManager.removeAll();
        mCalendarManager.removeAll();
        mBookmarkManager.removeAllBookmarks();
    }

    @Override
    public void verifyAutoRemoveData() {
        DeleteRecurringType type = mAppSettingsManager.getDeleteRecurringType();
        Date lastAutoDeleteDate = mAppSettingsManager.getLastAutodelete();

        if (type == null || lastAutoDeleteDate == null) {
            return;
        }

        Date now = new Date();
        Long daysDiff = DateUtils.daysDiff(lastAutoDeleteDate, now);
        Integer autoRemoveDayMax = 0;

        switch (type) {
            case WEEKLY:
                autoRemoveDayMax = 7;
                break;
            case WEEKLY2:
                autoRemoveDayMax = 14;
                break;
            case MONTHLY:
                autoRemoveDayMax = 30;
                break;
            case MONTHLY3:
                autoRemoveDayMax = 90;
                break;
            case YEARLY:
                autoRemoveDayMax = 365;
                break;
        }

        if (daysDiff >= autoRemoveDayMax) {
            removeAllData();
            mAppSettingsManager.saveLastAutodelete(now);
        }
    }

    @Override
    public void saveRecurringData(DeleteRecurringType type) {
        mAppSettingsManager.saveDeleteRecurringType(type);

        if (type == null) {
            mAppSettingsManager.saveLastAutodelete(null);
        } else {
            mAppSettingsManager.saveLastAutodelete(new Date());
        }
    }

    @Override
    public DeleteRecurringType getRecurringType() {
        return mAppSettingsManager.getDeleteRecurringType();
    }
}
