package com.fortyk.studentapp.activities;


import static com.fortyk.studentapp.utils.Constants.FILE_STORING_LOACTION;
import static com.fortyk.studentapp.utils.Constants.FILE_STORING_LOACTION_PATH;
import static com.fortyk.studentapp.utils.Constants.SD_CARD;
import static com.fortyk.studentapp.utils.Constants.getPath;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.utils.ConnectionDetector;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TeacherMainActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.dashboard)
    Button dashboardButton;
    @BindView(R.id.download)
    Button downloadButton;
    @BindView(R.id.upload)
    Button uploadButton;
    @BindView(R.id.logout_button)
    ImageView logout;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int roleid = extras.getInt("roleid");
            boolean isRPi = extras.getBoolean("isRPi");
            if (roleid == 1) {
                title.setText("Welcome Superadmin!");
            } else if (roleid == 2) {
                title.setText("Welcome Admin!");
            } else if (roleid == 3) {
                title.setText("Welcome Teacher!");
            }
            if (isRPi) {
                downloadButton.setVisibility(View.VISIBLE);
                uploadButton.setVisibility(View.GONE);
            } else {
                downloadButton.setVisibility(View.GONE);
                uploadButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.dashboard, R.id.download, R.id.upload, R.id.logout_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dashboard:
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    Intent intent = new Intent(TeacherMainActivity.this, TeacherProfileActivity.class);
                    startActivity(intent);
                } else {
                    cd.noInternetPopup();
                }
                break;
            case R.id.download:
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    downloadFileFromRPI();
                } else {
                    cd.noInternetPopup();
                }
                break;
            case R.id.upload:
                cd = new ConnectionDetector(this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    uploadFile();
                } else {
                    cd.noInternetPopup();
                }
                break;
            case R.id.logout_button:
                Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    private void downloadFileFromRPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.downloadFileFromRPI("Bearer " + Helper.getStringFromSettings(TeacherMainActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            if (Helper.isRPIConnected())
                                uploadSyncFileToRpi();
                            else {
                                Toast.makeText(TeacherMainActivity.this, "Download successful!", Toast.LENGTH_SHORT).show();
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    }.execute();
                }

                if (!response.isSuccessful()) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }

                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TeacherMainActivity.this, finalErrorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Helper.showToast(TeacherMainActivity.this, "Something went wrong, Please try again ");
            }
        });
    }

    private void downloadFileFromServer() {
        ApiInterface apiInterface = ApiClient.getLmsClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.downloadFileFromServer("Bearer " + Helper.getStringFromSettings(TeacherMainActivity.this, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeServerSyncResponseBodyToDisk(response.body());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Helper.showToast(TeacherMainActivity.this, "Upload successful!");
                        }
                    }.execute();
                }
                if (!response.isSuccessful()) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }
                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TeacherMainActivity.this, finalErrorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Helper.showToast(TeacherMainActivity.this, "Something went wrong, Please try again ");
            }
        });
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File studentlogfile;
            if (Helper.getStorageLocation(TeacherMainActivity.this, FILE_STORING_LOACTION) == SD_CARD) {
                // String dir = Environment.getExternalStorageDirectory()+File.separator+"/40kfiles/studentlog.zip";
                String dir = Helper.getStoragePath(TeacherMainActivity.this, FILE_STORING_LOACTION_PATH) + "/";
                studentlogfile = new File(dir, "studentlog.zip");
            } else {
                String dir = Environment.getExternalStorageDirectory().getPath() + "/40kfiles";
                studentlogfile = new File(dir, "studentlog.zip");
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(studentlogfile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("anupam", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private boolean writeServerSyncResponseBodyToDisk(ResponseBody body) {
        try {
            File studentlogfile;
            if (Helper.getStorageLocation(TeacherMainActivity.this, FILE_STORING_LOACTION) == SD_CARD) {
                // String dir = Environment.getExternalStorageDirectory()+File.separator+"/40kfiles/studentlog.zip";
                String dir = Helper.getStoragePath(TeacherMainActivity.this, FILE_STORING_LOACTION_PATH) + "/";
                studentlogfile = new File(dir, "syncData.zip");
            } else {
                String dir = Environment.getExternalStorageDirectory().getPath() + "/40kfiles";
                studentlogfile = new File(dir, "syncData.zip");
            }
            if (!studentlogfile.exists()) {
                studentlogfile.createNewFile();
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(studentlogfile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("anupam", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return true;
            } catch (IOException e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        } catch (IOException e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return false;
        }
    }

    private void uploadFile() {
        String path = getPath(TeacherMainActivity.this, "studentlog.zip");
        if (path == null) {
            Helper.showToast(TeacherMainActivity.this, "Please download the file from RPi network and try uploading again!");
            return;
        }

        File file = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length()));
        if (file_size < 1) {
            Helper.showToast(TeacherMainActivity.this, "All files are already updated!");
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("importfile", "studentlog", requestBody);
        ApiInterface apiInterface = ApiClient.getLmsClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.uploadFile("Bearer " + Helper.getStringFromSettings(TeacherMainActivity.this, Constants.ACCESS_TOKEN), fileToUpload);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (Helper.isRPIConnected())
                        downloadFileFromServer();
                    else {
                        Helper.showToast(TeacherMainActivity.this, "Upload successful!");
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                    /*Helper.showToast(TeacherMainActivity.this, "Upload successful!");
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }*/
                }

                if (!response.isSuccessful()) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    String errorMessage;
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        errorMessage = jObjError.getString("message");
                    } catch (Exception err) {
                        errorMessage = response.message();
                    }

                    String finalErrorMessage = errorMessage;
                    Helper.showToast(TeacherMainActivity.this, finalErrorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Helper.showToast(TeacherMainActivity.this, "Something went wrong, Please try again ");
            }
        });
    }

    private void uploadSyncFileToRpi() {
        String path = getPath(TeacherMainActivity.this, "syncData.zip");
        if (path == null) {
            // Helper.showToast(TeacherMainActivity.this, "Please download the file from RPi network and try uploading again!");
            return;
        }
        File file = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length()));
        if (file_size < 1) {
            //Helper.showToast(TeacherMainActivity.this, "All files are already updated!");
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(TeacherMainActivity.this, "Download successful!", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("importfile", file.getName(), requestBody);
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart(
                "importfile", file.getName(), requestBody
        );
        ApiInterface apiInterface = ApiClient.getClientWithTimeOutExtend().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.uploadSyncFileToRPI("Bearer " + Helper.getStringFromSettings(TeacherMainActivity.this, Constants.ACCESS_TOKEN), bodyBuilder.build());
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Toast.makeText(TeacherMainActivity.this, "Download successful!", Toast.LENGTH_SHORT).show();

                if (progressDialog != null) {
                    progressDialog.dismiss();
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
                    Helper.showToast(TeacherMainActivity.this, finalErrorMessage);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(TeacherMainActivity.this, "Download successful!", Toast.LENGTH_SHORT).show();

                //Helper.showToast(TeacherMainActivity.this, "Something went wrong, Please try again ");
            }
        });
    }
}