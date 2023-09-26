package com.kollectivemobile.euki.ui.common.listeners;

public interface AppointmentsListener {
    void newSelected();
    void dayTimeSelected();
    void alertSelected();
    void appointmentSelected(int position);
    void saveSelected();
    void cancelSelected();
}
