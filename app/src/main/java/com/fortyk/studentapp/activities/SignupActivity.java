package com.fortyk.studentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.School;
import com.fortyk.studentapp.models.SchoolsResponse;
import com.fortyk.studentapp.models.signup.SignupRequest;
import com.fortyk.studentapp.models.signup.Students;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;
import com.fortyk.studentapp.utils.searchablespinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.first_name)
    EditText first_name;
    @BindView(R.id.phone_number)
    EditText phone_number;
    @BindView(R.id.email_id)
    EditText email_id;
    @BindView(R.id.gender)
    RadioGroup gender;
    @BindView(R.id.male_radio)
    RadioButton male_radio;
    @BindView(R.id.female_radio)
    RadioButton female_radio;
    @BindView(R.id.curriculum_spinner)
    Spinner curriculum_spinner;
    @BindView(R.id.grade_spinner)
    Spinner grade_spinner;
    @BindView(R.id.country_spinner)
    Spinner country_spinner;
    @BindView(R.id.state_spinner)
    Spinner state_spinner;
    @BindView(R.id.city_spinner)
    Spinner city_spinner;
    @BindView(R.id.school_spinner)
    SearchableSpinner school_spinner;
    @BindView(R.id.select_school)
    TextView select_school;
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    private String curriculumId = "";
    private String grade = "";
    private String country = "";
    private String state = "";
    private String city = "";
    private ProgressDialog progressDialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ArrayList<String> schoolList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Helper.isGSECurriculum()) {
            setContentView(R.layout.activity_signup_gse);
        } else {
            setContentView(R.layout.activity_signup);
        }

        ButterKnife.bind(this);

        if (Helper.isGSECurriculum()) {

        }
        cd = new ConnectionDetector(SignupActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            getSchoolList();
        } else {
            Toast.makeText(this, "No Internet Connect", Toast.LENGTH_SHORT).show();

        }
        String countryJsonString;
        ArrayList<String> countryList = new ArrayList<>();

        if (Helper.isGSECurriculum()) {
            countryJsonString = Helper.getStringFromSettings(getApplicationContext(), Constants.INDONESIA_COUNTRIES);
            try {
                countryList = jsonStringToArray(countryJsonString, "indonesia_countries");
                countryList.add(0, "SELECT COUNTRY");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            countryJsonString = Helper.getStringFromSettings(getApplicationContext(), Constants.COUNTRY);
            try {
                countryList = jsonStringToArray(countryJsonString, "country");
                countryList.add(0, "SELECT COUNTRY");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ArrayList<String> stateList = new ArrayList<>();
        stateList.add("SELECT STATE");

        ArrayList<String> cityList = new ArrayList<>();
        cityList.add("SELECT CITY");

        ArrayList<String> schoolList = new ArrayList<>();
        schoolList.add("SELECT SCHOOL");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        country_spinner.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> stateSpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stateList);
        stateSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        state_spinner.setAdapter(stateSpinnerArrayAdapter);

        ArrayAdapter<String> citySpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cityList);
        citySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        city_spinner.setAdapter(citySpinnerArrayAdapter);

        ArrayAdapter<String> schoolSpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, schoolList);
        schoolSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        school_spinner.setAdapter(schoolSpinnerArrayAdapter);

        main_layout.requestFocus();
        curriculum_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String state = country_spinner.getSelectedItem().toString();
                String[] stateNameArray = state.split(" ");
                String stateName;
                if (stateNameArray.length > 1) {
                    stateName = stateNameArray[0].toLowerCase() + "_" + stateNameArray[1].toLowerCase();

                } else {
                    stateName = stateNameArray[0].toLowerCase();
                }
                String stateJsonString = Helper.getStringFromSettings(getApplicationContext(), stateName);
                ArrayList<String> stateList = new ArrayList<>();
                try {
                    stateList = jsonStringToArray(stateJsonString, stateName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stateList.add(0, "SELECT STATE");
                setupState(stateList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = state_spinner.getSelectedItem().toString();
                String[] stateNameArray = state.split(" ");
                String stateName;
                if (stateNameArray.length > 1) {
                    stateName = stateNameArray[0].toLowerCase() + "_" + stateNameArray[1].toLowerCase();

                } else {
                    stateName = stateNameArray[0].toLowerCase();
                }
                String stateJsonString = Helper.getStringFromSettings(getApplicationContext(), stateName);
                ArrayList<String> cityList = new ArrayList<>();
                try {
                    cityList = jsonStringToArray(stateJsonString, stateName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cityList.add(0, "SELECT CITY");
                setupCity(cityList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        school_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = state_spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().isEmpty()) {
                    password.setError("Please enter valid password");

                    //Toast.makeText(SignupActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                }
                if (s.length() < 6) {
                    password.setError("Password should be at least 6 letters");

                   // Toast.makeText(SignupActivity.this,, "password should be more than 6 letters ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*et_input.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                _textInput.onNext(et_input.text.toString())
            }

            override fun beforeTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        this.binding.searchEditTxt.textChanges().debounce(300)
                .onEach {
            filter(it.toString())
        }
            .launchIn(lifecycleScope)
        this.binding.searchEditTxt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.binding.searchEditTxt.clearFocus()
                val `in` = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                `in`.hideSoftInputFromWindow(this.binding.searchEditTxt.windowToken, 0)
                filter(this.binding.searchEditTxt.text.toString())
                return@OnEditorActionListener true
            }
            false
        })*/
    }

    @OnClick(R.id.login)
    void onClickSignup() {
        String usernameString = username.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String firstNameString = first_name.getText().toString().trim();
        String phoneNumberString = phone_number.getText().toString().trim();
        String emailID = email_id.getText().toString().trim();
        String genderId = "";
        String curriculum = curriculum_spinner.getSelectedItem().toString().trim();
        String grade = grade_spinner.getSelectedItem().toString().trim();
        String country = country_spinner.getSelectedItem().toString().trim();
        String state = state_spinner.getSelectedItem().toString().trim();
        String city = city_spinner.getSelectedItem().toString().trim();
        String school = school_spinner.getSelectedItem().toString().trim();

        if (male_radio.isChecked()) {
            genderId = "1";
        }
        if (female_radio.isChecked()) {
            genderId = "2";
        }
        if (usernameString == null || usernameString.isEmpty() || usernameString.length() < 3) {
            Toast.makeText(this, "Please enter valid username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordString == null || passwordString.isEmpty()) {
            Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordString.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 letters ", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!Helper.isGSECurriculum() && school.equalsIgnoreCase("select school")) {
            Toast.makeText(this, "Please select the school", Toast.LENGTH_SHORT).show();
            return;
        }

        if (curriculum.equalsIgnoreCase("select curriculum")) {
            Toast.makeText(this, "Please select the Curriculum", Toast.LENGTH_SHORT).show();
            return;
        }
        if (grade.equalsIgnoreCase("select grade")) {
            Toast.makeText(this, "Please select the Grade", Toast.LENGTH_SHORT).show();
            return;
        }
        if (country.equalsIgnoreCase("select country")) {
            Toast.makeText(this, "Please select the Country", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Helper.isGSECurriculum() && state.equalsIgnoreCase("select state")) {
            Toast.makeText(this, "Please select the State", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Helper.isGSECurriculum() && city.equalsIgnoreCase("select city")) {
            Toast.makeText(this, "Please select the City", Toast.LENGTH_SHORT).show();
            return;
        }
        if (firstNameString == null || firstNameString.isEmpty() || firstNameString.length() < 3) {
            Toast.makeText(this, "Please enter valid first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumberString == null || phoneNumberString.isEmpty() || phoneNumberString.length() < 8) {
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emailID != null && !emailID.isEmpty()) {
            boolean is_valid = Helper.validateEmail(emailID);
            if (!is_valid) {
                Toast.makeText(this, "Please enter valid email ID", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (curriculum.toLowerCase().equals("cambodia")) {
            curriculumId = Constants.CURRICULUM_CAMBODIA;
        }
        if (curriculum.toLowerCase().equals("gse")) {
            curriculumId = Constants.CURRICULUM_GSE;
        }

        cd = new ConnectionDetector(SignupActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hideSoftKeyboard();
            }
        } else {
            cd.noInternetPopup();
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String time = df.format(new Date());
        ApiInterface apiInterface;
        Call<ResponseBody> call = null;
        Students students = new Students(
                firstNameString, phoneNumberString, genderId,
                city, country, state, time,
                usernameString, passwordString);
        List<Students> studentsList = new ArrayList<>();
        studentsList.add(students);
        SignupRequest signupRequest = new SignupRequest(
                curriculumId, school, grade, studentsList);
        if (!Helper.isRPIConnected()) {
            apiInterface = ApiClient.getLmsClient().create(ApiInterface.class);
            call = apiInterface.signup("Bearer " + Helper.getStudentSignupAccessToken(), true, signupRequest);
        }
        if (call != null)
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.code() == 401) {
                        String errorMessage;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorMessage = jObjError.getString("errormessage");
                        } catch (Exception err) {
                            errorMessage = response.message();
                        }
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.code() == 400) {
                        String errorMessage;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            errorMessage = jObjError.getString("errormessage");
                        } catch (Exception err) {
                            errorMessage = response.message();
                        }
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (response.code() == 500) {
                        Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.code() == 404) {
                        Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_SHORT).show();
                        return;
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
                        Toast.makeText(getApplicationContext(), finalErrorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Something went wrong. Please try Again", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @OnClick(R.id.select_school)
    void selectSchool() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) { // Please, use a final int instead of hardcoded int value
            if (resultCode == RESULT_OK) {
                String value = (String) data.getExtras().getString("school");
                select_school.setText(value);
            }
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(email_id.getWindowToken(), 0);
    }


    private ArrayList<String> jsonStringToArray(String jsonString, String name) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        JSONObject jObjError = new JSONObject(jsonString);
        JSONArray jsonArray = jObjError.getJSONArray(name);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

    private void setupState(List<String> stateList) {
        ArrayAdapter<String> stateSpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stateList);
        stateSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        state_spinner.setAdapter(stateSpinnerArrayAdapter);
    }

    private void setupCity(List<String> cityList) {
        ArrayAdapter<String> citySpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cityList);
        citySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);

        city_spinner.setAdapter(citySpinnerArrayAdapter);
    }

    private void setupSchool(List<String> schoolList) {
        ArrayAdapter<String> schoolSpinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, schoolList);
        schoolSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        school_spinner.setAdapter(schoolSpinnerArrayAdapter);
        school_spinner.setPositiveButton("Add School", (dialog, which) -> {
            String searchQuery = school_spinner.getSearchText();
            if (!searchQuery.isEmpty())
                if (schoolList.contains(searchQuery)) {
                    Toast.makeText(this, "School Name is already existing. Please select school", Toast.LENGTH_SHORT).show();
                } else {
                    schoolList.add(searchQuery);
                    school_spinner.setSelection(schoolList.size() - 1);
                    dialog.dismiss();
                }
            else {
                Toast.makeText(this, "Please enter the school name", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void getSchoolList() {
        progressDialog = new ProgressDialog(SignupActivity.this);
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

                    schoolList.add("SELECT SCHOOL");
                    for (School school : response.body().getSchoolList()) {
                        schoolList.add(school.getSchoolname());
                    }
                    setupSchool(schoolList);
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
                    Helper.showToast(SignupActivity.this, finalErrorMessage);
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