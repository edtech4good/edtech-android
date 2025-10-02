package com.fortyk.studentapp.fragments;

import static android.view.View.GONE;
import static com.fortyk.studentapp.utils.Constants.getPath;
import static com.fortyk.studentapp.utils.Constants.stopMediaPlayer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.adapters.ImageOrderAdapter;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.lessons.Question;
import com.fortyk.studentapp.utils.Helper;
import com.fortyk.studentapp.utils.ItemMoveCallback;
import com.fortyk.studentapp.utils.OptionItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CorrectOrderImageFragment extends Fragment implements OptionItemClickListener, ImageOrderAdapter.TouchListener {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.question_title)
    TextView question_title;
    @BindView(R.id.question_audio)
    ImageView question_audio;
    @BindView(R.id.question_heading_audio)
    ImageView question_heading_audio;
    private ImageOrderAdapter adapter;
    private ImageView submit;
    private String answer = "";
    private MediaPlayer mediaPlayer;
    ItemTouchHelper touchHelper;

    private Question question;
    private List<QuestionModel> optionList = new ArrayList<>();
    private List<QuestionModel> answerList = new ArrayList<>();
    private int type = 0;
    private ImageView reset;
    private ImageView associate_audio;
    int tryAgainCount = 0;
    private ImageView tryAgain;
    private boolean clickSubmit = false;

    private ProgressDialog progressDialog;

    private String path = "";
    private String path1 = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mcq_image_single_response, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        optionList.clear();

        optionList.addAll(question.getQuestionobject().getQuestionoptions());
        Collections.shuffle(optionList);

        String title = question.getQuestionobject().getQuestionheading().getHeadingtext();
        if (title == null || title.isEmpty()) {
            question_title.setText("Listen and put the pictures in order.");
        } else {
            question_title.setText(question.getQuestionobject().getQuestionheading().getHeadingtext());
        }
        adapter = new ImageOrderAdapter(getActivity(), optionList, this, this);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        if (question.getQuestionobject().getQuestionheading().getHeadingfiles() != null) {
            question_heading_audio.setVisibility(View.VISIBLE);
            initheading();
        } else {
            question_heading_audio.setVisibility(GONE);
        }

        if (question.getQuestionobject().getQuestionfile() != null) {
            question_audio.setVisibility(View.VISIBLE);
        } else {
            question_audio.setVisibility(GONE);
        }

        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler_view);
        recycler_view.setAdapter(adapter);

        submit.setOnClickListener(v -> {
            submit.setEnabled(false);
            if (type == 2 && tryAgainCount == 3) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else if ((type == 3 && tryAgainCount == 1) || (type == 4 && tryAgainCount == 1)) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else {
                if (!clickSubmit) {
                    Toast.makeText(getActivity(), "Please correct the order", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                } else {
                    boolean answer = false;
                    int size = answerList.size();
                    List<QuestionModel> questionOption = question.getQuestionobject().getQuestionoptions();
                    for (int i = 0; i < questionOption.size(); i++) {
                        if (adapter.optionList.get(i).getQuestionoptionsequence() == questionOption.get(i).getQuestionoptionsequence()) {
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
                Collections.shuffle(optionList);
                answerList.clear();
                adapter.setShowCorrectAnswer(false);
                adapter.showAnswer(false);
                adapter.notifyDataSetChanged();
                optionList.clear();
                optionList.addAll(question.getQuestionobject().getQuestionoptions());
                adapter.notifyDataSetChanged();
            }
        });

        tryAgain.setOnClickListener(v -> {
            answerList.clear();
            adapter.setShowCorrectAnswer(false);
            adapter.showAnswer(false);
            adapter.notifyDataSetChanged();
            optionList.clear();
            optionList.addAll(question.getQuestionobject().getQuestionoptions());
            adapter.notifyDataSetChanged();
            submit.setVisibility(View.VISIBLE);
            submit.setEnabled(true);
            tryAgain.setVisibility(View.GONE);
            Collections.shuffle(optionList);
        });
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
            btSubmit.setOnClickListener(v -> {
                popupDialog.dismiss();
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(true, tryAgainCount);
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
                    clickSubmit = false;
                    if (type == 2) {
                        if (tryAgainCount == 3) {
                            optionList.clear();
                            optionList.addAll(question.getQuestionobject().getQuestionoptions());
                            adapter.setShowCorrectAnswer(false);
                            adapter.showAnswer(true);
                            adapter.notifyDataSetChanged();
                            submit.setImageResource(R.drawable.next_btn);
                            submit.setEnabled(true);

                        } else {
                            optionList.clear();
                            optionList.addAll(question.getQuestionobject().getQuestionoptions());
                            Collections.shuffle(optionList);
                            adapter.setShowCorrectAnswer(true);
                            adapter.notifyDataSetChanged();
                            submit.setVisibility(View.GONE);
                            tryAgain.setVisibility(View.VISIBLE);
                            submit.setImageResource(R.drawable.submit_unselected);
                            submit.setEnabled(true);
                        }
                        popupDialog.dismiss();

                    } else {
                        popupDialog.dismiss();
                        submit.setImageResource(R.drawable.submit_unselected);
                        ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
                        stopMediaPlayer(mediaPlayer);
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
        }catch (Exception e){

        }




    }

    @Override
    public void onTouch(RecyclerView.ViewHolder viewHolder) {
        if (touchHelper != null) {
            touchHelper.startDrag(viewHolder);
            clickSubmit = true;
            submit.setImageResource(R.drawable.submit_selected);
        }

       /* QuestionModel selectedOption = adapter.getSelected();
        if (selectedOption != null) {
            answerList.add(adapter.getSelected());
            // answerAdapter.notifyDataSetChanged();
            // adapter.removeItem(adapter.getLastCheckedPosition());

            stringBuilder.append(adapter.getSelected().getQuestionoptiontext() + " ");
            adapter.removeItem(adapter.getLastCheckedPosition());
            tx_answer.setText(stringBuilder);
        }*/
    }

    @Override
    public void onOptionClick(QuestionModel questionModel) {
    }

    @OnClick({R.id.question_heading_audio, R.id.question_audio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_audio:
                /*String content_file = question.getQuestionobject().getQuestionfile().getFilename();
                String path = getPath(getActivity(), content_file);
                playHeadingAudio(path);*/
                break;
            case R.id.question_heading_audio:
                //  String content_file1 = question.getQuestionobject().getQuestionheading().getHeadingfiles().getFilename();
                //   String path1 = getPath(getActivity(), content_file1);
                playHeadingAudio(path);
                break;
        }
    }

    private void playHeadingAudio(String file_name) {
       /* if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(file_name));
        }*/
        if (mediaPlayer != null) {
            try {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "File doesn't exist!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initheading() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

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
                progressDialog.dismiss();
            }
        } else {
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressDialog.dismiss();
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Media file not available", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
