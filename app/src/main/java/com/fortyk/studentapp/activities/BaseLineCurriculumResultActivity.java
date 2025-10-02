package com.fortyk.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseLineCurriculumResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_line_curriculum_result);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.start_button})
    public void onClick(View view) {
        if (view.getId() == R.id.start_button) {
            startActivity(new Intent(this, GradesActivity.class)
                    .putExtra("curriculumid", Helper.getStringFromSettings(getApplicationContext(), Constants.CURRICULUM_ID))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );

            finish();

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GradesActivity.class)
                .putExtra("curriculumid", Helper.getStringFromSettings(getApplicationContext(), Constants.CURRICULUM_ID))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
        );
        finish();
    }
}