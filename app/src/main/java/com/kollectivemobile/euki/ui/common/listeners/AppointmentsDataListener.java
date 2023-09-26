package com.kollectivemobile.euki.ui.common.listeners;

import com.kollectivemobile.euki.model.Appointment;

public interface AppointmentsDataListener {
    void saveAppointment(Appointment appointment);
    void cancelAppointment();
    void newAppointmentSelected();
    void showFutureAppointment();
}
