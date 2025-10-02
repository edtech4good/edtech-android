package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.fortyk.studentapp.models.QuestionOptionfile;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LearningPath implements Parcelable {
    @SerializedName("lessonlearningfileobject")
    private QuestionOptionfile lessonlearningfileobject;
    @SerializedName("lessonlearningid")
    private String lessonlearningid;
    @SerializedName("lessonlearningname")
    private String lessonlearningname;
    @SerializedName("lessonlearningdescription")
    private String lessonlearningdescription;
    @SerializedName("lessonid")
    private String lessonid;
    @SerializedName("lessonlearningorder")
    private int lessonlearningorder;
    @SerializedName("lessonpracticeid")
    private String lessonpracticeid;
    @SerializedName("lessonpracticeorder")
    private int lessonpracticeorder;
    @SerializedName("lessonpracticename")
    private String lessonpracticename;
    @SerializedName("lessonpracticedescription")
    private String lessonpracticedescription;
    @SerializedName("lessonpracticequestions")
    private List<LessonPracticeQuestions> lessonpracticequestions;
    private String lessonquizid;
    private int lessonquizorder;
    private String lessonquizname;
    private String lessonquizdescription;
    private List<LessonQuizQuestions> lessonquizquestions;
    private int learningpathtype;
    private String learningpathname;

    public LearningPath(QuestionOptionfile lessonlearningfileobject, String lessonlearningid, String lessonlearningname, String lessonlearningdescription, String lessonid, int lessonlearningorder, String lessonpracticeid, int lessonpracticeorder, String lessonpracticename, String lessonpracticedescription, List<LessonPracticeQuestions> lessonpracticequestions, String lessonquizid, int lessonquizorder, String lessonquizname, String lessonquizdescription, List<LessonQuizQuestions> lessonquizquestions, int learningpathtype, String learningpathname) {
        this.lessonlearningfileobject = lessonlearningfileobject;
        this.lessonlearningid = lessonlearningid;
        this.lessonlearningname = lessonlearningname;
        this.lessonlearningdescription = lessonlearningdescription;
        this.lessonid = lessonid;
        this.lessonlearningorder = lessonlearningorder;
        this.lessonpracticeid = lessonpracticeid;
        this.lessonpracticeorder = lessonpracticeorder;
        this.lessonpracticename = lessonpracticename;
        this.lessonpracticedescription = lessonpracticedescription;
        this.lessonpracticequestions = lessonpracticequestions;
        this.lessonquizid = lessonquizid;
        this.lessonquizorder = lessonquizorder;
        this.lessonquizname = lessonquizname;
        this.lessonquizdescription = lessonquizdescription;
        this.lessonquizquestions = lessonquizquestions;
        this.learningpathtype = learningpathtype;
        this.learningpathname = learningpathname;
    }

    protected LearningPath(Parcel in) {
        lessonlearningfileobject = in.readParcelable(QuestionOptionfile.class.getClassLoader());
        lessonlearningid = in.readString();
        lessonlearningname = in.readString();
        lessonlearningdescription = in.readString();
        lessonid = in.readString();
        lessonlearningorder = in.readInt();
        lessonpracticeid = in.readString();
        lessonpracticeorder = in.readInt();
        lessonpracticename = in.readString();
        lessonpracticedescription = in.readString();
        lessonpracticequestions = in.createTypedArrayList(LessonPracticeQuestions.CREATOR);
        lessonquizid = in.readString();
        lessonquizorder = in.readInt();
        lessonquizname = in.readString();
        lessonquizdescription = in.readString();
        lessonquizquestions = in.createTypedArrayList(LessonQuizQuestions.CREATOR);
        learningpathtype = in.readInt();
        learningpathname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(lessonlearningfileobject, flags);
        dest.writeString(lessonlearningid);
        dest.writeString(lessonlearningname);
        dest.writeString(lessonlearningdescription);
        dest.writeString(lessonid);
        dest.writeInt(lessonlearningorder);
        dest.writeString(lessonpracticeid);
        dest.writeInt(lessonpracticeorder);
        dest.writeString(lessonpracticename);
        dest.writeString(lessonpracticedescription);
        dest.writeTypedList(lessonpracticequestions);
        dest.writeString(lessonquizid);
        dest.writeInt(lessonquizorder);
        dest.writeString(lessonquizname);
        dest.writeString(lessonquizdescription);
        dest.writeTypedList(lessonquizquestions);
        dest.writeInt(learningpathtype);
        dest.writeString(learningpathname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LearningPath> CREATOR = new Creator<LearningPath>() {
        @Override
        public LearningPath createFromParcel(Parcel in) {
            return new LearningPath(in);
        }

        @Override
        public LearningPath[] newArray(int size) {
            return new LearningPath[size];
        }
    };

    public QuestionOptionfile getLessonlearningfileobject() {
        return lessonlearningfileobject;
    }

    public void setLessonlearningfileobject(QuestionOptionfile lessonlearningfileobject) {
        this.lessonlearningfileobject = lessonlearningfileobject;
    }

    public String getLessonlearningid() {
        return lessonlearningid;
    }

    public void setLessonlearningid(String lessonlearningid) {
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

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

    public int getLessonlearningorder() {
        return lessonlearningorder;
    }

    public void setLessonlearningorder(int lessonlearningorder) {
        this.lessonlearningorder = lessonlearningorder;
    }

    public String getLessonpracticeid() {
        return lessonpracticeid;
    }

    public void setLessonpracticeid(String lessonpracticeid) {
        this.lessonpracticeid = lessonpracticeid;
    }

    public int getLessonpracticeorder() {
        return lessonpracticeorder;
    }

    public void setLessonpracticeorder(int lessonpracticeorder) {
        this.lessonpracticeorder = lessonpracticeorder;
    }

    public String getLessonpracticename() {
        return lessonpracticename;
    }

    public void setLessonpracticename(String lessonpracticename) {
        this.lessonpracticename = lessonpracticename;
    }

    public String getLessonpracticedescription() {
        return lessonpracticedescription;
    }

    public void setLessonpracticedescription(String lessonpracticedescription) {
        this.lessonpracticedescription = lessonpracticedescription;
    }

    public List<LessonPracticeQuestions> getLessonpracticequestions() {
        return lessonpracticequestions;
    }

    public void setLessonpracticequestions(List<LessonPracticeQuestions> lessonpracticequestions) {
        this.lessonpracticequestions = lessonpracticequestions;
    }

    public String getLessonquizid() {
        return lessonquizid;
    }

    public void setLessonquizid(String lessonquizid) {
        this.lessonquizid = lessonquizid;
    }

    public int getLessonquizorder() {
        return lessonquizorder;
    }

    public void setLessonquizorder(int lessonquizorder) {
        this.lessonquizorder = lessonquizorder;
    }

    public String getLessonquizname() {
        return lessonquizname;
    }

    public void setLessonquizname(String lessonquizname) {
        this.lessonquizname = lessonquizname;
    }

    public String getLessonquizdescription() {
        return lessonquizdescription;
    }

    public void setLessonquizdescription(String lessonquizdescription) {
        this.lessonquizdescription = lessonquizdescription;
    }

    public List<LessonQuizQuestions> getLessonquizquestions() {
        return lessonquizquestions;
    }

    public void setLessonquizquestions(List<LessonQuizQuestions> lessonquizquestions) {
        this.lessonquizquestions = lessonquizquestions;
    }

    public int getLearningpathtype() {
        return learningpathtype;
    }

    public void setLearningpathtype(int learningpathtype) {
        this.learningpathtype = learningpathtype;
    }

    public String getLearningpathname() {
        return learningpathname;
    }

    public void setLearningpathname(String learningpathname) {
        this.learningpathname = learningpathname;
    }
}