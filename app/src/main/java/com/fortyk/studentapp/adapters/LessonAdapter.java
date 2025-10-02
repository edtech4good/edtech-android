package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.ChapterActivity;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.api.ApiClient;
import com.fortyk.studentapp.api.ApiInterface;
import com.fortyk.studentapp.models.curriculum.Lessons;
import com.fortyk.studentapp.models.lessons.LevelQuiz;
import com.fortyk.studentapp.models.lessons.LevelQuizResponse;
import com.fortyk.studentapp.utils.Constants;
import com.fortyk.studentapp.utils.Helper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    public List<Lessons> lessonList;
    Context context;
    String grade_title;
    String level_title;

    public LessonAdapter(Context context, List<Lessons> lessons, String grade_title, String level_title) {
        this.lessonList = lessons;
        this.context = context;
        this.grade_title = grade_title;
        this.level_title = level_title;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessonAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_lesson, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setEnabled(true);
        viewHolder.lessonImage.setImageResource(R.drawable.unlock);
        String lessonID = lessonList.get(position).getLessonid();
        String lessonName = lessonList.get(position).getLessonname();
        viewHolder.lessonTitle.setText(lessonList.get(position).getLessonheading());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonList.get(position).getLessonname().equals("Level Assessment")){
                   // getQuestions(lessonList.get(position).getLevelid());
                }else if(lessonList.get(position).getLessonname().equals("Q")){
                   // viewHolder.itemView.setEnabled(false);
                     getQuestions(lessonList.get(position).getLevelid());
                }else {
                    Intent intent = new Intent(context, ChapterActivity.class);
                    intent.putExtra("grade_title", grade_title);
                    intent.putExtra("level_title", level_title);
                    intent.putExtra("lesson_title", lessonName);
                    intent.putExtra("lesson_id", lessonID);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView lessonImage;
        public TextView lessonTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            lessonImage = (ImageView) itemLayoutView.findViewById(R.id.lesson_image);
            lessonTitle = (TextView) itemLayoutView.findViewById(R.id.lesson_title);
        }
    }

    private void getQuestions(String level_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LevelQuizResponse> call = apiInterface.getLevelQuizes(level_id, "Bearer " + Helper.getStringFromSettings(context, Constants.ACCESS_TOKEN));
        call.enqueue(new Callback<LevelQuizResponse>() {

            @Override
            public void onResponse(Call<LevelQuizResponse> call, retrofit2.Response<LevelQuizResponse> response) {
               /* if (progressDialog != null) {
                    progressDialog.dismiss();
                }*/

                if (response.isSuccessful()) {
                    Intent intent = new Intent(context, TemplateActivity.class);
                    intent.putExtra("type", 4);
                    intent.putExtra("levelquiz", response.body().getData());
                    intent.putExtra("grade_title", grade_title);
                    intent.putExtra("level_title", level_title);
                    intent.putExtra("lesson_title", "");
                    intent.putExtra("practice_title", "Level Quiz");
                    context.startActivity(intent);
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
                    Helper.showToast(context, finalErrorMessage);
                    return;
                }
            }

            @Override
            public void onFailure(Call<LevelQuizResponse> call, Throwable t) {
              /*  if (progressDialog != null)
                    progressDialog.dismiss();*/
                Helper.showToast(context, "Something went wrong, Please try again ");
            }
        });
    }
}