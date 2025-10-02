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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillBlankOptionAdapter extends RecyclerView.Adapter<FillBlankOptionAdapter.ViewHolder> {

    public List<QuestionModel> optionList;
    Context context;
    private TextSelectedListener listener;
    private int lastCheckedPosition = -1;

    public interface TextSelectedListener {
        void onTextSelected(QuestionModel questionModel, int position);
    }

    public FillBlankOptionAdapter(Context context, List<QuestionModel> lessons, TextSelectedListener listener) {
        this.optionList = lessons;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FillBlankOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FillBlankOptionAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_fill_blanks_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FillBlankOptionAdapter.ViewHolder holder, final int position) {
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
                    listener.onTextSelected(optionList.get(position), position);
                }
            }
        });
    }

    public void removeItem(int position) {
        optionList.remove(position);
        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }

    public QuestionModel getSelectedItem() {
        if (lastCheckedPosition != -1) {
            return optionList.get(lastCheckedPosition);
        } else {
            return null;
        }
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public void setLastCheckedPosition(int position) {
        lastCheckedPosition = position;
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
}