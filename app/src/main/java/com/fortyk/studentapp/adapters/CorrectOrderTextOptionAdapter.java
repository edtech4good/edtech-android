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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CorrectOrderTextOptionAdapter extends RecyclerView.Adapter<CorrectOrderTextOptionAdapter.ViewHolder> {

    public List<String> optionList;
    Context context;
    private TextSelectedListener listener;
    private int lastCheckedPosition = -1;

    public interface TextSelectedListener {
        void onTextSelected(String value, int position);
    }

    public CorrectOrderTextOptionAdapter(Context context, List<String> lessons, TextSelectedListener listener) {
        this.optionList = lessons;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public CorrectOrderTextOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CorrectOrderTextOptionAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_fill_blanks_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectOrderTextOptionAdapter.ViewHolder holder, final int position) {
        String option = optionList.get(position);
        holder.tx_option.setText(option);
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