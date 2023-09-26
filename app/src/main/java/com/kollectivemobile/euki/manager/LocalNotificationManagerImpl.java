package com.kollectivemobile.euki.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.alarm.AlarmReceiver;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LocalNotificationManagerImpl implements LocalNotificationManager {
    @Override
    public void createLocalNotification(Date date, Integer repeatDays, EukiCallback<String> callback) {
        String uuidString = UUID.randomUUID().toString();
        createLocalNotification(uuidString, date, repeatDays, callback);
    }

    @Override
    public void createLocalNotification(String id, Date date, Integer repeatDays) {
        createLocalNotification(id, date, repeatDays, null);
    }

    @Override
    public void createLocalNotification(String id, Date date, Integer repeatDays, EukiCallback<String> callback) {
        if (repeatDays == 0) {
            String idString = id + "_0";
            createNotification(idString, date, null);
        } else {
            for (int i=0; i<=60; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 24 * (repeatDays * i));
                String idString = id + "_" + i;
                createNotification(idString, date, null);
            }
        }

        if (callback != null) {
            callback.onSuccess(id);
        }
    }

    @Override
    public void createNotification(String id, Date date, EukiCallback<String> callback) {
        createAlarm(id, date);

        if (callback != null) {
            callback.onSuccess(id);
        }
    }

    @Override
    public void deleteNotification(String id) {
        for (int i=0; i<=60; i++) {
            String idString = id + "_" + i;
            deleteAlarm(idString);
        }
    }

    @Override
    public void deleteNotifications(List<String> ids) {
        for (String id : ids) {
            deleteNotification(id);
        }
    }

    private void createAlarm(String id, Date date){
        int idInteger = Math.abs(id.hashCode()) % 1000;
        AlarmManager alarmManager = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(App.getContext(), AlarmReceiver.class);
        final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent broadcast = PendingIntent.getBroadcast(App.getContext(), idInteger, notificationIntent, flag);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), broadcast);
    }

    public void deleteAlarm(String id){
        int idInteger = Math.abs(id.hashCode()) % 1000;
        AlarmManager alarmManager = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(App.getContext(), AlarmReceiver.class);

        final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent broadcast = PendingIntent.getBroadcast(App.getContext(), idInteger, notificationIntent, flag);
        alarmManager.cancel(broadcast);
    }
}
