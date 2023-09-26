package com.kollectivemobile.euki.ui.common.listeners;

import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;

public interface DailyLogViewListener {
    void selectedViewType(DailyLogAdapter.ViewType type, Integer index);
    void categorySelected(Integer index, Integer offset);
    void dataChanged();
    void infoAction();
    void checkIncludeBleedingCycle();
}
