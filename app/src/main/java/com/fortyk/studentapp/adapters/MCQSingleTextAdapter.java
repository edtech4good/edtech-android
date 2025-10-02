package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.QuestionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MCQSingleTextAdapter extends RecyclerView.Adapter<MCQSingleTextAdapter.ViewHolder> {

    private List<QuestionModel> questionModelList;
    private Context context;
    private int lastCheckedPosition = -1;
    private TemplateItemClickListener listener;
    private boolean isSubmit = false;

    public MCQSingleTextAdapter(Context context, List<QuestionModel> questionModelList, TemplateItemClickListener listener) {
        this.questionModelList = questionModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MCQSingleTextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MCQSingleTextAdapter.ViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.list_mcq_single_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MCQSingleTextAdapter.ViewHolder holder, final int position) {
        QuestionModel questionModel = questionModelList.get(position);
        holder.tx_option.setText(questionModel.getQuestionoptiontext());

        if (position == lastCheckedPosition) {
            holder.radio_image.setImageResource(R.drawable.ic_radio_button_selected);
        } else {
            holder.radio_image.setImageResource(R.drawable.ic_radio_button_unselected);
        }

        if (isSubmit && questionModel.isQuestionoptioniscorrect()) {
            holder.radio_image.setImageResource(R.drawable.ic_radio_button_selected_green);
            holder.tx_option.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            if (position == lastCheckedPosition) {
                holder.radio_image.setImageResource(R.drawable.ic_radio_button_selected);
            } else {
                holder.radio_image.setImageResource(R.drawable.ic_radio_button_unselected);
            }
            holder.tx_option.setTextColor(ContextCompat.getColor(context, R.color.white));

        }
        holder.itemView.setOnClickListener(v -> {

            if (lastCheckedPosition != position) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = position;
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);

            }
            if (listener != null) {
                listener.onItemClick(questionModelList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public void setLastCheckedPosition(int position) {
        lastCheckedPosition = position;
        notifyDataSetChanged();
    }

    public interface TemplateItemClickListener {
        void onItemClick(QuestionModel questionModel, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_option)
        TextView tx_option;
        @BindView(R.id.radio_image)
        ImageView radio_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public QuestionModel getSelectedItem() {
        if (lastCheckedPosition == -1) {
            return null;
        } else {
            return questionModelList.get(lastCheckedPosition);
        }
    }

    public void setSubmit(boolean value) {
        isSubmit = value;
    }

}