package com.kollectivemobile.euki.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.EmptyFragment;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.calendar.CalendarFragment;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.views.NavBar;
import com.kollectivemobile.euki.ui.common.views.NavBarListener;
import com.kollectivemobile.euki.ui.cycle.CycleFragment;
import com.kollectivemobile.euki.ui.home.HomeFragment;

import com.kollectivemobile.euki.ui.privacy.PrivacyFragment;
import com.kollectivemobile.euki.ui.track.TrackActivity;
import com.kollectivemobile.euki.utils.Utils;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends BaseActivity {
    @Inject CalendarManager mCalendarManager;
    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.bottom_nav_bar) NavBar nbMain;

    private Unbinder mUnbinder;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        mShowEukiLogo = true;
        ((App)getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    void setUIElements(){
        nbMain.setListener(new NavBarListener() {
            @Override
            public Boolean onTabSelected(int position) {
                if (position == 2) {
                    showDailyLog();
                    return false;
                }

                changeFragment(position);
                return true;
            }
        });

        nbMain.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateOverlays();
    }

    void validateOverlays() {
        if (mAppSettingsManager.shouldShowTabbarTutorial()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTabBarTutorial(0);
                }
            }, 1000);
        } else if (mAppSettingsManager.shouldShowPinUpdate()) {
            showPinCodeUpdate();
        }

        mAppSettingsManager.saveShouldShowPinUpdate(false);
    }

    void showPinCodeUpdate() {
        String currentPin = mAppSettingsManager.getPinCode();
        if (currentPin == null) {
            return;
        }

        String fakePin = currentPin.equals("1111") ? "2222" : "1111";

        String title = getString(R.string.pin_code_update_alert_title);
        String message = String.format(getString(R.string.pin_code_update_alert_message), fakePin);

        Dialogs.createSimpleDialog(this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    void showTabBarTutorial(final Integer index) {
        String title = Utils.getLocalized("tabbar_tutorial_title_" + index);
        String text = Utils.getLocalized("tabbar_tutorial_content_" + index);
        showTutorial(title, text, nbMain.getViewAtPosition(index), new MaterialTapTargetPrompt.PromptStateChangeListener() {
            @Override
            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    if (index < 4) {
                        showTabBarTutorial(index + 1);
                    } else {
                        if (mFragment instanceof CycleFragment) {
                            ((CycleFragment)mFragment).showTutorial();
                        }
                    }
                }
            }
        });
    }

    void changeFragment(int position){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        Fragment fragment = new EmptyFragment();

        switch (position){
            case 0:
                fragment = CycleFragment.newInstance();
                break;
            case 1:
                fragment = CalendarFragment.newInstance();
                break;
            case 2:
                break;
            case 3:
                fragment = HomeFragment.newInstance();
                break;
            case 4:
                fragment = PrivacyFragment.newInstance();
                break;
        }

        mFragment = fragment;
        replaceFragment(fragment, false);
    }

    void showDailyLog() {
        final Date date = new Date();
        mCalendarManager.getCalendarItem(date, new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem calendarItem) {
                startActivity(TrackActivity.makeIntent(MainActivity.this, date, calendarItem));
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    public void changeTabbarSelected(int index) {
        nbMain.setCurrentItem(index);
    }

    static public Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
