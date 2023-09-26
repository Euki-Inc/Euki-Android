package com.kollectivemobile.euki.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Appointment {
    private String mId;
    private String mTitle;
    private String mLocation;
    private Date mDate;
    private Integer mAlertOption;
    private Boolean mAlertShown;

    public Appointment() {
        this.mId = UUID.randomUUID().toString();
        this.mAlertOption = 0;
    }

    public Appointment(Date date) {
        this.mId = UUID.randomUUID().toString();
        this.mAlertOption = 0;
        this.mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Integer getAlertOption() {
        return mAlertOption;
    }

    public void setAlertOption(Integer alertOption) {
        mAlertOption = alertOption;
    }

    public Boolean getAlertShown() {
        return mAlertShown;
    }

    public void setAlertShown(Boolean alertShown) {
        mAlertShown = alertShown;
    }

    public Date getAlertDate() {
        if (mAlertOption == null || mDate == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        switch (mAlertOption) {
            case 1:
                calendar.add(Calendar.MINUTE, -30);
                break;
            case 2:
            case 3:
            case 4:
                calendar.add(Calendar.HOUR, -1 * (mAlertOption - 1));
                break;
            case 5:
            case 6:
            case 7:
                calendar.add(Calendar.HOUR, -24 * (mAlertOption - 4));
                break;
        }
        return calendar.getTime();
    }

    public boolean isDataCompleted() {
        if (mTitle == null || mTitle.isEmpty() || mLocation == null || mLocation.isEmpty() || mDate == null) {
            return false;
        }
        return true;
    }

    public Appointment copy() {
        Appointment appointment = new Appointment();
        appointment.mId = mId;
        appointment.mTitle = mTitle;
        appointment.mLocation = mLocation;
        appointment.mDate = mDate;
        appointment.mAlertOption = mAlertOption;
        appointment.mAlertShown = mAlertShown;
        return appointment;
    }
}
