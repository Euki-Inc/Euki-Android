package com.kollectivemobile.euki.ui.cycle;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.converter.SelectableValueConverter;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.model.SelectableValue;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.FragmentManagerHelper;
import com.kollectivemobile.euki.ui.common.adapter.CycleDaySummaryAdapter;
import com.kollectivemobile.euki.ui.cycle.days.DaysFragment;
import com.kollectivemobile.euki.ui.cycle.days.DaysFragmentListener;
import com.kollectivemobile.euki.ui.track.TrackActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DaySummaryFragment extends BaseFragment implements DaysFragmentListener {
    @BindView(R.id.rv_main) RecyclerView rvMain;
    @BindView(R.id.ll_empty) LinearLayout llEmpty;
    @BindView(R.id.iv_empty) ImageView ivEmpty;

    private CycleDaySummaryAdapter mAdapter;
    private CycleDayItem mCurrentItem;

    public static DaySummaryFragment newInstance() {
        Bundle args = new Bundle();
        DaySummaryFragment fragment = new DaySummaryFragment();
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
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cycle_day_summary, container, false);
    }

    private void setUIElements() {
        llEmpty.setVisibility(View.GONE);
        FragmentManagerHelper mFragmentManagerHelper = new FragmentManagerHelper(getActivity().getSupportFragmentManager());
        mFragmentManagerHelper.replace(R.id.fl_header_content, DaysFragment.newInstance(this), false);

        mAdapter = new CycleDaySummaryAdapter(getContext());
        rvMain.setAdapter(mAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvMain.setLayoutManager(layoutManager);
    }

    @Override
    public void itemChanged(CycleDayItem item) {
        mCurrentItem = item;
        List<SelectableValue> items = SelectableValueConverter.convert(item.getCalendarItem());
        mAdapter.update(items);

        if (llEmpty != null) {
            llEmpty.setVisibility(item.getCalendarItem() != null && item.getCalendarItem().hasData() ? View.GONE : View.VISIBLE);
            ivEmpty.setImageResource(item.isToday() ? R.drawable.ic_day_summary_empty : R.drawable.icon_calendar_past);
        }
    }

    @OnClick(R.id.btn_log_now)
    void logNow() {
        if (mCurrentItem == null) {
            return;
        }

        CalendarItem calendarItem = mCurrentItem.getCalendarItem();
        if (calendarItem == null) {
            calendarItem = new CalendarItem(mCurrentItem.getDate());
        }

        startActivity(TrackActivity.makeIntent(getContext(), mCurrentItem.getDate(), calendarItem));
    }
}
