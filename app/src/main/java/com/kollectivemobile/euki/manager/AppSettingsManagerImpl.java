package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.dao.AppSettingsDAO;
import com.kollectivemobile.euki.model.database.entity.AppSettings;
import com.kollectivemobile.euki.utils.Constants;

import org.apache.commons.lang3.Range;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppSettingsManagerImpl implements AppSettingsManager {
    private AppSettingsDAO mAppSettingsDAO;
    private AppSettings mAppSettings;

    public AppSettingsManagerImpl(AppSettingsDAO appSettingsDAO) {
        this.mAppSettingsDAO = appSettingsDAO;

        AppSettings appSettings = mAppSettingsDAO.getAppSettings();
        if (appSettings == null) {
            mAppSettingsDAO.insert(new AppSettings());
            appSettings = mAppSettingsDAO.getAppSettings();
        }

        this.mAppSettings = appSettings;
    }

    @Override
    public Boolean shouldShowOnboardingScreens() {
        return mAppSettings.getShowOnboardingScreens();
    }

    @Override
    public void saveShowOnboardinScreens(Boolean show) {
        mAppSettings.setShowOnboardingScreens(show);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public void savePinCode(String pinCode) {
        mAppSettings.setPinCode(pinCode);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public String getPinCode() {
        return mAppSettings.getPinCode();
    }

    @Override
    public void saveBookmarks(List<String> bookmarkIds) {
        mAppSettings.setBookmarkIds(bookmarkIds);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public List<String> getBookmarkIds() {
        return mAppSettings.getBookmarkIds();
    }

    @Override
    public void saveUsedItems(List<String> items) {
        mAppSettings.setHomeItemsUsed(items);
    }

    @Override
    public List<String> getUsedItems() {
        return mAppSettings.getHomeItemsUsed();
    }

    @Override
    public void saveNotUsedItems(List<String> items) {
        mAppSettings.setHomeItemsNotUsed(items);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public List<String> getNotUsedItems() {
        return mAppSettings.getHomeItemsNotUsed();
    }

    @Override
    public void saveDeleteRecurringType(Constants.DeleteRecurringType type) {
        mAppSettings.setDeleteRecurringType(type);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public Constants.DeleteRecurringType getDeleteRecurringType() {
        return mAppSettings.getDeleteRecurringType();
    }

    @Override
    public void saveLastAutodelete(Date date) {
        mAppSettings.setLastDateAutodelete(date);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public Date getLastAutodelete() {
        return mAppSettings.getLastDateAutodelete();
    }

    @Override
    public Boolean shouldShowTabbarTutorial() {
        if (mAppSettings.getShowTabbarTutorial()) {
            mAppSettings.setShowTabbarTutorial(false);
            mAppSettingsDAO.update(mAppSettings);
            return true;
        }
        return false;
    }

    @Override
    public Boolean shouldShowDailyLogTutorial() {
        if (mAppSettings.getShowDailyLogTutorial()) {
            mAppSettings.setShowDailyLogTutorial(false);
            mAppSettingsDAO.update(mAppSettings);
            return true;
        }
        return false;
    }

    @Override
    public Boolean shouldShowCycleSummaryTutorial() {
        if (mAppSettings.getShowCycleSummaryTutorial() == null || mAppSettings.getShowCycleSummaryTutorial()) {
            mAppSettings.setShowCycleSummaryTutorial(false);
            mAppSettingsDAO.update(mAppSettings);
            return true;
        }
        return false;
    }

    @Override
    public Boolean shouldShowCalendarTutorial() {
        if (mAppSettings.getShowCalendarTutorial()) {
            mAppSettings.setShowCalendarTutorial(false);
            mAppSettingsDAO.update(mAppSettings);
            return true;
        }
        return false;
    }

    @Override
    public void addDaityCounter() {
        Integer newCounter = mAppSettings.getDailyLogCounter() + 1;
        mAppSettings.setDailyLogCounter(newCounter);

        if (newCounter == 3) {
            mAppSettings.setShowCalendarTutorial(true);
        }

        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public Boolean isPrivacyAlreadyShown() {
        if (!mAppSettings.getPrivacyAlreadyShown()) {
            mAppSettings.setPrivacyAlreadyShown(true);
            mAppSettingsDAO.update(mAppSettings);
            return false;
        }
        return true;
    }

    @Override
    public Boolean shouldShowPinUpdate() {
        Boolean shouldShowPin = mAppSettings.getShouldShowPinUpdate();
        if (shouldShowPin != null && !shouldShowPin) {
            return false;
        }
        return getPinCode() != null;
    }

    @Override
    public void saveShouldShowPinUpdate(Boolean show) {
        Boolean shouldShowPin = mAppSettings.getShouldShowPinUpdate();
        if (show != shouldShowPin) {
            mAppSettings.setShouldShowPinUpdate(show);
            mAppSettingsDAO.update(mAppSettings);
        }
    }

    @Override
    public Date latestBleedingTracking() {
        return mAppSettings.getLatestBleedingTracking();
    }

    @Override
    public void saveLatestBleedingTracking(Date date) {
        mAppSettings.setLatestBleedingTracking(date);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public List<FilterItem> filterItems() {
        List<FilterItem> filterItems = mAppSettings.getFilterItems();

        if (filterItems == null) {
            filterItems = FilterItem.getAllCategories();
        }

        return filterItems;
    }

    @Override
    public void saveFilterItems(List<FilterItem> filterItems) {
        mAppSettings.setFilterItems(filterItems);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public void saveHiddenCyclePeriods(List<Range<Date>> periods) {
        mAppSettings.setHiddenCyclePeriods(periods);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public List<Range<Date>> hiddenCyclePeriods() {
        return mAppSettings.getHiddenCyclePeriods();
    }

    @Override
    public Boolean trackPeriodEnabled() {
        Boolean value = mAppSettings.getTrackPeriodEnabled();
        if (value == null) {
            return true;
        }
        return value;
    }

    @Override
    public void saveTrackPeriodEnabled(Boolean enabled) {
        mAppSettings.setTrackPeriodEnabled(enabled);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public Boolean periodPredictionEnabled() {
        Boolean value = mAppSettings.getPeriodPredictionEnabled();
        if (value == null) {
            return true;
        }
        return value;
    }

    @Override
    public void savePeriodPredictionEnabled(Boolean enabled) {
        mAppSettings.setPeriodPredictionEnabled(enabled);
        mAppSettingsDAO.update(mAppSettings);
    }

    @Override
    public void removeAll() {
        mAppSettings.getBookmarkIds().clear();
        mAppSettings.getHomeItemsUsed().clear();
        mAppSettings.getHomeItemsNotUsed().clear();
        mAppSettings.setLastDateAutodelete(null);
        mAppSettings.setPrivacyAlreadyShown(false);

        mAppSettings.getHiddenCyclePeriods().clear();
        mAppSettings.setTrackPeriodEnabled(true);
        mAppSettings.setPeriodPredictionEnabled(true);

        //TODO: Add logic to clear reminders

        mAppSettingsDAO.update(mAppSettings);

    }
}
