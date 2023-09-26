package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.Question;
import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizOptionAdapter extends RecyclerView.Adapter {
    private Quiz mQuiz;
    private Question mQuestion;
    private QuizOptionListener mListener;
    private WeakReference<Context> mContext;

    public QuizOptionAdapter(Context context, Quiz quiz, int position, QuizOptionListener listener) {
        mContext = new WeakReference<>(context);
        mQuiz = quiz;
        mQuestion = quiz.getQuestions().get(position);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_quiz_option, parent, false);
        return new QuizOptionHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuizOptionHolder optionHolder = (QuizOptionHolder) holder;
        String title = Utils.getLocalized(mQuestion.getOptions().get(position).first);
        Boolean isSelected = mQuestion.getAnswerIndex() != null && mQuestion.getAnswerIndex() == position;

        optionHolder.tvTitle.setText(title);
        optionHolder.ivIcon.setImageResource(isSelected ? R.drawable.ic_quiz_option_on : R.drawable.ic_quiz_option_off);
    }

    @Override
    public int getItemCount() {
        return mQuestion.getOptions().size();
    }

    static class QuizOptionHolder extends RecyclerView.ViewHolder {
        private QuizOptionListener mListener;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.iv_icon) ImageView ivIcon;

        public QuizOptionHolder(View itemView, QuizOptionListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.ll_main)
        void onClick() {
            if (mListener != null) {
                mListener.optionSelected(getLayoutPosition());
            }
        }
    }

    public interface QuizOptionListener {
        void optionSelected(int position);
    }
}
