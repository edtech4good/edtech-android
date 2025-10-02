package com.fortyk.studentapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.adapters.SchoolListAdapter;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.School;
import com.fortyk.studentapp.models.SchoolsResponse;
import com.fortyk.studentapp.models.curriculum.Levels;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchSchoolActivity extends AppCompatActivity {

    private SchoolListAdapter schoolListAdapter;

    private List<Levels> levelsList;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.clear_search)
    ImageView clear_search;
    @BindView(R.id.search_school)
    EditText search_school;
    private Bundle extras;
    private ProgressDialog progressDialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    List<School> schoolList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        ButterKnife.bind(this);

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        schoolListAdapter = new SchoolListAdapter(this, schoolList, school -> {
            Intent intent = new Intent();
            intent.putExtra("school", school.getSchoolname());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        recycler_view.setAdapter(schoolListAdapter);
        cd = new ConnectionDetector(SearchSchoolActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            getSchoolList();
        } else {
            Toast.makeText(this, "No Internet Connect", Toast.LENGTH_SHORT).show();

        }

        search_school.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                schoolListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getSchoolList() {
        progressDialog = new ProgressDialog(SearchSchoolActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getLmsClient().create(ApiInterface.class);
        Call<SchoolsResponse> call = apiInterface.getSchoolList();
        call.enqueue(new Callback<SchoolsResponse>() {

            @Override
            public void onResponse(Call<SchoolsResponse> call, retrofit2.Response<SchoolsResponse> response) {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        schoolList.addAll(response.body().getSchoolList());
                        schoolListAdapter.notifyDataSetChanged();
                    }

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
                    Helper.showToast(SearchSchoolActivity.this, finalErrorMessage);
                    return;
                }
            }

            @Override
            public void onFailure(Call<SchoolsResponse> call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }


}