package com.kollectivemobile.euki.utils;

import com.kollectivemobile.euki.R;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public enum DeleteRecurringType {
        WEEKLY, WEEKLY2, MONTHLY, MONTHLY3, YEARLY;
        private static final List<Integer> sTitleRes = Arrays.asList(R.string.weekly, R.string.weekly_2, R.string.monthly, R.string.monthly_3, R.string.yearly);
        public static final DeleteRecurringType values[] = values();

        public Integer getTextRestId() {
            return sTitleRes.get(ordinal());
        }
    }

    public enum CalendarCategory {
        BLEEDING, EMOTIONS, SEXUALACTIVITY, CONTRACEPTION, TEST, APPOITMENT, NOTE;
        public static final CalendarCategory values[] = values();
    }

    public enum BleedingSize {
        SPOTING, LIGHT, MEDIUM, HEAVY;
        public static final BleedingSize values[] = values();

        public String getText() {
            return String.format("bleeding_size_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_bleeding_size_%d", ordinal() + 1);
        }
    }

    public enum BleedingProducts {
        REUSABLEPAD, DISPOSABLEPAD, TAMPON, MENSTRUALCUP, MENSTRUALDISC, PERIODUNDERWEAR, LINER;
        public static final BleedingProducts values[] = values();

        public String getText() {
            return String.format("menstruation_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_menstruation_%d", ordinal() + 1);
        }
    }

    public enum BleedingClots {
        SMALL, BIg;
        public static final BleedingClots values[] = values();

        public String getText() {
            return String.format("bleeding_clots_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_bleeding_clot_%d", ordinal() + 1);
        }
    }

    public enum Emotions {
        CALM, STRESSED, UNMOTIVATED, SAD, HAPPY, IRRITABLE, ANGRY, ENERGETIC, HORNY;
        public static final Emotions values[] = values();

        public String getText() {
            return String.format("emotions_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_emotions_%d", ordinal() + 1);
        }
    }

    public enum Body {
        ACNE, BLOATING, CRAMPS, CRAVINGS, DISCHARGE, FATIGUE, FEVER, HEADACHE, ITCHY, NAUSEAS, SEVEREPAIN, STOMACHACHE, TENDERBREASTS, OVULATION;
        public static final Body values[] = values();

        public String getText() {
            return String.format("body_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_body_%d", ordinal() + 1);
        }
    }

    public enum SexualProtectionSTI {
        PROTECTED, UNPROTECTED;
        public static final SexualProtectionSTI values[] = values();

        public String getText() {
            return String.format("protection_sti_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_sexual_activity_sti_%d", ordinal() + 1);
        }
    }

    public enum SexualProtectionPregnancy {
        PROTECTED, UNPROTECTED;
        public static final SexualProtectionPregnancy values[] = values();

        public String getText() {
            return String.format("protection_pregnancy_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_sexual_activity_pregnancy_%d", ordinal() + 1);
        }
    }

    public enum SexualProtectionOther {
        MASTURBATION, ORALSEX, ORGASM, SEXTOYS, ANALSEX;
        public static final SexualProtectionOther values[] = values();

        public String getText() {
            return String.format("protection_other_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_sexual_activity_other_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionPills {
        TOOK, MISSED, DOUBLE;
        public static final ContraceptionPills values[] = values();

        public String getText() {
            return String.format("contraception_pill_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_pill_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionDailyOther {
        CONDOM, DIAPHRAGM, CERVICALCUP, SPONGE, SPERMICIDE, WITHDRAWAL, EMERGENCY;
        public static final ContraceptionDailyOther values[] = values();

        public String getText() {
            return String.format("contraception_other_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_other_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionIUD {
        NEW, CHECKEDSTRINGS, REMOVED, SHOT,
        ;
        public static final ContraceptionIUD values[] = values();

        public String getText() {
            return String.format("contraception_uid_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_iud_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionImplant {
        NEW, REMOVED;
        public static final ContraceptionImplant values[] = values();

        public String getText() {
            return String.format("contraception_implant_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_implant_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionPatch {
        NEW, REMOVED;
        public static final ContraceptionPatch values[] = values();

        public String getText() {
            return String.format("contraception_patch_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_patch_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionRing {
        NEW, REMOVED;
        public static final ContraceptionRing values[] = values();

        public String getText() {
            return String.format("contraception_ring_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_contraception_ring_%d", ordinal() + 1);
        }
    }

    public enum ContraceptionLongTermOther {
        INJECTION;
        public static final ContraceptionLongTermOther values[] = values();

        public String getText() {
            return "icon_contraception_injection";
        }

        public String getImageName() {
            return "icon_contraception_injection";
        }
    }

    public enum ContraceptionShot {
        SHOT;
        public static final ContraceptionShot values[] = values();

        public String getText() {
            return "shot";
        }

        public String getImageName() {
            return "icon_contraception_shot_1";
        }
    }

    public enum TestSTI {
        POSITIVE, NEGATIVE;
        public static final TestSTI values[] = values();

        public String getText() {
            return String.format("test_sti_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_test_sti_%d", ordinal() + 1);
        }
    }

    public enum TestPregnancy {
        POSITIVE, NEGATIVE;
        public static final TestPregnancy values[] = values();

        public String getText() {
            return String.format("test_pregnancy_%d", ordinal() + 1);
        }

        public String getImageName() {
            return String.format("icon_test_pregnancy_%d", ordinal() + 1);
        }
    }

    public static final List<Integer> sAlertOptions = Arrays.asList(R.string.none,
            R.string.option_30_mins, R.string.option_1_hr, R.string.option_2_hrs, R.string.option_3_hrs,
            R.string.option_1_day, R.string.option_2_day, R.string.option_3_day);

    public static final Integer sDaysBleedingTrackingAlert = 14;

    public static final Integer sMinDaysBetweenPeriods = 14;
    public static final Integer sMinDaysToShowNextPeriod = 3;
}
