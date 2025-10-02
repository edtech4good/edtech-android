package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LessonQuizzes implements Parcelable {
    private int lessonquizid;
    private int lessonid;
    private int lessonquizorder;
    private String lessonquizname;
    private String lessonquizdescription;
    private List<LessonQuizQuestions> lessonquizquestions;

    public LessonQuizzes(int lessonquizid, int lessonid, int lessonquizorder
            , String lessonquizname, String lessonquizdescription, List<LessonQuizQuestions> lessonquizquestions) {
        this.lessonquizid = lessonquizid;
        this.lessonid = lessonid;
        this.lessonquizorder = lessonquizorder;
        this.lessonquizname = lessonquizname;
        this.lessonquizdescription = lessonquizdescription;
        this.lessonquizquestions = lessonquizquestions;
    }

    protected LessonQuizzes(Parcel in) {
        lessonquizid = in.readInt();
        lessonid = in.readInt();
        lessonquizorder = in.readInt();
        lessonquizname = in.readString();
        lessonquizdescription = in.readString();
        lessonquizquestions = in.createTypedArrayList(LessonQuizQuestions.CREATOR);
    }

    public static final Creator<LessonQuizzes> CREATOR = new Creator<LessonQuizzes>() {
        @Override
        public LessonQuizzes createFromParcel(Parcel in) {
            return new LessonQuizzes(in);
        }

        @Override
        public LessonQuizzes[] newArray(int size) {
            return new LessonQuizzes[size];
        }
    };

    public int getLessonquizid() {
        return lessonquizid;
    }

    public void setLessonquizid(int lessonquizid) {
        this.lessonquizid = lessonquizid;
    }

    public int getLessonid() {
        return lessonid;
    }

    public void setLessonid(int lessonid) {
        this.lessonid = lessonid;
    }

    public int getLessonquizorder() {
        return lessonquizorder;
    }

    public void setLessonquizorder(int lessonquizorder) {
        this.lessonquizorder = lessonquizorder;
    }

    public String getLessonquizname() {
        return lessonquizname;
    }

    public void setLessonquizname(String lessonquizname) {
        this.lessonquizname = lessonquizname;
    }

    public String getLessonquizdescription() {
        return lessonquizdescription;
    }

    public void setLessonquizdescription(String lessonquizdescription) {
        this.lessonquizdescription = lessonquizdescription;
    }

    public List<LessonQuizQuestions> getLessonquizquestions() {
        return lessonquizquestions;
    }

    public void setLessonquizquestions(List<LessonQuizQuestions> lessonquizquestions) {
        this.lessonquizquestions = lessonquizquestions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lessonquizid);
        dest.writeInt(lessonid);
        dest.writeInt(lessonquizorder);
        dest.writeString(lessonquizname);
        dest.writeString(lessonquizdescription);
        dest.writeTypedList(lessonquizquestions);
    }
}
