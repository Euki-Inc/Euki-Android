package com.kollectivemobile.euki.ui.cycle;

import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.CycleManager;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.SwipeController;
import com.kollectivemobile.euki.ui.common.SwipeControllerActions;
import com.kollectivemobile.euki.ui.common.adapter.CycleSummaryAdapter;
import com.kollectivemobile.euki.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class CycleSummaryFragment extends BaseFragment {
    @Inject CycleManager mCycleManager;
    @Inject AppSettingsManager mAppSettingsManager;

    @BindView(R.id.rv_main) RecyclerView rvMain;
    @BindView(R.id.tv_cycle_length) TextView tvCycleLength;
    @BindView(R.id.tv_variation) TextView tvVariation;
    @BindView(R.id.tv_period_length) TextView tvPeriodLength;
    @BindView(R.id.tv_current_cycle) TextView tvCurrentCycle;

    private CycleSummaryAdapter mAdapter;
    private CyclePeriodData mCyclePeriodData;
    private SwipeController swipeController = null;

    public static CycleSummaryFragment newInstance() {
        Bundle args = new Bundle();
        CycleSummaryFragment fragment = new CycleSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cycle_summary, container, false);
    }

    private void setUIElements() {
        mAdapter = new CycleSummaryAdapter(getContext());
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                CyclePeriodItem item = mCyclePeriodData.getItems().get(position);
                mCycleManager.deletePeriod(item, new EukiCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        requestData();
                    }

                    @Override
                    public void onError(ServerError serverError) {
                    }
                });
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvMain);

        rvMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void updateUIElements() {
        String cycleLength, variation, periodLength, currentDay;

        if (mCyclePeriodData != null && mCyclePeriodData.getAverageCycleLength() != null) {
            cycleLength = String.format("%.1f", mCyclePeriodData.getAverageCycleLength());
        } else {
            cycleLength = "-";
        }

        if (mCyclePeriodData != null && mCyclePeriodData.getVariation() != null) {
            variation = mCyclePeriodData.getVariation() + "";
        } else {
            variation = "-";
        }

        if (mCyclePeriodData != null && mCyclePeriodData.getAveragePeriodLength() != null) {
            periodLength = String.format("%.1f", mCyclePeriodData.getAveragePeriodLength());
        } else {
            periodLength = "-";
        }

        if (mCyclePeriodData != null && mCyclePeriodData.getCurrentDayCycle() != null) {
            currentDay = mCyclePeriodData.getCurrentDayCycle() + "";
        } else {
            currentDay = "-";
        }

        tvCycleLength.setText(String.format(getString(R.string.cycle_summary_days_format), cycleLength) + Utils.suffix(cycleLength));
        tvVariation.setText(String.format(getString(R.string.cycle_summary_days_format), variation) + Utils.suffix(variation));
        tvPeriodLength.setText(String.format(getString(R.string.cycle_summary_days_format), periodLength) + Utils.suffix(periodLength));
        tvCurrentCycle.setText(String.format(getString(R.string.your_current_cycle_day), currentDay));
    }
    private void requestData() {
        mCycleManager.requestCyclePeriodData(new EukiCallback<CyclePeriodData>() {
            @Override
            public void onSuccess(CyclePeriodData cyclePeriodData) {
                mCyclePeriodData = cyclePeriodData;
                updateUIElements();
                mAdapter.update(cyclePeriodData);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }
}
