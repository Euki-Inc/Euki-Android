package com.kollectivemobile.euki.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.utils.Constants.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConstantsConverter {
    @TypeConverter
    public static DeleteRecurringType toDeleteRecurringType(Integer value) {
        if (value == null) return null;
        return DeleteRecurringType.values()[value];
    }

    @TypeConverter
    public static Integer fromDeleteRecurringType(DeleteRecurringType deleteRecurringType) {
        if (deleteRecurringType == null) return null;
        return deleteRecurringType.ordinal();
    }
    @TypeConverter
    public static CalendarCategory toCalendarType(Integer value) {
        if (value == null) return null;
        return CalendarCategory.values()[value];
    }

    @TypeConverter
    public static Integer fromCalendarType(CalendarCategory calendarCategory) {
        if (calendarCategory == null) return null;
        return calendarCategory.ordinal();
    }

    @TypeConverter
    public static BleedingSize toBleedingSize(Integer value) {
        if (value == null) return null;
        return BleedingSize.values()[value];
    }

    @TypeConverter
    public static Integer fromBleedingSize(BleedingSize bleedingSize) {
        if (bleedingSize == null) return null;
        return bleedingSize.ordinal();
    }

    @TypeConverter
    public static List<Emotions> toEmotionsList(String value) {
        if (value == null) return null;
        List<Emotions> emotionsList = new ArrayList<>();
        for (Integer integer : ArraysConverter.toList(value)) {
            emotionsList.add(Emotions.values[integer]);
        }
        return emotionsList;
    }

    @TypeConverter
    public static String fromEmotionsList(List<Emotions> emotionsList) {
        if (emotionsList == null) return null;
        List<Integer> emotionValues = new ArrayList<>();
        for (Emotions emotions : emotionsList) {
            emotionValues.add(emotions.ordinal());
        }
        return ArraysConverter.fromList(emotionValues);
    }

    @TypeConverter
    public static List<Body> toBodyList(String value) {
        if (value == null) return null;
        List<Body> bodyList = new ArrayList<>();
        for (Integer integer : ArraysConverter.toList(value)) {
            bodyList.add(Body.values[integer]);
        }
        return bodyList;
    }

    @TypeConverter
    public static String fromBodyList(List<Body> bodyList) {
        if (bodyList == null) return null;
        List<Integer> bodyValues = new ArrayList<>();
        for (Body body : bodyList) {
            bodyValues.add(body.ordinal());
        }
        return ArraysConverter.fromList(bodyValues);
    }

    @TypeConverter
    public static ContraceptionPills toContraceptionPills(Integer value) {
        if (value == null) return null;
        return ContraceptionPills.values()[value];
    }

    @TypeConverter
    public static Integer fromContraceptionPills(ContraceptionPills contraceptionPills) {
        if (contraceptionPills == null) return null;
        return contraceptionPills.ordinal();
    }

    @TypeConverter
    public static List<ContraceptionDailyOther> toContraceptionDailyOther(String value) {
        if (value == null) return null;
        List<ContraceptionDailyOther> contraceptionList = new ArrayList<>();
        for (Integer integer : ArraysConverter.toList(value)) {
            contraceptionList.add(ContraceptionDailyOther.values[integer]);
        }
        return contraceptionList;
    }

    @TypeConverter
    public static String fromContraceptionDailyOther(List<ContraceptionDailyOther> contraceptionsList) {
        if (contraceptionsList == null) return null;
        List<Integer> contraceptionValues = new ArrayList<>();
        for (ContraceptionDailyOther contraception : contraceptionsList) {
            contraceptionValues.add(contraception.ordinal());
        }
        return ArraysConverter.fromList(contraceptionValues);
    }

    @TypeConverter
    public static ContraceptionIUD toContraceptionIUD(Integer value) {
        if (value == null) return null;
        return ContraceptionIUD.values()[value];
    }

    @TypeConverter
    public static Integer fromContraceptionIUD(ContraceptionIUD contraceptionIUD) {
        if (contraceptionIUD == null) return null;
        return contraceptionIUD.ordinal();
    }

    @TypeConverter
    public static ContraceptionImplant toContraceptionImplant(Integer value) {
        if (value == null) return null;
        return ContraceptionImplant.values()[value];
    }

    @TypeConverter
    public static Integer fromContraceptionImplant(ContraceptionImplant contraceptionImplant) {
        if (contraceptionImplant == null) return null;
        return contraceptionImplant.ordinal();
    }

    @TypeConverter
    public static ContraceptionPatch toContraceptionPatch(Integer value) {
        if (value == null) return null;
        return ContraceptionPatch.values()[value];
    }

    @TypeConverter
    public static Integer fromContraceptionPatch(ContraceptionPatch contraceptionPatch) {
        if (contraceptionPatch == null) return null;
        return contraceptionPatch.ordinal();
    }

    @TypeConverter
    public static ContraceptionRing toContraceptionRing(Integer value) {
        if (value == null) return null;
        return ContraceptionRing.values()[value];
    }

    @TypeConverter
    public static Integer fromContraceptionRing(ContraceptionRing contraceptionRing) {
        if (contraceptionRing == null) return null;
        return contraceptionRing.ordinal();
    }

    @TypeConverter
    public static List<ContraceptionLongTermOther> toContraceptionLongTermOther(String value) {
        if (value == null) return null;
        List<ContraceptionLongTermOther> contraceptionList = new ArrayList<>();
        for (Integer integer : ArraysConverter.toList(value)) {
            contraceptionList.add(ContraceptionLongTermOther.values[integer]);
        }
        return contraceptionList;
    }

    @TypeConverter
    public static String fromContraceptionLongTermOther(List<ContraceptionLongTermOther> contraceptionsList) {
        if (contraceptionsList == null) return null;
        List<Integer> contraceptionValues = new ArrayList<>();
        for (ContraceptionLongTermOther contraception : contraceptionsList) {
            contraceptionValues.add(contraception.ordinal());
        }
        return ArraysConverter.fromList(contraceptionValues);
    }

    @TypeConverter
    public static TestSTI toTestSTI(Integer value) {
        if (value == null) return null;
        return TestSTI.values()[value];
    }

    @TypeConverter
    public static Integer fromTestSTI(TestSTI testSTI) {
        if (testSTI == null) return null;
        return testSTI.ordinal();
    }

    @TypeConverter
    public static TestPregnancy toTestPregnancy(Integer value) {
        if (value == null) return null;
        return TestPregnancy.values()[value];
    }

    @TypeConverter
    public static Integer fromTestPregnancy(TestPregnancy testPregnancy) {
        if (testPregnancy == null) return null;
        return testPregnancy.ordinal();
    }

    @TypeConverter
    public static List<Appointment> toAppointments(String value) {
        if (value == null) return null;
        Type type = new TypeToken<List<Appointment>>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromAppointments(List<Appointment> appointments) {
        if (appointments == null) return null;
        return new Gson().toJson(appointments);
    }
}
