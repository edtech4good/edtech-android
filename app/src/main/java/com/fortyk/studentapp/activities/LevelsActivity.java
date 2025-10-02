package com.fortyk.studentapp.activities;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.adapters.LevelsAdapter;
import com.fortyk.studentapp.models.curriculum.Grades;
import com.fortyk.studentapp.models.curriculum.Levels;
import com.fortyk.studentapp.models.lessons.LessonQuizQuestions;
import com.fortyk.studentapp.utils.Helper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LevelsActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view_levels)
    RecyclerView recyclerViewLevels;
    @BindView(R.id.logout_button)
    ImageView logout;
    @BindView(R.id.back_button)
    ImageView back;
    private Bundle extras;

    private LevelsAdapter levelsAdapter;

    private List<Levels> levelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        ButterKnife.bind(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            Intent i = getIntent();
            Grades grade = (Grades) i.getParcelableExtra("grade_data");

            title.setText(grade.getGradename() + " - " + "Levels");
            levelsList = grade.getLevelsList();

            Collections.sort(levelsList, new Comparator<Levels>()
            {
                @Override
                public int compare(Levels lhs, Levels rhs) {

                    return Integer.valueOf(lhs.getLevelorder()).compareTo(rhs.getLevelorder());
                }
            });

            recyclerViewLevels.setHasFixedSize(true);
            recyclerViewLevels.setLayoutManager(new GridLayoutManager(this, 3));
            levelsAdapter = new LevelsAdapter(this, levelsList, grade.getGradename());
            recyclerViewLevels.setAdapter(levelsAdapter);
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
                Intent intent = new Intent(LevelsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
