package com.fortyk.studentapp.api;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitJson = null;

    // Development local environment
    // public static String baseUrlString = "http://192.168.1.101/";
    // public static String uploadbaseUrl = "https://dev.lms.40kplusdashboard.com/";

    // Development online environment
    // public static String baseUrlString = "https://dev.rpi.api.40kplusdashboard.com/";
    // public static String uploadbaseUrl = "https://dev.lms.40kplusdashboard.com/";

    // Production local environment
    // public static String baseUrlString = "http://192.168.1.101/";
    // public static String uploadbaseUrl = "https://lms.api.40kplusdashboard.com/";

    // Production online environment
    // public static String baseUrlString = "https://rpi.api.40kplusdashboard.com/";
    // public static String uploadbaseUrl = "https://lms.api.40kplusdashboard.com/";

    //*******IMPORTANT*CONFIGRATION*DONE*FOR*BUILD*PIPELINE********
    //*************************************************************
    //*************************************************************
    //          Replace below line with Current environment

    public static String baseUrlString = "https://your-api-server.com/";
    public static String uploadbaseUrl = "https://your-lms-server.com/";
    public static String fileserverbaseUrl = "https://your-file-server.com/";

    private static Retrofit retrofitSync = null;
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrlString)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getLmsClient() {
        if (retrofitJson == null) {
            retrofitJson = new Retrofit.Builder()
                    .baseUrl(uploadbaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitJson;
    }

//   git commit --amend --date="YYYY-MM-DD HH:MM:SS"
    public static Retrofit getClientWithTimeOutExtend() {
        if (retrofitSync == null) {
            retrofitSync = new Retrofit.Builder()
                    .baseUrl(baseUrlString)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofitSync;
    }

}
