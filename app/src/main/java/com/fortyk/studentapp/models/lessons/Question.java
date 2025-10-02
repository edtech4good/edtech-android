package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Question implements Parcelable {
    @SerializedName("questionid")
    private String questionid;
    @SerializedName("templatetypeid")
    private int templatetypeid;
    @SerializedName("isdeleted")
    private boolean isdeleted;
    @SerializedName("oldquestionid")
    private int oldquestionid;
    @SerializedName("oldbrickid")
    private int oldbrickid;
    @SerializedName("oldquestiontemplateid")
    private int oldquestiontemplateid;
    @SerializedName("questionobject")
    private QuestionObject questionobject;

    public Question(String questionid, int templatetypeid, boolean isdeleted, int oldquestionid, int oldbrickid, int oldquestiontemplateid, QuestionObject questionobject) {
        this.questionid = questionid;
        this.templatetypeid = templatetypeid;
        this.isdeleted = isdeleted;
        this.oldquestionid = oldquestionid;
        this.oldbrickid = oldbrickid;
        this.oldquestiontemplateid = oldquestiontemplateid;
        this.questionobject = questionobject;
    }

    protected Question(Parcel in) {
        questionid = in.readString();
        templatetypeid = in.readInt();
        isdeleted = in.readByte() != 0;
        oldquestionid = in.readInt();
        oldbrickid = in.readInt();
        oldquestiontemplateid = in.readInt();
        questionobject = in.readParcelable(QuestionObject.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionid);
        dest.writeInt(templatetypeid);
        dest.writeByte((byte) (isdeleted ? 1 : 0));
        dest.writeInt(oldquestionid);
        dest.writeInt(oldbrickid);
        dest.writeInt(oldquestiontemplateid);
        dest.writeParcelable(questionobject, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public int getTemplatetypeid() {
        return templatetypeid;
    }

    public void setTemplatetypeid(int templatetypeid) {
        this.templatetypeid = templatetypeid;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public int getOldquestionid() {
        return oldquestionid;
    }

    public void setOldquestionid(int oldquestionid) {
        this.oldquestionid = oldquestionid;
    }

    public int getOldbrickid() {
        return oldbrickid;
    }

    public void setOldbrickid(int oldbrickid) {
        this.oldbrickid = oldbrickid;
    }

    public int getOldquestiontemplateid() {
        return oldquestiontemplateid;
    }

    public void setOldquestiontemplateid(int oldquestiontemplateid) {
        this.oldquestiontemplateid = oldquestiontemplateid;
    }

    public QuestionObject getQuestionobject() {
        return questionobject;
    }

    public void setQuestionobject(QuestionObject questionobject) {
        this.questionobject = questionobject;
    }
}
