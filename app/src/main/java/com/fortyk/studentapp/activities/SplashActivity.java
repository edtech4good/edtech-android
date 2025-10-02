package com.fortyk.studentapp.activities;

import static com.fortyk.studentapp.utils.Constants.FILE_STORING_LOACTION;
import static com.fortyk.studentapp.utils.Constants.FILE_STORING_LOACTION_PATH;
import static com.fortyk.studentapp.utils.Constants.INTERNAL_MEMORY;
import static com.fortyk.studentapp.utils.Constants.SD_CARD;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private static final int delay = 2000;
    final private int REQUEST_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e("yeshwanth", Helper.isTablet(this) + "");

        if (ApiClient.baseUrlString.equals("https://your-api-server.com/")
                && Helper.getStorageLocation(SplashActivity.this, FILE_STORING_LOACTION) == 0) {
            checkPermission();
        } else {
            openApp();
        }

        FirebaseCrashlytics.getInstance().log("test");
    }

    private void openApp() {
        new Handler().postDelayed(() -> {
            if (!Helper.isRPIConnected()) {
                boolean isLogin = Helper.getBooleanFromSettings(getApplicationContext(), Constants.IS_LOGIN);
                String curriculumid;
                if ((Helper.getStringFromSettings(getApplicationContext(), Constants.BASE_LINE_CURRICULUM_ID) == null
                        || Helper.getStringFromSettings(getApplicationContext(), Constants.BASE_LINE_CURRICULUM_ID).equals("null")) ||
                        Helper.getBooleanFromSettings(getApplicationContext(), Constants.BASE_LINE_PASSED)) {
                    curriculumid = Helper.getStringFromSettings(getApplicationContext(), Constants.CURRICULUM_ID);
                    Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, false);
                } else {
                    curriculumid = Helper.getStringFromSettings(getApplicationContext(), Constants.BASE_LINE_CURRICULUM_ID);
                    Helper.setBooleanInSettings(getApplicationContext(), Constants.IS_BASE_LINE, true);
                }
                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, GradesActivity.class)
                            .putExtra("curriculumid", curriculumid));
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            finish();
        }, delay);
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            final List<String> permissionsList = new ArrayList<>();
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            requestAppPermissions(permissionsList, REQUEST_PERMISSIONS);
        } else
            onPermissionsGranted();
    }

    public void requestAppPermissions(final List<String> permissionsList, final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissionsList) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                showMessageOKCancel("You have to grant permission...",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashActivity.this, permissionsList.toArray(new String[permissionsList.size()]), requestCode);
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), requestCode);
            }
        } else {
            onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsGranted();
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(SplashActivity.this)
                .setTitle("Runtime Permission")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onPermissionsGranted() {
        Toast.makeText(this, "Permissions Received", Toast.LENGTH_LONG).show();
        if (Helper.isRPIConnected())
            handleStorageLocation();
    }

    private void handleStorageLocation() {

        boolean sdCardAvailable = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = getExternalFilesDirs(null);
            String internalRoot = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();

            for (File file : externalDirs) {
                if (file == null) //solved NPE on some Lollipop devices
                    continue;
                String path = file.getPath().split("/Android")[0];

                if (!path.toLowerCase().startsWith(internalRoot)) {
                    sdCardAvailable = true;
                }

            }
        }

        if (sdCardAvailable && Helper.getStorageLocation(SplashActivity.this, FILE_STORING_LOACTION) == 0) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogThemeNormal);
            alertDialog.setCancelable(false);

            alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Please Choose memory location")
                    .setMessage("Please Choose memory location where you want to store files")
                    .setPositiveButton("Internal Storage", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(Environment.getExternalStorageDirectory(), "40kfiles");
                            if (!file.exists()) {
                                if (!file.mkdirs()) {
                                    Log.e("TravellerLog :: ", "Problem creating Image folder");
                                }
                            }
                            Helper.setStorageLocation(SplashActivity.this, FILE_STORING_LOACTION, INTERNAL_MEMORY);
                            openApp();
                        }
                    })

                    .setNegativeButton("SD Card", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Helper.setStorageLocation(SplashActivity.this, FILE_STORING_LOACTION, SD_CARD);
                            getExternalStorageDirectories();
                            openApp();
                        }
                    })

                    .show();

        } else {
            File file1 = new File(Environment.getExternalStorageDirectory(), "40kfiles");
            if (!file1.exists()) {
                if (!file1.mkdirs()) {
                    Log.e("TravellerLog :: ", "Problem creating Image folder");
                }
            }
            openApp();
        }
    }

    public void getExternalStorageDirectories() {

        List<String> results = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = getExternalFilesDirs(null);
            String internalRoot = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();

            for (File file : externalDirs) {
                if (file == null) //solved NPE on some Lollipop devices
                    continue;
                String path = file.getPath().split("/Android")[0];

                if (path.toLowerCase().startsWith(internalRoot))
                    continue;

                boolean addPath;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);

                } else {
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if (addPath) {
                    String publicDcimDirPath = file.getAbsolutePath() + "/40kfiles";
                    Helper.setStoragePath(SplashActivity.this, FILE_STORING_LOACTION_PATH, publicDcimDirPath);
                    try {
                        File newFile = new File(publicDcimDirPath);
                        if (!newFile.exists()) {
                            newFile.mkdirs();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (results.isEmpty()) { //Method 2 for all versions
            StringBuilder output = new StringBuilder();
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output.append(new String(buffer));
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if (!output.toString().trim().isEmpty()) {
                String devicePoints[] = output.toString().split("\n");
                for (String voldPoint : devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    results.remove(i--);
                }
            }
        }
    }
}
