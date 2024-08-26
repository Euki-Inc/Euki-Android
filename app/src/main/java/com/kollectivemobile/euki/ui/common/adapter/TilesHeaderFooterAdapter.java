package com.kollectivemobile.euki.ui.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;


public class TilesHeaderFooterAdapter extends AbstractHeaderFooterWrapperAdapter<TilesHeaderFooterAdapter.HeaderViewHolder, TilesHeaderFooterAdapter.FooterViewHolder> {
    private HeaderListener mHeaderListener;
    private FooterListener mFooterListener;
    private Boolean mIsEditing;
    private Object mHeaderObject;

    public TilesHeaderFooterAdapter(RecyclerView.Adapter adapter, HeaderListener headerListener, FooterListener footerListener) {
        setAdapter(adapter);
        mHeaderListener = headerListener;
        mFooterListener = footerListener;
        mIsEditing = false;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateHeaderItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tile_header, parent, false);
        HeaderViewHolder viewHolder = new HeaderViewHolder(view, mHeaderListener);
        return viewHolder;
    }

    @NonNull
    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tile_footer, parent, false);
        FooterViewHolder viewHolder = new FooterViewHolder(view, mFooterListener);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupFullSpanForGridLayoutManager(recyclerView);
    }

    @Override
    public void onBindHeaderItemViewHolder(@NonNull HeaderViewHolder holder, int localPosition) {
        if (mHeaderObject instanceof ReminderItem) {
            ReminderItem reminderItem = (ReminderItem)mHeaderObject;
            holder.tvTitle.setText(reminderItem.getTitle());
            holder.tvText.setText(reminderItem.getText());
            holder.btnDailyLog.setVisibility(View.GONE);
        }
        if (mHeaderObject instanceof Appointment) {
            Appointment appointment = (Appointment)mHeaderObject;
            holder.tvTitle.setText(appointment.getTitle());
            holder.tvText.setText(appointment.getLocation());
        }
    }

    @Override
    public void onBindFooterItemViewHolder(@NonNull FooterViewHolder holder, int localPosition) {
        holder.btnEdit.setText(App.getContext().getString(mIsEditing ? R.string.done : R.string.edit_tiles));
    }

    @Override
    public int getHeaderItemCount() {
        return mHeaderObject == null ? 0 : 1;
    }

    @Override
    public int getFooterItemCount() {
        return 1;
    }

    public void setupFullSpanForGridLayoutManager(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (!(lm instanceof GridLayoutManager)) {
            return;
        }

        final GridLayoutManager glm = (GridLayoutManager) lm;
        final GridLayoutManager.SpanSizeLookup origSizeLookup = glm.getSpanSizeLookup();
        final int spanCount = glm.getSpanCount();

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final long segmentedPosition = getSegmentedPosition(position);
                final int segment = extractSegmentPart(segmentedPosition);
                final int offset = extractSegmentOffsetPart(segmentedPosition);

                if (segment == SEGMENT_TYPE_NORMAL) {
                    return origSizeLookup.getSpanSize(offset);
                } else {
                    return spanCount; // header or footer
                }
            }
        });
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        TextView tvTitle;
        TextView tvText;
        Button btnDailyLog;

        private HeaderListener mHeaderListener;

        public HeaderViewHolder(View itemView, HeaderListener headerListener) {
            super(itemView);
            mHeaderListener = headerListener;

            rlMain = itemView.findViewById(R.id.rl_main);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvText = itemView.findViewById(R.id.tv_text);
            btnDailyLog = itemView.findViewById(R.id.btn_go_to_daily_log);

            itemView.findViewById(R.id.btn_dismiss).setOnClickListener(v -> {
                if (mHeaderListener != null) {
                    mHeaderListener.dismissPressed();
                }
            });

            btnDailyLog.setOnClickListener(v -> {
                if (mHeaderListener != null) {
                    mHeaderListener.goToDailyLogPressed();
                }
            });
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        View cvBookmarks;
        Button btnEdit;

        private FooterListener mFooterListener;

        public FooterViewHolder(View itemView, FooterListener footerListener) {
            super(itemView);
            mFooterListener = footerListener;

            cvBookmarks = itemView.findViewById(R.id.cv_bookmarks);
            btnEdit = itemView.findViewById(R.id.btn_edit);

            cvBookmarks.setOnClickListener(v -> {
                if (mFooterListener != null) {
                    mFooterListener.bookmarksPressed();
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (mFooterListener != null) {
                    mFooterListener.editPressed();
                }
            });
        }
    }

    public void update(Boolean isEditing) {
        mIsEditing = isEditing;
        notifyDataSetChanged();
    }

    public void updateHeader(Object object) {
        mHeaderObject = object;
        mHeaderAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public interface HeaderListener {
        void dismissPressed();
        void goToDailyLogPressed();
    }

    public interface FooterListener {
        void bookmarksPressed();
        void editPressed();
    }
}
