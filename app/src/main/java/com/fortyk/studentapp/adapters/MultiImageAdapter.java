package com.fortyk.studentapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.ImageOption;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.utils.OptionItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.fortyk.studentapp.utils.Constants.getPath;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder> {

    public List<QuestionModel> optionList;
    Context context;
    private OptionItemClickListener listener;
    private boolean isShowAnswer = false;
    private boolean showCorrectAnswerSelected = false;

    public MultiImageAdapter(Context context, List<QuestionModel> optionList,
                             OptionItemClickListener listener) {
        this.optionList = optionList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MultiImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MultiImageAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_multi_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultiImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        QuestionModel questionModel = optionList.get(position);

        holder.option_description.setVisibility(View.GONE);

        if (questionModel.isSelected()) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_bg));
        } else {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
        }

        if (isShowAnswer && questionModel.isQuestionoptioniscorrect()) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));

        }
        if (showCorrectAnswerSelected) {
            if (questionModel.isSelected() && questionModel.isQuestionoptioniscorrect()) {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));

            } else if (questionModel.isSelected() && !questionModel.isQuestionoptioniscorrect()) {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_red_bg));

            } else {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
            }
        }

        if(questionModel.getQuestionoptionfile() != null){
            String path = getPath(context, questionModel.getQuestionoptionfile().getFilename());
            Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.option_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showCorrectAnswerSelected && !isShowAnswer) {
                    if (!optionList.get(position).isSelected()) {
                        optionList.get(position).setSelected(true);
                        if (listener != null) {
                            listener.onOptionClick(optionList.get(position));
                        }
                        notifyItemChanged(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView option_image;
        public TextView option_description;
        public LinearLayout option_layout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            option_image = (ImageView) itemLayoutView.findViewById(R.id.option_image);
            option_description = (TextView) itemLayoutView.findViewById(R.id.option_description);
            option_layout = (LinearLayout) itemLayoutView.findViewById(R.id.option_layout);
        }
    }

    public List<QuestionModel> getSelectedItem() {
        List<QuestionModel> questionModels = new ArrayList<>();
        for (QuestionModel questionModel : optionList) {
            if (questionModel.isSelected())
                questionModels.add(questionModel);
        }
        return questionModels;
    }

    public void setShowAnswer(boolean value) {
        isShowAnswer = value;
        if (value) {
            showCorrectAnswerSelected = false;
        }
    }

    public void setShowCorrectAnswerSelected(boolean value) {
        showCorrectAnswerSelected = value;
        if (value) {
            isShowAnswer = false;
        }
    }
}