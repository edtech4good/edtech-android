package com.fortyk.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity {

    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.animation)
    ImageView animation;
    @BindView(R.id.title)
    ImageView title;
    @BindView(R.id.tiger)
    ImageView tiger;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.percentage)
    TextView percentage;
    @BindView(R.id.grade)
    TextView grade;
    @BindView(R.id.level)
    TextView level;
    @BindView(R.id.lesson)
    TextView lesson;
    @BindView(R.id.practice)
    TextView practice;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.tableRow3)
    TableRow tableRow3;

    private Bundle extras;

    public int resultTotalQuestion;
    public int resultCorrectCount;
    public String resultGrade;
    public String resultLevel;
    public String resultLesson = "";
    public String resultPractice = "";
    public int type;

    private int scorepercentage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        extras = getIntent().getExtras();
        if (extras != null) {
            resultTotalQuestion = extras.getInt("total_question");
            resultCorrectCount = extras.getInt("correct_count");
            resultGrade = extras.getString("grade_title");
            resultLevel = extras.getString("level_title");
            resultLesson = extras.getString("lesson_title");
            resultPractice = extras.getString("practice_title");
            type = extras.getInt("type");

            grade.setText(resultGrade);
            level.setText(resultLevel);
            if (resultLesson.equals("")) {
                tableRow3.setVisibility(View.GONE);
            } else {
                tableRow3.setVisibility(View.VISIBLE);
                lesson.setText(resultLesson);
            }
            practice.setText(resultPractice);
            scorepercentage = (int) (((double) resultCorrectCount / resultTotalQuestion) * 100);
            percentage.setText(scorepercentage + "%");
            score.setText(resultCorrectCount + "/" + resultTotalQuestion);
            if (scorepercentage >= 80) {
                title.setImageResource(R.drawable.congratulations_text);
                status.setText("Completed");
                main_layout.setBackground(getResources().getDrawable(R.drawable.lesson_positive_feedback));
                tiger.setBackground(getResources().getDrawable(R.drawable.congratulation_tiger));
                animation.setImageResource(R.drawable.animation_congrats);
            } else {
                animation.setImageResource(R.drawable.try_again_text);
                status.setText("Incomplete");
                main_layout.setBackground(getResources().getDrawable(R.drawable.lesson_negative_feedback));
                tiger.setBackground(getResources().getDrawable(R.drawable.oops_tiger));
                animation.setImageResource(R.drawable.animation_oops_level);
            }
        }
    }

    @OnClick({R.id.finish})
    public void onClick(View view) {
        if (view.getId() == R.id.finish) {
            if (!Helper.getBooleanFromSettings(getApplicationContext(), Constants.IS_BASE_LINE)) {
                if (scorepercentage >= 80) {
                    if (type == 3) {
                        ChapterActivity.chapterActivity.finish();
                    }
                }
            } else {
                startActivity(new Intent(this, GradesActivity.class)
                        .putExtra("curriculumid", Helper.getStringFromSettings(getApplicationContext(), Constants.CURRICULUM_ID))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
            }
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        if (!Helper.getBooleanFromSettings(getApplicationContext(), Constants.IS_BASE_LINE)) {
            if (scorepercentage >= 80) {
                if (type == 3) {
                    ChapterActivity.chapterActivity.finish();
                }
            }
        } else {
            startActivity(new Intent(this, GradesActivity.class)
                    .putExtra("curriculumid", Helper.getStringFromSettings(getApplicationContext(), Constants.CURRICULUM_ID))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
        }
        finish();
    }
}
