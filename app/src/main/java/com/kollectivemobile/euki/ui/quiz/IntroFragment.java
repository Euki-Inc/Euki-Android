package com.kollectivemobile.euki.ui.quiz;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.utils.Utils;

import butterknife.BindView;

public class IntroFragment extends BaseFragment {
    @BindView(R.id.tv_title) TextView tvTitle;

    private Quiz mQuiz;

    public static IntroFragment newInstance(Quiz quiz) {
        Bundle args = new Bundle();
        IntroFragment fragment = new IntroFragment();
        fragment.mQuiz = quiz;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_intro, container, false);
    }

    private void setUIElements() {
        tvTitle.setText(Utils.getLocalized(mQuiz.getInstructions()));
    }
}
