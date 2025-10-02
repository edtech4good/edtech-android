package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TemplateResponse implements Parcelable {
    private int template_id;
    private String template_title;
    @SerializedName("quizheading")
    private QuizHeading quizHeading;
    @SerializedName("quizfile")
    private QuestionOptionfile quizfile;
    @SerializedName("question")
    private List<QuestionModel> questionModel;

    public TemplateResponse(int template_id, String template_title,
                            QuizHeading quizHeading, QuestionOptionfile quizfile,
                            List<QuestionModel> questionModel) {
        this.template_id = template_id;
        this.template_title = template_title;
        this.quizHeading = quizHeading;
        this.quizfile = quizfile;
        this.questionModel = questionModel;
    }

    protected TemplateResponse(Parcel in) {
        template_id = in.readInt();
        template_title = in.readString();
        quizHeading = in.readParcelable(QuizHeading.class.getClassLoader());
        quizfile = in.readParcelable(QuestionOptionfile.class.getClassLoader());
        questionModel = in.createTypedArrayList(QuestionModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(template_id);
        dest.writeString(template_title);
        dest.writeParcelable(quizHeading, flags);
        dest.writeParcelable(quizfile, flags);
        dest.writeTypedList(questionModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TemplateResponse> CREATOR = new Creator<TemplateResponse>() {
        @Override
        public TemplateResponse createFromParcel(Parcel in) {
            return new TemplateResponse(in);
        }

        @Override
        public TemplateResponse[] newArray(int size) {
            return new TemplateResponse[size];
        }
    };

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public String getTemplate_title() {
        return template_title;
    }

    public void setTemplate_title(String template_title) {
        this.template_title = template_title;
    }

    public QuizHeading getQuizHeading() {
        return quizHeading;
    }

    public void setQuizHeading(QuizHeading quizHeading) {
        this.quizHeading = quizHeading;
    }

    public QuestionOptionfile getQuizfile() {
        return quizfile;
    }

    public void setQuizfile(QuestionOptionfile quizfile) {
        this.quizfile = quizfile;
    }

    public List<QuestionModel> getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(List<QuestionModel> questionModel) {
        this.questionModel = questionModel;
    }
}
