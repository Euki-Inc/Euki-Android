package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

public interface CycleManager {
    void requestCycleItems(EukiCallback<List<CycleDayItem>> callback);
    void requestCyclePeriodData(EukiCallback<CyclePeriodData> callback);
    void deletePeriod(CyclePeriodItem item, EukiCallback<Boolean> callback);
}
