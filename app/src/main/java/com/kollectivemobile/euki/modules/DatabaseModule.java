package com.kollectivemobile.euki.modules;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.model.database.AppDatabase;
import com.kollectivemobile.euki.model.database.dao.AppSettingsDAO;
import com.kollectivemobile.euki.model.database.dao.CalendarItemDAO;
import com.kollectivemobile.euki.model.database.dao.ReminderItemDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN should_show_pin_update Integer");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN latest_bleeding_tracking Integer");
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN filter_items TEXT");
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN hidden_cycle_periods TEXT");
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN track_period_enabled Integer");
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN period_prediction_enabled Integer");
            database.execSQL("ALTER TABLE AppSettings "
                    + "ADD COLUMN show_cycle_summary_tutorial Integer");
            database.execSQL("ALTER TABLE CalendarItem "
                    + "ADD COLUMN include_cycle_summary Integer");
            database.execSQL("ALTER TABLE CalendarItem "
                    + "ADD COLUMN bleeding_clots_counter TEXT");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE CalendarItem "
                    + "ADD COLUMN contraception_shot TEXT");
        }
    };

    @Singleton
    @Provides
    AppDatabase providesAppDatabase() {
//        return Room.databaseBuilder(App.getContext(), AppDatabase.class, "euki").build();
        return Room.databaseBuilder(App.getContext(), AppDatabase.class, "euki").allowMainThreadQueries().addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build();
    }

    @Provides
    AppSettingsDAO providesAppSettingsDAO(AppDatabase appDatabase) {
        return appDatabase.appSettingsDAO();
    }

    @Provides
    CalendarItemDAO providesCalendarItemDAO(AppDatabase appDatabase) {
        return appDatabase.calendarItemDAO();
    }

    @Provides
    ReminderItemDAO providesReminderItemDAO(AppDatabase appDatabase) {
        return appDatabase.reminderItemDAO();
    }
}
