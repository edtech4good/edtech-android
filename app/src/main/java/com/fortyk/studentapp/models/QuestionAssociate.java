package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QuestionAssociate implements Parcelable {
    private String questionassociatetext;
    @SerializedName("questionassociatefile")
    private QuestionOptionfile questionOptionfile;

    public QuestionAssociate(String questionassociatetext, QuestionOptionfile questionOptionfile) {
        this.questionassociatetext = questionassociatetext;
        this.questionOptionfile = questionOptionfile;
    }

    protected QuestionAssociate(Parcel in) {
        questionassociatetext = in.readString();
        questionOptionfile = in.readParcelable(QuestionOptionfile.class.getClassLoader());
    }

    public static final Creator<QuestionAssociate> CREATOR = new Creator<QuestionAssociate>() {
        @Override
        public QuestionAssociate createFromParcel(Parcel in) {
            return new QuestionAssociate(in);
        }

        @Override
        public QuestionAssociate[] newArray(int size) {
            return new QuestionAssociate[size];
        }
    };

    public String getQuestionassociatetext() {
        return questionassociatetext;
    }

    public void setQuestionassociatetext(String questionassociatetext) {
        this.questionassociatetext = questionassociatetext;
    }

    public QuestionOptionfile getQuestionOptionfile() {
        return questionOptionfile;
    }

    public void setQuestionOptionfile(QuestionOptionfile questionOptionfile) {
        this.questionOptionfile = questionOptionfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionassociatetext);
        dest.writeParcelable(questionOptionfile, flags);
    }
}
