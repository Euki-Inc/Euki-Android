package com.kollectivemobile.euki.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.ui.main.MainActivity;
import com.kollectivemobile.euki.ui.onboarding.OnboardingActivity;
import com.kollectivemobile.euki.ui.pin.CheckPinActivity;

import javax.inject.Inject;

public class SplashActivity extends Activity {
    @Inject AppSettingsManager mAppSettingsManager;
    @Inject ContentManager mContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((App)getApplication()).getAppComponent().inject(this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                showActivity();
            }
        }, 1000);
    }

    private void showActivity(){
        Intent intent;
        if (mAppSettingsManager.shouldShowOnboardingScreens()) {
            intent = new Intent(this, OnboardingActivity.class);
        } else if (mAppSettingsManager.getPinCode() != null) {
            intent = CheckPinActivity.makeIntent(this, true);
        } else {
            intent = MainActivity.makeIntent(this);
        }
        startActivity(intent);
        finish();
    }
}
