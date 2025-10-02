package com.fortyk.studentapp.fragments;

import static android.view.View.GONE;
import static com.fortyk.studentapp.utils.Constants.getPath;
import static com.fortyk.studentapp.utils.Constants.stopMediaPlayer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.adapters.CorrectOrderTextAdapter;
import com.fortyk.studentapp.adapters.CorrectOrderTextAnswerAdapter;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.lessons.Question;
import com.fortyk.studentapp.utils.Helper;
import com.fortyk.studentapp.utils.OnStartDragListener;
import com.fortyk.studentapp.utils.OptionItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CorrectOrderTextFragment extends Fragment implements OptionItemClickListener, OnStartDragListener {

    @BindView(R.id.question_title)
    TextView question_title;
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
    //@BindView(R.id.recycler_view_answer)
    //MyRecyclerView recycler_view_answer;
    @BindView(R.id.tx_answer)
    TextView tx_answer;
    @BindView(R.id.answerLLy)
    LinearLayout answer_layout;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private CorrectOrderTextAdapter adapter;
    private CorrectOrderTextAnswerAdapter answerAdapter;
    private ImageView submit;
    private MediaPlayer mediaPlayer;

    private Question question;
    private List<QuestionModel> optionList = new ArrayList<>();
    private List<QuestionModel> answerList = new ArrayList<>();
    private StringBuilder correctanswer = new StringBuilder();
    private int type = 0;
    private ImageView reset;
    private ImageView associate_audio;
    private ImageView tryAgain;
    private int tryAgainCount = 0;
    private String path = "";
    private int stopPosition = 0;
    private static final int delayTime = 3000;
    private StringBuilder stringBuilder = new StringBuilder();
    ItemTouchHelper mItemTouchHelper;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_correct_order_text, container, false);
        ButterKnife.bind(this, view);

        question = getArguments().getParcelable("question");
        type = getArguments().getInt("type");

        submit = getActivity().findViewById(R.id.submit);
        tryAgain = getActivity().findViewById(R.id.tryAgain);
        reset = getActivity().findViewById(R.id.reset);
        reset.setVisibility(View.VISIBLE);
        associate_audio = getActivity().findViewById(R.id.associate_audio);
        associate_audio.setVisibility(GONE);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = question.getQuestionobject().getQuestionheading().getHeadingtext();
        if (title == null || title.isEmpty()) {
            question_title.setText("Put the words in order.");
        } else {
            question_title.setText(question.getQuestionobject().getQuestionheading().getHeadingtext());
        }

        if (question.getQuestionobject().getQuestionheading().getHeadingfiles() != null) {
            question_heading_audio.setVisibility(View.VISIBLE);
            initheading();
        } else {
            question_heading_audio.setVisibility(GONE);
        }

        optionList.addAll(question.getQuestionobject().getQuestionoptions());
        for (int i = 0; i < question.getQuestionobject().getQuestionoptions().size(); i++) {
            correctanswer.append(question.getQuestionobject().getQuestionoptions().get(i).getQuestionoptiontext() + "  ");
        }

        Collections.shuffle(optionList);

        boolean hasfile;
        if (question.getQuestionobject().getQuestionfile() != null) {
            hasfile = true;
            ;
        } else {
            hasfile = false;
        }

        adapter = new CorrectOrderTextAdapter(getActivity(), optionList, this, hasfile);
        if (question.getQuestionobject().getQuestionfile() != null) {
            recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        } else {
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        }
        recycler_view.setAdapter(adapter);

        answerAdapter = new CorrectOrderTextAnswerAdapter(getActivity(), answerList, this);
      /*  recycler_view_answer.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recycler_view_answer.setAdapter(answerAdapter);

        recycler_view_answer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                QuestionModel selectedOption = adapter.getSelected();
                if (selectedOption != null) {
                    answerList.add(adapter.getSelected());
                    answerAdapter.notifyDataSetChanged();
                    adapter.removeItem(adapter.getLastCheckedPosition());
                }

                if (adapter.optionList.size() == 0) {
                    submit.setImageResource(R.drawable.submit_selected);
                } else {
                    submit.setImageResource(R.drawable.submit_unselected);
                }
                return false;
            }
        });*/

        tx_answer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                QuestionModel selectedOption = adapter.getSelected();
                if (selectedOption != null) {
                    answerList.add(adapter.getSelected());
                    // answerAdapter.notifyDataSetChanged();
                    // adapter.removeItem(adapter.getLastCheckedPosition());

                    stringBuilder.append(adapter.getSelected().getQuestionoptiontext() + " ");
                    adapter.removeItem(adapter.getLastCheckedPosition());
                    tx_answer.setText(stringBuilder);
                }

                if (adapter.optionList.size() == 0) {
                    submit.setImageResource(R.drawable.submit_selected);
                } else {
                    submit.setImageResource(R.drawable.submit_unselected);
                }
                return false;
            }
        });

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

        submit.setOnClickListener(v -> {
            submit.setEnabled(false);
            if (type == 2 && tryAgainCount == 3) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else if ((type == 3 && tryAgainCount == 1) || (type == 4 && tryAgainCount == 1)) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else {
                if (answerList.size() < question.getQuestionobject().getQuestionoptions().size()) {
                    Toast.makeText(getActivity(), "Please correct the order", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                } else {

                    boolean answer = false;
                    int size = answerList.size();
                    List<QuestionModel> questionOption = question.getQuestionobject().getQuestionoptions();
                    for (int i = 0; i < size; i++) {
                        if (answerList.get(i).getQuestionoptiontext().equals(questionOption.get(i).getQuestionoptiontext())) {
                            answer = true;
                        } else {
                            answer = false;
                            break;
                        }
                    }
                    openResultPopup(answer);
                }
            }
            submit.setImageResource(R.drawable.submit_unselected);
        });

        reset.setOnClickListener(v -> {
            submit.setEnabled(true);
            if (type == 2 && tryAgainCount == 3) {
                return;
            }
            if ((type == 3 && tryAgainCount == 1) || (type == 4 && tryAgainCount == 1)) {
                return;
            }
            if (submit.getVisibility() == View.VISIBLE) {

                answerList.clear();
                answerAdapter.setShowCorrectAnswer(false);
                answerAdapter.showAnswer(false);
                answerAdapter.notifyDataSetChanged();
                optionList.clear();
                optionList.addAll(question.getQuestionobject().getQuestionoptions());
                Collections.shuffle(optionList);
                adapter.notifyDataSetChanged();
            }
            tx_answer.setText("");
            stringBuilder.setLength(0);
            submit.setImageResource(R.drawable.submit_unselected);
        });

        tryAgain.setOnClickListener(v -> {
            answerList.clear();
            answerAdapter.setShowCorrectAnswer(false);
            answerAdapter.showAnswer(false);
            answerAdapter.notifyDataSetChanged();
            optionList.clear();
            optionList.addAll(question.getQuestionobject().getQuestionoptions());
            Collections.shuffle(optionList);
            adapter.notifyDataSetChanged();
            submit.setVisibility(View.VISIBLE);
            submit.setEnabled(true);
            tryAgain.setVisibility(View.GONE);
            submit.setImageResource(R.drawable.submit_unselected);
            tx_answer.setText("");
            stringBuilder.setLength(0);
        });
    }

    public void openResultPopup(boolean ans) {
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

        if (ans) {
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
                            optionList.clear();
                            answerList.clear();
                            answerList.addAll(question.getQuestionobject().getQuestionoptions());
                            // answerAdapter.setShowCorrectAnswer(false);
                            //  answerAdapter.showAnswer(true);
                            tx_answer.setText(correctanswer);
                            tx_answer.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                            adapter.notifyDataSetChanged();
                            answerAdapter.notifyDataSetChanged();
                            submit.setImageResource(R.drawable.next_btn);
                            submit.setEnabled(true);

                        } else {
                            optionList.clear();
                            answerAdapter.setShowCorrectAnswer(true);
                            Collections.shuffle(optionList);
                            adapter.notifyDataSetChanged();
                            answerAdapter.notifyDataSetChanged();
                            submit.setVisibility(View.GONE);
                            tryAgain.setVisibility(View.VISIBLE);
                            submit.setImageResource(R.drawable.submit_unselected);
                            submit.setEnabled(true);

                        }
                        popupDialog.dismiss();

                    } else {
                        popupDialog.dismiss();
                        submit.setImageResource(R.drawable.submit_unselected);
                        stopMediaPlayer(mediaPlayer);
                        ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
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

    @Override
    public void onOptionClick(QuestionModel questionModel) {

    }

    @OnClick({R.id.main_layout, R.id.play, R.id.pause, R.id.question_heading_audio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_layout:
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pause.setVisibility(View.GONE);
                    }
                }, delayTime);
                break;
            case R.id.play:
                if (videoView != null) {
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.GONE);
                    videoView.start();
                } else {
                    initResource(path);
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            pause.setVisibility(View.GONE);
                        }
                    }, delayTime);
                }
                break;
            case R.id.pause:
                pauseResource();
                break;
            case R.id.question_heading_audio:
                playHeadingAudio(path);
                break;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void initResource(String videoFilePath) {
        if (progressDialog != null) {
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
                    play.setVisibility(View.VISIBLE);
                    onStop();
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

    private void stopmediaplayer() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void pauseResource() {

        pause.setVisibility(View.GONE);
        stopPosition = videoView.getCurrentPosition();
        if (videoView.isPlaying()) {
            play.setVisibility(View.VISIBLE);
            videoView.pause();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        pauseResource();
    }

    @Override
    public void onStop() {
        super.onStop();
        pause.setVisibility(View.GONE);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
