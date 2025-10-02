package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.utils.ItemTouchHelperAdapter;
import com.fortyk.studentapp.utils.ItemTouchHelperViewHolder;
import com.fortyk.studentapp.utils.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CorrectOrderTextAnswerAdapter extends RecyclerView.Adapter<CorrectOrderTextAnswerAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    public List<QuestionModel> answerList;
    Context context;
    private boolean showCorrectAnswer = false;
    private boolean showAnswer = false;
    static OnItemClickListener mItemClickListener;
    private final OnStartDragListener mDragStartListener;

    public CorrectOrderTextAnswerAdapter(Context context, List<QuestionModel> answerList, OnStartDragListener dragListner) {
        this.answerList = answerList;
        this.context = context;
        this.mDragStartListener = dragListner;
    }

    @NonNull
    @Override
    public CorrectOrderTextAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CorrectOrderTextAnswerAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_correct_order_text_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectOrderTextAnswerAdapter.ViewHolder holder, final int position) {
        QuestionModel answer = answerList.get(position);
        holder.tx_answer.setText(answer.getQuestionoptiontext());
        if (showCorrectAnswer) {
            if (answer.getQuestionoptionsequence() == position) {
                holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.green));
            } else {
                holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.red));

            }
        } else if (showAnswer) {
            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }

      /*  if (answer.isBlank()) {
            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        } else {
            holder.tx_answer.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
      */

    /*    holder.tx_answer.setOnTouchListener(new View.OnTouchListener() {
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
    }*//**/

      /*  holder.tx_answer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public interface OnItemClickListener {
       void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

        @BindView(R.id.tx_answer)
        TextView tx_answer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public void setShowCorrectAnswer(boolean value) {
        showCorrectAnswer = value;
    }

    public void showAnswer(boolean value) {
        showCorrectAnswer = value;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Log.v("", "Log position" + fromPosition + " " + toPosition);
        if (fromPosition < answerList.size() && toPosition < answerList.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(answerList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(answerList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    public void updateList(List<QuestionModel> list) {
        answerList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        answerList.remove(position);
        notifyItemRemoved(position);
    }
}