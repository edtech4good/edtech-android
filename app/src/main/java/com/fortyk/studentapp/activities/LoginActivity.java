package com.fortyk.studentapp.activities;

import static com.fortyk.studentapp.utils.Constants.decoded;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.curriculum.BaseLineCurriculumResponse;
import com.fortyk.studentapp.models.login.LoginResponse;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText userName;
    @BindView(R.id.password)
    EditText passWord;
    @BindView(R.id.login)
    Button loginButton;
    @BindView(R.id.settings)
    ImageView settingsButton;
    @BindView(R.id.tx_signup)
    TextView signup;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    private ProgressDialog progressDialog;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean isRPi = true;
    AppUpdateManager appUpdateManager;
    InstallStateUpdatedListener listener;
    private static final int APP_UPDATE_REQUEST_CODE = 102;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userName.setCursorVisible(false);

        userName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userName.setCursorVisible(true);
                return false;
            }
        });
        if (!Helper.isRPIConnected()) {
            signup.setVisibility(View.VISIBLE);
        } else {
            signup.setVisibility(View.GONE);
        }
        String signupString = "Don't have Account? Sign Up";
        Spannable wordtoSpan = new SpannableString(signupString);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#FF40C4FF")), 20, signupString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signup.setText(wordtoSpan);
        setupRemoteConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRpiHealth();
        checkAppUpdates();
        if (appUpdateManager != null && !Helper.isRPIConnected())
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(appUpdateInfo -> {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdate();
                        }
                    });
    }

    @OnClick({R.id.login, R.id.settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (userName.getText().toString().equals("")) {
                    userName.setError("Mandatory field");
                } else if (passWord.getText().toString().equals("")) {
                    passWord.setError("Mandatory field");
                } else {
                    cd = new ConnectionDetector(LoginActivity.this);
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hideSoftKeyboard();
                        }
                        doLogin(userName.getText().toString(), passWord.getText().toString());
                    } else {
                        cd.noInternetPopup();
                    }
                }
                break;
            case R.id.settings:
                openSettings();
                break;
        }
    }

    @OnClick(R.id.tx_signup)
    void Signup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    private void doLogin(String username, String password) {
        Map<String, String> params = new HashMap<>();
        ApiInterface apiInterface;
        Call<LoginResponse> call;
        //isRPi = false;
        if (isRPi) {
            params.put("studentusername", username);
            params.put("studentpassword", password);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            call = apiInterface.doLogin(params);
        } else {
            params.put("lmsusername", username);
            params.put("lmsuserpassword", password);
            apiInterface = ApiClient.getLmsClient().create(ApiInterface.class);
            call = apiInterface.lmsLogin(params);
        }
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
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
                    Toast.makeText(getApplicationContext(), "UserName/Password not Matching", Toast.LENGTH_SHORT).show();
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
                if (response.body().isError()) {
                    Toast.makeText(getApplicationContext(), response.body().getErrormessage(), Toast.LENGTH_SHORT).show();
                } else {
                    String student = decoded(response.body().getData().getAccessToken());
                    try {
                        JSONObject jsonObject = new JSONObject(student);
                        Helper.setStringInSettings(getApplicationContext(), Constants.ACCESS_TOKEN, response.body().getData().getAccessToken());
                        int roleid = jsonObject.getInt("schooluserrole");
                        if (roleid == 1 || roleid == 2 || roleid == 3) {
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("roleid", roleid);
                            intent.putExtra("isRPi", isRPi);
                            startActivity(intent);
                        } else if (roleid == 4) {
                            //   Intent intent = new Intent(LoginActivity.this, GradesActivity.class);
                            //   String curriculumid = jsonObject.getString("curriculumid");
                            //    intent.putExtra("curriculumid", curriculumid);
                            if (isRPi) {
                                Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_LOGIN, true);
                                String curriculumid = jsonObject.getString("curriculumid");
                                String studentid = jsonObject.getString("studentid");
                                Helper.setStringInSettings(getApplicationContext(), Constants.CURRICULUM_ID, curriculumid);
                                getBaseLineCurriculum(curriculumid, studentid);

                            } else {
                                studentRPiPopup();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();

            }
        });
    }

    private void getBaseLineCurriculum(String curriculumId, String studentId) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseLineCurriculumResponse> call = apiInterface.getBaseLineCurriculum(curriculumId, studentId, "Bearer " + Helper.getStringFromSettings(LoginActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<BaseLineCurriculumResponse>() {

            @Override
            public void onResponse(Call<BaseLineCurriculumResponse> call, retrofit2.Response<BaseLineCurriculumResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, GradesActivity.class);
                    Helper.setStringInSettings(getApplicationContext(), Constants.BASE_LINE_CURRICULUM_ID, response.body().getData().getBaseline());
                    Helper.setBooleanInSettings(getApplicationContext(), Constants.BASE_LINE_PASSED, response.body().getData().isBaselinepassed());
                    if (response.body().getData().getBaseline() == null) {
                        Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, false);
                        intent.putExtra("curriculumid", curriculumId);
                    } else if ((response.body().getData().getBaseline() != null && response.body().getData().getBaseline().equals("null"))
                            || response.body().getData().isBaselinepassed()) {
                        Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, false);
                        intent.putExtra("curriculumid", curriculumId);
                    } else {
                        Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, true);
                        intent.putExtra("curriculumid", response.body().getData().getBaseline());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                if (response.code() == 404) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, GradesActivity.class);
                    Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, false);
                    intent.putExtra("curriculumid", curriculumId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
                    Helper.showToast(LoginActivity.this, finalErrorMessage);
                }
            }

            @Override
            public void onFailure(Call<BaseLineCurriculumResponse> call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                Helper.showToast(LoginActivity.this, "Please check your internet connection!");
            }
        });
    }


    private void checkRpiHealth() {
        try {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ApiInterface apiInterface;
            Call<ResponseBody> call;
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            call = apiInterface.checkrpihealth();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    isRPi = response.code() == 200;
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    isRPi = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null)
                progressDialog.dismiss();
        }


    }

    public void studentRPiPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setMessage(R.string.rpi_message).setTitle("Warning!").setCancelable(false).setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });

        alertDialogBuilder.setNegativeButton(" No ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(passWord.getWindowToken(), 0);
    }

    public void openSettings() {
        View resultLayout = getLayoutInflater().inflate(R.layout.settings_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        final AlertDialog popupDialog = builder.create();
        popupDialog.setView(resultLayout);
        popupDialog.setCancelable(false);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        popupDialog.show();
        Button rpiButton = resultLayout.findViewById(R.id.rpi_button);
        Button lmsButton = resultLayout.findViewById(R.id.lms_button);

        rpiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRPi = true;
                popupDialog.dismiss();
            }
        });
        lmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRPi = false;
                popupDialog.dismiss();
            }
        });
    }

    public void switchnet() {
        View resultLayout = getLayoutInflater().inflate(R.layout.switch_net_popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this
        );

        final AlertDialog popupDialog = builder.create();
        popupDialog.setView(resultLayout);
        popupDialog.setCancelable(false);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        popupDialog.show();
        TextView title = resultLayout.findViewById(R.id.title);
        Button okButton = resultLayout.findViewById(R.id.ok_btn);

        if (isRPi) {
            title.setText("Your Device is not connected to RPI. Please check your connection!");
        } else {
            title.setText("Your Device is not connected to 3G/4G. Please check your connection!");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });
    }

    private void checkAppUpdates() {

        appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(

                            appUpdateInfo,
                            this,
                            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                                    .setAllowAssetPackDeletion(true)
                                    .build(),
                            APP_UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create a listener to track request state updates.
        listener = state -> {
            // (Optional) Provide a download progress bar.
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
                long bytesDownloaded = state.bytesDownloaded();
                long totalBytesToDownload = state.totalBytesToDownload();
                // Implement progress bar.
            }
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdate();
            }
            // Log state or install the update.
        };
        appUpdateManager.registerListener(listener);

    }

    // Displays the snackbar notification and call to action.
    private void popupSnackbarForCompleteUpdate() {
        if (appUpdateManager != null && listener != null) {
            appUpdateManager.unregisterListener(listener);
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(R.id.main_layout),
                            "An update has just been downloaded.",
                            Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RESTART", view ->
                    appUpdateManager.completeUpdate());
            snackbar.setActionTextColor(
                    getResources().getColor(R.color.white));
            snackbar.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Update flow failed!", Toast.LENGTH_SHORT).show();
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "App is successfully Update ", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void setupRemoteConfig() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    task.isSuccessful();
                    displayWelcomeMessage();
                });

    }

    private void displayWelcomeMessage() {
        Helper.setStringInSettings(getApplicationContext(), Constants.CITY, mFirebaseRemoteConfig.getString("city"));
        Helper.setStringInSettings(getApplicationContext(), Constants.COUNTRY, mFirebaseRemoteConfig.getString("country"));
        Helper.setStringInSettings(getApplicationContext(), Constants.INDONESIA_COUNTRIES, mFirebaseRemoteConfig.getString("indonesia_countries"));
        Helper.setStringInSettings(getApplicationContext(), Constants.CAMBODIA, mFirebaseRemoteConfig.getString("cambodia"));
        Helper.setStringInSettings(getApplicationContext(), Constants.BANTEAY_MEANCHEY, mFirebaseRemoteConfig.getString("banteay_meanchey"));
        Helper.setStringInSettings(getApplicationContext(), Constants.BATTAMBANG, mFirebaseRemoteConfig.getString("battambang"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KAMPONG_CHAM, mFirebaseRemoteConfig.getString("kampong_cham"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KAMPONG_CHHNANG, mFirebaseRemoteConfig.getString("kampong_chhnang"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KAMPONG_SPEU, mFirebaseRemoteConfig.getString("kampong_speu"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KAMPONG_THOM, mFirebaseRemoteConfig.getString("kampong_thom"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KAMPOT, mFirebaseRemoteConfig.getString("kampot"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KANDAL, mFirebaseRemoteConfig.getString("kandal"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KOH_KONG, mFirebaseRemoteConfig.getString("koh_kong"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KRATIE, mFirebaseRemoteConfig.getString("kratie"));
        Helper.setStringInSettings(getApplicationContext(), Constants.MONDULKIRI, mFirebaseRemoteConfig.getString("mondulkiri"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PHNOM_PENH, mFirebaseRemoteConfig.getString("phnom_penh"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PREAH_VIHEAR, mFirebaseRemoteConfig.getString("preah_vihear"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PREY_VENG, mFirebaseRemoteConfig.getString("prey_veng"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PURSAT, mFirebaseRemoteConfig.getString("pursat"));
        Helper.setStringInSettings(getApplicationContext(), Constants.RATANAKIRI, mFirebaseRemoteConfig.getString("ratanakiri"));
        Helper.setStringInSettings(getApplicationContext(), Constants.SIEM_REAP, mFirebaseRemoteConfig.getString("siem_reap"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PREAH_SIHANOUK, mFirebaseRemoteConfig.getString("preah_sihanouk"));
        Helper.setStringInSettings(getApplicationContext(), Constants.STUNG_TRENG, mFirebaseRemoteConfig.getString("stung_treng"));
        Helper.setStringInSettings(getApplicationContext(), Constants.SVAY_RIENG, mFirebaseRemoteConfig.getString("svay_rieng"));
        Helper.setStringInSettings(getApplicationContext(), Constants.TAKEO, mFirebaseRemoteConfig.getString("takeo"));
        Helper.setStringInSettings(getApplicationContext(), Constants.ODDAR_MEANCHEY, mFirebaseRemoteConfig.getString("oddar_meanchey"));
        Helper.setStringInSettings(getApplicationContext(), Constants.KEP, mFirebaseRemoteConfig.getString("kep"));
        Helper.setStringInSettings(getApplicationContext(), Constants.PAILIN, mFirebaseRemoteConfig.getString("pailin"));
        Helper.setStringInSettings(getApplicationContext(), Constants.TBOUNG_KHMUM, mFirebaseRemoteConfig.getString("tboung_khmum"));


    }
}
