package com.kollectivemobile.euki.ui.cycle.days;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.CycleManager;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DaysFragment extends BaseFragment implements DaysFragmentListener {
    @Inject CycleManager mCycleManager;

    @BindView(R.id.rv_main) HorizontalCarouselRecyclerView rvMain;
    @BindView(R.id.iv_left) ImageView ivLeft;
    @BindView(R.id.iv_right) ImageView ivRight;
    @BindView(R.id.tv_info) TextView tvInfo;

    private List<CycleDayItem> mItems = new ArrayList<>();

    private DaysFragmentListener mListener;
    private Boolean mIsFirstTime = true;

    public static DaysFragment newInstance(DaysFragmentListener listener) {
        Bundle args = new Bundle();
        DaysFragment fragment = new DaysFragment();
        fragment.mListener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        requestData();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cycle_days, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsFirstTime) {
            mIsFirstTime = false;
            return;
        }

        refreshData();
    }

    private void requestData() {
        mCycleManager.requestCycleItems(new EukiCallback<List<CycleDayItem>>() {
            @Override
            public void onSuccess(final List<CycleDayItem> cycleDayItems) {
                mItems = cycleDayItems;
                rvMain.updateData(cycleDayItems);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvMain.scrollToPosition(cycleDayItems.size());
                        rvMain.smoothScrollToPosition(cycleDayItems.size() + 1);
                    }
                }, 100);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    private void refreshData() {
        mCycleManager.requestCycleItems(new EukiCallback<List<CycleDayItem>>() {
            @Override
            public void onSuccess(final List<CycleDayItem> cycleDayItems) {
                mItems = cycleDayItems;
                rvMain.updateData(cycleDayItems);
                mListener.itemChanged(mItems.get(rvMain.getCurrentIndex()));
                rvMain.smoothScrollToPosition(rvMain.getCurrentIndex() + 1);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    private void setUIElements() {
        rvMain.setListener(this);
        ivRight.setVisibility(View.GONE);

        ivLeft.setOnClickListener(view -> rvMain.smoothScrollToPosition(rvMain.getCurrentIndex() - 1));

        ivRight.setOnClickListener(view -> rvMain.smoothScrollToPosition(rvMain.getCurrentIndex() + 3));
    }

    @Override
    public void itemChanged(CycleDayItem item) {
        ivLeft.setVisibility(item == mItems.get(0) ? View.GONE : View.VISIBLE);
        ivRight.setVisibility(item == mItems.get(mItems.size() - 1) ? View.GONE : View.VISIBLE);

        Integer resId = (item.getCalendarItem() != null && item.getCalendarItem().hasData()) ? R.string.day_summary_data_title : item.isToday() ? R.string.day_summary_empty_today_title : R.string.day_summary_empty_past_title;
        tvInfo.setText(getString(resId));

        mListener.itemChanged(item);
    }
}
