package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.activities.VideoPlayerActivity;
import com.fortyk.studentapp.activities.templates.TemplateActivity;
import com.fortyk.studentapp.models.lessons.LearningPath;

import java.util.List;

import static com.fortyk.studentapp.utils.Constants.getPath;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ViewHolder> {

    private List<LearningPath> lessonLearnings;
    private Context context;
    private String grade_title;
    private String level_title;
    private String lesson_title;

    public ChaptersAdapter(Context context, List<LearningPath> learnings, String grade_title, String level_title, String lesson_title) {
        this.lessonLearnings = learnings;
        this.context = context;
        this.grade_title = grade_title;
        this.level_title = level_title;
        this.lesson_title = lesson_title;
    }

    @NonNull
    @Override
    public ChaptersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChaptersAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersAdapter.ViewHolder viewHolder, final int position) {
        LearningPath learningPath = lessonLearnings.get(position);
        String title = lessonLearnings.get(position).getLearningpathname();
        viewHolder.chapterTitle.setText(title);

        viewHolder.chapterImage.setImageResource(R.drawable.brick_unlock);
        String finalTitle = title;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (learningPath.getLearningpathtype() != 1) {
                    Intent intent = new Intent(context, TemplateActivity.class);
                    intent.putExtra("type", learningPath.getLearningpathtype());
                    intent.putExtra("lesson", lessonLearnings.get(position));
                    intent.putExtra("grade_title", grade_title);
                    intent.putExtra("level_title", level_title);
                    intent.putExtra("lesson_title", lesson_title);
                    intent.putExtra("practice_title", finalTitle);
                    context.startActivity(intent);
                }else{
                    String content_file = lessonLearnings.get(position).getLessonlearningfileobject().getFilename();
                    if (content_file.isEmpty() || content_file.equalsIgnoreCase("null") || content_file.equals(null)) {
                        Toast.makeText(context, "This brick is not having any content", Toast.LENGTH_LONG).show();
                    } else {
                        Intent ii = new Intent(context, VideoPlayerActivity.class);
                        ii.putExtra("path", getPath(context, content_file));
                        context.startActivity(ii);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonLearnings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView chapterImage;
        public TextView chapterTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            chapterImage = (ImageView) itemLayoutView.findViewById(R.id.chapter_image);
            chapterTitle = (TextView) itemLayoutView.findViewById(R.id.chapter_title);
        }
    }
}