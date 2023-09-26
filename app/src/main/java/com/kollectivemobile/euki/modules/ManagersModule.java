package com.kollectivemobile.euki.modules;

import com.kollectivemobile.euki.manager.AbortionContentManager;
import com.kollectivemobile.euki.manager.AbortionContentManagerImpl;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.AppSettingsManagerImpl;
import com.kollectivemobile.euki.manager.BookmarkManager;
import com.kollectivemobile.euki.manager.BookmarkManagerImpl;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.manager.CalendarManagerImpl;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.manager.ContentManagerImpl;
import com.kollectivemobile.euki.manager.ContraceptionContentManager;
import com.kollectivemobile.euki.manager.ContraceptionContentManagerImpl;
import com.kollectivemobile.euki.manager.CycleManager;
import com.kollectivemobile.euki.manager.CycleManagerImpl;
import com.kollectivemobile.euki.manager.HomeManager;
import com.kollectivemobile.euki.manager.HomeManagerImpl;
import com.kollectivemobile.euki.manager.LocalNotificationManager;
import com.kollectivemobile.euki.manager.LocalNotificationManagerImpl;
import com.kollectivemobile.euki.manager.PrivacyContentManager;
import com.kollectivemobile.euki.manager.PrivacyContentManagerImpl;
import com.kollectivemobile.euki.manager.PrivacyManager;
import com.kollectivemobile.euki.manager.PrivacyManagerImpl;
import com.kollectivemobile.euki.manager.QuizManager;
import com.kollectivemobile.euki.manager.QuizManagerImpl;
import com.kollectivemobile.euki.manager.ReminderManager;
import com.kollectivemobile.euki.manager.ReminderManagerImpl;
import com.kollectivemobile.euki.model.database.dao.AppSettingsDAO;
import com.kollectivemobile.euki.model.database.dao.CalendarItemDAO;
import com.kollectivemobile.euki.model.database.dao.ReminderItemDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagersModule {
    public ManagersModule(){
    }

    @Singleton
    @Provides
    public AbortionContentManager providesAbortionContentManager() {
        return new AbortionContentManagerImpl();
    }

    @Singleton
    @Provides
    public AppSettingsManager providesAppSettingsManager(AppSettingsDAO appSettingsDAO) {
        return new AppSettingsManagerImpl(appSettingsDAO);
    }

    @Singleton
    @Provides
    public BookmarkManager providesBookmarkManager(AppSettingsManager appSettingsManager, ContentManager contentManager) {
        return new BookmarkManagerImpl(appSettingsManager, contentManager);
    }

    @Singleton
    @Provides
    public CalendarManager providesCalendarManager(CalendarItemDAO calendarItemDAO, LocalNotificationManager localNotificationManager, AppSettingsManager appSettingsManager) {
        return new CalendarManagerImpl(calendarItemDAO, localNotificationManager, appSettingsManager);
    }

    @Singleton
    @Provides
    public ContentManager providesContentManager(AbortionContentManager abortionContentManager, PrivacyContentManager privacyContentManager) {
        return new ContentManagerImpl(abortionContentManager, privacyContentManager);
    }

    @Singleton
    @Provides
    public ContraceptionContentManager providesContraceptionContentManager(ContentManager contentManager) {
        return new ContraceptionContentManagerImpl(contentManager);
    }

    @Singleton
    @Provides
    public HomeManager providesHomeManager(AppSettingsDAO appSettingsDAO) {
        return new HomeManagerImpl(appSettingsDAO);
    }

    @Singleton
    @Provides
    public LocalNotificationManager providesLocalNotificationManager() {
        return new LocalNotificationManagerImpl();
    }

    @Singleton
    @Provides
    public PrivacyContentManager providesPrivacyContentManager(AppSettingsManager appSettingsManager) {
        return new PrivacyContentManagerImpl(appSettingsManager);
    }

    @Singleton
    @Provides
    public PrivacyManager providesPrivacyManager(CalendarManager calendarManager, LocalNotificationManager localNotificationManager,
                                                 ReminderManager reminderManager, AppSettingsManager appSettingsManager,
                                                 BookmarkManager bookmarkManager) {
        return new PrivacyManagerImpl(calendarManager, localNotificationManager, reminderManager, appSettingsManager, bookmarkManager);
    }

    @Singleton
    @Provides
    public QuizManager providesQuizManager() {
        return new QuizManagerImpl();
    }

    @Singleton
    @Provides
    public ReminderManager providesReminderManager(ReminderItemDAO reminderItemDAO, LocalNotificationManager localNotificationManager) {
        return new ReminderManagerImpl(reminderItemDAO, localNotificationManager);
    }

    @Singleton
    @Provides
    public CycleManager providesCycleManager(AppSettingsManager appSettingsManager, CalendarManager calendarManager) {
        return new CycleManagerImpl(appSettingsManager, calendarManager);
    }
}
