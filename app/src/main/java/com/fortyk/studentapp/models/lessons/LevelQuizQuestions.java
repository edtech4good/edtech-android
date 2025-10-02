package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LevelQuizQuestions implements Parcelable {
    private String levelquizquestionid;
    private String levelid;
    private String questionid;
    private boolean levelquizquestionstatus;
    private int levelquizquestionorder;
    @SerializedName("question")
    private Question question;

    public LevelQuizQuestions(String levelquizquestionid, String levelid, String questionid, boolean levelquizquestionstatus, int levelquizquestionorder, Question question) {
        this.levelquizquestionid = levelquizquestionid;
        this.levelid = levelid;
        this.questionid = questionid;
        this.levelquizquestionstatus = levelquizquestionstatus;
        this.levelquizquestionorder = levelquizquestionorder;
        this.question = question;
    }

    protected LevelQuizQuestions(Parcel in) {
        levelquizquestionid = in.readString();
        levelid = in.readString();
        questionid = in.readString();
        levelquizquestionstatus = in.readByte() != 0;
        levelquizquestionorder = in.readInt();
        question = in.readParcelable(Question.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(levelquizquestionid);
        dest.writeString(levelid);
        dest.writeString(questionid);
        dest.writeByte((byte) (levelquizquestionstatus ? 1 : 0));
        dest.writeInt(levelquizquestionorder);
        dest.writeParcelable(question, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LevelQuizQuestions> CREATOR = new Creator<LevelQuizQuestions>() {
        @Override
        public LevelQuizQuestions createFromParcel(Parcel in) {
            return new LevelQuizQuestions(in);
        }

        @Override
        public LevelQuizQuestions[] newArray(int size) {
            return new LevelQuizQuestions[size];
        }
    };

    public String getLevelquizquestionid() {
        return levelquizquestionid;
    }

    public void setLevelquizquestionid(String levelquizquestionid) {
        this.levelquizquestionid = levelquizquestionid;
    }

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public boolean isLevelquizquestionstatus() {
        return levelquizquestionstatus;
    }

    public void setLevelquizquestionstatus(boolean levelquizquestionstatus) {
        this.levelquizquestionstatus = levelquizquestionstatus;
    }

    public int getLevelquizquestionorder() {
        return levelquizquestionorder;
    }

    public void setLevelquizquestionorder(int levelquizquestionorder) {
        this.levelquizquestionorder = levelquizquestionorder;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
