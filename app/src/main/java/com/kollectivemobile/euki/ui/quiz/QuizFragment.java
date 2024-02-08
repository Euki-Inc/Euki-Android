package com.kollectivemobile.euki.ui.quiz;

import static com.kollectivemobile.euki.ui.quiz.QuizActivity.QUIZ_TYPE;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.ContraceptionContentManager;
import com.kollectivemobile.euki.manager.MenstruationContentManager;
import com.kollectivemobile.euki.manager.QuizManager;
import com.kollectivemobile.euki.model.Question;
import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.model.QuizMethod;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.adapter.MethodAdapter;
import com.kollectivemobile.euki.ui.common.adapter.QuizQuestionsFragmentAdapter;
import com.kollectivemobile.euki.ui.home.content.ContentItemActivity;
import com.kollectivemobile.euki.utils.KKViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class QuizFragment extends BaseFragment implements MethodAdapter.MethodListener {
    @Inject
    QuizManager mQuizManager;
    @Inject
    ContraceptionContentManager mContraceptionContentManager;
    @Inject
    MenstruationContentManager mMenstruationContentManager;
    @BindView(R.id.kkvp_questions)
    KKViewPager vpQuestions;
    @BindView(R.id.rv_methods)
    RecyclerView rvMethods;

    private MethodAdapter mMethodAdapter;
    private Quiz mQuiz;
    private List<QuizMethod> mMethods;
    private int mCurrentPage = 0;
    private QuizType quizType;

    public static QuizFragment newInstance(QuizType quizType) {
        Bundle args = new Bundle();
        args.putSerializable(QUIZ_TYPE, quizType);

        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            quizType = (QuizType) getArguments().getSerializable(QUIZ_TYPE);
        }
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        requestQuiz();
        setHasOptionsMenu(true);
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public boolean showBack() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_done:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestQuiz() {
        switch (quizType) {
            case CONTRACEPTION:
                requestContraceptionQuiz();
                break;

            case MENSTRUATION:
                requestMenstruationQuiz();
                break;
        }
    }

    private void requestContraceptionQuiz() {
        mQuizManager.getContraceptionQuiz(new EukiCallback<Quiz>() {
            @Override
            public void onSuccess(Quiz quiz) {
                handleQuizResponse(quiz);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        });
    }

    private void requestMenstruationQuiz() {
        mQuizManager.getMenstruationQuiz(new EukiCallback<Quiz>() {
            @Override
            public void onSuccess(Quiz quiz) {
                handleQuizResponse(quiz);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        });
    }

    private void handleQuizResponse(Quiz quiz) {
        mQuiz = quiz;
        setUIElements();
    }

    private void setUIElements() {
        vpQuestions.setAdapter(new QuizQuestionsFragmentAdapter(getFragmentManager(), getActivity(), mQuiz, quizType));
        vpQuestions.setAnimationEnabled(true);
        vpQuestions.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mCurrentPage = i;
                update(mQuiz);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        mMethods = mQuizManager.getMethods(quizType);
        mMethodAdapter = new MethodAdapter(getActivity(), mMethods, this);
        rvMethods.setAdapter(mMethodAdapter);
        rvMethods.setLayoutManager(new GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false));
    }

    @Override
    public void methodSelected(int position) {
        switch (quizType) {
            case CONTRACEPTION:
                startActivity(ContentItemActivity.makeIntent(getActivity(), mContraceptionContentManager.getMethodContentItem(position)));
                break;
            case MENSTRUATION:
                startActivity(ContentItemActivity.makeIntent(getActivity(), mMenstruationContentManager.getMethodContentItem(position)));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(Quiz quiz) {
        List<Integer> selectedIndexes = new ArrayList<>();

        if (mCurrentPage > 0 && mCurrentPage < quiz.getQuestions().size() + 1) {
            int index = mCurrentPage - 1;
            Question question = quiz.getQuestions().get(index);

            if (question.getAnswerIndex() != null) {
                selectedIndexes.addAll(question.getOptions().get(question.getAnswerIndex()).second);
            }
        } else if (mCurrentPage == quiz.getQuestions().size() + 1) {
            switch (quizType) {
                case CONTRACEPTION:
                    selectedIndexes.addAll(mQuizManager.getresultContraception(mQuiz).second);
                    break;
                case MENSTRUATION:
                    selectedIndexes.addAll(mQuizManager.getResultMenstruation(mQuiz).second);
                    break;
            }
        }

        for (int i = 0; i < mMethods.size(); i++) {
            QuizMethod method = mMethods.get(i);
            method.setSelected(selectedIndexes.contains(i + 1));
        }

        mMethodAdapter.notifyDataSetChanged();
    }
}
