package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.fortyk.studentapp.models.QuestionModel;
import com.fortyk.studentapp.models.QuestionOptionfile;
import com.fortyk.studentapp.models.QuizHeading;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionObject implements Parcelable {

    @SerializedName("questionoptions")
    private List<QuestionModel> questionoptions;
    @SerializedName("questionfile")
    private QuestionOptionfile questionfile;
    @SerializedName("questiontext")
    private String questiontext;
    @SerializedName("questionheading")
    private QuizHeading questionheading;
    @SerializedName("questiondistractors")
    private List<QuestionDistractors> QuestionDistractors;

    public QuestionObject(List<QuestionModel> questionoptions, QuestionOptionfile questionfile,
                          String questiontext, QuizHeading questionheading,
                          List<com.fortyk.studentapp.models.lessons.QuestionDistractors> questionDistractors) {
        this.questionoptions = questionoptions;
        this.questionfile = questionfile;
        this.questiontext = questiontext;
        this.questionheading = questionheading;
        QuestionDistractors = questionDistractors;
    }

    protected QuestionObject(Parcel in) {
        questionoptions = in.createTypedArrayList(QuestionModel.CREATOR);
        questionfile = in.readParcelable(QuestionOptionfile.class.getClassLoader());
        questiontext = in.readString();
        questionheading = in.readParcelable(QuizHeading.class.getClassLoader());
        QuestionDistractors = in.createTypedArrayList(com.fortyk.studentapp.models.lessons.QuestionDistractors.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(questionoptions);
        dest.writeParcelable(questionfile, flags);
        dest.writeString(questiontext);
        dest.writeParcelable(questionheading, flags);
        dest.writeTypedList(QuestionDistractors);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionObject> CREATOR = new Creator<QuestionObject>() {
        @Override
        public QuestionObject createFromParcel(Parcel in) {
            return new QuestionObject(in);
        }

        @Override
        public QuestionObject[] newArray(int size) {
            return new QuestionObject[size];
        }
    };

    public List<QuestionModel> getQuestionoptions() {
        return questionoptions;
    }

    public void setQuestionoptions(List<QuestionModel> questionoptions) {
        this.questionoptions = questionoptions;
    }

    public QuestionOptionfile getQuestionfile() {
        return questionfile;
    }

    public void setQuestionfile(QuestionOptionfile questionfile) {
        this.questionfile = questionfile;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext = questiontext;
    }

    public QuizHeading getQuestionheading() {
        return questionheading;
    }

    public void setQuestionheading(QuizHeading questionheading) {
        this.questionheading = questionheading;
    }

    public List<com.fortyk.studentapp.models.lessons.QuestionDistractors> getQuestionDistractors() {
        return QuestionDistractors;
    }

    public void setQuestionDistractors(List<com.fortyk.studentapp.models.lessons.QuestionDistractors> questionDistractors) {
        QuestionDistractors = questionDistractors;
    }
}
