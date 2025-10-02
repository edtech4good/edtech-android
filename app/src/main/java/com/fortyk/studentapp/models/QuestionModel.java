package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionModel implements Parcelable {
    private String questionoptionid;
    private String questionoptiontext;
    private int questionoptionsequence;
    private boolean questionoptioniscorrect;
    private QuestionOptionfile questionoptionfile;
    private QuestionAssociate questionassociate;
    private boolean selectediscorrect;
    private boolean isSelected;

    public QuestionModel(String questionoptionid, String questionoptiontext,
                         int questionoptionsequence, boolean questionoptioniscorrect,
                         QuestionOptionfile questionoptionfile,
                         QuestionAssociate questionassociate, boolean selectediscorrect,
                         boolean isSelected) {
        this.questionoptionid = questionoptionid;
        this.questionoptiontext = questionoptiontext;
        this.questionoptionsequence = questionoptionsequence;
        this.questionoptioniscorrect = questionoptioniscorrect;
        this.questionoptionfile = questionoptionfile;
        this.questionassociate = questionassociate;
        this.selectediscorrect = selectediscorrect;
        this.isSelected = isSelected;
    }

    protected QuestionModel(Parcel in) {
        questionoptionid = in.readString();
        questionoptiontext = in.readString();
        questionoptionsequence = in.readInt();
        questionoptioniscorrect = in.readByte() != 0;
        questionoptionfile = in.readParcelable(QuestionOptionfile.class.getClassLoader());
        questionassociate = in.readParcelable(QuestionAssociate.class.getClassLoader());
        selectediscorrect = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionoptionid);
        dest.writeString(questionoptiontext);
        dest.writeInt(questionoptionsequence);
        dest.writeByte((byte) (questionoptioniscorrect ? 1 : 0));
        dest.writeParcelable(questionoptionfile, flags);
        dest.writeParcelable(questionassociate, flags);
        dest.writeByte((byte) (selectediscorrect ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

    public String getQuestionoptionid() {
        return questionoptionid;
    }

    public void setQuestionoptionid(String questionoptionid) {
        this.questionoptionid = questionoptionid;
    }

    public String getQuestionoptiontext() {
        return questionoptiontext;
    }

    public void setQuestionoptiontext(String questionoptiontext) {
        this.questionoptiontext = questionoptiontext;
    }

    public int getQuestionoptionsequence() {
        return questionoptionsequence;
    }

    public void setQuestionoptionsequence(int questionoptionsequence) {
        this.questionoptionsequence = questionoptionsequence;
    }

    public boolean isQuestionoptioniscorrect() {
        return questionoptioniscorrect;
    }

    public void setQuestionoptioniscorrect(boolean questionoptioniscorrect) {
        this.questionoptioniscorrect = questionoptioniscorrect;
    }

    public QuestionOptionfile getQuestionoptionfile() {
        return questionoptionfile;
    }

    public void setQuestionoptionfile(QuestionOptionfile questionoptionfile) {
        this.questionoptionfile = questionoptionfile;
    }

    public QuestionAssociate getQuestionassociate() {
        return questionassociate;
    }

    public void setQuestionassociate(QuestionAssociate questionassociate) {
        this.questionassociate = questionassociate;
    }

    public boolean isSelectediscorrect() {
        return selectediscorrect;
    }

    public void setSelectediscorrect(boolean selectediscorrect) {
        this.selectediscorrect = selectediscorrect;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}