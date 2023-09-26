package com.kollectivemobile.euki.ui.dailylog;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.calendar.appointments.FutureAppointmentActivity;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.InsetDecoration;
import com.kollectivemobile.euki.ui.common.SwipeController;
import com.kollectivemobile.euki.ui.common.SwipeControllerActions;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogFilterAdapter;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogFilterFooterAdapter;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogFooterAdapter;
import com.kollectivemobile.euki.ui.common.listeners.FilterItemListener;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.advrecyclerview.animator.DraggableItemAnimator;
import com.kollectivemobile.euki.utils.advrecyclerview.animator.GeneralItemAnimator;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class DailyLogFragment extends BaseFragment implements DailyLogAdapter.DailyLogListener, FilterItemListener, View.OnClickListener, MaterialTapTargetPrompt.PromptStateChangeListener {
    @Inject AppSettingsManager mAppSettingsManager;
    @Inject CalendarManager mCalendarManager;
    @BindView(R.id.rv_main) RecyclerView rvMain;
    @BindView(R.id.tv_done) TextView tvDone;

    private DailyLogListener mListener;
    private DailyLogAdapter mAdapter;
    private DailyLogFooterAdapter mFooterAdapter;
    private CalendarItem mCalendarItem;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private DailyLogFilterAdapter mFilterAdapter;
    private DailyLogFilterFooterAdapter mFilterFooterAdapter;
    private List<FilterItem> mFilterItems;
    private Boolean mIsEditing = false;
    private Boolean shouldShowBleedingAlert = false;

    private SwipeController swipeController = null;
    private Integer mTutorialIndex = 0;

    public static DailyLogFragment newInstance(CalendarItem calendarItem, DailyLogListener listener) {
        Bundle args = new Bundle();
        DailyLogFragment fragment = new DailyLogFragment();
        fragment.mCalendarItem = calendarItem;
        fragment.mListener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        mFilterItems = mAppSettingsManager.filterItems();
        shouldShowBleedingAlert = mCalendarManager.shouldShowIncludeCycleAlert(mCalendarItem.getDate());
        setUIElements();
        showCounter();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_log, container, false);
    }

    @Override
    public String getTitle() {
        return DateUtils.toString(mCalendarItem.getDate(), DateUtils.eeeMMMdd);
    }

    private void setUIElements() {
        CalendarItem calendarItem;
        if (mCalendarItem == null) {
            calendarItem = new CalendarItem();
        } else {
            calendarItem = mCalendarItem.copy();
        }
        mCalendarItem = calendarItem;

        mAdapter = new DailyLogAdapter(getActivity(), getContext(), this, mAppSettingsManager.trackPeriodEnabled());
        mFooterAdapter = new DailyLogFooterAdapter(mAdapter, this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        rvMain.setLayoutManager(mLinearLayoutManager);
        rvMain.addItemDecoration(new InsetDecoration(getContext(), InsetDecoration.VERTICAL_LIST));

        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(requireContext(), R.drawable.material_shadow_z3));
        mRecyclerViewDragDropManager.setCheckCanDropEnabled(true);
        mFilterAdapter = new DailyLogFilterAdapter(getContext(), this);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mFilterAdapter);
        mFilterFooterAdapter = new DailyLogFilterFooterAdapter(mWrappedAdapter, this);
        mRecyclerViewDragDropManager.attachRecyclerView(rvMain);

        final RecyclerView.ItemAnimator animator = new DraggableItemAnimator();
        rvMain.setItemAnimator(animator);

        updateAdapter();

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.delete_appointment), getString(R.string.delete), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int foundIndex = 0;
                        for (int index = 0; index < mFilterItems.size(); index++) {
                            if (mFilterItems.get(index).getTitle().equals("appointment")) {
                                foundIndex = index;
                                break;
                            }
                        }

                        int fixedPosition = position - foundIndex - 1;
                        Appointment appointment = mCalendarItem.getAppointments().get(fixedPosition);
                        mCalendarItem.getAppointments().remove(appointment);
                        updateAdapter();
                    }
                }).show();
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

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(new EukiCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        getActivity().finish();
                    }

                    @Override
                    public void onError(ServerError serverError) {
                        getActivity().finish();
                    }
                });
            }
        });
    }

    private void updateAdapter() {
        if (mIsEditing) {
            rvMain.setAdapter(mFilterFooterAdapter);
            mFilterAdapter.update(mFilterItems);
        } else {
            rvMain.setAdapter(mFooterAdapter);
            mAdapter.update(mCalendarItem, mFilterItems);
        }
    }

    private void showCounter() {
        CalendarItem item = mCalendarItem.copy();
        item.setAppointments(mAdapter.getAppointments());
        String text = String.format(getString(R.string.done_tracking), item.dataCount());
        tvDone.setText(text);
    }

    public void save(EukiCallback<Boolean> callback) {
        mCalendarItem.setAppointments(mAdapter.getAppointments());
        mCalendarManager.saveItem(mCalendarItem, callback);
        mCalendarManager.updateLatestBleedingTracking(mCalendarItem.getDate());
    }

    @Override
    public void filterChanged(int position) {
        int lastOnIndex = 0;
        for (int i=0; i<mFilterItems.size(); i++) {
            FilterItem filterItem = mFilterItems.get(i);
            if (!filterItem.getOn()) {
                lastOnIndex = i;
                break;
            }
        }

        FilterItem filterItem = mFilterItems.get(position);
        boolean isOn = !filterItem.getOn();
        filterItem.setOn(isOn);

        mFilterItems.remove(filterItem);
        if (isOn) {
            mFilterItems.add(lastOnIndex, filterItem);
        } else {
            mFilterItems.add(filterItem);
        }

        mFilterAdapter.update(mFilterItems);
        mAppSettingsManager.saveFilterItems(mFilterItems);
        mListener.refreshFilterItems();
    }

    @Override
    public void moveItems(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        FilterItem filterItem = mFilterItems.remove(fromPosition);
        mFilterItems.add(toPosition, filterItem);
        mFilterAdapter.update(mFilterItems);
        mAppSettingsManager.saveFilterItems(mFilterItems);
        mListener.refreshFilterItems();
    }

    @Override
    public void onClick(View view) {
        mIsEditing = !mIsEditing;
        updateAdapter();
    }

    @Override
    public void futureAppointmentSelected() {
        startActivity(FutureAppointmentActivity.makeIntent(getActivity(), mCalendarItem.getDate()));
    }

    @Override
    public void bleedingSelected() {
        if (mAppSettingsManager.shouldShowDailyLogTutorial()) {
            mTutorialIndex = 0;
            mInteractionListener.showTutorial(getString(R.string.track_counter_tutorial_title),
                    getString(R.string.track_counter_tutorial_content),
                    getView().findViewById(R.id.sb_bleeding_product_1).findViewById(R.id.iv_icon),
                    this);
        }
    }

    @Override
    public void categoryOpened(final Integer index) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLinearLayoutManager.scrollToPosition(index);
            }
        }, 200);
    }

    @Override
    public void categoryOpened(final Integer index, final Integer offset) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLinearLayoutManager.scrollToPositionWithOffset(index, offset * -1);
            }
        }, 200);
    }

    @Override
    public void dataChanged() {
        showCounter();
    }

    @Override
    public void checkIncludeBleedingCycle() {
        if (shouldShowBleedingAlert) {
            shouldShowBleedingAlert = false;
            showBleedingAlert();
        }
    }

    private void showBleedingAlert() {
        new AlertDialog.Builder(getActivity())
            .setMessage(getString(R.string.bleeding_tracking_alert_message))
            .setPositiveButton(getString(R.string.bleeding_tracking_alert_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCalendarItem.setIncludeCycleSummary(true);
                    mAdapter.notifyDataSetChanged();
                }
            })
            .setNegativeButton(getString(R.string.bleeding_tracking_alert_cancel), null).show();
    }

    @Override
    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
        View tutorialView = getView().findViewById(R.id.ll_include_cycle);
        ViewGroup.LayoutParams params = tutorialView.getLayoutParams();

        if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
            if (mTutorialIndex == 0) {
                mTutorialIndex = 1;

                tutorialView.setVisibility(View.VISIBLE);
                params.width = (int)getResources().getDimension(R.dimen.dimen_x13_5);

                mInteractionListener.showTutorial(getString(R.string.track_bleeding_tutorial_title),
                        getString(R.string.track_bleeding_tutorial_content), R.id.ll_include_cycle, this);
            } else {
                tutorialView.setVisibility(View.GONE);
                params.width = MATCH_PARENT;
            }
        }
    }
}
