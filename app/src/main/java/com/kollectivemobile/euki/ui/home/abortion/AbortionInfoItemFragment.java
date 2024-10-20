package com.kollectivemobile.euki.ui.home.abortion;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.ui.common.adapter.AbortionHeaderFooterAdapter;
import com.kollectivemobile.euki.ui.home.content.ContentItemFragment;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;


public class AbortionInfoItemFragment extends ContentItemFragment {
    private RecyclerView rvMain;

    public static AbortionInfoItemFragment newInstance(ContentItem contentItem) {
        return newInstance(contentItem, null);
    }

    public static AbortionInfoItemFragment newInstance(ContentItem contentItem, ContentItem expandContentItem) {
        Bundle args = new Bundle();
        AbortionInfoItemFragment fragment = new AbortionInfoItemFragment();
        fragment.mContentItem = contentItem;
        fragment.mExpandContentItem = expandContentItem;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Manual view binding
        rvMain = view.findViewById(R.id.rv_main);
    }

    @Override
    protected AbstractHeaderFooterWrapperAdapter getHeaderFooterAdapter(RecyclerView.Adapter adapter) {
        return new AbortionHeaderFooterAdapter(getActivity(), adapter, mContentItem, this, this);
    }
}
