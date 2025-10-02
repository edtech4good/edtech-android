package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.fortyk.studentapp.models.AssociateModel;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.fortyk.studentapp.utils.Constants.getPath;

public class AssociateAdapter extends RecyclerView.Adapter<AssociateAdapter.ViewHolder> {

    private List<AssociateModel> questionModelList;
    private Context context;
    private AssociateItemClickListener listener;

    private boolean isSubmit = false;

    private AssociateFragment associateFragment;

    public interface AssociateItemClickListener {
        void onItemClick(AssociateModel questionModel, int position);
    }

    public AssociateAdapter(Context context, List<AssociateModel> questionModelList, AssociateItemClickListener listener,
            AssociateFragment massociateFragment) {
        this.questionModelList = questionModelList;
        this.context = context;
        this.listener = listener;
        this.associateFragment = massociateFragment;
    }

    @NonNull
    @Override
    public AssociateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssociateAdapter.ViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.list_associate_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssociateAdapter.ViewHolder holder, final int position) {
        AssociateModel questionModel = questionModelList.get(position);

        if(questionModel.getQuestionoptiontext() != null) {
            holder.text_layout.setVisibility(VISIBLE);
            holder.tx_associate_question.setVisibility(VISIBLE);
            holder.img_associate_question.setVisibility(GONE);
            holder.audio_associate_question.setVisibility(View.GONE);
            holder.tx_associate_question.setText(questionModel.getQuestionoptiontext());

            if(questionModel.getQuestionoptionfile() != null){
                String extension = questionModel.getQuestionoptionfile().getFileext();
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")){
                    holder.audiotext_associate.setVisibility(VISIBLE);
                    String content_file = questionModel.getQuestionoptionfile().getFilename();
                    String path = getPath(context, content_file);
                    Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.audiotext_associate);
                }else if (extension.equalsIgnoreCase("mp3")) {
                    holder.audiotext_associate.setVisibility(VISIBLE);
                    String content_file = questionModel.getQuestionoptionfile().getFilename();
                    String path = getPath(context, content_file);
                    holder.audiotext_associate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  playAudio(path);
                            associateFragment.playOptionAudio(path);
                        }
                    });
                }
            }
        }
        else{
            holder.text_layout.setVisibility(GONE);
            holder.tx_associate_question.setVisibility(View.GONE);
            holder.audiotext_associate.setVisibility(View.GONE);
            if(questionModel.getQuestionoptionfile() != null){
                String extension = questionModel.getQuestionoptionfile().getFileext();
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")){
                    holder.img_associate_question.setVisibility(VISIBLE);
                    holder.audio_associate_question.setVisibility(View.GONE);
                    String content_file = questionModel.getQuestionoptionfile().getFilename();
                    String path = getPath(context, content_file);
                    Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.img_associate_question);
                }else if (extension.equalsIgnoreCase("mp3")) {
                    holder.img_associate_question.setVisibility(GONE);
                    holder.audio_associate_question.setVisibility(VISIBLE);
                    String content_file = questionModel.getQuestionoptionfile().getFilename();
                    String path = getPath(context, content_file);
                    holder.audio_associate_question.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  playAudio(path);
                            associateFragment.playOptionAudio(path);
                        }
                    });
                }
            }
        }

        if(isSubmit){
            if (questionModel.getQuestionassociate().getQuestionassociatetext() != null) {
                holder.selected_option.setVisibility(VISIBLE);
                holder.empty_bg.setBackground(context.getResources().getDrawable(R.drawable.border_curve_green));
                holder.tx_option.setVisibility(VISIBLE);
                holder.empty_bg.setBackground(null);
                holder.tx_option.setText(questionModel.getQuestionassociate().getQuestionassociatetext());
                holder.tx_option.setTextColor(context.getResources().getColor(R.color.white));
                holder.tx_option.setBackgroundResource(R.drawable.border_curve_green);
                holder.img_option.setVisibility(GONE);
                holder.audio_option.setVisibility(GONE);
            }
            else if (questionModel.getQuestionassociate().getQuestionOptionfile() != null) {
                holder.selected_option.setVisibility(VISIBLE);
                String extension = questionModel.getQuestionassociate().getQuestionOptionfile().getFileext();
                if (extension.equalsIgnoreCase("mp3")) {
                    holder.tx_option.setVisibility(GONE);
                    holder.img_option.setVisibility(GONE);
                    holder.empty_bg.setBackground(null);
                    holder.audio_option.setVisibility(VISIBLE);
                    holder.audio_option.setImageResource(R.drawable.ans_audio);
                    holder.audio_option.setBackgroundResource(R.drawable.border_curve_green);
                    String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                    String path = getPath(context, content_file);
                    holder.audio_option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   playAudio(path);
                            associateFragment.playOptionAudio(path);
                        }
                    });
                } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")) {
                    holder.tx_option.setVisibility(GONE);
                    holder.img_option.setVisibility(VISIBLE);
                    holder.audio_option.setVisibility(GONE);
                    holder.empty_bg.setBackground(context.getResources().getDrawable(R.drawable.border_curve_green));
                    String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                    String path = getPath(context, content_file);
                    Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.img_option);
                }
            }
        }else{
            if (questionModel.getQuestionassociate() != null) {
                if (questionModel.getQuestionassociate().getQuestionassociatetext() != null) {
                    holder.selected_option.setVisibility(VISIBLE);
                    holder.empty_bg.setBackground(null);
                    holder.tx_option.setVisibility(VISIBLE);
                    holder.tx_option.setText(questionModel.getQuestionassociate().getQuestionassociatetext());
                    holder.img_option.setVisibility(GONE);
                    holder.audio_option.setVisibility(GONE);
                }
                else if (questionModel.getQuestionassociate().getQuestionOptionfile() != null) {
                    holder.selected_option.setVisibility(VISIBLE);
                    holder.empty_bg.setBackground(null);
                    String extension = questionModel.getQuestionassociate().getQuestionOptionfile().getFileext();
                    if (extension.equalsIgnoreCase("mp3")) {
                        holder.tx_option.setVisibility(GONE);
                        holder.img_option.setVisibility(GONE);
                        holder.audio_option.setVisibility(VISIBLE);
                        String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                        String path = getPath(context, content_file);
                        holder.audio_option.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //  playAudio(path);
                                associateFragment.playOptionAudio(path);
                            }
                        });
                    } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")) {
                        holder.tx_option.setVisibility(GONE);
                        holder.img_option.setVisibility(VISIBLE);
                        holder.audio_option.setVisibility(GONE);
                        String content_file = questionModel.getQuestionassociate().getQuestionOptionfile().getFilename();
                        String path = getPath(context, content_file);
                        Glide.with(context).load(path).placeholder(R.drawable.placeholder_rec).into(holder.img_option);
                    }
                }
            }else{
                holder.selected_option.setVisibility(View.GONE);
                holder.empty_bg.setBackground(ContextCompat.getDrawable(context, R.drawable.line_dash_bg));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(questionModelList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_option)
        LinearLayout selected_option;
        @BindView(R.id.empty_bg)
        LinearLayout empty_bg;
        @BindView(R.id.text_layout)
        LinearLayout text_layout;
        @BindView(R.id.tx_associate_question)
        TextView tx_associate_question;
        @BindView(R.id.audiotext_associate)
        ImageView audiotext_associate;
        @BindView(R.id.img_associate_question)
        ImageView img_associate_question;
        @BindView(R.id.audio_associate_question)
        ImageView audio_associate_question;
        @BindView(R.id.tx_option)
        TextView tx_option;
        @BindView(R.id.img_option)
        ImageView img_option;
        @BindView(R.id.audio_option)
        ImageView audio_option;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void playAudio(String file_name) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, Uri.parse(file_name));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSubmit(boolean value) {
        isSubmit = value;
    }
}