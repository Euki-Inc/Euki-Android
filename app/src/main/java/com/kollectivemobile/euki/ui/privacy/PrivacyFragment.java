package com.kollectivemobile.euki.ui.privacy;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.PrivacyContentManager;
import com.kollectivemobile.euki.manager.PrivacyManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.home.search.SearchActivity;
import com.kollectivemobile.euki.ui.main.MainActivity;
import com.kollectivemobile.euki.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PrivacyFragment extends BaseFragment {
    @Inject PrivacyContentManager mPrivacyContentManager;
    @Inject PrivacyManager mPrivacyManager;
    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.swch_recurring) Switch swchRecurring;
    @BindView(R.id.tv_recurring) TextView tvRecurring;
    @BindView(R.id.ll__recurring_container) LinearLayout llRecurring;
    @BindView(R.id.db_recurring) SeekBar sbRecurring;
    @BindView(R.id.b_set_pin) Button bSetPin;
    @BindView(R.id.ll_remove_pin) LinearLayout llRemovePin;
    @BindView(R.id.v_remove_pin) View vRemovePin;

    private Integer mRecurringCounter = 4;
    private Boolean isFirstTime = true;

    public static PrivacyFragment newInstance() {
        Bundle args = new Bundle();
        PrivacyFragment fragment = new PrivacyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePinOptions();
    }

    @Override
    public String getTitle() {
        return getString(R.string.privacy);
    }

    private void setUIElements() {
        sbRecurring.setMax(mRecurringCounter);
        sbRecurring.incrementProgressBy(1);
        sbRecurring.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        swchRecurring.setOnCheckedChangeListener(mOnCheckedChangeListener);
        isFirstTime = mPrivacyManager.getRecurringType() != null;
        updateUIElements();
    }

    private void updateUIElements() {
        Constants.DeleteRecurringType recurringType = mPrivacyManager.getRecurringType();
        tvRecurring.setText(getTitle(recurringType));
        swchRecurring.setChecked(recurringType != null);
        sbRecurring.setProgress(recurringType == null ? 0 : recurringType.ordinal());
        llRecurring.setVisibility(swchRecurring.isChecked() ? View.VISIBLE : View.GONE);
    }

    private String getTitle(Integer index) {
        Constants.DeleteRecurringType recurringType = Constants.DeleteRecurringType.values[index];
        return getTitle(recurringType);
    }

    private String getTitle(Constants.DeleteRecurringType type) {
        if (type == null) {
            return "";
        }
        return getString(type.getTextRestId());
    }

    private void updatePinOptions() {
        Boolean hasPinCode = mAppSettingsManager.getPinCode() != null;
        bSetPin.setText(hasPinCode ? getString(R.string.reset_pin) : getString(R.string.set_pin));
        llRemovePin.setVisibility(hasPinCode ? View.VISIBLE : View.GONE);
        vRemovePin.setVisibility(hasPinCode ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.b_delette_all_data)
    void deleteAllData() {
        Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.confirm_delete_all_now), getString(R.string.ok), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPrivacyManager.removeAllData();
            }
        }).show();
    }

    @OnClick(R.id.b_set_pin)
    void setPin() {
        startActivity(PrivacyPinSetupActivity.makeIntent(getActivity()));
    }

    @OnClick(R.id.b_remove_pin)
    void removePin() {
        Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.confirm_remove_pin), getString(R.string.ok), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAppSettingsManager.savePinCode(null);
                updatePinOptions();
            }
        }).show();
    }

    @OnClick(R.id.b_pricavy_faqs)
    void showPrivacyFaqs() {
        showContentItem(mPrivacyContentManager.getPrivacyFAQs());
    }

    @OnClick(R.id.b_privacy_statement)
    void showPrivacyStatement() {
        showContentItem(mPrivacyContentManager.getPrivacyStatement());
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                tvRecurring.setText(getTitle(progress));

                Constants.DeleteRecurringType recurringType = Constants.DeleteRecurringType.values[progress];
                if (recurringType != mPrivacyManager.getRecurringType()) {
                    mPrivacyManager.saveRecurringData(recurringType);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isFirstTime) {
                isFirstTime = false;
                return;
            }

            if (isChecked) {
                Dialogs.createTwoOptionsDialog(getActivity(), null, getString(R.string.weekly_recurring_info), getString(R.string.ok), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPrivacyManager.saveRecurringData(Constants.DeleteRecurringType.WEEKLY);
                        updateUIElements();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateUIElements();
                    }
                }).show();
            } else {
                mPrivacyManager.saveRecurringData(null);
                updateUIElements();
            }
        }
    };
}
