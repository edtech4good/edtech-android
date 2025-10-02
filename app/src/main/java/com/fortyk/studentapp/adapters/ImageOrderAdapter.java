package com.fortyk.studentapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.fortyk.studentapp.utils.ItemMoveCallback;
import com.fortyk.studentapp.utils.OptionItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fortyk.studentapp.utils.Constants.getPath;

public class ImageOrderAdapter extends RecyclerView.Adapter<ImageOrderAdapter.ViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract {

    public List<QuestionModel> optionList;
    Context context;
    private OptionItemClickListener listener;
    private TouchListener touchListener;
    private ViewHolder viewHolder;
    private boolean showCorrectAnswer = false;
    private boolean showAnswer = false;


    public interface TouchListener {
        void onTouch(RecyclerView.ViewHolder viewHolder);
    }

    public ImageOrderAdapter(Context context, List<QuestionModel> optionList,
                             OptionItemClickListener listener, TouchListener touchListener) {
        this.optionList = optionList;
        this.context = context;
        this.listener = listener;
        this.touchListener = touchListener;
    }

    @NonNull
    @Override
    public ImageOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageOrderAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_single_image, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ImageOrderAdapter.ViewHolder holder, final int position) {
        viewHolder = holder;
        QuestionModel option = optionList.get(position);
        if (option.getQuestionoptiontext() != null && !option.getQuestionoptiontext().isEmpty()) {
            holder.option_description.setText(option.getQuestionoptiontext());
            holder.option_description.setVisibility(View.VISIBLE);
        } else {
            holder.option_description.setVisibility(View.GONE);
        }
        if (option.isSelected()) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_bg));
        } else {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
        }

        if (showCorrectAnswer) {
            if (option.getQuestionoptionsequence() == position) {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));
            } else {
                holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_red_bg));
            }
        } else if (showAnswer) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_green_bg));
        }

        if(option.getQuestionoptionfile() != null){
            String path = getPath(context, option.getQuestionoptionfile().getFilename());
            Glide.with(context).load(path).placeholder(R.drawable.placeholder_sqr).into(holder.option_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.option_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!showCorrectAnswer && !showAnswer) {
                    if (touchListener != null) {
                        touchListener.onTouch(holder);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(optionList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(optionList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {

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
            option_image = itemLayoutView.findViewById(R.id.option_image);
            option_description = itemLayoutView.findViewById(R.id.option_description);
            option_layout = itemLayoutView.findViewById(R.id.option_layout);
        }
    }

    public List<QuestionModel> getSelectedOption() {
        List<QuestionModel> questionModels = new ArrayList<>();
        for (QuestionModel option : optionList) {
            if (option.isSelected()) {
                questionModels.add(option);
            }
        }
        return questionModels;
    }

    public void clearSelected() {
        for (int i = 0; i < optionList.size(); i++) {
            optionList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public List<QuestionModel> getOptionList() {
        return optionList;
    }

    public void setShowCorrectAnswer(boolean value) {
        showCorrectAnswer = value;
    }

    public void showAnswer(boolean value) {
        showAnswer = value;
    }

}