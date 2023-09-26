package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.utils.Constants.*;

import org.apache.commons.lang3.Range;

import java.util.Date;
import java.util.List;

public interface AppSettingsManager {
    Boolean shouldShowOnboardingScreens();
    void saveShowOnboardinScreens(Boolean show);
    void savePinCode(String pinCode);
    String getPinCode();
    void saveBookmarks(List<String> bookmarkIds);
    List<String> getBookmarkIds();
    void saveUsedItems(List<String> items);
    List<String> getUsedItems();
    void saveNotUsedItems(List<String> items);
    List<String> getNotUsedItems();
    void saveDeleteRecurringType(DeleteRecurringType type);
    DeleteRecurringType getDeleteRecurringType();
    void saveLastAutodelete(Date date);
    Date getLastAutodelete();
    Boolean shouldShowTabbarTutorial();
    Boolean shouldShowDailyLogTutorial();
    Boolean shouldShowCycleSummaryTutorial();
    Boolean shouldShowCalendarTutorial();
    void addDaityCounter();
    Boolean isPrivacyAlreadyShown();
    Boolean shouldShowPinUpdate();
    void saveShouldShowPinUpdate(Boolean show);
    Date latestBleedingTracking();
    void saveLatestBleedingTracking(Date date);
    List<FilterItem> filterItems();
    void saveFilterItems(List<FilterItem> filterItems);
    void saveHiddenCyclePeriods(List<Range<Date>> periods);
    List<Range<Date>> hiddenCyclePeriods();
    Boolean trackPeriodEnabled();
    void saveTrackPeriodEnabled(Boolean enabled);
    Boolean periodPredictionEnabled();
    void savePeriodPredictionEnabled(Boolean enabled);
    void removeAll();
}
