package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Grades implements Parcelable {
    private String gradeid;
    private String curriculumid;
    private String gradename;
    private String gradedescription;
    private int gradeorder;
    private boolean isdeleted;
    @SerializedName("levels")
    private List<Levels> levelsList;

    public Grades(String gradeid, String curriculumid, String gradename, String gradedescription, int gradeorder, boolean isdeleted, List<Levels> levelsList) {
        this.gradeid = gradeid;
        this.curriculumid = curriculumid;
        this.gradename = gradename;
        this.gradedescription = gradedescription;
        this.gradeorder = gradeorder;
        this.isdeleted = isdeleted;
        this.levelsList = levelsList;
    }

    protected Grades(Parcel in) {
        gradeid = in.readString();
        curriculumid = in.readString();
        gradename = in.readString();
        gradedescription = in.readString();
        gradeorder = in.readInt();
        isdeleted = in.readByte() != 0;
        levelsList = in.createTypedArrayList(Levels.CREATOR);
    }

    public static final Creator<Grades> CREATOR = new Creator<Grades>() {
        @Override
        public Grades createFromParcel(Parcel in) {
            return new Grades(in);
        }

        @Override
        public Grades[] newArray(int size) {
            return new Grades[size];
        }
    };

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public String getCurriculumid() {
        return curriculumid;
    }

    public void setCurriculumid(String curriculumid) {
        this.curriculumid = curriculumid;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public String getGradedescription() {
        return gradedescription;
    }

    public void setGradedescription(String gradedescription) {
        this.gradedescription = gradedescription;
    }

    public int getGradeorder() {
        return gradeorder;
    }

    public void setGradeorder(int gradeorder) {
        this.gradeorder = gradeorder;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public List<Levels> getLevelsList() {
        return levelsList;
    }

    public void setLevelsList(List<Levels> levelsList) {
        this.levelsList = levelsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gradeid);
        dest.writeString(curriculumid);
        dest.writeString(gradename);
        dest.writeString(gradedescription);
        dest.writeInt(gradeorder);
        dest.writeByte((byte) (isdeleted ? 1 : 0));
        dest.writeTypedList(levelsList);
    }
}
