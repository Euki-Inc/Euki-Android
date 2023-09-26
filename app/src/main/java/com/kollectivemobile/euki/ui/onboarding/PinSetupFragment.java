package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class PinSetupFragment extends BaseFragment {
    @Inject AppSettingsManager mAppSettingsManager;

    @BindViews({R.id.tv_digit_1, R.id.tv_digit_2, R.id.tv_digit_3, R.id.tv_digit_4}) protected List<TextView> tvDigits;
    @BindViews({R.id.v_digit_1, R.id.v_digit_2, R.id.v_digit_3, R.id.v_digit_4}) protected List<View> vDigits;
    @BindView(R.id.btn_set_pin) protected Button btnSetPin;

    protected String code = "";

    public static PinSetupFragment newInstance() {
        Bundle args = new Bundle();
        PinSetupFragment fragment = new PinSetupFragment();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_pin_code, container, false);
    }

    protected void initInjection() {
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
    }

    protected void setUIElements() {
        showDots();
    }

    protected void showDots() {
        int textRes = code.length() != 4 ? R.string.skip : R.string.set_pin;
        btnSetPin.setText(textRes);

        int codeLenght = code.length();

        for (TextView textView : tvDigits) {
            Integer number = Utils.parseInt(textView.getTag().toString());
            String digit = codeLenght > (number - 1) ? Character.toString(code.charAt(number - 1)) : "";
            textView.setText(digit);
        }

        for (View lineView : vDigits) {
            int colorRes = codeLenght > 0 ? R.color.blueberry : R.color.blueberry;
            lineView.setBackgroundColor(ContextCompat.getColor(App.getContext(), colorRes));
        }
    }

    @OnClick(R.id.btn_set_pin)
    public void setPin() {
        if (code.length() == 4) {
            mAppSettingsManager.savePinCode(code);
            mInteractionListener.replaceFragment(PinConfirmationFragment.newInstance(code.length() == 4), true);
        } else {
            mInteractionListener.replaceFragment(TermsAndCondsFragment.newInstance(), true);
        }
    }

    @OnClick({R.id.tv_button_0, R.id.tv_button_1, R.id.tv_button_2, R.id.tv_button_3,
            R.id.tv_button_4, R.id.tv_button_5, R.id.tv_button_6, R.id.tv_button_7,
            R.id.tv_button_8, R.id.tv_button_9, R.id.tv_button_back})
    void buttonPressed(View view) {
        Integer number = Utils.parseInt(view.getTag().toString());

        if (number != null) {
            if (code.length() == 4) {
                return;
            }
            code = code + number;
        } else {
            if (code.length() > 0) {
                code = code.substring(0, code.length() - 1);
            }
        }
        showDots();
    }
}
