package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

public interface ReminderManager {
    void getReminders(EukiCallback<List<ReminderItem>> callback);
    void addReminder(ReminderItem reminderItem);
    void removeReminder(ReminderItem reminderItem);
    void updateReminder(ReminderItem reminderItem);
    void pendingNotify(EukiCallback<ReminderItem> callback);
    void removeAll();
}
