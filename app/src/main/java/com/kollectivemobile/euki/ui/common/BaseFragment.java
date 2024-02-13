package com.kollectivemobile.euki.ui.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.listeners.LinkListener;
import com.kollectivemobile.euki.manager.AbortionContentManager;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.browser.BrowserActivity;
import com.kollectivemobile.euki.ui.calendar.reminders.RemindersActivity;
import com.kollectivemobile.euki.ui.home.HomeFragment;
import com.kollectivemobile.euki.ui.home.abortion.AbortionInfoItemFragment;
import com.kollectivemobile.euki.ui.home.abortion.MedicalAbortionFragment;
import com.kollectivemobile.euki.ui.home.content.ContentItemActivity;
import com.kollectivemobile.euki.ui.home.content.ContentItemFragment;
import com.kollectivemobile.euki.ui.quiz.QuizActivity;
import com.kollectivemobile.euki.utils.Utils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by pAk on 6/17/16.
 */
public abstract class BaseFragment extends Fragment implements LinkListener {
    @Inject
    AbortionContentManager mAbortionContentManager;
    @Inject
    ContentManager mContentManager;

    protected InteractionListener mInteractionListener;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mInteractionListener = (InteractionListener) context;
        } else {
            throw new RuntimeException("Context should implements InteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = onCreateViewCalled(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        mInteractionListener.updateTitle(getTitle().toUpperCase(), showBack());
        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
    }

    public void showProgressDialog() {
        mInteractionListener.showProgressDialog();
    }

    public void dismissProgressDialog() {
        mInteractionListener.dismissProgressDialog();
    }

    public void showProgressBar() {
        mInteractionListener.showProgressBar();
    }

    public void hideProgressBar() {
        mInteractionListener.hideProgressBar();
    }

    public void showError(String message) {
        mInteractionListener.showError(message);
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public String getTitle() {
        return "";
    }

    public boolean showBack() {
        return false;
    }

    protected abstract View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void linkClicked(String link) {
        if (link.equals("abortion")) {
            HomeFragment.ShouldShowAbortion = true;
            getActivity().finish();
            return;
        }
        if (link.equals("reminders")) {
            getActivity().startActivity(RemindersActivity.makeIntent(getActivity()));
            return;
        }
        if (link.equals("resources") || link.equals("sexuality_resources") || link.equals("method_information")
                || link.equals("symptom_management") || link.equals("menstrual_cycle_101") || link.equals("contraception")) {
            ContentItem contentItem = mContentManager.getContentItem(link);
            if (contentItem != null) {
                Intent intent = ContentItemActivity.makeIntent(getActivity(), contentItem);
                startActivity(intent);
                return;
            }
        }


        if (link.equals("product_quiz")){
            ContentItem contentItem = mContentManager.getContentItem(link);
            if (contentItem != null) {
                Intent intent = QuizActivity.makeIntent(getActivity(), QuizType.MENSTRUATION);
                startActivity(intent);
                return;
            }
        }

        if (link.equals("telehealth_popup_info") || link.equals("implant_content_3_info")) {
            Dialogs.createSimpleDialog(getActivity(), "", Utils.getLocalized(link), null).show();
            return;
        }

        String url = link;
        Uri uri = Uri.parse(link);
        if (uri != null && uri.getPath().endsWith(".pdf")) {
            url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + link;
        }
        startActivity(BrowserActivity.makeIntent(getActivity(), url));
    }

    @Override
    public void phoneClicked(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    public interface InteractionListener {
        void replaceFragment(Fragment fragment, boolean addToBackStack);

        void showProgressDialog();

        void dismissProgressDialog();

        void showProgressBar();

        void hideProgressBar();

        void showError(String message);

        void updateTitle(String title, boolean showBack);

        void showTutorial(String title, String text, View view, MaterialTapTargetPrompt.PromptStateChangeListener listener);

        void showTutorial(String title, String text, Integer resId, MaterialTapTargetPrompt.PromptStateChangeListener listener);
    }

    protected void showContentItem(ContentItem contentItem) {
        EukiCallback<ContentItem> callback = new EukiCallback<ContentItem>() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                mInteractionListener.replaceFragment(ContentItemFragment.newInstance(contentItem), true);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        };

        Fragment fragment = null;
        switch (contentItem.getId()) {
            case "compare_methods":
                startActivity(QuizActivity.makeIntent(getActivity(), QuizType.CONTRACEPTION));
                break;
            case "product_quiz":
                startActivity(QuizActivity.makeIntent(getActivity(), QuizType.MENSTRUATION));
                break;
            case "medical_abortion":
                fragment = MedicalAbortionFragment.newInstance();
                break;
            case "suction_or_vacuum":
                fragment = AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionSuctionVacuum());
                break;
            case "dilation_evacuation":
                fragment = AbortionInfoItemFragment.newInstance(mAbortionContentManager.getAbortionDilationEvacuation());
                break;
            case "concerned_sti_hiv":
                mContentManager.getSTIs(callback);
                break;
            case "concerned_pregnancy":
                mContentManager.getPregnancyOptions(callback);
                break;
            default:
                fragment = ContentItemFragment.newInstance(contentItem);
                break;
        }

        if (fragment != null) {
            mInteractionListener.replaceFragment(fragment, true);
        }
    }
}
