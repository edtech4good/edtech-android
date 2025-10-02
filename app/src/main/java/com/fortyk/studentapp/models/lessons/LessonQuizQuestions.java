package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LessonQuizQuestions implements Parcelable {
    private String lessonquizquestionid;
    private String lessonquizid;
    private String questionid;
    private int lessonquizquestionorder;
    @SerializedName("question")
    private Question question;

    public LessonQuizQuestions(String lessonquizquestionid, String lessonquizid, String questionid, int lessonquizquestionorder, Question question) {
        this.lessonquizquestionid = lessonquizquestionid;
        this.lessonquizid = lessonquizid;
        this.questionid = questionid;
        this.lessonquizquestionorder = lessonquizquestionorder;
        this.question = question;
    }

    protected LessonQuizQuestions(Parcel in) {
        lessonquizquestionid = in.readString();
        lessonquizid = in.readString();
        questionid = in.readString();
        lessonquizquestionorder = in.readInt();
        question = in.readParcelable(Question.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lessonquizquestionid);
        dest.writeString(lessonquizid);
        dest.writeString(questionid);
        dest.writeInt(lessonquizquestionorder);
        dest.writeParcelable(question, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LessonQuizQuestions> CREATOR = new Creator<LessonQuizQuestions>() {
        @Override
        public LessonQuizQuestions createFromParcel(Parcel in) {
            return new LessonQuizQuestions(in);
        }

        @Override
        public LessonQuizQuestions[] newArray(int size) {
            return new LessonQuizQuestions[size];
        }
    };

    public String getLessonquizquestionid() {
        return lessonquizquestionid;
    }

    public void setLessonquizquestionid(String lessonquizquestionid) {
        this.lessonquizquestionid = lessonquizquestionid;
    }

    public String getLessonquizid() {
        return lessonquizid;
    }

    public void setLessonquizid(String lessonquizid) {
        this.lessonquizid = lessonquizid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public int getLessonquizquestionorder() {
        return lessonquizquestionorder;
    }

    public void setLessonquizquestionorder(int lessonquizquestionorder) {
        this.lessonquizquestionorder = lessonquizquestionorder;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
