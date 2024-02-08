package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.View;

import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.ui.quiz.IntroFragment;
import com.kollectivemobile.euki.ui.quiz.QuestionFragment;
import com.kollectivemobile.euki.ui.quiz.ResultFragment;

public class QuizQuestionsFragmentAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private Quiz mQuiz;

    private QuizType mQuizType;

    public QuizQuestionsFragmentAdapter(FragmentManager fm, Context context, Quiz quiz, QuizType quizType) {
        super(fm);
        mContext = context;
        mQuiz = quiz;
        mQuizType = quizType;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        }
        return super.isViewFromObject(view, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return IntroFragment.newInstance(mQuiz);
        }
        if (i < mQuiz.getQuestions().size() + 1) {
            return QuestionFragment.newInstance(mQuiz, i - 1);
        }
        return ResultFragment.newInstance(mQuiz, mQuizType);
    }

    @Override
    public int getCount() {
        return mQuiz.getQuestions().size() + 2;
    }
}
