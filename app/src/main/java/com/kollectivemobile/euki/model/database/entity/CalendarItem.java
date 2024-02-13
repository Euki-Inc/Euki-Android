package com.kollectivemobile.euki.model.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.utils.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity(tableName = "CalendarItem")
public class CalendarItem {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "date")
    private Date mDate;
    @ColumnInfo(name = "bleeding_size")
    private BleedingSize mBleedingSize;
    @ColumnInfo(name = "bleeding_products_counter")
    private List<Integer> mBleedingProductsCounter;
    @ColumnInfo(name = "bleeding_clots_counter")
    private List<Integer> mBleedingClotsCounter;
    @ColumnInfo(name = "emotions")
    private List<Emotions> mEmotions;
    @ColumnInfo(name = "body")
    private List<Body> mBody;
    @ColumnInfo(name = "sexual_protection_sti_counter")
    private List<Integer> mSexualProtectionSTICounter;
    @ColumnInfo(name = "sexual_protection_pregnancy_counter")
    private List<Integer> mSexualProtectionPregnancyCounter;
    @ColumnInfo(name = "sexual_other_counter")
    private List<Integer> mSexualOtherCounter;
    @ColumnInfo(name = "contraception_pills")
    private ContraceptionPills mContraceptionPills;
    @ColumnInfo(name = "contraception_daily_other")
    private List<ContraceptionDailyOther> mContraceptionDailyOther;
    @ColumnInfo(name = "contraception_iud")
    private ContraceptionIUD mContraceptionIUD;
    @ColumnInfo(name = "contraception_implant")
    private ContraceptionImplant mContraceptionImplant;
    @ColumnInfo(name = "contraception_patch")
    private ContraceptionPatch mContraceptionPatch;
    @ColumnInfo(name = "contraception_ring")
    private ContraceptionRing mContraceptionRing;
    @ColumnInfo(name = "contraception_long_term_others")
    private List<ContraceptionLongTermOther> mContraceptionLongTermOthers;
    @ColumnInfo(name = "tests_sti")
    private TestSTI mTestSTI;
    @ColumnInfo(name = "tests_pregnancy")
    private TestPregnancy mTestPregnancy;
    @ColumnInfo(name = "appointments")
    private List<Appointment> mAppointments;
    @ColumnInfo(name = "note")
    private String mNote;
    @ColumnInfo(name = "filter_items")
    private List<FilterItem> mFilterItems;
    @ColumnInfo(name = "include_cycle_summary")
    private Boolean mIncludeCycleSummary;

    public CalendarItem() {
        this.mDate = new Date();
        this.mBleedingProductsCounter = new ArrayList<>(Collections.nCopies(7, 0));
        this.mBleedingClotsCounter = new ArrayList<>(Collections.nCopies(2, 0));
        this.mEmotions = new ArrayList<>();
        this.mBody = new ArrayList<>();
        this.mSexualProtectionSTICounter = new ArrayList<>(Collections.nCopies(2, 0));
        this.mSexualProtectionPregnancyCounter = new ArrayList<>(Collections.nCopies(2, 0));
        this.mSexualOtherCounter = new ArrayList<>(Collections.nCopies(5, 0));
        this.mContraceptionDailyOther = new ArrayList<>();
        this.mContraceptionLongTermOthers = new ArrayList<>();
        this.mAppointments = new ArrayList<>();
        this.mFilterItems = FilterItem.getAllCategories();
        this.mIncludeCycleSummary = false;
    }

    public CalendarItem(Date date) {
        this();
        this.mDate = date;
    }

    public boolean hasBleeding() {
        if (mBleedingClotsCounter != null) {
            for (Integer count : mBleedingClotsCounter) {
                if (count > 0) {
                    return true;
                }
            }
        }

        if (mBleedingProductsCounter != null) {
            for (Integer count : mBleedingProductsCounter) {
                if (count > 0) {
                    return true;
                }
            }
        }
        return mBleedingSize != null;
    }

    public boolean hasPeriod() {
        if (mIncludeCycleSummary == null || !mIncludeCycleSummary) {
            return false;
        }

        if (mBleedingClotsCounter != null) {
            for (Integer count : mBleedingClotsCounter) {
                if (count > 0) {
                    return true;
                }
            }
        }

        return mBleedingSize != null;
    }

    public boolean hasEmotions() {
        return this.mEmotions.size() > 0;
    }

    public boolean hasBody() {
        return this.mBody.size() > 0;
    }

    public boolean hasSexualActivity() {
        if (mSexualProtectionSTICounter != null) {
            for (Integer count : mSexualProtectionSTICounter) {
                if (count > 0) {
                    return true;
                }
            }
        }

        if (mSexualProtectionPregnancyCounter != null) {
            for (Integer count : mSexualProtectionPregnancyCounter) {
                if (count > 0) {
                    return true;
                }
            }
        }

        if (mSexualOtherCounter != null) {
            for (Integer count : mSexualOtherCounter) {
                if (count > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasContraception() {
        return this.mContraceptionPills != null || (this.mContraceptionDailyOther != null && this.mContraceptionDailyOther.size() > 0) ||
                this.mContraceptionIUD != null || this.mContraceptionImplant != null || this.mContraceptionPatch != null ||
                this.mContraceptionRing != null;
    }

    public boolean hasTest() {
        return this.mTestSTI != null || this.mTestPregnancy != null;
    }

    public boolean hasAppointment() {
        if (this.mAppointments == null) {
            return false;
        }
        return this.mAppointments.size() > 0;
    }

    public boolean hasNote() {
        return this.mNote != null && !this.mNote.isEmpty();
    }

    public boolean hasData() {
        return hasBleeding() || hasEmotions() || hasBody() || hasSexualActivity() || hasContraception() ||
                hasTest() || hasAppointment() || hasNote();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public BleedingSize getBleedingSize() {
        return mBleedingSize;
    }

    public void setBleedingSize(BleedingSize bleedingSize) {
        mBleedingSize = bleedingSize;
    }

    public List<Integer> getBleedingProductsCounter() {
        if (mBleedingProductsCounter == null) {
            return new ArrayList<>(Collections.nCopies(7, 0));
        }
        return mBleedingProductsCounter;
    }

    public void setBleedingProductsCounter(List<Integer> bleedingProductsCounter) {
        mBleedingProductsCounter = bleedingProductsCounter;
    }

    public List<Integer> getBleedingClotsCounter() {
        if (mBleedingClotsCounter == null) {
            return new ArrayList<>(Collections.nCopies(2, 0));
        }
        return mBleedingClotsCounter;
    }

    public void setBleedingClotsCounter(List<Integer> bleedingClotsCounter) {
        mBleedingClotsCounter = bleedingClotsCounter;
    }

    public List<Emotions> getEmotions() {
        return mEmotions;
    }

    public void setEmotions(List<Emotions> emotions) {
        mEmotions = emotions;
    }

    public List<Body> getBody() {
        return mBody;
    }

    public void setBody(List<Body> body) {
        mBody = body;
    }

    public List<Integer> getSexualProtectionSTICounter() {
        return mSexualProtectionSTICounter;
    }

    public void setSexualProtectionSTICounter(List<Integer> sexualProtectionSTICounter) {
        mSexualProtectionSTICounter = sexualProtectionSTICounter;
    }

    public List<Integer> getSexualProtectionPregnancyCounter() {
        return mSexualProtectionPregnancyCounter;
    }

    public void setSexualProtectionPregnancyCounter(List<Integer> sexualProtectionPregnancyCounter) {
        mSexualProtectionPregnancyCounter = sexualProtectionPregnancyCounter;
    }

    public List<Integer> getSexualOtherCounter() {
        return mSexualOtherCounter;
    }

    public void setSexualOtherCounter(List<Integer> sexualOtherCounter) {
        mSexualOtherCounter = sexualOtherCounter;
    }

    public ContraceptionPills getContraceptionPills() {
        return mContraceptionPills;
    }

    public void setContraceptionPills(ContraceptionPills contraceptionPills) {
        mContraceptionPills = contraceptionPills;
    }

    public List<ContraceptionDailyOther> getContraceptionDailyOther() {
        return mContraceptionDailyOther;
    }

    public void setContraceptionDailyOther(List<ContraceptionDailyOther> contraceptionDailyOther) {
        mContraceptionDailyOther = contraceptionDailyOther;
    }

    public ContraceptionIUD getContraceptionIUD() {
        return mContraceptionIUD;
    }

    public void setContraceptionIUD(ContraceptionIUD contraceptionIUD) {
        mContraceptionIUD = contraceptionIUD;
    }

    public ContraceptionImplant getContraceptionImplant() {
        return mContraceptionImplant;
    }

    public void setContraceptionImplant(ContraceptionImplant contraceptionImplant) {
        mContraceptionImplant = contraceptionImplant;
    }

    public ContraceptionPatch getContraceptionPatch() {
        return mContraceptionPatch;
    }

    public void setContraceptionPatch(ContraceptionPatch contraceptionPatch) {
        mContraceptionPatch = contraceptionPatch;
    }

    public ContraceptionRing getContraceptionRing() {
        return mContraceptionRing;
    }

    public void setContraceptionRing(ContraceptionRing contraceptionRing) {
        mContraceptionRing = contraceptionRing;
    }

    public List<ContraceptionLongTermOther> getContraceptionLongTermOthers() {
        return mContraceptionLongTermOthers;
    }

    public void setContraceptionLongTermOthers(List<ContraceptionLongTermOther> contraceptionLongTermOthers) {
        mContraceptionLongTermOthers = contraceptionLongTermOthers;
    }

    public TestSTI getTestSTI() {
        return mTestSTI;
    }

    public void setTestSTI(TestSTI testSTI) {
        mTestSTI = testSTI;
    }

    public TestPregnancy getTestPregnancy() {
        return mTestPregnancy;
    }

    public void setTestPregnancy(TestPregnancy testPregnancy) {
        mTestPregnancy = testPregnancy;
    }

    public List<Appointment> getAppointments() {
        return mAppointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        mAppointments = appointments;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public List<FilterItem> getFilterItems() {
        return mFilterItems;
    }

    public void setFilterItems(List<FilterItem> filterItems) {
        mFilterItems = filterItems;
    }

    public Boolean getIncludeCycleSummary() {
        if (mIncludeCycleSummary == null) {
            return false;
        }
        return mIncludeCycleSummary;
    }

    public void setIncludeCycleSummary(Boolean includeCycleSummary) {
        mIncludeCycleSummary = includeCycleSummary;
    }

    public CalendarItem copy() {
        CalendarItem calendarItem = new CalendarItem();
        calendarItem.id = id;
        calendarItem.mDate = mDate;
        calendarItem.mBleedingSize = mBleedingSize;
        calendarItem.mBleedingProductsCounter = new ArrayList<>(mBleedingProductsCounter);
        calendarItem.mBleedingClotsCounter = new ArrayList<>(mBleedingClotsCounter != null ? mBleedingClotsCounter : new ArrayList<>(Collections.nCopies(2, 0)));
        calendarItem.mEmotions = new ArrayList<>(mEmotions);
        calendarItem.mBody = new ArrayList<>(mBody);
        calendarItem.mSexualProtectionSTICounter = new ArrayList<>(mSexualProtectionSTICounter);
        calendarItem.mSexualProtectionPregnancyCounter = new ArrayList<>(mSexualProtectionPregnancyCounter);
        calendarItem.mSexualOtherCounter = new ArrayList<>(mSexualOtherCounter);
        calendarItem.mContraceptionPills = mContraceptionPills;
        calendarItem.mContraceptionDailyOther = new ArrayList<>(mContraceptionDailyOther);
        calendarItem.mContraceptionIUD = mContraceptionIUD;
        calendarItem.mContraceptionImplant = mContraceptionImplant;
        calendarItem.mContraceptionPatch = mContraceptionPatch;
        calendarItem.mContraceptionRing = mContraceptionRing;
        calendarItem.mContraceptionLongTermOthers = new ArrayList<>(mContraceptionLongTermOthers);
        calendarItem.mTestSTI = mTestSTI;
        calendarItem.mTestPregnancy = mTestPregnancy;
        calendarItem.mAppointments = new ArrayList<>(mAppointments);
        calendarItem.mNote = mNote;
        calendarItem.mFilterItems = new ArrayList<>(mFilterItems);
        calendarItem.mIncludeCycleSummary = mIncludeCycleSummary;
        return calendarItem;
    }

    // Counts logic

    private Integer bleedingCount() {
        Integer count = 0;

        if (mBleedingSize != null) {
            count++;
        }

        if (mBleedingClotsCounter != null) {
            for (Integer value : mBleedingClotsCounter) {
                if (value > 0) {
                    count++;
                }
            }
        }

        return count;
    }

    private Integer sexualActivityCount() {
        Integer count = 0;

        for (Integer value : mSexualProtectionSTICounter) {
            if (value > 0) {
                count++;
            }
        }
        for (Integer value : mSexualProtectionPregnancyCounter) {
            if (value > 0) {
                count++;
            }
        }
        for (Integer value : mSexualOtherCounter) {
            if (value > 0) {
                count++;
            }
        }

        return count;
    }

    private Integer contraceptionCount() {
        Integer count = 0;

        if (mContraceptionPills != null) {
            count++;
        }
        if (!mContraceptionDailyOther.isEmpty()) {
            count++;
        }
        if (mContraceptionIUD != null) {
            count++;
        }
        if (mContraceptionImplant != null) {
            count++;
        }
        if (mContraceptionPatch != null) {
            count++;
        }
        if (mContraceptionRing != null) {
            count++;
        }
        if (!mContraceptionLongTermOthers.isEmpty()) {
            count++;
        }

        return count;
    }

    private Integer testCount() {
        Integer count = 0;

        if (mTestSTI != null) {
            count++;
        }
        if (mTestPregnancy != null) {
            count++;
        }

        return count;
    }

    public Integer dataCount() {
        return bleedingCount() +
                mEmotions.size() +
                mBody.size() +
                sexualActivityCount() +
                contraceptionCount() +
                testCount() +
                (hasAppointment() ? 1 : 0) +
                (hasNote() ? 1 : 0);
    }
}
