package com.kollectivemobile.euki;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.kollectivemobile.euki.components.AppComponent;
import com.kollectivemobile.euki.components.DaggerAppComponent;
import com.kollectivemobile.euki.modules.AppModule;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class App extends Application {
    private static Context sContext;
    private AppComponent mAppComponent;
    private Boolean wasBackground = false;

    @Override
    public void onCreate(){
        super.onCreate();
        sContext = this;

        mAppComponent = DaggerAppComponent.builder()

                .appModule(new AppModule(this))
                .build();

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    public Boolean getWasBackground() {
        if (wasBackground) {
            wasBackground = false;
            return true;
        }

        return false;
    }

    public static Context getContext(){
        return sContext;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        private int numStarted = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (numStarted == 0) {
                Log.d("App To Foreground", "-----");
            }
            numStarted++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) activity;
                if (baseActivity.getAppSettingsManager().getPinCode() != null) {
                    activity.getWindow().getDecorView().findViewById(android.R.id.content).setAlpha(1);
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) activity;
                if (baseActivity.getAppSettingsManager().getPinCode() != null) {
                    activity.getWindow().getDecorView().findViewById(android.R.id.content).setAlpha(0);
                }
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0) {
                wasBackground = true;
                Log.d("App To Background", "-----");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };
}
