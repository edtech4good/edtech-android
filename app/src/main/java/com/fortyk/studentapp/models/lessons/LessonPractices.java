package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonPractices implements Parcelable {
    private int lessonpracticeid;
    private int lessonid;
    private int lessonpracticeorder;
    private String lessonpracticename;
    private String lessonpracticedescription;
    @SerializedName("lessonpracticequestions")
    private List<LessonPracticeQuestions> lessonpracticequestions;

    public LessonPractices(int lessonpracticeid, int lessonid, int lessonpracticeorder,
                           String lessonpracticename, String lessonpracticedescription,
                           List<LessonPracticeQuestions> lessonpracticequestions) {
        this.lessonpracticeid = lessonpracticeid;
        this.lessonid = lessonid;
        this.lessonpracticeorder = lessonpracticeorder;
        this.lessonpracticename = lessonpracticename;
        this.lessonpracticedescription = lessonpracticedescription;
        this.lessonpracticequestions = lessonpracticequestions;
    }

    protected LessonPractices(Parcel in) {
        lessonpracticeid = in.readInt();
        lessonid = in.readInt();
        lessonpracticeorder = in.readInt();
        lessonpracticename = in.readString();
        lessonpracticedescription = in.readString();
        lessonpracticequestions = in.createTypedArrayList(LessonPracticeQuestions.CREATOR);
    }

    public static final Creator<LessonPractices> CREATOR = new Creator<LessonPractices>() {
        @Override
        public LessonPractices createFromParcel(Parcel in) {
            return new LessonPractices(in);
        }

        @Override
        public LessonPractices[] newArray(int size) {
            return new LessonPractices[size];
        }
    };

    public int getLessonpracticeid() {
        return lessonpracticeid;
    }

    public void setLessonpracticeid(int lessonpracticeid) {
        this.lessonpracticeid = lessonpracticeid;
    }

    public int getLessonid() {
        return lessonid;
    }

    public void setLessonid(int lessonid) {
        this.lessonid = lessonid;
    }

    public int getLessonpracticeorder() {
        return lessonpracticeorder;
    }

    public void setLessonpracticeorder(int lessonpracticeorder) {
        this.lessonpracticeorder = lessonpracticeorder;
    }

    public String getLessonpracticename() {
        return lessonpracticename;
    }

    public void setLessonpracticename(String lessonpracticename) {
        this.lessonpracticename = lessonpracticename;
    }

    public String getLessonpracticedescription() {
        return lessonpracticedescription;
    }

    public void setLessonpracticedescription(String lessonpracticedescription) {
        this.lessonpracticedescription = lessonpracticedescription;
    }

    public List<LessonPracticeQuestions> getLessonpracticequestions() {
        return lessonpracticequestions;
    }

    public void setLessonpracticequestions(List<LessonPracticeQuestions> lessonpracticequestions) {
        this.lessonpracticequestions = lessonpracticequestions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lessonpracticeid);
        dest.writeInt(lessonid);
        dest.writeInt(lessonpracticeorder);
        dest.writeString(lessonpracticename);
        dest.writeString(lessonpracticedescription);
        dest.writeTypedList(lessonpracticequestions);
    }
}
