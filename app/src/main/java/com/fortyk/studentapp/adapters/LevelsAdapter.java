package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.LessonActivity;
import com.fortyk.studentapp.models.curriculum.Levels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.ViewHolder> {

    private List<Levels> levelsList;
    private Context context;
    private String grade_title;

    public LevelsAdapter(Context context, List<Levels> levels, String grade_title) {
        this.levelsList = levels;
        this.context = context;
        this.grade_title = grade_title;
    }

    @NonNull
    @Override
    public LevelsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LevelsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_level, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LevelsAdapter.ViewHolder viewHolder, final int position) {

        String levelid = levelsList.get(position).getLevelid();
        String title = levelsList.get(position).getLevelname();
        viewHolder.levelTitle.setText(title);
        viewHolder.levelImage.setImageResource(R.drawable.brick_unlock);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LessonActivity.class);
                intent.putExtra("level_data", levelsList.get(position));
                intent.putExtra("grade_title", grade_title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return levelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView levelImage;
        public TextView levelTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            levelImage = (ImageView) itemLayoutView.findViewById(R.id.level_image);
            levelTitle = (TextView) itemLayoutView.findViewById(R.id.level_title);
        }
    }
}