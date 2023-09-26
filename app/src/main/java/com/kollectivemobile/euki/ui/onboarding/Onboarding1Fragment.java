package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import butterknife.OnClick;

public class Onboarding1Fragment extends BaseFragment {

    public static Onboarding1Fragment newInstance() {
        Bundle args = new Bundle();
        Onboarding1Fragment fragment = new Onboarding1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_1, container, false);
    }

    @OnClick(R.id.btn_start)
    void start() {
        mInteractionListener.replaceFragment(new Onboarding2Fragment(), true);
    }
}
