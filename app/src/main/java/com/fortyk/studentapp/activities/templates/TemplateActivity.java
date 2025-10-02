package com.fortyk.studentapp.activities.templates;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.BaseLineCurriculumResultActivity;
import com.fortyk.studentapp.activities.ResultActivity;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.fragments.AssociateFragment;
import com.fortyk.studentapp.fragments.CorrectOrderImageFragment;
import com.fortyk.studentapp.fragments.CorrectOrderTextFragment;
import com.fortyk.studentapp.fragments.FillInBlankFragment;
import com.fortyk.studentapp.fragments.MCQImageMultiFragment;
import com.fortyk.studentapp.fragments.MCQImageSingleFragment;
import com.fortyk.studentapp.fragments.MCQTextMultiFragment;
import com.fortyk.studentapp.fragments.MCQTextSingleFragment;
import com.fortyk.studentapp.models.Result.LessonPractice;
import com.fortyk.studentapp.models.Result.LessonQuiz;
import com.fortyk.studentapp.models.lessons.LearningPath;
import com.fortyk.studentapp.models.lessons.LessonPracticeQuestions;
import com.fortyk.studentapp.models.lessons.LessonQuizQuestions;
import com.fortyk.studentapp.models.lessons.LevelQuiz;
import com.fortyk.studentapp.models.lessons.LevelQuizQuestions;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;
import com.fortyk.studentapp.utils.HorizontalProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TemplateActivity extends FragmentActivity {
    @BindView(R.id.question_type)
    TextView questionType;
    @BindView(R.id.question_number)
    TextView questionNumber;
    @BindView(R.id.associate_audio)
    ImageView associateAudio;
    @BindView(R.id.submit)
    ImageView submit;
    @BindView(R.id.reset)
    ImageView reset;
    @BindView(R.id.horizontalProgressBar)
    HorizontalProgressBar horizontalProgressBar;
    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private Bundle extras;
    private List<LessonPracticeQuestions> lessonPracticeQuestionsList = new ArrayList<>();
    private List<LessonQuizQuestions> lessonQuizQuestionsList = new ArrayList<>();
    private List<LevelQuizQuestions> levelQuizQuestionsList = new ArrayList<>();
    private LearningPath learningPath;
    private LevelQuiz levelQuiz;
    private int position = 0;
    private int type = 0;

    public int resultTotalQuestion;
    public int resultCorrectCount = 0;
    public String resultGrade;
    public String resultLevel;
    public String resultLesson = "";
    public String resultPractice = "";
    private List<LessonPractice> lessonPractices = new ArrayList<>();
    private List<LessonQuiz> lessonQuizzes = new ArrayList<>();
    private List<com.fortyk.studentapp.models.Result.LevelQuiz> levelQuizs = new ArrayList<>();

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this);
        extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt("type");
            if (type != 4) {
                learningPath = extras.getParcelable("lesson");
            } else {
                levelQuiz = extras.getParcelable("levelquiz");
            }
            resultGrade = extras.getString("grade_title");
            resultLevel = extras.getString("level_title");
            resultLesson = extras.getString("lesson_title");
            resultPractice = extras.getString("practice_title");
            // Get all questions of the Practice from database
            if (type == 2) {
                lessonPracticeQuestionsList.addAll(learningPath.getLessonpracticequestions());
                Collections.sort(lessonPracticeQuestionsList, new Comparator<LessonPracticeQuestions>() {
                    @Override
                    public int compare(LessonPracticeQuestions lhs, LessonPracticeQuestions rhs) {

                        return Integer.valueOf(lhs.getLessonpracticequestionorder()).compareTo(rhs.getLessonpracticequestionorder());
                    }
                });
                if (lessonPracticeQuestionsList.size() > 0) {
                    resultTotalQuestion = lessonPracticeQuestionsList.size();
                    showPractice(lessonPracticeQuestionsList.get(position));
                }
            } else if (type == 3) {
                lessonQuizQuestionsList.addAll(learningPath.getLessonquizquestions());
                Collections.sort(lessonQuizQuestionsList, new Comparator<LessonQuizQuestions>() {
                    @Override
                    public int compare(LessonQuizQuestions lhs, LessonQuizQuestions rhs) {

                        return Integer.valueOf(lhs.getLessonquizquestionorder()).compareTo(rhs.getLessonquizquestionorder());
                    }
                });
                if (lessonQuizQuestionsList.size() > 0) {
                    resultTotalQuestion = lessonQuizQuestionsList.size();
                    showQuiz(lessonQuizQuestionsList.get(position));
                }
            } else if (type == 4) {
                levelQuizQuestionsList.addAll(levelQuiz.getLevelQuizQuestions());
                Collections.sort(levelQuizQuestionsList, new Comparator<LevelQuizQuestions>() {
                    @Override
                    public int compare(LevelQuizQuestions lhs, LevelQuizQuestions rhs) {

                        return Integer.valueOf(lhs.getLevelquizquestionorder()).compareTo(rhs.getLevelquizquestionorder());
                    }
                });
                if (levelQuizQuestionsList.size() > 0) {
                    resultTotalQuestion = levelQuizQuestionsList.size();
                    showLevelQuiz(levelQuizQuestionsList.get(position));
                }
            }
            if (position < lessonPracticeQuestionsList.size()) {
                questionNumber.setText("Question " + (position + 1) + " of " + lessonPracticeQuestionsList.size());
            }

            if (position < lessonQuizQuestionsList.size()) {
                questionNumber.setText("Question " + (position + 1) + " of " + lessonQuizQuestionsList.size());
            }

            if (position < levelQuizQuestionsList.size()) {
                questionNumber.setText("Question " + (position + 1) + " of " + levelQuizQuestionsList.size());
            }
        }
        //submit click crash issue solved and changes are made in all 8 templates
    }

    public void nextQuestion(boolean isCorrect, int tries) {
        position++;
        if (type == 2) {
            if (position < lessonPracticeQuestionsList.size()) {
                questionNumber.setText("Question " + (position + 1) + " of " + lessonPracticeQuestionsList.size());
                showPractice(lessonPracticeQuestionsList.get(position));

                addLessonPractice(isCorrect, lessonPracticeQuestionsList.get(position - 1).getLessonpracticeid(),
                        lessonPracticeQuestionsList.get(position - 1).getLessonpracticequestionid(),
                        lessonPracticeQuestionsList.get(position - 1).getQuestionid(), tries);
            } else {
                addLessonPractice(isCorrect, lessonPracticeQuestionsList.get(position - 1).getLessonpracticeid(),
                        lessonPracticeQuestionsList.get(position - 1).getLessonpracticequestionid(),
                        lessonPracticeQuestionsList.get(position - 1).getQuestionid(), tries);
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    saveLessonPractice(learningPath.getLessonpracticeid());
                } else {
                    cd.noInternetPopup();
                }
            }
        } else if (type == 3) {
            if (position < lessonQuizQuestionsList.size()) {
                showQuiz(lessonQuizQuestionsList.get(position));
                questionNumber.setText("Question " + (position + 1) + " of " + lessonQuizQuestionsList.size());
                addLessonQuiz(isCorrect, lessonQuizQuestionsList.get(position - 1).getLessonquizid(),
                        lessonQuizQuestionsList.get(position - 1).getLessonquizquestionid(),
                        lessonQuizQuestionsList.get(position - 1).getQuestionid());
            } else {
                addLessonQuiz(isCorrect, lessonQuizQuestionsList.get(position - 1).getLessonquizid(),
                        lessonQuizQuestionsList.get(position - 1).getLessonquizquestionid(),
                        lessonQuizQuestionsList.get(position - 1).getQuestionid());
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    saveLessonQuiz(learningPath.getLessonquizid());
                } else {
                    cd.noInternetPopup();
                }
            }
        } else if (type == 4) {
            if (position < levelQuizQuestionsList.size()) {
                showLevelQuiz(levelQuizQuestionsList.get(position));
                questionNumber.setText("Question " + (position + 1) + " of " + levelQuizQuestionsList.size());
                addLevelQuiz(isCorrect, levelQuizQuestionsList.get(position - 1).getLevelid(),
                        levelQuizQuestionsList.get(position - 1).getLevelquizquestionid(),
                        levelQuizQuestionsList.get(position - 1).getQuestionid());

            } else {
                addLevelQuiz(isCorrect, levelQuizQuestionsList.get(position - 1).getLevelid(),
                        levelQuizQuestionsList.get(position - 1).getLevelquizquestionid(),
                        levelQuizQuestionsList.get(position - 1).getQuestionid());
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    saveLevelQuiz(levelQuiz.getLevelid());
                } else {
                    cd.noInternetPopup();
                }
            }
        }
        submit.setEnabled(true);
    }

    public void addLessonPractice(boolean iscorrect, String lessonpracticeid, String lessonpracticequestionid, String questionid, int tries) {
        lessonPractices.add(new LessonPractice(iscorrect, lessonpracticeid, lessonpracticequestionid, questionid, tries));
    }

    public void addLessonQuiz(boolean iscorrect, String lessonquizid, String lessonquizquestionid, String questionid) {
        lessonQuizzes.add(new LessonQuiz(iscorrect, lessonquizid, lessonquizquestionid, questionid));
    }

    public void addLevelQuiz(boolean iscorrect, String levelid, String levelquizquestionid, String questionid) {
        levelQuizs.add(new com.fortyk.studentapp.models.Result.LevelQuiz(iscorrect, levelid, levelquizquestionid, questionid));
    }

    private void showPractice(LessonPracticeQuestions lessonPracticeQuestions) {
        switch (lessonPracticeQuestions.getQuestion().getTemplatetypeid()) {
            case 1:
                loadMCQTextFragment();
                break;
            case 2:
                loadMCQSingleImageFragment();
                break;
            case 3:
                loadMCQTextMultiFragment();
                break;
            case 4:
                loadMCQMultiImageFragment();
                break;
            case 5:
                loadCorrectOrderTextFragment();
                break;
            case 6:
                loadImageOrderFragment();
                break;
            case 7:
                loadAssociateFragment();
                break;
            case 8:
                loadFillBlanksFragment();
                break;
        }
    }

    private void showQuiz(LessonQuizQuestions lessonQuizQuestions) {
        switch (lessonQuizQuestions.getQuestion().getTemplatetypeid()) {
            case 1:
                loadMCQTextFragment();
                break;
            case 2:
                loadMCQSingleImageFragment();
                break;
            case 3:
                loadMCQTextMultiFragment();
                break;
            case 4:
                loadMCQMultiImageFragment();
                break;
            case 5:
                loadCorrectOrderTextFragment();
                break;
            case 6:
                loadImageOrderFragment();
                break;
            case 7:
                loadAssociateFragment();
                break;
            case 8:
                loadFillBlanksFragment();
                break;
        }

    }

    private void showLevelQuiz(LevelQuizQuestions levelQuizQuestions) {
        switch (levelQuizQuestions.getQuestion().getTemplatetypeid()) {
            case 1:
                loadMCQTextFragment();
                break;
            case 2:
                loadMCQSingleImageFragment();
                break;
            case 3:
                loadMCQTextMultiFragment();
                break;
            case 4:
                loadMCQMultiImageFragment();
                break;
            case 5:
                loadCorrectOrderTextFragment();
                break;
            case 6:
                loadImageOrderFragment();
                break;
            case 7:
                loadAssociateFragment();
                break;
            case 8:
                loadFillBlanksFragment();
                break;
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    private void loadMCQTextFragment() {
        questionType.setText("MCQ (Single response) Text and Audio");

        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        MCQTextSingleFragment mcqTextSingleFragment = new MCQTextSingleFragment();
        mcqTextSingleFragment.setArguments(bundle);
        addFragment(mcqTextSingleFragment);
    }

    public void loadMCQTextMultiFragment() {
        questionType.setText("MCQ (Multi Correct) Text and Audio");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        MCQTextMultiFragment mcqTextMultiResponseFragment = new MCQTextMultiFragment();
        mcqTextMultiResponseFragment.setArguments(bundle);
        addFragment(mcqTextMultiResponseFragment);
    }

    public void loadMCQSingleImageFragment() {
        questionType.setText("MCQ (Single response) Picture");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        MCQImageSingleFragment mcqImageSingleFragment = new MCQImageSingleFragment();
        mcqImageSingleFragment.setArguments(bundle);
        addFragment(mcqImageSingleFragment);
    }

    public void loadMCQMultiImageFragment() {
        questionType.setText("MCQ (Multi Correct) Picture");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        MCQImageMultiFragment mcqImageMultiResponseFragment = new MCQImageMultiFragment();
        mcqImageMultiResponseFragment.setArguments(bundle);
        addFragment(mcqImageMultiResponseFragment);
    }

    public void loadCorrectOrderTextFragment() {
        questionType.setText("Correct Ordering (Text sequence)");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        CorrectOrderTextFragment correctOrderTextFragment = new CorrectOrderTextFragment();
        correctOrderTextFragment.setArguments(bundle);
        addFragment(correctOrderTextFragment);
    }

    public void loadImageOrderFragment() {
        questionType.setText("Correct Ordering (Pic sequence)");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        CorrectOrderImageFragment correctOrderImageFragment = new CorrectOrderImageFragment();
        correctOrderImageFragment.setArguments(bundle);
        addFragment(correctOrderImageFragment);
    }

    public void loadFillBlanksFragment() {
        questionType.setText("Fill in the blanks");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        FillInBlankFragment fillInBlankFragment = new FillInBlankFragment();
        fillInBlankFragment.setArguments(bundle);
        addFragment(fillInBlankFragment);
    }

    public void loadAssociateFragment() {
        questionType.setText("Associate");
        Bundle bundle = new Bundle();
        if (type == 2) {
            bundle.putParcelable("question", lessonPracticeQuestionsList.get(position).getQuestion());
        } else if (type == 3) {
            bundle.putParcelable("question", lessonQuizQuestionsList.get(position).getQuestion());
        } else {
            bundle.putParcelable("question", levelQuizQuestionsList.get(position).getQuestion());
        }
        bundle.putInt("type", type);
        AssociateFragment associateFragment = new AssociateFragment();
        associateFragment.setArguments(bundle);
        addFragment(associateFragment);
    }

    @Override
    public void onBackPressed() {
        backConfirmPopup();
    }

    private void backConfirmPopup() {
        View confirmLayout = getLayoutInflater().inflate(R.layout.back_confirm_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final AlertDialog popupDialog = builder.create();
        popupDialog.setView(confirmLayout);
        popupDialog.setCancelable(false);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        popupDialog.show();

        TextView message = confirmLayout.findViewById(R.id.message);
        TextView yesBtn = confirmLayout.findViewById(R.id.yes_btn);
        TextView noBtn = confirmLayout.findViewById(R.id.no_btn);

        if (type == 2) {
            message.setText("Do you want to exit the practice?");
        } else if (type == 3) {
            message.setText("Do you want to exit the quiz?");
        } else if (type == 4) {
            message.setText("Do you want to exit the quiz?");
        }

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
                finish();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });
    }

    private void openResult() {
        if (!Helper.getBooleanFromSettings(getApplicationContext(), Constants.IS_BASE_LINE)) {
            Helper.setBooleanInSettings(getApplicationContext(), Constants.BASE_LINE_PASSED, true);
            Intent intent = new Intent(TemplateActivity.this, ResultActivity.class);
            intent.putExtra("total_question", resultTotalQuestion);
            intent.putExtra("correct_count", resultCorrectCount);
            intent.putExtra("grade_title", resultGrade);
            intent.putExtra("level_title", resultLevel);
            intent.putExtra("lesson_title", resultLesson);
            intent.putExtra("practice_title", resultPractice);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        } else {
            Helper.setBooleanInSettings(getApplicationContext(), Constants.BASE_LINE_PASSED, true);
            Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, false);
            Intent intent = new Intent(TemplateActivity.this, BaseLineCurriculumResultActivity.class);
            intent.putExtra("total_question", resultTotalQuestion);
            intent.putExtra("correct_count", resultCorrectCount);
            intent.putExtra("grade_title", resultGrade);
            intent.putExtra("level_title", resultLevel);
            intent.putExtra("lesson_title", resultLesson);
            intent.putExtra("practice_title", resultPractice);
            intent.putExtra("type", type);
            startActivity(intent);
            finish();
        }
    }

    private void saveLessonPractice(String lessonpracticeid) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getLessonPracticeResult(lessonpracticeid, lessonPractices, "Bearer " + Helper.getStringFromSettings(TemplateActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                lessonPractices.clear();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    finish();
                }

                if (!response.isSuccessful()) {
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }
                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TemplateActivity.this, finalErrorMessage);
                    finish();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                lessonPractices.clear();
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(TemplateActivity.this, "You are offline! Unable to save your result.");
                finish();
            }
        });
    }

    private void saveLessonQuiz(String lessonquizid) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getLessonQuizResult(lessonquizid, lessonQuizzes, "Bearer " + Helper.getStringFromSettings(TemplateActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                lessonQuizzes.clear();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    openResult();
                }

                if (!response.isSuccessful()) {
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }
                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TemplateActivity.this, finalErrorMessage);
                    openResult();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                lessonQuizzes.clear();
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(TemplateActivity.this, "You are offline! Unable to save your result.");
                openResult();
            }
        });
    }

    private void saveLevelQuiz(String levelid) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getLevelQuizResult(levelid, levelQuizs, "Bearer " + Helper.getStringFromSettings(TemplateActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                levelQuizs.clear();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    openResult();
                }

                if (!response.isSuccessful()) {
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }
                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TemplateActivity.this, finalErrorMessage);
                    openResult();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                levelQuizs.clear();
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(TemplateActivity.this, "You are offline! Unable to save your result.");
                openResult();
            }
        });
    }
}
