package com.kollectivemobile.euki.ui.quiz;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.QuizManager;
import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;

public class ResultFragment extends BaseFragment {
    @Inject
    QuizManager mQuizManager;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Quiz mQuiz;

    private QuizType mQuizType;

    public static ResultFragment newInstance(Quiz quiz, QuizType quizType) {
        Bundle args = new Bundle();
        ResultFragment fragment = new ResultFragment();
        fragment.mQuiz = quiz;
        fragment.mQuizType = quizType;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        EventBus.getDefault().register(this);
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void setUIElements() {
        switch (mQuizType) {
            case CONTRACEPTION:
                tvTitle.setText(Utils.getLocalized(mQuizManager.getresultContraception(mQuiz).first));
                break;
            case MENSTRUATION:
                tvTitle.setText(Utils.getLocalized(mQuizManager.getResultMenstruation(mQuiz).first));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(Quiz quiz) {
        setUIElements();
    }
}
