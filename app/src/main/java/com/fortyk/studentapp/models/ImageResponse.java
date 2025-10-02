package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ImageResponse implements Parcelable {

    private int id;
    private int question_number;
    private int template_id;
    private String template_title;
    private String title;
    private int program_id;
    private int level_number;
    private int lesson_id;
    private int brick_id;
    private String resource_path;
    private int total_questions;

    private List<ImageOption> imageOptions;


    public ImageResponse(int id, int question_number, int template_id, String template_title,
                         String title, int program_id, int level_number, int lesson_id,
                         int brick_id, String resource_path, int total_questions,
                         List<ImageOption> imageOptions) {
        this.id = id;
        this.question_number = question_number;
        this.template_id = template_id;
        this.template_title = template_title;
        this.title = title;
        this.program_id = program_id;
        this.level_number = level_number;
        this.lesson_id = lesson_id;
        this.brick_id = brick_id;
        this.resource_path = resource_path;
        this.total_questions = total_questions;
        this.imageOptions = imageOptions;
    }

    protected ImageResponse(Parcel in) {
        id = in.readInt();
        question_number = in.readInt();
        template_id = in.readInt();
        template_title = in.readString();
        title = in.readString();
        program_id = in.readInt();
        level_number = in.readInt();
        lesson_id = in.readInt();
        brick_id = in.readInt();
        resource_path = in.readString();
        total_questions = in.readInt();
        imageOptions = in.createTypedArrayList(ImageOption.CREATOR);
    }

    public static final Creator<ImageResponse> CREATOR = new Creator<ImageResponse>() {
        @Override
        public ImageResponse createFromParcel(Parcel in) {
            return new ImageResponse(in);
        }

        @Override
        public ImageResponse[] newArray(int size) {
            return new ImageResponse[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getQuestion_number() {
        return question_number;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public String getTemplate_title() {
        return template_title;
    }

    public String getTitle() {
        return title;
    }

    public int getProgram_id() {
        return program_id;
    }

    public int getLevel_number() {
        return level_number;
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public int getBrick_id() {
        return brick_id;
    }

    public String getResource_path() {
        return resource_path;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public List<ImageOption> getImageOptions() {
        return imageOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(question_number);
        dest.writeInt(template_id);
        dest.writeString(template_title);
        dest.writeString(title);
        dest.writeInt(program_id);
        dest.writeInt(level_number);
        dest.writeInt(lesson_id);
        dest.writeInt(brick_id);
        dest.writeString(resource_path);
        dest.writeInt(total_questions);
        dest.writeTypedList(imageOptions);
    }
}
