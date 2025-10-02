package com.fortyk.studentapp.fragments;

import static android.view.View.GONE;
import static com.fortyk.studentapp.utils.Constants.getPath;
import static com.fortyk.studentapp.utils.Constants.stopMediaPlayer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.adapters.MCQSingleTextAdapter;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.lessons.Question;
import com.fortyk.studentapp.utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MCQTextSingleFragment extends Fragment implements MCQSingleTextAdapter.TemplateItemClickListener {
    @BindView(R.id.question_title)
    TextView questionTitle;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.pause)
    ImageView pause;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.question_heading_audio)
    ImageView question_heading_audio;

    private int stopPosition = 0;
    private static final int delayTime = 3000;

    private String answer = "";

    private MediaPlayer mediaPlayer;

    private ImageView submit;
    private ImageView reset;
    private ImageView associate_audio;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private MCQSingleTextAdapter adapter;

    private Question question;
    private List<QuestionModel> questionModelList = new ArrayList<>();
    private int type = 0;
    private int tryAgainCount = 0;
    private String path = "";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        question = getArguments().getParcelable("question");
        type = getArguments().getInt("type");
        View view = inflater.inflate(R.layout.fragment_mcq_text_single_response, container, false);
        ButterKnife.bind(this, view);
        submit = getActivity().findViewById(R.id.submit);
        reset = getActivity().findViewById(R.id.reset);
        associate_audio = getActivity().findViewById(R.id.associate_audio);
        associate_audio.setVisibility(GONE);
        reset.setVisibility(GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = question.getQuestionobject().getQuestionheading().getHeadingtext();
        if (title == null || title.isEmpty()) {
            questionTitle.setText("Choose the correct answer.");
        } else {
            questionTitle.setText(question.getQuestionobject().getQuestionheading().getHeadingtext());
        }

        if (question.getQuestionobject().getQuestionheading().getHeadingfiles() != null) {
            question_heading_audio.setVisibility(View.VISIBLE);
            initheading();
        } else {
            question_heading_audio.setVisibility(GONE);
        }

        questionModelList.addAll(question.getQuestionobject().getQuestionoptions());
        Collections.shuffle(questionModelList);
        adapter = new MCQSingleTextAdapter(getActivity(), questionModelList, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recycler_view.setAdapter(adapter);

        if (question.getQuestionobject().getQuestionfile() != null) {
            String content_file = question.getQuestionobject().getQuestionfile().getFilename();
            path = getPath(getActivity(), content_file);
            String extension = question.getQuestionobject().getQuestionfile().getFileext();
            if (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("3gp") || extension.equalsIgnoreCase("flv") || extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("avi") || extension.equalsIgnoreCase("mpeg")) {
                imageView.setVisibility(GONE);
                videoView.setVisibility(View.VISIBLE);
                pause.setVisibility(GONE);
                initResource(path);
            } else if (extension.equalsIgnoreCase("mp3")) {

                imageView.setVisibility(GONE);
                videoView.setVisibility(View.VISIBLE);
                pause.setVisibility(GONE);
                videoView.setBackground(getResources().getDrawable(R.drawable.media_audio_bg));
                initResource(path);
            } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp")) {

                imageView.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(path).placeholder(R.drawable.placeholder_rec).into(imageView);
                videoView.setVisibility(GONE);
                pause.setVisibility(GONE);
                play.setVisibility(GONE);
                mainLayout.setEnabled(false);
            }
        } else {
            mainLayout.setVisibility(GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                if (type == 2 && tryAgainCount == 3) {
                    stopMediaPlayer(mediaPlayer);
                    ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
                } else if ((type == 3 && tryAgainCount == 1) || (type == 4 && tryAgainCount == 1)) {
                    stopMediaPlayer(mediaPlayer);
                    ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
                } else {
                    if (adapter.getLastCheckedPosition() == -1) {
                        Toast.makeText(getActivity(), "Please select an answer.", Toast.LENGTH_SHORT).show();
                        submit.setEnabled(true);
                    } else {
                        QuestionModel selectOption = adapter.getSelectedItem();
                        if (selectOption.isQuestionoptioniscorrect()) {
                            openResultPopup(true);
                        } else {
                            openResultPopup(false);
                        }
                    }
                }
                submit.setImageResource(R.drawable.submit_unselected);
            }
        });
    }

    @OnClick({R.id.main_layout, R.id.play, R.id.pause, R.id.question_heading_audio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_layout:
                stopmediaplayer();
                play.setVisibility(GONE);
                pause.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pause.setVisibility(GONE);
                    }
                }, delayTime);
                break;
            case R.id.play:
                stopmediaplayer();
                if (videoView != null) {
                    play.setVisibility(GONE);
                    pause.setVisibility(View.VISIBLE);
                    videoView.start();
                } else {
                    initResource(path);
                    play.setVisibility(GONE);
                    pause.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            pause.setVisibility(GONE);
                        }
                    }, delayTime);
                }
                break;
            case R.id.pause:
                pauseResource();
                break;
            case R.id.question_heading_audio:
                playHeadingAudio(path);
        }
    }

    private void initResource(String videoFilePath) {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        if (videoFilePath != null) {
            videoView.setVideoPath(videoFilePath);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onStop();
                    play.setVisibility(View.VISIBLE);

                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    return false;
                }
            });
        }
    }

    private void initheading() {
        if (progressDialog != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        String content_file = question.getQuestionobject().getQuestionheading().getHeadingfiles().getFilename();
        path = getPath(getActivity(), content_file);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (Helper.isRPIConnected()) {
            try {
                mediaPlayer.setDataSource(getActivity(), Uri.parse(path));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        } else {
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                if (progressDialog != null)
                    progressDialog.dismiss();

            }
        }


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Media file not available", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void pauseResource() {
        if (videoView.isPlaying()) {
            videoView.pause();
            play.setVisibility(View.VISIBLE);
            stopmediaplayer();
        }
        pause.setVisibility(GONE);
        stopPosition = videoView.getCurrentPosition();

    }

    @Override
    public void onPause() {
        super.onPause();
        pauseResource();
    }

    @Override
    public void onStop() {
        super.onStop();
        pause.setVisibility(GONE);
    }

    public void openResultPopup(boolean answer) {
        View resultLayout = getActivity().getLayoutInflater().inflate(R.layout.result_popup_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final AlertDialog popupDialog = builder.create();
        popupDialog.setView(resultLayout);
        popupDialog.setCancelable(false);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        popupDialog.show();

        ImageView background = resultLayout.findViewById(R.id.image_bg);
        TextView message = resultLayout.findViewById(R.id.message);
        Button btSubmit = resultLayout.findViewById(R.id.submit);

        if (answer) {
            btSubmit.setBackground(getResources().getDrawable(R.drawable.next_btn));
            background.setBackground(getResources().getDrawable(R.drawable.success_bg));
            message.setText("Well done!");
            playAudio(R.raw.correct_answer);
            ((TemplateActivity) getActivity()).resultCorrectCount++;

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupDialog.dismiss();
                    stopMediaPlayer(mediaPlayer);
                    ((TemplateActivity) getActivity()).nextQuestion(true, tryAgainCount);
                }
            });
        } else {
            background.setBackground(getResources().getDrawable(R.drawable.fail_bg));
            message.setText("Wrong answer!");
            playAudio(R.raw.incorrect_answer);

            if (type == 2) {
                if (tryAgainCount < 2) {
                    btSubmit.setBackground(getResources().getDrawable(R.drawable.try_again));
                } else {
                    btSubmit.setBackground(getResources().getDrawable(R.drawable.view_answer));
                }
            } else {
                btSubmit.setBackground(getResources().getDrawable(R.drawable.next_btn));
            }

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryAgainCount++;
                    if (type == 2) {
                        if (tryAgainCount == 3) {
                            showAnswer();
                            popupDialog.dismiss();
                        } else {
                            Collections.shuffle(questionModelList);
                            adapter.setLastCheckedPosition(-1);
                            submit.setImageResource(R.drawable.submit_unselected);
                            submit.setEnabled(true);
                            popupDialog.dismiss();

                        }
                    } else {
                        stopMediaPlayer(mediaPlayer);
                        ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
                        Collections.shuffle(questionModelList);
                        adapter.setLastCheckedPosition(-1);
                        submit.setImageResource(R.drawable.submit_unselected);
                        popupDialog.dismiss();

                    }
                }
            });
        }
    }

    private void playAudio(int file_name) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), file_name);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
        } catch (Exception e) {

        }
    }

    private void playHeadingAudio(String file_name) {

        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(GONE);
        }
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } else {
            Toast.makeText(getActivity(), "File doesn't exist!", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopmediaplayer() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onItemClick(QuestionModel questionModel, int position) {
        submit.setImageResource(R.drawable.submit_selected);
        answer = questionModel.getQuestionoptiontext();
    }

    private void showAnswer() {

        submit.setImageResource(R.drawable.next_btn);
        submit.setEnabled(true);
        adapter.setSubmit(true);
        adapter.setLastCheckedPosition(-1);
        for (QuestionModel questionModel : questionModelList) {
            if (questionModel.isQuestionoptioniscorrect()) {
                questionModel.setSelectediscorrect(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
