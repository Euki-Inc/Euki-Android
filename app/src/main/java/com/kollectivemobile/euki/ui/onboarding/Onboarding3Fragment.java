package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import butterknife.OnClick;

public class Onboarding3Fragment extends BaseFragment {

    public static Onboarding3Fragment newInstance() {
        Bundle args = new Bundle();
        Onboarding3Fragment fragment = new Onboarding3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_3, container, false);
    }

    @OnClick(R.id.btn_pin)
    void pin() {
        mInteractionListener.replaceFragment(PinSetupFragment.newInstance(), true);
    }

    @OnClick(R.id.btn_no_pin)
    void noPin() {
        mInteractionListener.replaceFragment(TermsAndCondsFragment.newInstance(), true);
    }
}
