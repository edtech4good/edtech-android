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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MCQMultiTextAdapter extends RecyclerView.Adapter<MCQMultiTextAdapter.ViewHolder> {

    private List<QuestionModel> questionModelList;
    private Context context;
    private TemplateItemClickListener listener;
    private boolean isSubmit = false;


    public MCQMultiTextAdapter(Context context, List<QuestionModel> questionModelList, TemplateItemClickListener listener) {
        this.questionModelList = questionModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MCQMultiTextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MCQMultiTextAdapter.ViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.list_mcq_multi_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MCQMultiTextAdapter.ViewHolder holder, final int position) {
        QuestionModel questionModel = questionModelList.get(position);
        holder.tx_option.setText(questionModel.getQuestionoptiontext());

        if (questionModel.isSelected()) {
            holder.checkbox_image.setImageResource(R.drawable.ic_checkbox_checked);
        } else {
            holder.checkbox_image.setImageResource(R.drawable.ic_check_box);
        }
        if (isSubmit && questionModel.isQuestionoptioniscorrect()) {
            holder.checkbox_image.setImageResource(R.drawable.ic_checkbox_checked_green);
            holder.tx_option.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            holder.tx_option.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.itemView.setOnClickListener(v -> {
            if (questionModelList.get(position).isSelected()) {
                questionModelList.get(position).setSelected(false);
            } else {
                questionModelList.get(position).setSelected(true);
            }
            notifyItemChanged(position);
            if (listener != null) {
                listener.onItemClick(questionModelList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }


    public interface TemplateItemClickListener {
        void onItemClick(QuestionModel questionModel, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_option)
        TextView tx_option;
        @BindView(R.id.checkbox_image)
        ImageView checkbox_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<QuestionModel> getSelectedItem() {
        List<QuestionModel> questionModels = new ArrayList<>();
        for (QuestionModel questionModel : questionModelList) {
            if (questionModel.isSelected())
                questionModels.add(questionModel);
        }
        return questionModels;
    }

    public void setSubmit(boolean value) {
        isSubmit = value;
    }
}