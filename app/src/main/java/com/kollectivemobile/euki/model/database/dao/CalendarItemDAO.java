package com.kollectivemobile.euki.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kollectivemobile.euki.model.database.entity.CalendarItem;

import java.util.List;

@Dao
public interface CalendarItemDAO {
    @Insert
    void insert(CalendarItem calendarItem);

    @Query("SELECT * FROM calendarItem")
    List<CalendarItem> getCalendarItems();

    @Query("DELETE FROM calendarItem")
    void deleteAll();

    @Delete
    void delete(CalendarItem calendarItem);

    @Update
    void update(CalendarItem calendarItem);
}
