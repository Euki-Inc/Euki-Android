package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.QuizMethod;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MethodAdapter extends RecyclerView.Adapter {
    private List<QuizMethod> mMethods;
    private MethodListener mListener;
    private WeakReference<Context> mContext;

    public MethodAdapter(Context context, List<QuizMethod> methods, MethodListener listener) {
        mMethods = new ArrayList<>();
        mMethods.addAll(methods);
        mContext = new WeakReference<>(context);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_quiz_method, parent, false);
        return new BookmarkHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookmarkHolder methodHolder = (BookmarkHolder) holder;
        QuizMethod method = mMethods.get(position);

        methodHolder.tvTitle.setText(Utils.getLocalized(method.getTitle()));
        methodHolder.tvTitle.setAlpha((float)(method.getSelected() ? 1.0 : 0.3));
        methodHolder.ivIcon.setImageResource(Utils.getImageId(method.getImageName()));
        methodHolder.ivIcon.setAlpha((float)(method.getSelected() ? 1.0 : 0.3));
    }

    @Override
    public int getItemCount() {
        return mMethods.size();
    }

    static class BookmarkHolder extends RecyclerView.ViewHolder {
        private MethodListener mListener;
        @BindView(R.id.iv_icon) ImageView ivIcon;
        @BindView(R.id.tv_title) TextView tvTitle;

        public BookmarkHolder(View itemView, MethodListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.ll_main)
        void onClick() {
            if (mListener != null) {
                mListener.methodSelected(getLayoutPosition());
            }
        }
    }

    public interface MethodListener {
        void methodSelected(int position);
    }
}
