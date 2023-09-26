package com.kollectivemobile.euki.model.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.utils.Constants.*;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "AppSettings")
public class AppSettings {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "show_onboarding_screens")
    private Boolean mShowOnboardingScreens;
    @ColumnInfo(name = "pin_code")
    private String mPinCode;
    @ColumnInfo(name = "bookmark_ids")
    private List<String> mBookmarkIds;
    @ColumnInfo(name = "home_items_used")
    private List<String> mHomeItemsUsed;
    @ColumnInfo(name = "home_items_not_used")
    private List<String> mHomeItemsNotUsed;
    @ColumnInfo(name = "delete_recurring_type")
    private DeleteRecurringType mDeleteRecurringType;
    @ColumnInfo(name = "last_date_auto_delete")
    private Date mLastDateAutodelete;
    @ColumnInfo(name = "show_tabbar_tutorial")
    private Boolean mShowTabbarTutorial;
    @ColumnInfo(name = "show_dailyy_log_tutorial")
    private Boolean mShowDailyLogTutorial;
    @ColumnInfo(name = "show_cycle_summary_tutorial")
    private Boolean mShowCycleSummaryTutorial;
    @ColumnInfo(name = "show_calendar_tutorial")
    private Boolean mShowCalendarTutorial;
    @ColumnInfo(name = "daily_log_counter")
    private Integer mDailyLogCounter;
    @ColumnInfo(name = "privacy_already_shown")
    private Boolean mPrivacyAlreadyShown;
    @ColumnInfo(name = "main_titles")
    private Map<String, String> mMainTitles;
    @ColumnInfo(name = "should_show_pin_update")
    private Boolean mShouldShowPinUpdate;
    @ColumnInfo(name = "latest_bleeding_tracking")
    private Date mLatestBleedingTracking;
    @ColumnInfo(name = "filter_items")
    private List<FilterItem> mFilterItems;
    @ColumnInfo(name = "hidden_cycle_periods")
    private List<Range<Date>> mHiddenCyclePeriods;
    @ColumnInfo(name = "track_period_enabled")
    private Boolean mTrackPeriodEnabled;
    @ColumnInfo(name = "period_prediction_enabled")
    private Boolean mPeriodPredictionEnabled;

    public AppSettings() {
        this.mShowOnboardingScreens = true;
        this.mShowTabbarTutorial = true;
        this.mShowDailyLogTutorial = true;
        this.mShowCycleSummaryTutorial = true;
        this.mShowCalendarTutorial = false;
        this.mDailyLogCounter = 0;
        this.mPrivacyAlreadyShown = false;
        this.mShouldShowPinUpdate = true;

        this.mLatestBleedingTracking = null;
        this.mFilterItems = null;
        this.mHiddenCyclePeriods = new ArrayList<>();
        this.mTrackPeriodEnabled = true;
        this.mPeriodPredictionEnabled = true;

        this.mBookmarkIds = new ArrayList<>();
        this.mHomeItemsUsed = new ArrayList<>();
        this.mHomeItemsNotUsed = new ArrayList<>();
        this.mMainTitles = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getShowOnboardingScreens() {
        return mShowOnboardingScreens;
    }

    public void setShowOnboardingScreens(Boolean showOnboardingScreens) {
        mShowOnboardingScreens = showOnboardingScreens;
    }

    public String getPinCode() {
        return mPinCode;
    }

    public void setPinCode(String pinCode) {
        mPinCode = pinCode;
    }

    public List<String> getBookmarkIds() {
        return mBookmarkIds;
    }

    public void setBookmarkIds(List<String> bookmarkIds) {
        mBookmarkIds = bookmarkIds;
    }

    public List<String> getHomeItemsUsed() {
        return mHomeItemsUsed;
    }

    public void setHomeItemsUsed(List<String> homeItemsUsed) {
        mHomeItemsUsed = homeItemsUsed;
    }

    public List<String> getHomeItemsNotUsed() {
        return mHomeItemsNotUsed;
    }

    public void setHomeItemsNotUsed(List<String> homeItemsNotUsed) {
        mHomeItemsNotUsed = homeItemsNotUsed;
    }

    public DeleteRecurringType getDeleteRecurringType() {
        return mDeleteRecurringType;
    }

    public void setDeleteRecurringType(DeleteRecurringType deleteRecurringType) {
        mDeleteRecurringType = deleteRecurringType;
    }

    public Date getLastDateAutodelete() {
        return mLastDateAutodelete;
    }

    public void setLastDateAutodelete(Date lastDateAutodelete) {
        mLastDateAutodelete = lastDateAutodelete;
    }

    public Boolean getShowTabbarTutorial() {
        return mShowTabbarTutorial;
    }

    public void setShowTabbarTutorial(Boolean showTabbarTutorial) {
        mShowTabbarTutorial = showTabbarTutorial;
    }

    public Boolean getShowDailyLogTutorial() {
        return mShowDailyLogTutorial;
    }

    public void setShowDailyLogTutorial(Boolean showDailyLogTutorial) {
        mShowDailyLogTutorial = showDailyLogTutorial;
    }

    public Boolean getShowCycleSummaryTutorial() {
        return mShowCycleSummaryTutorial;
    }

    public void setShowCycleSummaryTutorial(Boolean showCycleSummaryTutorial) {
        mShowCycleSummaryTutorial = showCycleSummaryTutorial;
    }

    public Boolean getShowCalendarTutorial() {
        return mShowCalendarTutorial;
    }

    public void setShowCalendarTutorial(Boolean showCalendarTutorial) {
        mShowCalendarTutorial = showCalendarTutorial;
    }

    public Integer getDailyLogCounter() {
        return mDailyLogCounter;
    }

    public void setDailyLogCounter(Integer dailyLogCounter) {
        mDailyLogCounter = dailyLogCounter;
    }

    public Boolean getPrivacyAlreadyShown() {
        return mPrivacyAlreadyShown;
    }

    public void setPrivacyAlreadyShown(Boolean privacyAlreadyShown) {
        mPrivacyAlreadyShown = privacyAlreadyShown;
    }

    public Map<String, String> getMainTitles() {
        return mMainTitles;
    }

    public void setMainTitles(Map<String, String> mainTitles) {
        mMainTitles = mainTitles;
    }

    public Boolean getShouldShowPinUpdate() {
        return mShouldShowPinUpdate;
    }

    public void setShouldShowPinUpdate(Boolean shouldShowPinUpdate) {
        mShouldShowPinUpdate = shouldShowPinUpdate;
    }

    public Date getLatestBleedingTracking() {
        return mLatestBleedingTracking;
    }

    public void setLatestBleedingTracking(Date latestBleedingTracking) {
        mLatestBleedingTracking = latestBleedingTracking;
    }

    public List<FilterItem> getFilterItems() {
        return mFilterItems;
    }

    public void setFilterItems(List<FilterItem> filterItems) {
        mFilterItems = filterItems;
    }

    public List<Range<Date>> getHiddenCyclePeriods() {
        return mHiddenCyclePeriods;
    }

    public void setHiddenCyclePeriods(List<Range<Date>> hiddenCyclePeriods) {
        mHiddenCyclePeriods = hiddenCyclePeriods;
    }

    public Boolean getTrackPeriodEnabled() {
        return mTrackPeriodEnabled;
    }

    public void setTrackPeriodEnabled(Boolean trackPeriodEnabled) {
        mTrackPeriodEnabled = trackPeriodEnabled;
    }

    public Boolean getPeriodPredictionEnabled() {
        return mPeriodPredictionEnabled;
    }

    public void setPeriodPredictionEnabled(Boolean periodPredictionEnabled) {
        mPeriodPredictionEnabled = periodPredictionEnabled;
    }
}
