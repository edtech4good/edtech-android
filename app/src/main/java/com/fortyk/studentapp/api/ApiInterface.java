package com.fortyk.studentapp.api;

import com.fortyk.studentapp.models.Result.LessonPractice;
import com.fortyk.studentapp.models.Result.LessonQuiz;
import com.fortyk.studentapp.models.Result.LevelQuiz;
import com.fortyk.studentapp.models.SchoolsResponse;
import com.fortyk.studentapp.models.curriculum.BaseLineCurriculumResponse;
import com.fortyk.studentapp.models.curriculum.CurriculumResponse;
import com.fortyk.studentapp.models.lessons.LessonResponse;
import com.fortyk.studentapp.models.lessons.LevelQuizResponse;
import com.fortyk.studentapp.models.login.LoginResponse;
import com.fortyk.studentapp.models.signup.SignupRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> doLogin(
            @FieldMap Map<String, String> params
    );

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("auth/school/login")
    Call<LoginResponse> lmsLogin(
            @FieldMap Map<String, String> params
    );


    @Headers("Accept: application/json")
    @GET("curriculum/{curriculumid}/{studentid}/baseline")
    Call<BaseLineCurriculumResponse> getBaseLineCurriculum(
            @Path("curriculumid") String curriculumid,
            @Path("studentid") String studentid,
            @Header("Authorization") String access_token

    );

    @Headers("Accept: application/json")
    @GET("curriculum/{curriculumid}/map")
    Call<CurriculumResponse> getCurriculum(
            @Path("curriculumid") String curriculumid,
            @Header("Authorization") String access_token

    );

    @Headers("Accept: application/json")
    @GET("question/lesson/{lessonid}")
    Call<LessonResponse> getLessonChapters(
            @Path("lessonid") String lessonid,
            @Header("Authorization") String access_token

    );

    @Headers("Accept: application/json")
    @GET("question/level/{levelid}")
    Call<LevelQuizResponse> getLevelQuizes(
            @Path("levelid") String levelid,
            @Header("Authorization") String access_token
    );

    @Headers("Accept: application/json")
    @POST("result/lesson/practice/{lessonpracticeid}")
    Call<ResponseBody> getLessonPracticeResult(
            @Path("lessonpracticeid") String lessonpracticeid,
            @Body List<LessonPractice> lessonPractices,
            @Header("Authorization") String access_token

    );

    @Headers("Accept: application/json")
    @POST("result/lesson/quiz/{lessonquizid}")
    Call<ResponseBody> getLessonQuizResult(
            @Path("lessonquizid") String lessonquizid,
            @Body List<LessonQuiz> lessonQuizs,
            @Header("Authorization") String access_token
    );

    @Headers("Accept: application/json")
    @POST("result/level/quiz/{levelid}")
    Call<ResponseBody> getLevelQuizResult(
            @Path("levelid") String levelid,
            @Body List<LevelQuiz> levelQuizs,
            @Header("Authorization") String access_token
    );

    @Headers("Accept: application/json")
    @GET("export/log")
    Call<ResponseBody> downloadFileFromRPI(
            @Header("Authorization") String access_token
    );

    @Headers("Accept: application/json")
    @GET("sync")
    Call<ResponseBody> downloadFileFromServer(
            @Header("Authorization") String access_token
    );

    @Headers("Accept: application/json")
    @Multipart
    @PUT("log/import")
    Call<ResponseBody> uploadFile(
            @Header("Authorization") String access_token,
            @Part MultipartBody.Part file
    );

    @Headers("Accept: application/json")
    @PUT("import/master")
    Call<ResponseBody> uploadSyncFileToRPI(
            @Header("Authorization") String access_token,
            @Body RequestBody requestBody
    );

    @Headers("Accept: text/html")
    @GET("version")
    Call<ResponseBody> checkrpihealth();

    @Headers("Accept: application/json")
    @GET("school")
    Call<SchoolsResponse> getSchoolList();


    @Headers("Accept: application/json")
    @POST("student/create")
    Call<ResponseBody> signup(
            @Header("Authorization") String access_token,
            @Query("cloud") boolean cloud,
            @Body SignupRequest signupRequest
    );


}
