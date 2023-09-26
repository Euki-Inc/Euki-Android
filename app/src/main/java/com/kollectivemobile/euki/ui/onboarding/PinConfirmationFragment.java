package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class PinConfirmationFragment extends BaseFragment {
    @BindView(R.id.tv_message) TextView tvMessage;

    private Boolean mHasPin;

    public static PinConfirmationFragment newInstance(Boolean hasPin) {
        Bundle args = new Bundle();
        PinConfirmationFragment fragment = new PinConfirmationFragment();
        fragment.mHasPin = hasPin;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_confirmation, container, false);
    }

    private void setUIElements() {
        int textRes = mHasPin ? R.string.pin_code_message : R.string.no_pin_code_message;
        tvMessage.setText(getString(textRes));
    }

    @OnClick(R.id.btn_got_it)
    void gotIt() {
        mInteractionListener.replaceFragment(OnboardingFakeCodeFragment.newInstance(), true);
    }
}
