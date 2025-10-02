package com.fortyk.studentapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.LevelsActivity;
import com.fortyk.studentapp.models.curriculum.Grades;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {

    private List<Grades> gradesList;
    private Context context;

    public GradesAdapter(Context context, List<Grades> grades) {
        this.gradesList = grades;
        this.context = context;
    }

    @NonNull
    @Override
    public GradesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_grade, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GradesAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        String gradeid = gradesList.get(position).getGradeid();
        String title = gradesList.get(position).getGradename();
        viewHolder.gradeTitle.setText(title);
        viewHolder.gradeImage.setImageResource(R.drawable.brick_unlock);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LevelsActivity.class);
                intent.putExtra("grade_data", gradesList.get(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return gradesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView gradeImage;
        public TextView gradeTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            gradeImage = (ImageView) itemLayoutView.findViewById(R.id.grade_image);
            gradeTitle = (TextView) itemLayoutView.findViewById(R.id.grade_title);
        }
    }
}