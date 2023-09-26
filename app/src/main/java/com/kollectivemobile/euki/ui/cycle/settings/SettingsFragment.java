package com.kollectivemobile.euki.ui.cycle.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class SettingsFragment extends BaseFragment {
    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.stch_track_period) Switch stchTrackPeriod;
    @BindView(R.id.stch_period_prediction) Switch stchPeriodPrediction;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cycle_settings, container, false);
    }

    @Override
    public boolean showBack() {
        return true;
    }

    private void setUIElements() {
        Boolean trackPeriodEnabled = mAppSettingsManager.trackPeriodEnabled();
        stchTrackPeriod.setChecked(trackPeriodEnabled);
        stchPeriodPrediction.setChecked(mAppSettingsManager.periodPredictionEnabled());
        stchPeriodPrediction.setEnabled(trackPeriodEnabled);
    }

    @OnCheckedChanged(R.id.stch_track_period)
    void trackPeriod(boolean checked) {
        mAppSettingsManager.saveTrackPeriodEnabled(checked);
        if (!checked) {
            mAppSettingsManager.savePeriodPredictionEnabled(false);
        }
        setUIElements();
    }

    @OnCheckedChanged(R.id.stch_period_prediction)
    void periodPrediction(boolean checked) {
        mAppSettingsManager.savePeriodPredictionEnabled(checked);
    }
}
