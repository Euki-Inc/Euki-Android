package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.SelectableValue;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.views.SelectableButton;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import com.kollectivemobile.euki.utils.Constants.*;
import com.kollectivemobile.euki.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarDayAdapter extends RecyclerView.Adapter {
    private final Integer VIEW_TYPE_HEADER = 0;
    private final Integer VIEW_TYPE_VALUE = 1;
    private final Integer VIEW_TYPE_TEXT = 2;

    private CalendarItem mCalendarItem;

    private List<Object> mObjects;
    private Context mContext;

    public CalendarDayAdapter(Context context) {
        mObjects = new ArrayList<>();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.layout_calendar_day_header, parent, false);
            return new CalendarHeaderHolder(view);
        }
        if (viewType == VIEW_TYPE_VALUE) {
            View view = inflater.inflate(R.layout.layout_calendar_day_value, parent, false);
            return new CalendarValueHolder(view);
        }

        View view = inflater.inflate(R.layout.layout_calendar_day_text, parent, false);
        return new CalendarTextHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarHeaderHolder) {
            FilterItem filterItem = (FilterItem)mObjects.get(position);
            CalendarHeaderHolder headerHolder = (CalendarHeaderHolder)holder;
            headerHolder.vCircle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, filterItem.getColor())));
            headerHolder.tvTitle.setText(Utils.getLocalized(filterItem.getTitle()));
            return;
        }

        if (holder instanceof CalendarValueHolder) {
            SelectableValue selectableValue = (SelectableValue)mObjects.get(position);
            CalendarValueHolder valueHolder = (CalendarValueHolder)holder;
            valueHolder.sbValue.setCounter(selectableValue.getCounter());
            valueHolder.sbValue.setTitle(selectableValue.getTitle());
            valueHolder.sbValue.setImageRes(Utils.getImageId(selectableValue.getIconName()));
            valueHolder.sbValue.hideBorder();
        }

        if (holder instanceof CalendarTextHolder) {
            String text = (String)mObjects.get(position);
            CalendarTextHolder calendarTextHolder = (CalendarTextHolder)holder;
            calendarTextHolder.tvText.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mObjects.get(position);
        if (object instanceof FilterItem) {
            return VIEW_TYPE_HEADER;
        }
        if (object instanceof SelectableValue) {
            return VIEW_TYPE_VALUE;
        }
        return VIEW_TYPE_TEXT;
    }

    public void updateObjects(CalendarItem calendarItem) {
        mCalendarItem = calendarItem;

        List<FilterItem> filterItems = FilterItem.getAllCategories();
        List<Object> objects = new ArrayList<>();

        if (calendarItem.hasBleeding()) {
            objects.add(filterItems.get(0));
            if (calendarItem.getBleedingSize() != null) {
                objects.add(new SelectableValue(calendarItem.getBleedingSize().getImageName(), calendarItem.getBleedingSize().getText(), 0));
            }

            for (int index = 0; index < calendarItem.getBleedingClotsCounter().size(); index++) {
                int counter = calendarItem.getBleedingClotsCounter().get(index);
                if (counter > 0) {
                    BleedingClots bleedingClots = BleedingClots.values[index];
                    objects.add(new SelectableValue(bleedingClots.getImageName(), bleedingClots.getText(), counter));
                }
            }

            for (int index = 0; index<calendarItem.getBleedingProductsCounter().size(); index++) {
                int counter = calendarItem.getBleedingProductsCounter().get(index);
                if (counter > 0) {
                    BleedingProducts bleedingProducts = BleedingProducts.values[index];
                    objects.add(new SelectableValue(bleedingProducts.getImageName(), bleedingProducts.getText(), counter));
                }
            }
        }

        if (calendarItem.hasEmotions()) {
            objects.add(filterItems.get(1));
            for (Emotions emotions : calendarItem.getEmotions()) {
                objects.add(new SelectableValue(emotions.getImageName(), emotions.getText(), 0));
            }
        }
        if (calendarItem.hasBody()) {
            objects.add(filterItems.get(2));
            for (Body body : calendarItem.getBody()) {
                objects.add(new SelectableValue(body.getImageName(), body.getText(), 0));
            }
        }
        if (calendarItem.hasSexualActivity()) {
            objects.add(filterItems.get(3));
            for (int index = 0; index < calendarItem.getSexualProtectionSTICounter().size(); index++) {
                int counter = calendarItem.getSexualProtectionSTICounter().get(index);
                if (counter > 0) {
                    SexualProtectionSTI sexualProtectionSTI = SexualProtectionSTI.values[index];
                    objects.add(new SelectableValue(sexualProtectionSTI.getImageName(), sexualProtectionSTI.getText() + "_list", counter));
                }
            }
            for (int index = 0; index < calendarItem.getSexualProtectionPregnancyCounter().size(); index++) {
                int counter = calendarItem.getSexualProtectionPregnancyCounter().get(index);
                if (counter > 0) {
                    SexualProtectionPregnancy sexualProtectionPregnancy = SexualProtectionPregnancy.values[index];
                    objects.add(new SelectableValue(sexualProtectionPregnancy.getImageName(), sexualProtectionPregnancy.getText() + "_list", counter));
                }
            }
            for (int index = 0; index < calendarItem.getSexualOtherCounter().size(); index++) {
                int counter = calendarItem.getSexualOtherCounter().get(index);
                if (counter > 0) {
                    SexualProtectionOther sexualProtectionOther = SexualProtectionOther.values[index];
                    objects.add(new SelectableValue(sexualProtectionOther.getImageName(), sexualProtectionOther.getText(), counter));
                }
            }
        }
        if (calendarItem.hasContraception()) {
            objects.add(filterItems.get(4));
            if (calendarItem.getContraceptionPills() != null) {
                objects.add(new SelectableValue(calendarItem.getContraceptionPills().getImageName(), calendarItem.getContraceptionPills().getText() + "_list", 0));
            }
            for (ContraceptionDailyOther contraceptionDailyOther : calendarItem.getContraceptionDailyOther()) {
                objects.add(new SelectableValue(contraceptionDailyOther.getImageName(), contraceptionDailyOther.getText(), 0));
            }
            if (calendarItem.getContraceptionIUD() != null) {
                objects.add(new SelectableValue(calendarItem.getContraceptionIUD().getImageName(), calendarItem.getContraceptionIUD().getText() + "_list", 0));
            }
            if (calendarItem.getContraceptionImplant() != null) {
                objects.add(new SelectableValue(calendarItem.getContraceptionImplant().getImageName(), calendarItem.getContraceptionImplant().getText() + "_list", 0));
            }
            if (calendarItem.getContraceptionRing() != null) {
                objects.add(new SelectableValue(calendarItem.getContraceptionRing().getImageName(), calendarItem.getContraceptionRing().getText() + "_list", 0));
            }
            for (ContraceptionLongTermOther contraceptionLongTermOther : calendarItem.getContraceptionLongTermOthers()) {
                objects.add(new SelectableValue(contraceptionLongTermOther.getImageName(), contraceptionLongTermOther.getText(), 0));
            }
        }
        if (calendarItem.hasTest()) {
            objects.add(filterItems.get(5));
            if (calendarItem.getTestSTI() != null) {
                objects.add(new SelectableValue(calendarItem.getTestSTI().getImageName(), calendarItem.getTestSTI().getText() + "_list", 0));
            }
            if (calendarItem.getTestPregnancy() != null) {
                objects.add(new SelectableValue(calendarItem.getTestPregnancy().getImageName(), calendarItem.getTestPregnancy().getText() + "_list", 0));
            }
        }
        if (calendarItem.getAppointments().size() > 0) {
            objects.add(filterItems.get(6));
        }
        for (Appointment appointment : calendarItem.getAppointments()) {
            StringBuffer sb = new StringBuffer();
            if (appointment.getTitle() != null) {
                sb.append(appointment.getTitle());
            }
            if (appointment.getLocation() != null) {
                sb.append(" - ");
                sb.append(appointment.getLocation());
            }
            if (appointment.getDate() != null) {
                sb.append(" ");
                sb.append(DateUtils.toString(appointment.getDate(), DateUtils.eeeMMMdyyyyhmma));
            }
            objects.add(sb.toString());
        }
        if (calendarItem.hasNote()) {
            objects.add(filterItems.get(7));
            objects.add(calendarItem.getNote());
        }

        mObjects.clear();
        mObjects.addAll(objects);
        notifyDataSetChanged();
    }

    public List<Object> getObjects() {
        return mObjects;
    }

    public class CalendarHeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_circle) View vCircle;
        @BindView(R.id.tv_title) TextView tvTitle;

        public CalendarHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CalendarValueHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sb_value) SelectableButton sbValue;

        public CalendarValueHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CalendarTextHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.tv_text) TextView tvText;

        public CalendarTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
