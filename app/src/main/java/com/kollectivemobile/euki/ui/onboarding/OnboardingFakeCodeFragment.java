package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindViews;
import butterknife.OnClick;

public class OnboardingFakeCodeFragment extends BaseFragment {
    @Inject AppSettingsManager mAppSettingsManager;

    @BindViews({R.id.tv_digit_1, R.id.tv_digit_2, R.id.tv_digit_3, R.id.tv_digit_4})
    List<TextView> textViews;

    public static OnboardingFakeCodeFragment newInstance() {
        OnboardingFakeCodeFragment fragment = new OnboardingFakeCodeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjection();
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_pin, container, false);
    }

    protected void initInjection() {
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
    }

    private void setUIElements() {
        String code = (mAppSettingsManager.getPinCode().equals("1111")) ? "2222" : "1111";
        String letter = code.substring(0, 1);
        for (TextView textView : textViews) {
            textView.setText(letter);
        }
    }

    @OnClick(R.id.btn_next)
    void next() {
        mInteractionListener.replaceFragment(TermsAndCondsFragment.newInstance(), true);
    }
}
