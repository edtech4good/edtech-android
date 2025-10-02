package com.fortyk.studentapp.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Base64;

import com.fortyk.studentapp.api.ApiClient;

import java.io.UnsupportedEncodingException;

public class Constants {

    public static double screenSizeInches = 0.0;
    public static final String fromBrickFilePath = "from_brick_file_path";
    public static String preferences = "preferences";
    public static String storagePreference = "storagePreference";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String IS_LOGIN = "is_login";
    public static final String CURRICULUM_ID = "curriculum_id";
    public static final String FILE_STORING_LOACTION = "FILE_STORING_LOACTION";
    public static final String FILE_STORING_LOACTION_PATH = "FILE_STORING_LOACTION_PATH";
    public static final int SD_CARD = 1;
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String INDONESIA_COUNTRIES = "indonesia_countries";
    public static final String STATE = "state";
    public static final String CAMBODIA = "cambodia";
    public static final String BANTEAY_MEANCHEY = "banteay_meanchey";
    public static final String BATTAMBANG = "battambang";
    public static final String KAMPONG_CHAM = "kampong_cham";
    public static final String KAMPONG_CHHNANG = "kampong_chhnang";
    public static final String KAMPONG_SPEU = "kampong_speu";
    public static final String KAMPONG_THOM = "kampong_thom";
    public static final String KAMPOT = "kampot";
    public static final String KANDAL = "kandal";
    public static final String KOH_KONG = "koh_kong";
    public static final String KRATIE = "kratie";
    public static final String MONDULKIRI = "mondulkiri";
    public static final String PHNOM_PENH = "phnom_penh";
    public static final String PREAH_VIHEAR = "preah_vihear";
    public static final String PREY_VENG = "prey_veng";
    public static final String PURSAT = "pursat";
    public static final String RATANAKIRI = "ratanakiri";
    public static final String SIEM_REAP = "siem_reap";
    public static final String PREAH_SIHANOUK = "preah_sihanouk";
    public static final String STUNG_TRENG = "stung_treng";
    public static final String SVAY_RIENG = "svay_rieng";
    public static final String TAKEO = "takeo";
    public static final String ODDAR_MEANCHEY = "oddar_meanchey";
    public static final String KEP = "kep";
    public static final String PAILIN = "pailin";
    public static final String TBOUNG_KHMUM = "tboung_khmum";
    public static final String BASE_LINE_PASSED = "base_line_passed";
    public static final String BASE_LINE_CURRICULUM_ID = "base_line_curriculum_id";
    public static final String IS_BASE_LINE = "is_base_line";
    public static final int INTERNAL_MEMORY = 2;
    public static final String CURRICULUM_CAMBODIA = "37b866b4-55cf-4667-b5ba-8db0edf0202b";
    public static final String CURRICULUM_GSE = "71afa21a-f0e4-412c-83f0-87d1df7f185d";
    public static final String PRODUCTION_STUDENT_SIGNUP_ACCESS_TOKEN = "xKNp5aqvhGAVvX7C5pwC7waCj857VwbGkj6SsDxG57tBm3BXbAFG53KStKkFfUf6fkWDT4bKgLvjp7Varme65mNRsdWHfWMj9ckERtxw8jMBKgMJpAG8L9gBkxWqusLELc6xkUDXwhYRbdKdWujXnNEZaCw9BPmDScYKdEwxyVXtRTGjpRaDDSKBSxCERPyLj33zRsdZHzPJKg2yKPeCCxJhLtG4JFpJkjhhFA3eSEmf8pwt8PfLNEJwCZFJ6bMC";
    public static final String DEVELOPMENT_STUDENT_SIGNUP_ACCESS_TOKEN = "KqCTT4TpxxSAjjEEFxxqZbfZVgz38SpE4Lad99DWs5yuC5RG4mLzyr35dxjXuX8TEUQmLxSqfvnV5yzGgLneWQtpVp99Ubds5qXrm7VJQeFysTRAaKHMYM8jCLRbSydH8eZ2eVCckDLVEPzVm6HU25MNxvhmzjnBHExHvzAY4XnQYe3Ckq82TuChnDeNDq9NRB9UX6HPNhGyYgXepaSyFjshP5QPyW4ATZNMSU4N79mUnpRMSd3kFgtmpMUH3GJK";

    public static String getPath(Context context, String content_file) {
        String path = "";
        if (content_file.isEmpty() || content_file.equalsIgnoreCase("null") || content_file.equals(null)) {
        } else {
            if (!ApiClient.baseUrlString.equals("https://your-api-server.com/")) {
                path = ApiClient.fileserverbaseUrl + content_file;
            } else {
                path = Environment.getExternalStorageDirectory().getPath() + "/your-resource-path/" + content_file;
                if (Helper.getStorageLocation(context, FILE_STORING_LOACTION) == SD_CARD) {
                    path = Helper.getStoragePath(context, FILE_STORING_LOACTION_PATH) + "/" + content_file;
                }
            }
        }
        return path;
    }

    public static void stopMediaPlayer(MediaPlayer mediaPlayer) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (Exception ignored) {

        }

    }

    public static String decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
            return getJson(split[1]);
        } catch (Exception e) {
            //Error
        }
        return JWTEncoded;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
