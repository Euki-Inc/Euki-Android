package com.kollectivemobile.euki.ui.home.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AbortionContentManager;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.home.abortion.AbortionInfoItemFragment;
import com.kollectivemobile.euki.ui.home.abortion.MedicalAbortionFragment;

import javax.inject.Inject;

public class ContentItemActivity extends BaseActivity {
    final static String CONTENT_ITEM_KEY = "CONTENT_ITEM_KEY";
    final static String EXPANDED_ITEM_KEY = "EXPANDED_ITEM_KEY";

    @Inject
    AbortionContentManager mAbortionContentManager;
    @Inject
    ContentManager mContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ((App) getApplication()).getAppComponent().inject(this);
        if (savedInstanceState == null) {
            ContentItem contentItem = getIntent().getParcelableExtra(CONTENT_ITEM_KEY);
            ContentItem expandedItem = getIntent().getParcelableExtra(EXPANDED_ITEM_KEY);
            showContentItem(contentItem, expandedItem);
//            showContentItem(mContentManager.getContentItem("miscarriage_faqs"), null);
        }
    }

    public static Intent makeIntent(Context context, ContentItem contentItem) {
        return makeIntent(context, contentItem, null);
    }

    public static Intent makeIntent(Context context, ContentItem contentItem, ContentItem expandedItem) {
        Intent intent = new Intent(context, ContentItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CONTENT_ITEM_KEY, contentItem);
        bundle.putParcelable(EXPANDED_ITEM_KEY, expandedItem);
        intent.putExtras(bundle);
        return intent;
    }

    protected void showContentItem(ContentItem contentItem, final ContentItem expandedItem) {
        EukiCallback<ContentItem> callback = new EukiCallback<ContentItem>() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                replaceFragment(ContentItemFragment.newInstance(contentItem, expandedItem), false);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        };

        Fragment fragment = null;
        switch (contentItem.getId()) {
            case "compare_methods":
                break;
            case "medical_abortion":
                fragment = MedicalAbortionFragment.newInstance();
                break;
            case "suction_or_vacuum":
                fragment = AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionSuctionVacuum(), expandedItem);
                break;
            case "dilation_evacuation":
                fragment = AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionDilationEvacuation(), expandedItem);
                break;
            case "concerned_sti_hiv":
                mContentManager.getSTIs(callback);
                break;
            case "concerned_pregnancy":
                mContentManager.getPregnancyOptions(callback);
                break;
            default:
                if (contentItem.getAbottionItem() != null && contentItem.getAbottionItem() == true) {
                    fragment = AbortionInfoItemFragment.newInstance(contentItem, expandedItem);
                } else {
                    fragment = ContentItemFragment.newInstance(contentItem, expandedItem);
                }
                break;
        }

        if (fragment != null) {
            replaceFragment(fragment, false);
        }
    }
}
