package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

public class Lessons implements Parcelable {
    private String lessonheading;
    private String lessonid;
    private String levelid;
    private String lessonname;
    private String lessondescription;
    private int practicecount;
    private int quizcount;
    private int lessonpasspercentage;
    private int lessonorder;
    private boolean isdeleted;

    public Lessons(String lessonheading, String lessonid, String levelid, String lessonname, String lessondescription, int practicecount, int quizcount, int lessonpasspercentage, int lessonorder, boolean isdeleted) {
        this.lessonheading = lessonheading;
        this.lessonid = lessonid;
        this.levelid = levelid;
        this.lessonname = lessonname;
        this.lessondescription = lessondescription;
        this.practicecount = practicecount;
        this.quizcount = quizcount;
        this.lessonpasspercentage = lessonpasspercentage;
        this.lessonorder = lessonorder;
        this.isdeleted = isdeleted;
    }

    protected Lessons(Parcel in) {
        lessonheading = in.readString();
        lessonid = in.readString();
        levelid = in.readString();
        lessonname = in.readString();
        lessondescription = in.readString();
        practicecount = in.readInt();
        quizcount = in.readInt();
        lessonpasspercentage = in.readInt();
        lessonorder = in.readInt();
        isdeleted = in.readByte() != 0;
    }

    public static final Creator<Lessons> CREATOR = new Creator<Lessons>() {
        @Override
        public Lessons createFromParcel(Parcel in) {
            return new Lessons(in);
        }

        @Override
        public Lessons[] newArray(int size) {
            return new Lessons[size];
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

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeByte((byte) (isdeleted ? 1 : 0));
    }
}