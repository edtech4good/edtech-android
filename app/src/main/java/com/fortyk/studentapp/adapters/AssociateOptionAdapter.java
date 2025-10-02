package com.fortyk.studentapp.adapters;

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
import com.fortyk.studentapp.fragments.AssociateFragment;
import com.fortyk.studentapp.models.QuestionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.fortyk.studentapp.utils.Constants.getPath;

public class AssociateOptionAdapter extends RecyclerView.Adapter<AssociateOptionAdapter.ViewHolder> {

    private List<QuestionModel> questionModelList;
    private Context context;
    private int lastCheckedPosition = -1;

    private boolean showAnswer = false;
    private boolean showCorrectAnswers = false;

    private AssociateFragment associateFragment;

    public AssociateOptionAdapter(Context context, List<QuestionModel> questionModelList, AssociateFragment massociateFragment) {
        this.questionModelList = questionModelList;
        this.context = context;
        this.associateFragment = massociateFragment;
    }

    @NonNull
    @Override
    public AssociateOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssociateOptionAdapter.ViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.list_associate_option_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssociateOptionAdapter.ViewHolder holder, final int position) {
        QuestionModel questionModel = questionModelList.get(position);
        String extension = "";
        String path = "";
        if (questionModel.getQuestionassociate() != null) {
            if (questionModel.getQuestionassociate().getQuestionassociatetext() != null) {
                holder.tx_option.setVisibility(VISIBLE);
                holder.tx_option.setText(questionModel.getQuestionassociate().getQuestionassociatetext());
                holder.img_option.setVisibility(GONE);
                holder.audio_option.setVisibility(GONE);

            } else {
                if (questionModel.getQuestionassociate().getQuestionOptionfile() != null) {
                    extension = questionModel.getQuestionassociate().getQuestionOptionfile().getFileext();
                    if (extension.equalsIgnoreCase("mp3")) {
                        holder.tx_option.setVisibility(GONE);
                        holder.img_option.setVisibility(GONE);
                        holder.audio_option.setVisibility(VISIBLE);
                        String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                        path = getPath(context, content_file);
                       /* holder.audio_option.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playAudio(path);
                            }
                        });*/
                    } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")) {
                        holder.tx_option.setVisibility(GONE);
                        holder.img_option.setVisibility(VISIBLE);
                        holder.audio_option.setVisibility(GONE);
                        String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                        path = getPath(context, content_file);
                        Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.img_option);
                    }
                }
            }
        }
       // holder.tx_option.setText(questionModel.getQuestionoptiontext());
        if (lastCheckedPosition == position) {
            holder.content_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_order_bg_selected));
        } else {
            holder.content_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_order_bg));
        }

        String finalExtension = extension;
        String finalPath = path;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalExtension.equalsIgnoreCase("mp3")) {
                   // playAudio(finalPath);
                    associateFragment.playOptionAudio(finalPath);
                }
                if (lastCheckedPosition != position) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = position;
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tx_option)
        TextView tx_option;
        @BindView(R.id.img_option)
        ImageView img_option;
        @BindView(R.id.audio_option)
        ImageView audio_option;
        @BindView(R.id.content_layout)
        LinearLayout content_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public QuestionModel getSelectedItem() {
        if (lastCheckedPosition != -1) {
            return questionModelList.get(lastCheckedPosition);
        } else {
            return null;
        }
    }

    public int getSelectedPosition() {
        return lastCheckedPosition;
    }

    public void setLastCheckedPosition(int position) {
        lastCheckedPosition = position;
    }
}