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

import java.util.Collections;
import java.util.List;

import static com.fortyk.studentapp.utils.Constants.getPath;

public class SingleImageAdapter extends RecyclerView.Adapter<SingleImageAdapter.ViewHolder> {

    public List<QuestionModel> optionList;
    Context context;
    private OptionItemClickListener listener;
    private int lastCheckedPosition = -1;
    private boolean isShowAnswer = false;
    private boolean showCorrectAnswerSelected = false;

    public SingleImageAdapter(Context context, List<QuestionModel> optionList,
                              OptionItemClickListener listener) {
        this.optionList = optionList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SingleImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SingleImageAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_single_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        QuestionModel questionModel = optionList.get(position);

        holder.option_description.setVisibility(View.GONE);

        if (lastCheckedPosition == position) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_bg));
        } else {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
        }

        if (isShowAnswer && questionModel.isQuestionoptioniscorrect()) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));
        }
        if (showCorrectAnswerSelected) {
            if (lastCheckedPosition == position && questionModel.isQuestionoptioniscorrect()) {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));

            } else if (lastCheckedPosition == position && !questionModel.isQuestionoptioniscorrect()) {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_red_bg));

            } else {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
            }
        }

        if(questionModel.getQuestionoptionfile() != null){
            String path = getPath(context, questionModel.getQuestionoptionfile().getFilename());
            Glide.with(context).load(path).placeholder(R.drawable.placeholder_sqr).into(holder.option_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showCorrectAnswerSelected && !isShowAnswer) {
                    if (lastCheckedPosition != position) {
                        int copyOfLastCheckedPosition = lastCheckedPosition;
                        lastCheckedPosition = position;
                        notifyItemChanged(copyOfLastCheckedPosition);
                        notifyItemChanged(lastCheckedPosition);

                        if (listener != null) {
                            listener.onOptionClick(optionList.get(position));
                        }
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

    public void clearSelected() {
        lastCheckedPosition = -1;
        Collections.shuffle(optionList);
        notifyDataSetChanged();
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