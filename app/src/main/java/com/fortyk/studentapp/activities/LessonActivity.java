package com.fortyk.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.adapters.LessonAdapter;
import com.fortyk.studentapp.models.curriculum.Lessons;
import com.fortyk.studentapp.models.curriculum.Levels;
import com.fortyk.studentapp.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonActivity extends BaseActivity {

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.logout_button)
    ImageView logout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view_lesson)
    RecyclerView recyclerViewLesson;

    private Bundle extras;

    private LessonAdapter lessonAdapter;

    private List<Lessons> lessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        ButterKnife.bind(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            Intent i = getIntent();
            Levels level = (Levels) i.getParcelableExtra("level_data");
            String grade_title = i.getStringExtra("grade_title");

            title.setText(grade_title + " - " + level.getLevelname());

            lessons = level.getLessonsList();

            if(level.isHasquiz()){
                lessons.add(new Lessons("Q", "", level.getLevelid(), "Q", "",
                        0, 0, 0, 0, false));
            }

            recyclerViewLesson.setHasFixedSize(true);
            recyclerViewLesson.setLayoutManager(new GridLayoutManager(this, 5));
            lessonAdapter = new LessonAdapter(LessonActivity.this, lessons, grade_title, level.getLevelname());
            recyclerViewLesson.setAdapter(lessonAdapter);

        }
    }

    @OnClick({R.id.back_button, R.id.logout_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.logout_button:
                Helper.clearToken(getApplicationContext());
                Intent intent = new Intent(LessonActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}

