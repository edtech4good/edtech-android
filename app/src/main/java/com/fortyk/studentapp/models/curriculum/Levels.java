package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Levels implements Parcelable {
    private String levelid;
    private String gradeid;
    private String levelname;
    private String leveldescription;
    private boolean isdeleted;
    private boolean levelstatus;
    private int levelorder;
    @SerializedName("lessons")
    private List<Lessons> lessonsList;
    private boolean hasquiz;

    public Levels(String levelid, String gradeid, String levelname, String leveldescription, boolean isdeleted, boolean levelstatus, int levelorder, List<Lessons> lessonsList, boolean hasquiz) {
        this.levelid = levelid;
        this.gradeid = gradeid;
        this.levelname = levelname;
        this.leveldescription = leveldescription;
        this.isdeleted = isdeleted;
        this.levelstatus = levelstatus;
        this.levelorder = levelorder;
        this.lessonsList = lessonsList;
        this.hasquiz = hasquiz;
    }

    protected Levels(Parcel in) {
        levelid = in.readString();
        gradeid = in.readString();
        levelname = in.readString();
        leveldescription = in.readString();
        isdeleted = in.readByte() != 0;
        levelstatus = in.readByte() != 0;
        levelorder = in.readInt();
        lessonsList = in.createTypedArrayList(Lessons.CREATOR);
        hasquiz = in.readByte() != 0;
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
        dest.writeTypedList(lessonsList);
        dest.writeByte((byte) (hasquiz ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Levels> CREATOR = new Creator<Levels>() {
        @Override
        public Levels createFromParcel(Parcel in) {
            return new Levels(in);
        }

        @Override
        public Levels[] newArray(int size) {
            return new Levels[size];
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

    public List<Lessons> getLessonsList() {
        return lessonsList;
    }

    public void setLessonsList(List<Lessons> lessonsList) {
        this.lessonsList = lessonsList;
    }

    public boolean isHasquiz() {
        return hasquiz;
    }

    public void setHasquiz(boolean hasquiz) {
        this.hasquiz = hasquiz;
    }
}