package com.kollectivemobile.euki.manager;

import android.util.Log;

import com.kollectivemobile.euki.model.database.dao.ReminderItemDAO;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderManagerImpl implements ReminderManager {
    private ReminderItemDAO mReminderItemDAO;
    private LocalNotificationManager mLocalNotificationManager;

    public ReminderManagerImpl(ReminderItemDAO reminderItemDAO, LocalNotificationManager localNotificationManager) {
        this.mReminderItemDAO = reminderItemDAO;
        this.mLocalNotificationManager = localNotificationManager;
    }

    @Override
    public void getReminders(EukiCallback<List<ReminderItem>> callback) {
        callback.onSuccess(mReminderItemDAO.getReminderItem());
    }

    @Override
    public void addReminder(ReminderItem reminderItem) {
        long reminderId = mReminderItemDAO.insert(reminderItem);
        mLocalNotificationManager.createLocalNotification(reminderId + "", reminderItem.getDate(), reminderItem.getRepeatDays());
    }

    @Override
    public void removeReminder(ReminderItem reminderItem) {
        mReminderItemDAO.delete(reminderItem);
        mLocalNotificationManager.deleteNotification(reminderItem.getId() + "");
    }

    @Override
    public void updateReminder(ReminderItem reminderItem) {
        reminderItem.setLastAlert(null);
        mReminderItemDAO.update(reminderItem);
        mLocalNotificationManager.deleteNotification(reminderItem.getId() + "");
        mLocalNotificationManager.createLocalNotification(reminderItem.getId() + "", reminderItem.getDate(), reminderItem.getRepeatDays());
    }

    @Override
    public void pendingNotify(EukiCallback<ReminderItem> callback) {
        List<ReminderItem> items = mReminderItemDAO.getReminderItem();
        ReminderItem toAlertReminder = null;
        Date toAlertDate = null;
        Date nowDate = new Date();

        for (ReminderItem reminderItem : items) {
            if (reminderItem.getLastAlert() != null && DateUtils.isSameDate(nowDate, reminderItem.getLastAlert())) {
                continue;
            }

            Date todayAlertDate = getTodayAlert(reminderItem);
            if (todayAlertDate == null) {
                continue;
            }

            if (toAlertDate == null) {
                toAlertReminder = reminderItem;
                toAlertDate = todayAlertDate;
            } else {
                if (toAlertDate.before(todayAlertDate)) {
                    toAlertReminder = reminderItem;
                    toAlertDate = todayAlertDate;
                }
            }
        }

        if (toAlertReminder != null) {
            toAlertReminder.setLastAlert(nowDate);
            mReminderItemDAO.update(toAlertReminder);
        }

        callback.onSuccess(toAlertReminder);
    }

    @Override
    public void removeAll() {
        mReminderItemDAO.deleteAll();
    }

    private Date getTodayAlert(ReminderItem reminderItem) {
        Date nowDate = new Date();
        Date startDate = reminderItem.getDate();
        Integer repeatDays = reminderItem.getRepeatDays();

        if (startDate == null || repeatDays == null || startDate.after(nowDate)) {
            return null;
        }

        Date currentDate = startDate;

        while (currentDate.before(nowDate)) {
            if (DateUtils.isSameDate(nowDate, currentDate)) {
                return currentDate;
            }

            if (repeatDays == 0) {
                return null;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.HOUR, 24 * repeatDays);
            currentDate = calendar.getTime();
        }

        return null;
    }
}
