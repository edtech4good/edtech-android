package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Curriculum implements Parcelable {
    private String curriculumid;
    private String curriculumname;
    private String curriculumdescription;
    private boolean isdeleted;
    @SerializedName("grades")
    private List<Grades> gradesList;

    public Curriculum(String curriculumid, String curriculumname, String curriculumdescription, boolean isdeleted, List<Grades> gradesList) {
        this.curriculumid = curriculumid;
        this.curriculumname = curriculumname;
        this.curriculumdescription = curriculumdescription;
        this.isdeleted = isdeleted;
        this.gradesList = gradesList;
    }

    protected Curriculum(Parcel in) {
        curriculumid = in.readString();
        curriculumname = in.readString();
        curriculumdescription = in.readString();
        isdeleted = in.readByte() != 0;
        gradesList = in.createTypedArrayList(Grades.CREATOR);
    }

    public static final Creator<Curriculum> CREATOR = new Creator<Curriculum>() {
        @Override
        public Curriculum createFromParcel(Parcel in) {
            return new Curriculum(in);
        }

        @Override
        public Curriculum[] newArray(int size) {
            return new Curriculum[size];
        }
    };

    public String getCurriculumid() {
        return curriculumid;
    }

    public void setCurriculumid(String curriculumid) {
        this.curriculumid = curriculumid;
    }

    public String getCurriculumname() {
        return curriculumname;
    }

    public void setCurriculumname(String curriculumname) {
        this.curriculumname = curriculumname;
    }

    public String getCurriculumdescription() {
        return curriculumdescription;
    }

    public void setCurriculumdescription(String curriculumdescription) {
        this.curriculumdescription = curriculumdescription;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public List<Grades> getGradesList() {
        return gradesList;
    }

    public void setGradesList(List<Grades> gradesList) {
        this.gradesList = gradesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(curriculumid);
        dest.writeString(curriculumname);
        dest.writeString(curriculumdescription);
        dest.writeByte((byte) (isdeleted ? 1 : 0));
        dest.writeTypedList(gradesList);
    }
}
