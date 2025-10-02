package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.utils.OptionItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CorrectOrderTextAdapter extends RecyclerView.Adapter<CorrectOrderTextAdapter.ViewHolder> {

    public List<QuestionModel> optionList;
    Context context;
    private OptionItemClickListener listener;
    private int lastCheckedPosition = -1;
    private boolean hasfile;

    public CorrectOrderTextAdapter(Context context, List<QuestionModel> optionList, OptionItemClickListener listener, boolean hasfile) {
        this.optionList = optionList;
        this.context = context;
        this.listener = listener;
        this.hasfile = hasfile;

    }

    @NonNull
    @Override
    public CorrectOrderTextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CorrectOrderTextAdapter.ViewHolder viewHolder;
        if(hasfile) {
            viewHolder = new CorrectOrderTextAdapter.ViewHolder(LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.list_correct_text_item, parent, false));
        }else{
            viewHolder = new CorrectOrderTextAdapter.ViewHolder(LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.list_correct_text_item_wrap, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectOrderTextAdapter.ViewHolder holder, final int position) {

        QuestionModel option = optionList.get(position);
        holder.tx_option.setText(option.getQuestionoptiontext());
        if (lastCheckedPosition == position) {
            holder.content_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_order_bg_selected));
        } else {
            holder.content_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_order_bg));
        }

        holder.itemView.setOnClickListener(v -> {
            if (lastCheckedPosition != position) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = position;
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);

                if (listener != null) {
                    listener.onOptionClick(optionList.get(position));
                }
            }
        });
    }

    public void removeItem(int position) {
        optionList.remove(position);
        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }

    @Override

    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_option)
        TextView tx_option;
        @BindView(R.id.content_layout)
        LinearLayout content_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public QuestionModel getSelected() {
        if (lastCheckedPosition != -1) {
            return optionList.get(lastCheckedPosition);
        }
        return null;
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

}