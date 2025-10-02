package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonChapters implements Parcelable {
    private String lessonheading;
    private String lessonid;
    private String levelid;
    private String lessonname;
    private String lessondescription;
    private int practicecount;
    private int quizcount;
    private int lessonpasspercentage;
    private int lessonorder;
    private boolean lessonstatus;
    private boolean isdeleted;
    @SerializedName("learningpath")
    private List<LearningPath> learningpath;

    public LessonChapters(String lessonheading, String lessonid, String levelid, String lessonname, String lessondescription, int practicecount, int quizcount, int lessonpasspercentage, int lessonorder, boolean lessonstatus, boolean isdeleted, List<LearningPath> learningpath) {
        this.lessonheading = lessonheading;
        this.lessonid = lessonid;
        this.levelid = levelid;
        this.lessonname = lessonname;
        this.lessondescription = lessondescription;
        this.practicecount = practicecount;
        this.quizcount = quizcount;
        this.lessonpasspercentage = lessonpasspercentage;
        this.lessonorder = lessonorder;
        this.lessonstatus = lessonstatus;
        this.isdeleted = isdeleted;
        this.learningpath = learningpath;
    }

    protected LessonChapters(Parcel in) {
        lessonheading = in.readString();
        lessonid = in.readString();
        levelid = in.readString();
        lessonname = in.readString();
        lessondescription = in.readString();
        practicecount = in.readInt();
        quizcount = in.readInt();
        lessonpasspercentage = in.readInt();
        lessonorder = in.readInt();
        lessonstatus = in.readByte() != 0;
        isdeleted = in.readByte() != 0;
        learningpath = in.createTypedArrayList(LearningPath.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lessonheading);
        dest.writeString(lessonid);
        dest.writeString(levelid);
        dest.writeString(lessonname);
        dest.writeString(lessondescription);
        dest.writeInt(practicecount);
        dest.writeInt(quizcount);
        dest.writeInt(lessonpasspercentage);
        dest.writeInt(lessonorder);
        dest.writeByte((byte) (lessonstatus ? 1 : 0));
        dest.writeByte((byte) (isdeleted ? 1 : 0));
        dest.writeTypedList(learningpath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LessonChapters> CREATOR = new Creator<LessonChapters>() {
        @Override
        public LessonChapters createFromParcel(Parcel in) {
            return new LessonChapters(in);
        }

        @Override
        public LessonChapters[] newArray(int size) {
            return new LessonChapters[size];
        }
    };

    public String getLessonheading() {
        return lessonheading;
    }

    public void setLessonheading(String lessonheading) {
        this.lessonheading = lessonheading;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public String getLessonname() {
        return lessonname;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }

    public String getLessondescription() {
        return lessondescription;
    }

    public void setLessondescription(String lessondescription) {
        this.lessondescription = lessondescription;
    }

    public int getPracticecount() {
        return practicecount;
    }

    public void setPracticecount(int practicecount) {
        this.practicecount = practicecount;
    }

    public int getQuizcount() {
        return quizcount;
    }

    public void setQuizcount(int quizcount) {
        this.quizcount = quizcount;
    }

    public int getLessonpasspercentage() {
        return lessonpasspercentage;
    }

    public void setLessonpasspercentage(int lessonpasspercentage) {
        this.lessonpasspercentage = lessonpasspercentage;
    }

    public int getLessonorder() {
        return lessonorder;
    }

    public void setLessonorder(int lessonorder) {
        this.lessonorder = lessonorder;
    }

    public boolean isLessonstatus() {
        return lessonstatus;
    }

    public void setLessonstatus(boolean lessonstatus) {
        this.lessonstatus = lessonstatus;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public List<LearningPath> getLearningpath() {
        return learningpath;
    }

    public void setLearningpath(List<LearningPath> learningpath) {
        this.learningpath = learningpath;
    }
}
