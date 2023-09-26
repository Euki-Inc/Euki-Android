package com.kollectivemobile.euki.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CalendarFilter implements Parcelable {
    private Boolean mBleedingOn;
    private Boolean mEmotionsOn;
    private Boolean mBodyOn;
    private Boolean mSexualActivityOn;
    private Boolean mContraceptionOn;
    private Boolean mTestOn;
    private Boolean mAppointmentOn;
    private Boolean mNoteOn;

    public CalendarFilter() {
        mBleedingOn = false;
        mEmotionsOn = false;
        mBodyOn = false;
        mSexualActivityOn = false;
        mContraceptionOn = false;
        mTestOn = false;
        mAppointmentOn = false;
        mNoteOn = false;
    }

    protected CalendarFilter(Parcel in) {
        byte tmpMBleedingOn = in.readByte();
        mBleedingOn = tmpMBleedingOn == 0 ? null : tmpMBleedingOn == 1;
        byte tmpMEmotionsOn = in.readByte();
        mEmotionsOn = tmpMEmotionsOn == 0 ? null : tmpMEmotionsOn == 1;
        byte tmpMBodyOn = in.readByte();
        mBodyOn = tmpMBodyOn == 0 ? null : tmpMBodyOn == 1;
        byte tmpMSexualActivityOn = in.readByte();
        mSexualActivityOn = tmpMSexualActivityOn == 0 ? null : tmpMSexualActivityOn == 1;
        byte tmpMContraceptionOn = in.readByte();
        mContraceptionOn = tmpMContraceptionOn == 0 ? null : tmpMContraceptionOn == 1;
        byte tmpMTestOn = in.readByte();
        mTestOn = tmpMTestOn == 0 ? null : tmpMTestOn == 1;
        byte tmpMAppointmentOn = in.readByte();
        mAppointmentOn = tmpMAppointmentOn == 0 ? null : tmpMAppointmentOn == 1;
        byte tmpMNoteOn = in.readByte();
        mNoteOn = tmpMNoteOn == 0 ? null : tmpMNoteOn == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mBleedingOn == null ? 0 : mBleedingOn ? 1 : 2));
        dest.writeByte((byte) (mEmotionsOn == null ? 0 : mEmotionsOn ? 1 : 2));
        dest.writeByte((byte) (mBodyOn == null ? 0 : mBodyOn ? 1 : 2));
        dest.writeByte((byte) (mSexualActivityOn == null ? 0 : mSexualActivityOn ? 1 : 2));
        dest.writeByte((byte) (mContraceptionOn == null ? 0 : mContraceptionOn ? 1 : 2));
        dest.writeByte((byte) (mTestOn == null ? 0 : mTestOn ? 1 : 2));
        dest.writeByte((byte) (mAppointmentOn == null ? 0 : mAppointmentOn ? 1 : 2));
        dest.writeByte((byte) (mNoteOn == null ? 0 : mNoteOn ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalendarFilter> CREATOR = new Creator<CalendarFilter>() {
        @Override
        public CalendarFilter createFromParcel(Parcel in) {
            return new CalendarFilter(in);
        }

        @Override
        public CalendarFilter[] newArray(int size) {
            return new CalendarFilter[size];
        }
    };

    public Boolean showAll() {
        if (this.mBleedingOn && this.mEmotionsOn && this.mBodyOn && this.mSexualActivityOn &&
                this.mContraceptionOn && this.mTestOn && this.mAppointmentOn && this.mNoteOn) {
            return true;
        }
        if (!this.mBleedingOn && !this.mEmotionsOn && !this.mBodyOn && !this.mSexualActivityOn &&
                !this.mContraceptionOn && !this.mTestOn && !this.mAppointmentOn && !this.mNoteOn) {
            return true;
        }
        return false;
    }

    public Integer getFiltersCount() {
        Integer count = 0;
        if (mBleedingOn) {
            count++;
        }
        if (mEmotionsOn) {
            count++;
        }
        if (mBodyOn) {
            count++;
        }
        if (mSexualActivityOn) {
            count++;
        }
        if (mContraceptionOn) {
            count++;
        }
        if (mTestOn) {
            count++;
        }
        if (mAppointmentOn) {
            count++;
        }
        if (mNoteOn) {
            count++;
        }
        return count;
    }

    public Boolean getBleedingOn() {
        return mBleedingOn;
    }

    public void setBleedingOn(Boolean bleedingOn) {
        mBleedingOn = bleedingOn;
    }

    public Boolean getEmotionsOn() {
        return mEmotionsOn;
    }

    public void setEmotionsOn(Boolean emotionsOn) {
        mEmotionsOn = emotionsOn;
    }

    public Boolean getBodyOn() {
        return mBodyOn;
    }

    public void setBodyOn(Boolean bodyOn) {
        mBodyOn = bodyOn;
    }

    public Boolean getSexualActivityOn() {
        return mSexualActivityOn;
    }

    public void setSexualActivityOn(Boolean sexualActivityOn) {
        mSexualActivityOn = sexualActivityOn;
    }

    public Boolean getContraceptionOn() {
        return mContraceptionOn;
    }

    public void setContraceptionOn(Boolean contraceptionOn) {
        mContraceptionOn = contraceptionOn;
    }

    public Boolean getTestOn() {
        return mTestOn;
    }

    public void setTestOn(Boolean testOn) {
        mTestOn = testOn;
    }

    public Boolean getAppointmentOn() {
        return mAppointmentOn;
    }

    public void setAppointmentOn(Boolean appointmentOn) {
        mAppointmentOn = appointmentOn;
    }

    public Boolean getNoteOn() {
        return mNoteOn;
    }

    public void setNoteOn(Boolean noteOn) {
        mNoteOn = noteOn;
    }
}
