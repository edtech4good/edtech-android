package com.fortyk.studentapp.models.Result;

public class LessonQuiz {

    private boolean iscorrect;
    private String lessonquizid;
    private String lessonquizquestionid;
    private String questionid;

    public LessonQuiz(boolean iscorrect, String lessonquizid, String lessonquizquestionid, String questionid) {
        this.iscorrect = iscorrect;
        this.lessonquizid = lessonquizid;
        this.lessonquizquestionid = lessonquizquestionid;
        this.questionid = questionid;
    }

    public boolean isIscorrect() {
        return iscorrect;
    }

    public void setIscorrect(boolean iscorrect) {
        this.iscorrect = iscorrect;
    }

    public String getLessonquizid() {
        return lessonquizid;
    }

    public void setLessonquizid(String lessonquizid) {
        this.lessonquizid = lessonquizid;
    }

    public String getLessonquizquestionid() {
        return lessonquizquestionid;
    }

    public void setLessonquizquestionid(String lessonquizquestionid) {
        this.lessonquizquestionid = lessonquizquestionid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}
