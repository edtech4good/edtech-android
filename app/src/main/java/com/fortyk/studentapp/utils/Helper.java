package com.fortyk.studentapp.utils;

import static com.fortyk.studentapp.utils.Constants.DEVELOPMENT_STUDENT_SIGNUP_ACCESS_TOKEN;
import static com.fortyk.studentapp.utils.Constants.PRODUCTION_STUDENT_SIGNUP_ACCESS_TOKEN;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.api.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkConnection(Context context) {
        ConnectionDetector cd = new ConnectionDetector(context);
        return cd.isConnectingToInternet();
    }

    public static String getStudentSignupAccessToken() {
        if (ApiClient.baseUrlString.equals("https://your-dev-api-server.com/")) {
            return DEVELOPMENT_STUDENT_SIGNUP_ACCESS_TOKEN;
        } else {
            return PRODUCTION_STUDENT_SIGNUP_ACCESS_TOKEN;
        }
    }


    public static void setStringInSettings(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settingsWriter.putString(key, value);
        settingsWriter.commit();
    }

    public static String getStringFromSettings(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        return settings.getString(key, "");
    }

    public static void setIntInSettings(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settingsWriter.putInt(key, value);
        settingsWriter.commit();
    }

    public static void setStorageLocation(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.storagePreference, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settingsWriter.putInt(key, value);
        settingsWriter.commit();
    }


    public static int getStorageLocation(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.storagePreference, 0);
        return settings.getInt(key, 0);
    }

    public static void setStoragePath(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.storagePreference, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settingsWriter.putString(key, value);
        settingsWriter.commit();
    }

    public static String getStoragePath(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.storagePreference, 0);
        return settings.getString(key, "");
    }

    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^(.+)@(.+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void setBooleanInSettings(Context context, String key, Boolean value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settingsWriter.putBoolean(key, value);
        settingsWriter.commit();
    }

    public static int getIntFromSettings(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        return settings.getInt(key, 0);
    }

    public static Boolean getBooleanFromSettings(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        return settings.getBoolean(key, false);
    }


    public static void clearToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.preferences, 0);
        SharedPreferences.Editor settingsWriter = settings.edit();
        settings.edit().clear().apply();
        settingsWriter.commit();
    }

    public static void showSnackbar(Context context, View view, String message, boolean action) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView sbTextView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        sbTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (action) {
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        } else {
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        snackbar.show();
    }

    public static void showTopSnackBar(Activity activity, View parentLayout, String message, boolean error) {
        Snackbar snack = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView sbTextView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        sbTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        final Snackbar.SnackbarLayout snackBarLayout1 = (Snackbar.SnackbarLayout) snack.getView();
        for (int i = 0; i < snackBarLayout1.getChildCount(); i++) {
            View parent = snackBarLayout1.getChildAt(i);
            if (parent instanceof LinearLayout) {
                parent.setRotation(180);
                break;
            }
        }

        if (error) {
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
        }
        snack.show();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int calculateNumberOfColumns(Context context, int base) {
        int columns = base;
        String screenSize = getScreenSizeCategory(context);

        if (screenSize.equals("small")) {
            if (base != 1) {
                columns = columns - 1;
            }
        } else if (screenSize.equals("normal")) {
            columns += 2;
        } else if (screenSize.equals("large")) {
            columns += 2;
        } else if (screenSize.equals("xlarge")) {
            columns += 3;
        }

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = (int) (columns * 1.5);
        }

        return columns;
    }

    protected static String getScreenSizeCategory(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                // small screens are at least 426dp x 320dp
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                // normal screens are at least 470dp x 320dp
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                // large screens are at least 640dp x 480dp
                return "large";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                // xlarge screens are at least 960dp x 720dp
                return "xlarge";
            default:
                return "undefined";
        }
    }

    public static boolean isRPIConnected() {
        return ApiClient.baseUrlString.equals("https://your-api-server.com/");
    }

    public static boolean isGSECurriculum() {
        return false;
    }


    /**
     * Checks if the device is a tablet or a phone
     *
     * @param activityContext The Activity Context.
     * @return Returns true if the device is a Tablet
     */
    public static boolean isTabletDevice(Context activityContext) {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
}
