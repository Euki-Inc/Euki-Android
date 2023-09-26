package com.kollectivemobile.euki.model;

import java.util.List;

public class CyclePeriodData {
    private Double mAverageCycleLength;
    private Integer mVariation;
    private Double mAveragePeriodLength;
    private Integer mCurrentDayCycle;
    private Integer mMaxCycleLength;
    private List<CyclePeriodItem> mItems;

    public CyclePeriodData(Double averageCycleLength, Integer variation, Double averagePeriodLength, Integer currentDayCycle, Integer maxCycleLength, List<CyclePeriodItem> items) {
        mAverageCycleLength = averageCycleLength;
        mVariation = variation;
        mAveragePeriodLength = averagePeriodLength;
        mCurrentDayCycle = currentDayCycle;
        mMaxCycleLength = maxCycleLength;
        mItems = items;
    }

    public Double getAverageCycleLength() {
        return mAverageCycleLength;
    }

    public void setAverageCycleLength(Double averageCycleLength) {
        mAverageCycleLength = averageCycleLength;
    }

    public Integer getVariation() {
        return mVariation;
    }

    public void setVariation(Integer variation) {
        mVariation = variation;
    }

    public Double getAveragePeriodLength() {
        return mAveragePeriodLength;
    }

    public void setAveragePeriodLength(Double averagePeriodLength) {
        mAveragePeriodLength = averagePeriodLength;
    }

    public Integer getCurrentDayCycle() {
        return mCurrentDayCycle;
    }

    public void setCurrentDayCycle(Integer currentDayCycle) {
        mCurrentDayCycle = currentDayCycle;
    }

    public Integer getMaxCycleLength() {
        return mMaxCycleLength;
    }

    public void setMaxCycleLength(Integer maxCycleLength) {
        mMaxCycleLength = maxCycleLength;
    }

    public List<CyclePeriodItem> getItems() {
        return mItems;
    }

    public void setItems(List<CyclePeriodItem> items) {
        mItems = items;
    }
}
