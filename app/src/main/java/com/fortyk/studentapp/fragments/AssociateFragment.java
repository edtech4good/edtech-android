package com.fortyk.studentapp.fragments;

import static android.view.View.GONE;
import static com.fortyk.studentapp.utils.Constants.getPath;
import static com.fortyk.studentapp.utils.Constants.stopMediaPlayer;

import android.app.Fragment;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.adapters.AssociateAdapter;
import com.fortyk.studentapp.adapters.AssociateOptionAdapter;
import com.fortyk.studentapp.models.AssociateModel;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.lessons.Question;
import com.fortyk.studentapp.utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssociateFragment extends Fragment implements AssociateAdapter.AssociateItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.recycler_view_option)
    RecyclerView recycler_view_option;
    @BindView(R.id.question_title)
    TextView question_title;
    @BindView(R.id.question_audio)
    ImageView question_audio;
    @BindView(R.id.question_heading_audio)
    ImageView question_heading_audio;
    int correct_answers_size = 0;
    private AssociateAdapter adapter;
    private AssociateOptionAdapter optionAdapter;
    //  private List<QuestionModel> originalList = new ArrayList<>();
    private List<QuestionModel> answerList = new ArrayList<>();
    private List<QuestionModel> optionList = new ArrayList<>();
    private List<AssociateModel> associateModels = new ArrayList<>();
    private ImageView submit;
    private String answer = "";

    private MediaPlayer mediaPlayer;

    private Question question;
    private int type = 0;
    private ImageView reset;
    private ImageView associate_audio;
    int tryAgainCount = 0;
    private ImageView tryAgain;
    private boolean clickSubmit = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_associate, container, false);
        ButterKnife.bind(this, view);
        question = getArguments().getParcelable("question");
        type = getArguments().getInt("type");
        submit = getActivity().findViewById(R.id.submit);
        tryAgain = getActivity().findViewById(R.id.tryAgain);
        reset = getActivity().findViewById(R.id.reset);
        reset.setVisibility(View.VISIBLE);
        associate_audio = getActivity().findViewById(R.id.associate_audio);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* String title = question.getQuestionobject().getQuestionheading().getHeadingtext();
        if(!title.equals(null) || !title.isEmpty() || !title.equals("")) {
            question_title.setText(question.getQuestionobject().getQuestionheading().getHeadingtext());
        }*/

        if (question.getQuestionobject().getQuestionheading().getHeadingfiles() != null) {
            associate_audio.setVisibility(View.VISIBLE);
        } else {
            associate_audio.setVisibility(GONE);
        }

        optionList.clear();
        optionList.addAll(question.getQuestionobject().getQuestionoptions());
        Collections.shuffle(optionList);

        optionAdapter = new AssociateOptionAdapter(getActivity(), optionList, AssociateFragment.this);
        recycler_view_option.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recycler_view_option.setAdapter(optionAdapter);
        associateModels.clear();

        for (QuestionModel questionModel : question.getQuestionobject().getQuestionoptions()) {
            associateModels.add(new AssociateModel(questionModel.getQuestionoptionid(),
                    questionModel.getQuestionoptiontext(), questionModel.getQuestionoptionsequence(),
                    questionModel.isQuestionoptioniscorrect(), questionModel.getQuestionoptionfile(),
                    null, questionModel.isSelectediscorrect(),
                    questionModel.isSelected()));
        }

        adapter = new AssociateAdapter(getActivity(), associateModels, this, AssociateFragment.this);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recycler_view.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        submit.setOnClickListener(v -> {
            submit.setEnabled(false);
            if (type == 2 && tryAgainCount == 3) {
                stopMediaPlayer(mediaPlayer);
                mediaPlayer = null;
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else if (type == 3 && tryAgainCount == 1) {
                stopMediaPlayer(mediaPlayer);
                mediaPlayer = null;
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else if (type == 4 && tryAgainCount == 1) {
                stopMediaPlayer(mediaPlayer);
                mediaPlayer = null;
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else {
                boolean ans = false;
                if (answerList.size() > 0) {
                    int size = question.getQuestionobject().getQuestionoptions().size();
                    for (int i = 0; i < size; i++) {
                        if (associateModels.get(i).getQuestionassociate() == null) {
                            Toast.makeText(getActivity(), "Please match the answers", Toast.LENGTH_SHORT).show();
                            submit.setEnabled(true);
                            return;
                        }
                    }

                    for (int i = 0; i < size; i++) {

                        if (associateModels.get(i).getQuestionassociate().equals(
                                question.getQuestionobject().getQuestionoptions().get(i).getQuestionassociate())) {
                            ans = true;
                        } else {
                            ans = false;
                            break;
                        }
                    }

                    openResultPopup(ans);

                } else {
                    Toast.makeText(getActivity(), "Please match the answers", Toast.LENGTH_SHORT).show();
                    optionAdapter.setLastCheckedPosition(-1);
                    optionAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    submit.setEnabled(true);
                    return;
                }
            }
            submit.setImageResource(R.drawable.submit_unselected);
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(true);
                if (submit.getVisibility() == View.VISIBLE) {
                    associateModels.clear();
                    for (QuestionModel questionModel : question.getQuestionobject().getQuestionoptions()) {
                        associateModels.add(new AssociateModel(questionModel.getQuestionoptionid(),
                                questionModel.getQuestionoptiontext(), questionModel.getQuestionoptionsequence(),
                                questionModel.isQuestionoptioniscorrect(), questionModel.getQuestionoptionfile(),
                                null, questionModel.isSelectediscorrect(),
                                questionModel.isSelected()));
                    }

                    optionList.clear();
                    optionList.addAll(question.getQuestionobject().getQuestionoptions());
                    Collections.shuffle(optionList);
                    answerList.clear();
                    optionAdapter.setLastCheckedPosition(-1);
                    optionAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    submit.setImageResource(R.drawable.submit_unselected);
                }
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
                associateModels.clear();
                for (QuestionModel questionModel : question.getQuestionobject().getQuestionoptions()) {
                    associateModels.add(new AssociateModel(questionModel.getQuestionoptionid(),
                            questionModel.getQuestionoptiontext(), questionModel.getQuestionoptionsequence(),
                            questionModel.isQuestionoptioniscorrect(), questionModel.getQuestionoptionfile(),
                            null, questionModel.isSelectediscorrect(),
                            questionModel.isSelected()));
                }

                optionList.clear();
                optionList.addAll(question.getQuestionobject().getQuestionoptions());
                Collections.shuffle(optionList);
                answerList.clear();
                optionAdapter.setLastCheckedPosition(-1);
                optionAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                submit.setImageResource(R.drawable.submit_unselected);
            }
        });

        associate_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content_file1 = question.getQuestionobject().getQuestionheading().getHeadingfiles().getFilename();
                String path1 = getPath(getActivity(), content_file1);
                playHeadingAudio(path1);
            }
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
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupDialog.dismiss();
                    stopMediaPlayer(mediaPlayer);
                    mediaPlayer = null;
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
                        } else {
                            submit.setImageResource(R.drawable.submit_unselected);
                            submit.setVisibility(View.GONE);
                            tryAgain.setVisibility(View.VISIBLE);
                            submit.setEnabled(true);
                        }

                    } else {
                        submit.setImageResource(R.drawable.submit_unselected);
                        stopMediaPlayer(mediaPlayer);
                        mediaPlayer = null;
                        ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);

                    }
                    popupDialog.dismiss();
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
    public void onItemClick(AssociateModel questionModel, int position) {
        QuestionModel selectedOption = optionAdapter.getSelectedItem();
        if (selectedOption != null) {
            if (associateModels.get(position).getQuestionassociate() == null) {
                answerList.add(selectedOption);
                associateModels.get(position).setQuestionassociate(selectedOption.getQuestionassociate());
                adapter.notifyDataSetChanged();
                submit.setImageResource(R.drawable.submit_selected);
                optionList.remove(optionAdapter.getSelectedPosition());
                optionAdapter.setLastCheckedPosition(-1);
                optionAdapter.notifyDataSetChanged();
            }
        } else if (associateModels.size() > 0) {
            Toast.makeText(getActivity(), "Please select the option", Toast.LENGTH_SHORT).show();
        }
    }

    private void playHeadingAudio(String file_name) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return;
        }
        stopMediaPlayer(mediaPlayer);
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (Helper.isRPIConnected()) {
            try {
                mediaPlayer.setDataSource(getActivity(), Uri.parse(file_name));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mediaPlayer.setDataSource(file_name);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();

            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
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

    private void showAnswer() {

        submit.setImageResource(R.drawable.next_btn);
        submit.setEnabled(true);
        adapter.setSubmit(true);
        associateModels.clear();

        for (QuestionModel questionModel : question.getQuestionobject().getQuestionoptions()) {
            associateModels.add(new AssociateModel(questionModel.getQuestionoptionid(),
                    questionModel.getQuestionoptiontext(), questionModel.getQuestionoptionsequence(),
                    questionModel.isQuestionoptioniscorrect(), questionModel.getQuestionoptionfile(),
                    questionModel.getQuestionassociate(), questionModel.isSelectediscorrect(),
                    questionModel.isSelected()));
        }
        adapter.notifyDataSetChanged();
    }

    public void playOptionAudio(String file_name) {
        stopMediaPlayer(mediaPlayer);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (Helper.isRPIConnected()) {
            try {
                mediaPlayer.setDataSource(getActivity(), Uri.parse(file_name));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mediaPlayer.setDataSource(file_name);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Media file not available", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
