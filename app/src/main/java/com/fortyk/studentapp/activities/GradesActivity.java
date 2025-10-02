package com.fortyk.studentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.adapters.GradesAdapter;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.curriculum.CurriculumResponse;
import com.fortyk.studentapp.models.curriculum.Grades;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class GradesActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logout_button)
    ImageView logout;
    @BindView(R.id.recycler_view_grades)
    RecyclerView recyclerViewGrades;
    private GradesAdapter gradesAdapter;
    private List<Grades> gradesList = new ArrayList<>();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        ButterKnife.bind(this);

        recyclerViewGrades.setHasFixedSize(true);
        recyclerViewGrades.setLayoutManager(new GridLayoutManager(this, 3));
        gradesAdapter = new GradesAdapter(this, gradesList);
        recyclerViewGrades.setAdapter(gradesAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String curriculumid = extras.getString("curriculumid");

            cd = new ConnectionDetector(this);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                getCurriculum(curriculumid);
            } else {
                cd.noInternetPopup();
            }
        }
    }

    private void getCurriculum(String Curriculum_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CurriculumResponse> call = apiInterface.getCurriculum(Curriculum_id, "Bearer " + Helper.getStringFromSettings(GradesActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<CurriculumResponse>() {

            @Override
            public void onResponse(Call<CurriculumResponse> call, retrofit2.Response<CurriculumResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    gradesList.clear();
                    gradesList.addAll(response.body().getData().getGradesList());
                    Collections.sort(gradesList, new Comparator<Grades>() {
                        @Override
                        public int compare(Grades lhs, Grades rhs) {
                            return Integer.valueOf(lhs.getGradeorder()).compareTo(rhs.getGradeorder());
                        }
                    });
                    gradesAdapter.notifyDataSetChanged();
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
                    Helper.showToast(GradesActivity.this, finalErrorMessage);
                    return;
                }
            }

            @Override
            public void onFailure(Call<CurriculumResponse> call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(GradesActivity.this, "Please check your internet connection!");
            }
        });
    }

    @OnClick({R.id.logout_button})
    public void onClick(View view) {
            Helper.clearToken(getApplicationContext());
            Intent intent = new Intent(GradesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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

        message.setText("Do you want to exit?");
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


}
