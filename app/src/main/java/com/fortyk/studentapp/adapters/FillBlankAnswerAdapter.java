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
import com.fortyk.studentapp.models.FillBlanksModel;
import com.fortyk.studentapp.models.QuestionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillBlankAnswerAdapter extends RecyclerView.Adapter<FillBlankAnswerAdapter.ViewHolder> {

    public List<FillBlanksModel> fillBlanksModelList;
    public List<QuestionModel> originalFillBlanksList;
    Context context;
    private FillBlanksListener listener;
    private int lastCheckedPosition = -1;
    private boolean showAnswer = false;
    private boolean showCorrectAnswers = false;

    private int correctposition = 0;

    private int questionlength = 0;

    public interface FillBlanksListener {
        void onTextClick(int id, int position);
    }

    public FillBlankAnswerAdapter(Context context,
                                  List<FillBlanksModel> fillBlanksModelList, List<QuestionModel> originalFillBlanksList,
                                  int question_length, FillBlanksListener listener) {
        this.fillBlanksModelList = fillBlanksModelList;
        this.originalFillBlanksList = originalFillBlanksList;
        this.questionlength = question_length;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FillBlankAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FillBlankAnswerAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_fill_blanks_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FillBlankAnswerAdapter.ViewHolder holder, final int position) {
        FillBlanksModel answer = fillBlanksModelList.get(position);
        holder.tx_answer.setText(answer.getAnswer());
        if (answer.isBlank()) {
            holder.blank_layout.setVisibility(View.VISIBLE);
           /* if(questionlength > 52){
                ViewGroup.LayoutParams layoutParams = holder.blank_layout.getLayoutParams();
                layoutParams.width = 150;
                holder.blank_layout.setLayoutParams(layoutParams);
            }else{
                ViewGroup.LayoutParams layoutParams = holder.blank_layout.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.blank_layout.setLayoutParams(layoutParams);
            }*/

            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            if (showCorrectAnswers) {
            } else if (showAnswer) {
                holder.tx_answer.setText(originalFillBlanksList.get(correctposition).getQuestionoptiontext());
                holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.green));
                correctposition++;
            }
        } else {
            holder.blank_layout.setVisibility(View.GONE);
            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.itemView.setOnClickListener(v -> {

            if (answer.isBlank() && (answer.getAnswer().isEmpty() || answer.getAnswer().equals(""))) {
                if (listener != null) {
                    listener.onTextClick(answer.getId(), position);
                }
            }
        });
    }

    public void removeItem(int position) {
        fillBlanksModelList.remove(position);
        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }

    @Override

    public int getItemCount() {
        return fillBlanksModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_answer)
        TextView tx_answer;
        @BindView(R.id.blank_layout)
        View blank_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setShowAnswer(boolean value) {
        showAnswer = value;
    }

    public void setShowCorrectAnswers(boolean value) {
        showCorrectAnswers = value;
    }
}