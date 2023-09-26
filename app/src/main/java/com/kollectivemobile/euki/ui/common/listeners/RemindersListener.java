package com.kollectivemobile.euki.ui.common.listeners;

public interface RemindersListener {
    void newSelected();
    void dayTimeSelected();
    void repeatSelected();
    void reminderSelected(int position);
    void saveSelected(String title, String text);
    void cancelSelected();
}
