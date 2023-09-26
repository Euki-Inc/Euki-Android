package com.kollectivemobile.euki.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kollectivemobile.euki.model.database.entity.AppSettings;

@Dao
public interface AppSettingsDAO {
    @Insert
    void insert(AppSettings appSettings);

    @Query("SELECT * FROM appSettings LIMIT 1")
    AppSettings getAppSettings();

    @Update
    void update(AppSettings appSettings);

    @Delete
    void delete(AppSettings appSettings);
}
