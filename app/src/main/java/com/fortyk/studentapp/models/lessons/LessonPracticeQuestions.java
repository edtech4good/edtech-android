package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class LessonPracticeQuestions implements Parcelable {
    @SerializedName("lessonpracticequestionid")
    private String lessonpracticequestionid;
    @SerializedName("lessonpracticeid")
    private String lessonpracticeid;
    @SerializedName("questionid")
    private String questionid;
    @SerializedName("lessonpracticequestionorder")
    private int lessonpracticequestionorder;
    @SerializedName("question")
    private Question question;

    public LessonPracticeQuestions(String lessonpracticequestionid, String lessonpracticeid, String questionid, int lessonpracticequestionorder, Question question) {
        this.lessonpracticequestionid = lessonpracticequestionid;
        this.lessonpracticeid = lessonpracticeid;
        this.questionid = questionid;
        this.lessonpracticequestionorder = lessonpracticequestionorder;
        this.question = question;
    }

    protected LessonPracticeQuestions(Parcel in) {
        lessonpracticequestionid = in.readString();
        lessonpracticeid = in.readString();
        questionid = in.readString();
        lessonpracticequestionorder = in.readInt();
        question = in.readParcelable(Question.class.getClassLoader());
    }

    public static final Creator<LessonPracticeQuestions> CREATOR = new Creator<LessonPracticeQuestions>() {
        @Override
        public LessonPracticeQuestions createFromParcel(Parcel in) {
            return new LessonPracticeQuestions(in);
        }

        @Override
        public LessonPracticeQuestions[] newArray(int size) {
            return new LessonPracticeQuestions[size];
        }
    };

    public String getLessonpracticequestionid() {
        return lessonpracticequestionid;
    }

    public void setLessonpracticequestionid(String lessonpracticequestionid) {
        this.lessonpracticequestionid = lessonpracticequestionid;
    }

    public String getLessonpracticeid() {
        return lessonpracticeid;
    }

    public void setLessonpracticeid(String lessonpracticeid) {
        this.lessonpracticeid = lessonpracticeid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public int getLessonpracticequestionorder() {
        return lessonpracticequestionorder;
    }

    public void setLessonpracticequestionorder(int lessonpracticequestionorder) {
        this.lessonpracticequestionorder = lessonpracticequestionorder;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lessonpracticequestionid);
        dest.writeString(lessonpracticeid);
        dest.writeString(questionid);
        dest.writeInt(lessonpracticequestionorder);
        dest.writeParcelable(question, flags);
    }
}
