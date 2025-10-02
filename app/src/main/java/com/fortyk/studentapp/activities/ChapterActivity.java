package com.fortyk.studentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.adapters.ChaptersAdapter;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.lessons.LearningPath;
import com.fortyk.studentapp.models.lessons.LessonResponse;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class ChapterActivity extends BaseActivity {

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.logout_button)
    ImageView logout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view_chapters)
    RecyclerView recyclerViewChapters;
    private Bundle extras;

    private ChaptersAdapter chaptersAdapter;
    private List<LearningPath> lessonLearnings = new ArrayList<>();
    private List<LearningPath> lessonChapters = new ArrayList<>();

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog progressDialog;

    public static BaseActivity chapterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bricks);
        ButterKnife.bind(this);

        chapterActivity = this;

        extras = getIntent().getExtras();
        if (extras != null) {

            String grade_title = extras.getString("grade_title");
            String level_title = extras.getString("level_title");
            String lesson_title = extras.getString("lesson_title");
            String lesson_id = extras.getString("lesson_id");

            title.setText(grade_title + " - " + level_title + " - " + lesson_title);

            recyclerViewChapters.setHasFixedSize(true);
            recyclerViewChapters.setLayoutManager(new GridLayoutManager(this, 3));
            chaptersAdapter = new ChaptersAdapter(ChapterActivity.this, lessonLearnings, grade_title, level_title, lesson_title);
            recyclerViewChapters.setAdapter(chaptersAdapter);

            cd = new ConnectionDetector(this);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                getChapters(lesson_id);
            } else {
                cd.noInternetPopup();
            }
        }
    }

    private void getChapters(String lesson_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LessonResponse> call = apiInterface.getLessonChapters(lesson_id, "Bearer " + Helper.getStringFromSettings(ChapterActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<LessonResponse>() {

            @Override
            public void onResponse(Call<LessonResponse> call, retrofit2.Response<LessonResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    lessonLearnings.clear();
                    sort(response.body().getData().getLearningpath());
                  /*  Collections.sort(lessonLearnings, new Comparator<LearningPath>() {
                        @Override
                        public int compare(LearningPath lhs, LearningPath rhs) {
                            return lhs.getLearningpathname().compareToIgnoreCase(rhs.getLearningpathname());
                        }
                    });*/
                    chaptersAdapter.notifyDataSetChanged();
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
                    Helper.showToast(ChapterActivity.this, finalErrorMessage);
                    return;
                }
            }

            @Override
            public void onFailure(Call<LessonResponse> call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(ChapterActivity.this, "Please check your internet connection!");
            }
        });
    }

    private void sort(List<LearningPath> learningPathList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            lessonLearnings.addAll(learningPathList);
            Collections.sort(lessonLearnings, (lhs, rhs) -> {
                int sorted;
                sorted = Integer.valueOf(lhs.getLearningpathtype()).compareTo(rhs.getLearningpathtype());
                if (sorted == 0) {
                    if (lhs.getLearningpathtype() == 1 && rhs.getLearningpathtype() == 1) {
                        sorted = Integer.valueOf(lhs.getLessonlearningorder()).compareTo(rhs.getLessonlearningorder());
                    } else if (lhs.getLearningpathtype() == 2 && rhs.getLearningpathtype() == 2) {
                        sorted = Integer.valueOf(lhs.getLessonpracticeorder()).compareTo(rhs.getLessonpracticeorder());
                    } else if (lhs.getLearningpathtype() == 3 && rhs.getLearningpathtype() == 3) {
                        sorted = Integer.valueOf(lhs.getLessonquizorder()).compareTo(rhs.getLessonquizorder());
                    }
                }

                return sorted;
            });
            handler.post(() -> {
                chaptersAdapter.notifyDataSetChanged();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            });
        });
    }


    @OnClick({R.id.back_button, R.id.logout_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.logout_button:
                Helper.clearToken(getApplicationContext());
                Intent intent = new Intent(ChapterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
