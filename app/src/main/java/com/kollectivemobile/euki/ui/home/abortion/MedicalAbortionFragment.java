package com.kollectivemobile.euki.ui.home.abortion;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AbortionContentManager;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.TextUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MedicalAbortionFragment extends BaseFragment {
    static private final String ScrollViewOffsetKey = "ScrollViewOffsetKey";

    @Inject AbortionContentManager mAbortionContentManager;

    @BindView(R.id.iv_info_first_day) ImageView ivInfoFirstDay;
    @BindView(R.id.tv_info_first_day) TextView tvInfoFirstday;
    @BindView(R.id.tv_pick_date) TextView tvPickDate;
    @BindView(R.id.tv_weeks_pregnant) TextView tvWeeksPregnant;
    @BindView(R.id.ll_pills_options) LinearLayout llPillsOptions;
    @BindView(R.id.iv_pill_options) ImageView ivPillOptions;
    @BindView(R.id.tv_pill_options) TextView tvPillOptions;
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.sv_main) ScrollView svMain;

    private Boolean mFirstDaySelected = false;
    private Boolean mPillOptionsSelected = false;
    private Date mDateSelected = new Date();
    private long mWeeks = -1;

    public static MedicalAbortionFragment newInstance() {
        Bundle args = new Bundle();
        MedicalAbortionFragment fragment = new MedicalAbortionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();

        if (savedInstanceState != null) {
            final int position = savedInstanceState.getInt(ScrollViewOffsetKey);
            svMain.post(new Runnable() {
                public void run() {
                    svMain.scrollTo(0, position);
                }
            });
        }
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abortion_medical, container, false);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (svMain != null) {
            outState.putInt(ScrollViewOffsetKey, svMain.getScrollY());
        }
    }

    @Override
    public boolean showBack() {
        return true;
    }

    private void setUIElements() {
        String content = getString(R.string.medical_abortion_content);
        Map<String, String> linksMap = new HashMap<>();
        linksMap.put(App.getContext().getString(R.string.world_health_organization), "https://www.who.int/publications/i/item/9789240039483");
        linksMap.put(App.getContext().getString(R.string.abortion_on_demand), "https://abortionondemand.org/");
        linksMap.put("ⓘ", "telehealth_popup_info");
        tvContent.setText(TextUtils.getSpannable(content, linksMap, this, null));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        linksMap = new HashMap<>();
        linksMap.put("Drugs.com", "http://www.drugs.com");
        linksMap.put(App.getContext().getString(R.string.women_help_women), "https://womenhelp.org/");
        linksMap.put(App.getContext().getString(R.string.women_on_waves), "https://www.womenonwaves.org/en/page/801/what-do-abortion-pills--mifepristone--ru486--mifeprex--mifegyne--look-like");
        tvPillOptions.setText(TextUtils.getSpannable(getString(R.string.pills_content), linksMap, this, null));
        tvPillOptions.setMovementMethod(LinkMovementMethod.getInstance());

        updateUIElements();
    }

    private void updateUIElements() {
        int resId = mFirstDaySelected ? R.drawable.icon_info_on : R.drawable.icon_info_off;
        ivInfoFirstDay.setImageResource(resId);
        tvInfoFirstday.setVisibility(mFirstDaySelected ? View.VISIBLE : View.GONE);

        resId = mPillOptionsSelected ? R.drawable.icon_info_on : R.drawable.icon_info_off;
        ivPillOptions.setImageResource(resId);
        tvPillOptions.setVisibility(mPillOptionsSelected ? View.VISIBLE : View.GONE);

        llPillsOptions.setVisibility(mWeeks == -1 ? View.GONE : View.VISIBLE);
        tvWeeksPregnant.setVisibility(mWeeks == -1 ? View.GONE : View.VISIBLE);

        if (mWeeks != -1) {
            tvPickDate.setText(DateUtils.toString(mDateSelected, DateUtils.DateLongFormat));
            String weeksText = String.format(getString(R.string.weeks_pregnand_format), DateUtils.weeksDiff(mDateSelected, new Date()));
            tvWeeksPregnant.setText(weeksText);
        }
    }

    private void showAbortionAfter12() {
        Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.abortion_over_12_weeks), getString(R.string.visit_website), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                linkClicked("https://www.who.int/publications/i/item/9789240039483");
            }
        }).show();
    }

    @OnClick(R.id.iv_info_first_day)
    void infoFirstDay() {
        mFirstDaySelected = !mFirstDaySelected;
        updateUIElements();
    }

    @OnClick(R.id.iv_pill_options)
    void pillOptions() {
        mPillOptionsSelected = !mPillOptionsSelected;
        updateUIElements();
    }

    @OnClick(R.id.rl_pick_date)
    void pickDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDateSelected);

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mDateSelected);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR, year);
                        mDateSelected = cal.getTime();
                        mWeeks = DateUtils.weeksDiff(mDateSelected, new Date());
                        updateUIElements();

                        if (mWeeks > 12) {
                            showAbortionAfter12();
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.show();
    }

    @OnClick(R.id.ll_miso)
    void misoSelected() {
        if (mWeeks <= 12) {
            mInteractionListener.replaceFragment(AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionMiso12()), true);
        } else {
            showAbortionAfter12();
        }
    }

    @OnClick(R.id.ll_mife_miso)
    void mifeMisoSelected() {
        if (mWeeks <= 12) {
            mInteractionListener.replaceFragment(AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionMifeMiso12()), true);
        } else {
            showAbortionAfter12();
        }
    }
}
