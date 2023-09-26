package com.kollectivemobile.euki.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kollectivemobile.euki.model.database.entity.ReminderItem;

import java.util.List;

@Dao
public interface ReminderItemDAO {
    @Insert
    long insert(ReminderItem reminderItem);

    @Query("SELECT * FROM reminderItem")
    List<ReminderItem> getReminderItem();

    @Delete
    void delete(ReminderItem reminderItem);

    @Query("DELETE FROM reminderItem")
    void deleteAll();

    @Update
    void update(ReminderItem reminderItem);
}
