package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.fortyk.studentapp.models.QuestionOptionfile;
import com.google.gson.annotations.SerializedName;

public class LessonLearnings implements Parcelable {

    @SerializedName("lessonlearningfileobject")
    private QuestionOptionfile lessonlearningfileobject;
    private int lessonlearningid;
    private String lessonlearningname;
    private String lessonlearningdescription;
    private String lessonlearningfile;
    private int lessonid;
    private int lessonlearningorder;

    public LessonLearnings(QuestionOptionfile lessonlearningfileobject, int lessonlearningid, String lessonlearningname, String lessonlearningdescription, String lessonlearningfile, int lessonid, int lessonlearningorder) {
        this.lessonlearningfileobject = lessonlearningfileobject;
        this.lessonlearningid = lessonlearningid;
        this.lessonlearningname = lessonlearningname;
        this.lessonlearningdescription = lessonlearningdescription;
        this.lessonlearningfile = lessonlearningfile;
        this.lessonid = lessonid;
        this.lessonlearningorder = lessonlearningorder;
    }

    public QuestionOptionfile getLessonlearningfileobject() {
        return lessonlearningfileobject;
    }

    public void setLessonlearningfileobject(QuestionOptionfile lessonlearningfileobject) {
        this.lessonlearningfileobject = lessonlearningfileobject;
    }

    public int getLessonlearningid() {
        return lessonlearningid;
    }

    public void setLessonlearningid(int lessonlearningid) {
        this.lessonlearningid = lessonlearningid;
    }

    public String getLessonlearningname() {
        return lessonlearningname;
    }

    public void setLessonlearningname(String lessonlearningname) {
        this.lessonlearningname = lessonlearningname;
    }

    public String getLessonlearningdescription() {
        return lessonlearningdescription;
    }

    public void setLessonlearningdescription(String lessonlearningdescription) {
        this.lessonlearningdescription = lessonlearningdescription;
    }

    public String getLessonlearningfile() {
        return lessonlearningfile;
    }

    public void setLessonlearningfile(String lessonlearningfile) {
        this.lessonlearningfile = lessonlearningfile;
    }

    public int getLessonid() {
        return lessonid;
    }

    public void setLessonid(int lessonid) {
        this.lessonid = lessonid;
    }

    public int getLessonlearningorder() {
        return lessonlearningorder;
    }

    public void setLessonlearningorder(int lessonlearningorder) {
        this.lessonlearningorder = lessonlearningorder;
    }

    public static Creator<LessonLearnings> getCREATOR() {
        return CREATOR;
    }

    protected LessonLearnings(Parcel in) {
        lessonlearningfileobject = in.readParcelable(QuestionOptionfile.class.getClassLoader());
        lessonlearningid = in.readInt();
        lessonlearningname = in.readString();
        lessonlearningdescription = in.readString();
        lessonlearningfile = in.readString();
        lessonid = in.readInt();
        lessonlearningorder = in.readInt();
    }

    public static final Creator<LessonLearnings> CREATOR = new Creator<LessonLearnings>() {
        @Override
        public LessonLearnings createFromParcel(Parcel in) {
            return new LessonLearnings(in);
        }

        @Override
        public LessonLearnings[] newArray(int size) {
            return new LessonLearnings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(lessonlearningfileobject, flags);
        dest.writeInt(lessonlearningid);
        dest.writeString(lessonlearningname);
        dest.writeString(lessonlearningdescription);
        dest.writeString(lessonlearningfile);
        dest.writeInt(lessonid);
        dest.writeInt(lessonlearningorder);
    }
}
