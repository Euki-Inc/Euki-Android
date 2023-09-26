package com.kollectivemobile.euki.components;

import com.kollectivemobile.euki.modules.AppModule;
import com.kollectivemobile.euki.modules.DatabaseModule;
import com.kollectivemobile.euki.modules.ManagersModule;
import com.kollectivemobile.euki.ui.calendar.CalendarDayFragment;
import com.kollectivemobile.euki.ui.calendar.CalendarFragment;
import com.kollectivemobile.euki.ui.calendar.CalendarListFragment;
import com.kollectivemobile.euki.ui.calendar.CalendarMonthFragment;
import com.kollectivemobile.euki.ui.calendar.appointments.AppointmentsFragment;
import com.kollectivemobile.euki.ui.calendar.appointments.FutureAppointmentFragment;
import com.kollectivemobile.euki.ui.calendar.reminders.RemindersFragment;
import com.kollectivemobile.euki.ui.calendar.weeklycalendar.WeeklyCalendarFragment;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.cycle.CycleFragment;
import com.kollectivemobile.euki.ui.cycle.CycleSummaryFragment;
import com.kollectivemobile.euki.ui.cycle.DaySummaryFragment;
import com.kollectivemobile.euki.ui.cycle.days.DaysFragment;
import com.kollectivemobile.euki.ui.cycle.settings.SettingsFragment;
import com.kollectivemobile.euki.ui.dailylog.DailyLogFragment;
import com.kollectivemobile.euki.ui.main.MainActivity;
import com.kollectivemobile.euki.ui.onboarding.OnboardingFakeCodeFragment;
import com.kollectivemobile.euki.ui.onboarding.TermsAndCondsFragment;
import com.kollectivemobile.euki.ui.pin.CheckPinFragment;
import com.kollectivemobile.euki.ui.privacy.PrivacyFragment;
import com.kollectivemobile.euki.ui.privacy.PrivacyPinSetupFragment;
import com.kollectivemobile.euki.ui.quiz.QuizFragment;
import com.kollectivemobile.euki.ui.SplashActivity;
import com.kollectivemobile.euki.ui.bookmarks.BookmarksFragment;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.home.HomeFragment;
import com.kollectivemobile.euki.ui.home.abortion.MainAbortionFragment;
import com.kollectivemobile.euki.ui.home.abortion.MedicalAbortionFragment;
import com.kollectivemobile.euki.ui.home.content.ContentItemActivity;
import com.kollectivemobile.euki.ui.home.content.ContentItemFragment;
import com.kollectivemobile.euki.ui.home.search.SearchFragment;
import com.kollectivemobile.euki.ui.onboarding.PinSetupFragment;
import com.kollectivemobile.euki.ui.quiz.ResultFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ManagersModule.class,
                DatabaseModule.class
        }
)

public interface AppComponent {
    void inject(MainActivity activity);
    void inject(BaseFragment fragment);
    void inject(SplashActivity activity);
    void inject(PinSetupFragment fragment);
    void inject(OnboardingFakeCodeFragment fragment);
    void inject(HomeFragment fragment);
    void inject(MainAbortionFragment fragment);
    void inject(MedicalAbortionFragment fragment);
    void inject(ContentItemFragment fragment);
    void inject(BookmarksFragment fragment);
    void inject(ContentItemActivity activity);
    void inject(SearchFragment fragment);
    void inject(QuizFragment fragment);
    void inject(ResultFragment fragment);
    void inject(CalendarMonthFragment fragment);
    void inject(CalendarListFragment fragment);
    void inject(RemindersFragment fragment);
    void inject(DailyLogFragment fragment);
    void inject(CalendarDayFragment fragment);
    void inject(PrivacyFragment fragment);
    void inject(PrivacyPinSetupFragment fragment);
    void inject(BaseActivity activity);
    void inject(CheckPinFragment fragment);
    void inject(AppointmentsFragment fragment);
    void inject(FutureAppointmentFragment fragment);
    void inject(CalendarFragment fragment);
    void inject(TermsAndCondsFragment fragment);
    void inject(WeeklyCalendarFragment fragment);
    void inject(DaySummaryFragment fragment);
    void inject(CycleSummaryFragment fragment);
    void inject(SettingsFragment fragment);
    void inject(DaysFragment fragment);
    void inject(CycleFragment fragment);
}