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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.adapters.FillBlankAnswerAdapter;
import com.fortyk.studentapp.adapters.FillBlankOptionAdapter;
import com.fortyk.studentapp.models.FillBlanksModel;
import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.SelectedFillBlanks;
import com.fortyk.studentapp.models.lessons.Question;
import com.fortyk.studentapp.models.lessons.QuestionDistractors;
import com.fortyk.studentapp.utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillInBlankFragment extends Fragment implements FillBlankOptionAdapter.TextSelectedListener,
        FillBlankAnswerAdapter.FillBlanksListener {
    @BindView(R.id.question_title)
    TextView question_title;
    @BindView(R.id.question_audio)
    ImageView question_audio;
    @BindView(R.id.question_heading_audio)
    ImageView question_heading_audio;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.recycler_view_answer)
    RecyclerView recycler_view_answer;
    private FillBlankOptionAdapter adapter;
    private FillBlankAnswerAdapter blankAnswerAdapter;
    private List<String> optionList = new ArrayList<>();
    private List<FillBlanksModel> answerList = new ArrayList<>();
    private List<FillBlanksModel> answerListTemp = new ArrayList<>();
    private List<QuestionModel> questionModelList = new ArrayList<>();
    private ImageView submit;
    private MediaPlayer mediaPlayer;
    private String tempAnswer = "";
    private String answer = "";
    private String myAns = "";
    private int selectedPosition = -1;
    private int optionSize = 0;
    private List<String> tempSelectedList = new ArrayList<>();

    private List<QuestionModel> originalFillBlanksList = new ArrayList<>();
    private List<SelectedFillBlanks> answeredFillBlanksList = new ArrayList<>();
    private List<String> mSelectedItem = new ArrayList<>();

    private int type = 0;
    private ImageView reset;
    private ImageView associate_audio;
    int tryAgainCount = 0;
    private ImageView tryAgain;
    private Question question;
    private boolean clickSubmit = false;

    private StringBuilder stringAnswer = new StringBuilder();

    private ProgressDialog progressDialog;

    private String path = "";
    private String path1 = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_fill_blanks, container, false);
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
        createList();
        createFillBlankList();

        String title = question.getQuestionobject().getQuestionheading().getHeadingtext();
        if (title == null || title.isEmpty()) {
            question_title.setText("Fill in the blanks.");
        } else {
            question_title.setText(question.getQuestionobject().getQuestionheading().getHeadingtext());
        }
        adapter = new FillBlankOptionAdapter(getActivity(), questionModelList, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.HORIZONTAL, false));
        recycler_view.setAdapter(adapter);
        int question_length = question.getQuestionobject().getQuestiontext().length();
        blankAnswerAdapter = new FillBlankAnswerAdapter(getActivity(), answerList, originalFillBlanksList, question_length, this);
        /*if(question_length > 52) {
           // recycler_view_answer.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS));
          //  recycler_view_answer.setLayoutManager(new (getActivity(), 5));
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.CENTER);
           // layoutManager.setAlignContent(AlignContent.FLEX_START);
            recycler_view_answer.setLayoutManager(layoutManager);
        }else{
            recycler_view_answer.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        }*/

        recycler_view_answer.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recycler_view_answer.setAdapter(blankAnswerAdapter);

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

        submit.setOnClickListener(v -> {
            submit.setEnabled(false);
            if (type == 2 && tryAgainCount == 3) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else if ((type == 3 && tryAgainCount == 1) || (type == 4 && tryAgainCount == 1)) {
                stopMediaPlayer(mediaPlayer);
                ((TemplateActivity) getActivity()).nextQuestion(false, tryAgainCount);
            } else {
                boolean ans = false;
                if (answeredFillBlanksList.size() < question.getQuestionobject().getQuestionoptions().size()) {
                    Toast.makeText(getActivity(), "Please fill in the blanks.", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    Collections.sort(answeredFillBlanksList, new Comparator<SelectedFillBlanks>() {
                        @Override
                        public int compare(SelectedFillBlanks lhs, SelectedFillBlanks rhs) {

                            return Integer.valueOf(lhs.getPosition()).compareTo(rhs.getPosition());
                        }
                    });
                    for (int i = 0; i < answeredFillBlanksList.size(); i++) {
                        stringBuilder.append(answeredFillBlanksList.get(i).getText());
                    }

                    if (stringBuilder.toString().equals(stringAnswer.toString())) {
                        ans = true;
                    } else {
                        ans = false;
                    }
                }
                openResultPopup(ans);
            }
            submit.setImageResource(R.drawable.submit_unselected);
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
                createFillBlankList();
                selectedPosition = -1;
                createList();
                mSelectedItem.clear();
                answeredFillBlanksList.clear();
                submit.setImageResource(R.drawable.submit_unselected);
                adapter.setLastCheckedPosition(-1);
                adapter.notifyDataSetChanged();
                blankAnswerAdapter.notifyDataSetChanged();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(true);
                if (submit.getVisibility() == View.VISIBLE) {
                    createFillBlankList();
                    selectedPosition = -1;
                    createList();
                    answeredFillBlanksList.clear();
                    adapter.notifyDataSetChanged();
                    blankAnswerAdapter.notifyDataSetChanged();
                    submit.setImageResource(R.drawable.submit_unselected);
                    mSelectedItem.clear();
                }
            }
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
        Button btsubmit = resultLayout.findViewById(R.id.submit);

        if (ans) {
            btsubmit.setBackground(getResources().getDrawable(R.drawable.next_btn));
            background.setBackground(getResources().getDrawable(R.drawable.success_bg));
            message.setText("Well done!");
            playAudio(R.raw.correct_answer);
            ((TemplateActivity) getActivity()).resultCorrectCount++;
            btsubmit.setOnClickListener(new View.OnClickListener() {
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
                    btsubmit.setBackground(getResources().getDrawable(R.drawable.try_again));
                } else {
                    btsubmit.setBackground(getResources().getDrawable(R.drawable.view_answer));
                }
            } else {
                btsubmit.setBackground(getResources().getDrawable(R.drawable.next_btn));
            }

            btsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryAgainCount++;

                    if (type == 2) {
                        if (tryAgainCount == 3) {
                            optionList.clear();
                            questionModelList.clear();
                            answerList.clear();
                            if (question.getQuestionobject().getQuestionDistractors() != null) {
                                for (QuestionDistractors questionDistractor : question.getQuestionobject().getQuestionDistractors()) {
                                    questionModelList.add(new QuestionModel(questionDistractor.getQuestiondistractorid(),
                                            questionDistractor.getQuestiondistractortext(),
                                            0, false,
                                            null, null, false, false));
                                }
                            }
                            createFillBlankList();
                            blankAnswerAdapter.setShowCorrectAnswers(false);
                            blankAnswerAdapter.setShowAnswer(true);
                            adapter.notifyDataSetChanged();
                            blankAnswerAdapter.notifyDataSetChanged();
                            reset.setVisibility(GONE);
                            submit.setImageResource(R.drawable.next_btn);
                            submit.setEnabled(true);

                        } else {
                            optionList.clear();
                            Collections.shuffle(optionList);
                            adapter.notifyDataSetChanged();
                            blankAnswerAdapter.notifyDataSetChanged();
                            submit.setVisibility(View.GONE);
                            tryAgain.setVisibility(View.VISIBLE);
                            submit.setImageResource(R.drawable.submit_unselected);
                            submit.setEnabled(true);
                        }
                    } else {
                        submit.setImageResource(R.drawable.submit_unselected);
                        stopMediaPlayer(mediaPlayer);
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

    private void createList() {
        questionModelList.clear();
        originalFillBlanksList.clear();
        stringAnswer.setLength(0);
        questionModelList.addAll(question.getQuestionobject().getQuestionoptions());
        for (int i = 0; i < question.getQuestionobject().getQuestionoptions().size(); i++) {
            stringAnswer.append(question.getQuestionobject().getQuestionoptions().get(i).getQuestionoptiontext());
        }

        originalFillBlanksList.addAll(question.getQuestionobject().getQuestionoptions());
        if (question.getQuestionobject().getQuestionDistractors() != null)
            for (QuestionDistractors questionDistractor : question.getQuestionobject().getQuestionDistractors()) {
                questionModelList.add(new QuestionModel(questionDistractor.getQuestiondistractorid(),
                        questionDistractor.getQuestiondistractortext(),
                        0, false,
                        null, null, false, false));
            }
        Collections.shuffle(questionModelList);
    }

    private void createFillBlankList() {
        answerList.clear();

        String questionText = question.getQuestionobject().getQuestiontext();
        String[] arrOfStr = questionText.split("-----");
        int size = arrOfStr.length;

        for (int i = 0; i < size; i++) {
            if (arrOfStr[i].equals("")) {
                answerList.add(new FillBlanksModel("", true, i, 0));
            } else {
                answerList.add(new FillBlanksModel(arrOfStr[i], false, i, 0));
                if (i < size - 1) {
                    answerList.add(new FillBlanksModel("", true, i, 0));
                }
            }
        }

        if (questionText.endsWith("-----")) {
            answerList.add(new FillBlanksModel("", true, size, 0));
        }
    }

    @Override
    public void onTextSelected(QuestionModel questionModel, int position) {
        tempAnswer = questionModel.getQuestionoptiontext();
        selectedPosition = position;
    }

    @Override
    public void onTextClick(int id, int position) {
        QuestionModel selectOption = adapter.getSelectedItem();

        if (selectOption != null) {
            answerList.get(position).setAnswer(selectOption.getQuestionoptiontext());
            blankAnswerAdapter.notifyItemChanged(position);
            mSelectedItem.add(selectOption.getQuestionoptiontext());
            if (mSelectedItem.size() == question.getQuestionobject().getQuestionoptions().size()) {
                submit.setImageResource(R.drawable.submit_selected);
            }
            answeredFillBlanksList.add(new SelectedFillBlanks(position, selectOption.getQuestionoptionsequence(), selectOption.getQuestionoptiontext()));
            if (adapter.getLastCheckedPosition() != -1) {
                adapter.removeItem(adapter.getLastCheckedPosition());
                adapter.notifyDataSetChanged();
            }

        } else {
            Toast.makeText(getActivity(), "Please select the option", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.question_heading_audio, R.id.question_audio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_audio:
               /* String content_file = question.getQuestionobject().getQuestionfile().getFilename();
                String path = getPath(getActivity(), content_file);
                playHeadingAudio(path);*/
                break;
            case R.id.question_heading_audio:
                //  String content_file1 = question.getQuestionobject().getQuestionheading().getHeadingfiles().getFilename();
                //  String path1 = getPath(getActivity(), content_file1);
                playHeadingAudio(path);
                break;
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

    private void playHeadingAudio(String file_name) {
       /* if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(file_name));
        }*/
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
}
