package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionDistractors  implements Parcelable {
    private String questiondistractorid;
    private String questiondistractortext;

    public QuestionDistractors(String questiondistractorid, String questiondistractortext) {
        this.questiondistractorid = questiondistractorid;
        this.questiondistractortext = questiondistractortext;
    }

    protected QuestionDistractors(Parcel in) {
        questiondistractorid = in.readString();
        questiondistractortext = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questiondistractorid);
        dest.writeString(questiondistractortext);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionDistractors> CREATOR = new Creator<QuestionDistractors>() {
        @Override
        public QuestionDistractors createFromParcel(Parcel in) {
            return new QuestionDistractors(in);
        }

        @Override
        public QuestionDistractors[] newArray(int size) {
            return new QuestionDistractors[size];
        }
    };

    public String getQuestiondistractorid() {
        return questiondistractorid;
    }

    public void setQuestiondistractorid(String questiondistractorid) {
        this.questiondistractorid = questiondistractorid;
    }

    public String getQuestiondistractortext() {
        return questiondistractortext;
    }

    public void setQuestiondistractortext(String questiondistractortext) {
        this.questiondistractortext = questiondistractortext;
    }
}
