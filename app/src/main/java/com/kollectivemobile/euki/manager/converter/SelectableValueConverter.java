package com.kollectivemobile.euki.manager.converter;

import com.kollectivemobile.euki.model.SelectableValue;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectableValueConverter {
    public static List<SelectableValue> convert(CalendarItem calendarItem) {
        List<SelectableValue> selectItems = new ArrayList<>();

        if (calendarItem == null) {
            return selectItems;
        }

        //Bleeding Items

        if (calendarItem.getBleedingSize() != null) {
            selectItems.add(new SelectableValue(calendarItem.getBleedingSize().getImageName(), calendarItem.getBleedingSize().getText(), 0));
        }
        for (int index = 0; index < calendarItem.getBleedingClotsCounter().size(); index++) {
            int counter = calendarItem.getBleedingClotsCounter().get(index);
            if (counter > 0) {
                Constants.BleedingClots bleedingClots = Constants.BleedingClots.values[index];
                selectItems.add(new SelectableValue(bleedingClots.getImageName(), bleedingClots.getText(), counter));
            }
        }
        for (int index = 0; index<calendarItem.getBleedingProductsCounter().size(); index++) {
            int counter = calendarItem.getBleedingProductsCounter().get(index);
            if (counter > 0) {
                Constants.BleedingProducts bleedingProducts = Constants.BleedingProducts.values[index];
                selectItems.add(new SelectableValue(bleedingProducts.getImageName(), bleedingProducts.getText(), counter));
            }
        }

        //Emotions Items

        for (Constants.Emotions emotions : calendarItem.getEmotions()) {
            selectItems.add(new SelectableValue(emotions.getImageName(), emotions.getText(), 0));
        }

        //Body Items

        for (Constants.Body body : calendarItem.getBody()) {
            selectItems.add(new SelectableValue(body.getImageName(), body.getText(), 0));
        }

        //Sexual Activity Items

        for (int index = 0; index < calendarItem.getSexualProtectionSTICounter().size(); index++) {
            int counter = calendarItem.getSexualProtectionSTICounter().get(index);
            if (counter > 0) {
                Constants.SexualProtectionSTI sexualProtectionSTI = Constants.SexualProtectionSTI.values[index];
                selectItems.add(new SelectableValue(sexualProtectionSTI.getImageName(), sexualProtectionSTI.getText() + "_list", counter));
            }
        }
        for (int index = 0; index < calendarItem.getSexualProtectionPregnancyCounter().size(); index++) {
            int counter = calendarItem.getSexualProtectionPregnancyCounter().get(index);
            if (counter > 0) {
                Constants.SexualProtectionPregnancy sexualProtectionPregnancy = Constants.SexualProtectionPregnancy.values[index];
                selectItems.add(new SelectableValue(sexualProtectionPregnancy.getImageName(), sexualProtectionPregnancy.getText() + "_list", counter));
            }
        }
        for (int index = 0; index < calendarItem.getSexualOtherCounter().size(); index++) {
            int counter = calendarItem.getSexualOtherCounter().get(index);
            if (counter > 0) {
                Constants.SexualProtectionOther sexualProtectionOther = Constants.SexualProtectionOther.values[index];
                selectItems.add(new SelectableValue(sexualProtectionOther.getImageName(), sexualProtectionOther.getText(), counter));
            }
        }

        //Contraception Items

        if (calendarItem.getContraceptionPills() != null) {
            selectItems.add(new SelectableValue(calendarItem.getContraceptionPills().getImageName(), calendarItem.getContraceptionPills().getText() + "_list", 0));
        }
        for (Constants.ContraceptionDailyOther contraceptionDailyOther : calendarItem.getContraceptionDailyOther()) {
            selectItems.add(new SelectableValue(contraceptionDailyOther.getImageName(), contraceptionDailyOther.getText(), 0));
        }
        if (calendarItem.getContraceptionIUD() != null) {
            selectItems.add(new SelectableValue(calendarItem.getContraceptionIUD().getImageName(), calendarItem.getContraceptionIUD().getText() + "_list", 0));
        }
        if (calendarItem.getContraceptionImplant() != null) {
            selectItems.add(new SelectableValue(calendarItem.getContraceptionImplant().getImageName(), calendarItem.getContraceptionImplant().getText() + "_list", 0));
        }
        if (calendarItem.getContraceptionRing() != null) {
            selectItems.add(new SelectableValue(calendarItem.getContraceptionRing().getImageName(), calendarItem.getContraceptionRing().getText() + "_list", 0));
        }

        if (calendarItem.getContraceptionShot() != null) {
            selectItems.add(new SelectableValue(calendarItem.getContraceptionShot().getImageName(), calendarItem.getContraceptionShot().getText(), 0));
        }

        for (Constants.ContraceptionLongTermOther contraceptionLongTermOther : calendarItem.getContraceptionLongTermOthers()) {
            selectItems.add(new SelectableValue(contraceptionLongTermOther.getImageName(), contraceptionLongTermOther.getText(), 0));
        }

        //Test Items

        if (calendarItem.getTestSTI() != null) {
            selectItems.add(new SelectableValue(calendarItem.getTestSTI().getImageName(), calendarItem.getTestSTI().getText() + "_list", 0));
        }
        if (calendarItem.getTestPregnancy() != null) {
            selectItems.add(new SelectableValue(calendarItem.getTestPregnancy().getImageName(), calendarItem.getTestPregnancy().getText() + "_list", 0));
        }

        //Apointment Item

        if (calendarItem.hasAppointment()) {
            selectItems.add(new SelectableValue("icon_tracking_appointment", "appointment", 0));
        }

        //Note Item

        if (calendarItem.hasNote()) {
            selectItems.add(new SelectableValue("icon_tracking_note", "note", 0));
        }

        return selectItems;
    }
}
