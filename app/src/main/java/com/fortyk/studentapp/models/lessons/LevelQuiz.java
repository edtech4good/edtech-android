package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LevelQuiz implements Parcelable {
    private String levelid;
    private String gradeid;
    private String levelname;
    private String leveldescription;
    private boolean isdeleted;
    private boolean levelstatus;
    private int levelorder;
    @SerializedName("levelquizquestions")
    private List<LevelQuizQuestions> levelQuizQuestions;

    public LevelQuiz(String levelid, String gradeid, String levelname, String leveldescription, boolean isdeleted, boolean levelstatus, int levelorder, List<LevelQuizQuestions> levelQuizQuestions) {
        this.levelid = levelid;
        this.gradeid = gradeid;
        this.levelname = levelname;
        this.leveldescription = leveldescription;
        this.isdeleted = isdeleted;
        this.levelstatus = levelstatus;
        this.levelorder = levelorder;
        this.levelQuizQuestions = levelQuizQuestions;
    }

    protected LevelQuiz(Parcel in) {
        levelid = in.readString();
        gradeid = in.readString();
        levelname = in.readString();
        leveldescription = in.readString();
        isdeleted = in.readByte() != 0;
        levelstatus = in.readByte() != 0;
        levelorder = in.readInt();
        levelQuizQuestions = in.createTypedArrayList(LevelQuizQuestions.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(levelid);
        dest.writeString(gradeid);
        dest.writeString(levelname);
        dest.writeString(leveldescription);
        dest.writeByte((byte) (isdeleted ? 1 : 0));
        dest.writeByte((byte) (levelstatus ? 1 : 0));
        dest.writeInt(levelorder);
        dest.writeTypedList(levelQuizQuestions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LevelQuiz> CREATOR = new Creator<LevelQuiz>() {
        @Override
        public LevelQuiz createFromParcel(Parcel in) {
            return new LevelQuiz(in);
        }

        @Override
        public LevelQuiz[] newArray(int size) {
            return new LevelQuiz[size];
        }
    };

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public String getLeveldescription() {
        return leveldescription;
    }

    public void setLeveldescription(String leveldescription) {
        this.leveldescription = leveldescription;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public boolean isLevelstatus() {
        return levelstatus;
    }

    public void setLevelstatus(boolean levelstatus) {
        this.levelstatus = levelstatus;
    }

    public int getLevelorder() {
        return levelorder;
    }

    public void setLevelorder(int levelorder) {
        this.levelorder = levelorder;
    }

    public List<LevelQuizQuestions> getLevelQuizQuestions() {
        return levelQuizQuestions;
    }

    public void setLevelQuizQuestions(List<LevelQuizQuestions> levelQuizQuestions) {
        this.levelQuizQuestions = levelQuizQuestions;
    }
}
