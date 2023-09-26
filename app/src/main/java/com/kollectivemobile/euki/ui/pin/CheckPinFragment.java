package com.kollectivemobile.euki.ui.pin;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.main.MainActivity;
import com.kollectivemobile.euki.ui.onboarding.PinSetupFragment;
import com.kollectivemobile.euki.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class CheckPinFragment extends PinSetupFragment {
    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.fake_screen) View fakeScreen;
    @BindView(R.id.tv_error) TextView tvError;

    private Boolean shouldShowMainViewController = false;

    public static CheckPinFragment newInstance(Boolean shouldShowMainViewController) {
        Bundle args = new Bundle();
        CheckPinFragment fragment = new CheckPinFragment();
        fragment.shouldShowMainViewController = shouldShowMainViewController;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_check, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        fakeScreen.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        code = "";
        showDots();
    }

    @Override
    protected void initInjection() {
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void setPin() {
        String currentPin = mAppSettingsManager.getPinCode();
        String fakePin = currentPin.equals("1111") ? "2222" : "1111";

        if (currentPin != null && currentPin.equals(code)) {
            if (shouldShowMainViewController) {
                startActivity(MainActivity.makeIntent(getActivity()));
            } else {
                getActivity().finish();
            }
        } else if (code.equals(fakePin)) {
            fakeScreen.setVisibility(View.VISIBLE);
        } else {
            tvError.setVisibility(View.VISIBLE);
            code = "";
            showDots();
        }
    }

    @Override
    protected void showDots() {
        super.showDots();
        Boolean isCompleted = code.length() == 4;
        btnSetPin.setAlpha(isCompleted ? 1.0F : 0.5F);
        btnSetPin.setEnabled(isCompleted);
        btnSetPin.setText(getString(R.string.go));

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
