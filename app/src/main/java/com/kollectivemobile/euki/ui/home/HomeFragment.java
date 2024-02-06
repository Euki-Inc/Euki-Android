package com.kollectivemobile.euki.ui.home;

import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.manager.HomeManager;
import com.kollectivemobile.euki.manager.ReminderManager;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.model.TileItem;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.bookmarks.BookmarksActivity;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.adapter.TilesAdapter;
import com.kollectivemobile.euki.ui.common.adapter.TilesHeaderFooterAdapter;
import com.kollectivemobile.euki.ui.common.holder.TileHolder;
import com.kollectivemobile.euki.ui.home.abortion.MainAbortionActivity;
import com.kollectivemobile.euki.ui.home.content.ContentItemActivity;
import com.kollectivemobile.euki.ui.home.search.SearchActivity;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.advrecyclerview.animator.DraggableItemAnimator;
import com.kollectivemobile.euki.utils.advrecyclerview.animator.GeneralItemAnimator;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements TileHolder.TileListener, TilesHeaderFooterAdapter.HeaderListener, TilesHeaderFooterAdapter.FooterListener {
    public static Boolean ShouldShowAbortion = false;

    @Inject
    HomeManager mHomeManager;
    @Inject
    ContentManager mContentManager;
    @Inject
    ReminderManager mReminderManager;

    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    private GridLayoutManager mLayoutManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private TilesAdapter mTilesAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private TilesHeaderFooterAdapter mHeaderFooterAdapter;

    private List<TileItem> mAllItems;
    private List<TileItem> mUsedItems;
    private List<TileItem> mNotUsedItems;
    private Boolean mIsEditing = false;
    private Boolean mIsLock = false;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        createItems();
        setUIElements();
        setHasOptionsMenu(true);
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
            case R.id.item_search_text:
                startActivity(SearchActivity.makeIntent(getActivity()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPendingNotification();
        checkShowAbortion();
    }

    private void createItems() {
        mAllItems = new ArrayList<>();
        mHomeManager.getMainItems();
        Pair<List<TileItem>, List<TileItem>> orderItems = mHomeManager.getHomeOrder();
        mUsedItems = orderItems.first;
        mNotUsedItems = orderItems.second;
        mAllItems.addAll(mUsedItems);
        mAllItems.addAll(mNotUsedItems);
    }

    private void setUIElements() {
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();

        mTilesAdapter = new TilesAdapter(getActivity(), this);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mTilesAdapter);

        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable((NinePatchDrawable) ContextCompat.getDrawable(requireContext(), R.drawable.material_shadow_z3));
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(250);

        mRecyclerViewDragDropManager.setDragStartItemAnimationDuration(250);
        mRecyclerViewDragDropManager.setDraggingItemAlpha(0.8f);

        GeneralItemAnimator animator = new DraggableItemAnimator();

        mHeaderFooterAdapter = new TilesHeaderFooterAdapter(mWrappedAdapter, this, this);
        rvMain.setAdapter(mHeaderFooterAdapter);
        rvMain.setItemAnimator(animator);

        mRecyclerViewDragDropManager.attachRecyclerView(rvMain);
        mTilesAdapter.update(mUsedItems, mIsEditing);
        updateLayout();
    }

    private void checkPendingNotification() {
        mReminderManager.pendingNotify(new EukiCallback<ReminderItem>() {
            @Override
            public void onSuccess(ReminderItem reminderItem) {
                mHeaderFooterAdapter.updateHeader(reminderItem);
            }

            @Override
            public void onError(ServerError serverError) {
                mHeaderFooterAdapter.updateHeader(null);
            }
        });
    }

    private void checkShowAbortion() {
        if (ShouldShowAbortion) {
            ShouldShowAbortion = false;
            startActivity(MainAbortionActivity.makeIntent(getActivity()));
        }
    }

    @Override
    public void tileSelected(int position) {
        TileItem tileItem = mAllItems.get(position);
        if (mIsEditing) {
            showEditTitle(tileItem);
        } else {
            showItem(tileItem);
        }
    }

    private void showEditTitle(final TileItem tileItem) {
        String title;
        if (tileItem.getTitle() != null && !tileItem.getTitle().isEmpty()) {
            title = tileItem.getTitle();
        } else {
            title = Utils.getLocalized(tileItem.getId());
        }

        Dialogs.showTextDialog(getActivity(), R.string.change_name, R.string.name, title, new EukiCallback<String>() {
            @Override
            public void onSuccess(String title) {
                if (title != null && !title.isEmpty()) {
                    tileItem.setTitle(title);
                } else {
                    tileItem.setTitle(null);
                }
                mHomeManager.saveTitle(tileItem, tileItem.getTitle());
                updateItems();
            }

            @Override
            public void onError(ServerError serverError) {

            }
        });
    }

    private void showItem(TileItem tileItem) {
        EukiCallback<ContentItem> callback = new EukiCallback<ContentItem>() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                Intent intent = ContentItemActivity.makeIntent(getActivity(), contentItem);
                startActivity(intent);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        };

        switch (tileItem.getId()) {
            case "abortion":
                startActivity(MainAbortionActivity.makeIntent(getActivity()));
                break;
            case "contraception":
                mContentManager.getContraception(callback);
                break;
            case "sexuality":
                mContentManager.getSexRelationships(callback);
                break;
            case "miscarriage":
                mContentManager.getMiscarriage(callback);
                break;
            case "pregnancy_options":
                mContentManager.getPregnancyOptions(callback);
                break;
            case "stis":
                mContentManager.getSTIs(callback);
                break;
            case "menstruation":
                mContentManager.getMenstruationOptions(callback);
                break;
        }
    }

    @Override
    public void actionSelected(int position) {
        if (mIsLock) {
            return;
        }

        mIsLock = true;
        TileItem tileItem = mAllItems.get(position);
        Boolean isUsed = tileItem.getUsed();
        tileItem.setUsed(!isUsed);

        if (isUsed) {
            mUsedItems.remove(tileItem);
            mNotUsedItems.add(tileItem);
        } else {
            mNotUsedItems.remove(tileItem);
            mUsedItems.add(tileItem);
        }

        updateItems();
        mIsLock = false;
    }

    @Override
    public void orderChanged(List<TileItem> tileItems) {
        int usedCount = mUsedItems.size();
        mUsedItems.clear();

        for (int i = 0; i < usedCount; i++) {
            mUsedItems.add(tileItems.get(i));
        }

        updateArrays();
    }

    @Override
    public void editStarted() {
        if (mIsEditing) {
            return;
        }

        mIsEditing = true;
        mTilesAdapter.update(mAllItems, mIsEditing);
        mHeaderFooterAdapter.update(mIsEditing);
        updateItems();
        updateLayout();
    }

    @Override
    public void bookmarksPressed() {
        startActivity(BookmarksActivity.makeIntent(getActivity()));
    }

    @Override
    public void editPressed() {
        mIsEditing = !mIsEditing;
        updateItems();
        updateLayout();
    }

    private void updateItems() {
        updateArrays();
        mTilesAdapter.update(mAllItems, mIsEditing);
        mHeaderFooterAdapter.update(mIsEditing);
    }

    private void updateArrays() {
        mAllItems.clear();
        mAllItems.addAll(mUsedItems);

        if (mIsEditing) {
            mAllItems.addAll(mNotUsedItems);
        }

        mHomeManager.saveOrder(mUsedItems, mNotUsedItems);
    }

    private void updateLayout() {
        int spanCount = 2;
        if (!mIsEditing && mUsedItems.size() <= 3) {
            spanCount = 1;
        }

        if (mLayoutManager == null || mLayoutManager.getSpanCount() != spanCount) {
            mLayoutManager = new GridLayoutManager(requireContext(), spanCount, RecyclerView.VERTICAL, false);
            mLayoutManager.setSpanCount(spanCount);
            rvMain.setLayoutManager(mLayoutManager);
            mHeaderFooterAdapter.update(mIsEditing);
            mHeaderFooterAdapter.setupFullSpanForGridLayoutManager(rvMain);

            int margin = spanCount == 1 ? 0 : Utils.dpFromInt(6);
            rvMain.setPadding(margin, margin, margin, margin);
        }
    }

    @Override
    public void dismissPressed() {
        checkPendingNotification();
    }

    @Override
    public void goToDailyLogPressed() {
        //TODO: Add logic to open the dqily log
    }
}
