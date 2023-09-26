package com.kollectivemobile.euki.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.kollectivemobile.euki.model.converters.ArraysConverter;
import com.kollectivemobile.euki.model.converters.ConstantsConverter;
import com.kollectivemobile.euki.model.converters.DateConverter;
import com.kollectivemobile.euki.model.database.dao.AppSettingsDAO;
import com.kollectivemobile.euki.model.database.dao.CalendarItemDAO;
import com.kollectivemobile.euki.model.database.dao.ReminderItemDAO;
import com.kollectivemobile.euki.model.database.entity.AppSettings;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;

@Database(
        entities = {CalendarItem.class, ReminderItem.class, AppSettings.class},
        version = 3,
        exportSchema = false
)

@TypeConverters({ArraysConverter.class, ConstantsConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CalendarItemDAO calendarItemDAO();
    public abstract ReminderItemDAO reminderItemDAO();
    public abstract AppSettingsDAO appSettingsDAO();
}
