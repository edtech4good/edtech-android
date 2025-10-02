package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QuizHeading implements Parcelable {
    private String headingtext;
    @SerializedName("headingfile")
    private QuestionOptionfile headingfiles;

    public QuizHeading(String headingtext, QuestionOptionfile headingfiles) {
        this.headingtext = headingtext;
        this.headingfiles = headingfiles;
    }

    protected QuizHeading(Parcel in) {
        headingtext = in.readString();
        headingfiles = in.readParcelable(QuestionOptionfile.class.getClassLoader());
    }

    public static final Creator<QuizHeading> CREATOR = new Creator<QuizHeading>() {
        @Override
        public QuizHeading createFromParcel(Parcel in) {
            return new QuizHeading(in);
        }

        @Override
        public QuizHeading[] newArray(int size) {
            return new QuizHeading[size];
        }
    };

    public String getHeadingtext() {
        return headingtext;
    }

    public void setHeadingtext(String headingtext) {
        this.headingtext = headingtext;
    }

    public QuestionOptionfile getHeadingfiles() {
        return headingfiles;
    }

    public void setHeadingfiles(QuestionOptionfile headingfiles) {
        this.headingfiles = headingfiles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headingtext);
        dest.writeParcelable(headingfiles, flags);
    }
}
