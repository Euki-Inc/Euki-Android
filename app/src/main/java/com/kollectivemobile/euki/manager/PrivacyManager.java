package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.utils.Constants.*;

public interface PrivacyManager {
    void removeAllData();
    void verifyAutoRemoveData();
    void saveRecurringData(DeleteRecurringType type);
    DeleteRecurringType getRecurringType();
}
