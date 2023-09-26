package com.kollectivemobile.euki.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.OnClick;

public class TermsAndCondsFragment extends BaseFragment {
    @Inject AppSettingsManager mAppSettingsManager;

    public static TermsAndCondsFragment newInstance() {
        Bundle args = new Bundle();
        TermsAndCondsFragment fragment = new TermsAndCondsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_terms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
    }

    @OnClick(R.id.btn_agree)
    void agree() {
        mAppSettingsManager.saveShowOnboardinScreens(false);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.terms_of_use_accept), null).show();
    }
}
