package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.Date;
import java.util.List;

public interface LocalNotificationManager {
    void createLocalNotification(Date date, Integer repeatDays, EukiCallback<String> callback);
    void createLocalNotification(String id, Date date, Integer repeatDays);
    void createLocalNotification(String id, Date date, Integer repeatDays, EukiCallback<String> callback);
    void createNotification(String id, Date date, EukiCallback<String> callback);
    void deleteNotification(String id);
    void deleteNotifications(List<String> ids);
}
