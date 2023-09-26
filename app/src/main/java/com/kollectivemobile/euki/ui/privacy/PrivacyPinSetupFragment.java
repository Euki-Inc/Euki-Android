package com.kollectivemobile.euki.ui.privacy;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.onboarding.PinSetupFragment;
import com.kollectivemobile.euki.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class PrivacyPinSetupFragment extends PinSetupFragment {
    enum PinCodeType {
        CREATE, CONFIRM
    }

    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_error) TextView tvError;

    private PinCodeType mPinCodeType = PinCodeType.CREATE;

    public static PrivacyPinSetupFragment newInstance() {
        Bundle args = new Bundle();
        PrivacyPinSetupFragment fragment = new PrivacyPinSetupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy_pin_code, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvError.setVisibility(View.GONE);
    }

    @Override
    protected void initInjection() {
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void setUIElements() {
        super.setUIElements();
        mPinCodeType = mAppSettingsManager.getPinCode() == null ? PinCodeType.CREATE : PinCodeType.CONFIRM;
        updateCodeType();
        showDots();
    }

    private void updateCodeType() {
        Integer titleResId = 0;
        switch (mPinCodeType) {
            case CREATE:
                titleResId = mAppSettingsManager.getPinCode() == null ? R.string.onboarding_set_pin_title : R.string.set_new_pin;
                break;
            case CONFIRM:
                titleResId = R.string.confirm_existing_pin;
                break;
        }
        tvTitle.setText(getString(titleResId));
        tvTitle.setGravity(titleResId == R.string.onboarding_set_pin_title ? Gravity.LEFT : Gravity.CENTER_HORIZONTAL);
    }

    @Override
    protected void showDots() {
        super.showDots();
        Boolean isCompleted = code.length() == 4;

        if (mPinCodeType == PinCodeType.CREATE && mAppSettingsManager.getPinCode() != null) {
            btnSetPin.setAlpha(isCompleted ? 1.0F : 0.5F);
            btnSetPin.setEnabled(isCompleted);
            btnSetPin.setText(getString(R.string.set_new_pin_button).toUpperCase());
        }

        if (mPinCodeType == PinCodeType.CONFIRM) {
            btnSetPin.setAlpha(isCompleted ? 1.0F : 0.5F);
            btnSetPin.setEnabled(isCompleted);
            btnSetPin.setText(getString(R.string.next).toUpperCase());

            int codeLenght = code.length();

            for (TextView textView : tvDigits) {
                Integer number = Utils.parseInt(textView.getTag().toString());
                String digit = codeLenght > (number - 1) ? "*" : "";
                textView.setText(digit);
            }

            for (View lineView : vDigits) {
                int colorRes = codeLenght > 0 ? R.color.blueberry : R.color.blueberry;
                lineView.setBackgroundColor(ContextCompat.getColor(App.getContext(), colorRes));
            }
        }
    }

    @Override
    public void setPin() {
        if (mPinCodeType == PinCodeType.CONFIRM) {
            String currentPin = mAppSettingsManager.getPinCode();
            if (currentPin != null && currentPin.equals(code)) {
                tvError.setVisibility(View.GONE);
                code = "";
                mPinCodeType = PinCodeType.CREATE;
                updateCodeType();
            } else {
                tvError.setVisibility(View.VISIBLE);
                code = "";
            }
            showDots();
        } else {
            Integer message = code.isEmpty() ? R.string.new_pin_skip : R.string.new_pin_confirmation;
            Toast.makeText(getActivity(), getString(message), Toast.LENGTH_LONG).show();

            if (code.length() == 4) {
                mAppSettingsManager.savePinCode(code);
            }

            getActivity().finish();
        }
    }
}
