package com.kollectivemobile.euki.model;

import java.util.Date;

public class CyclePeriodItem {
    private Date mInitialDate;
    private Date mEndDate;
    private Integer mDuration;

    public CyclePeriodItem(Date initialDate, Date endDate, Integer duration) {
        mInitialDate = initialDate;
        mEndDate = endDate;
        mDuration = duration;
    }

    public Date getInitialDate() {
        return mInitialDate;
    }

    public void setInitialDate(Date initialDate) {
        mInitialDate = initialDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public Integer getDuration() {
        return mDuration;
    }

    public void setDuration(Integer duration) {
        mDuration = duration;
    }
}
