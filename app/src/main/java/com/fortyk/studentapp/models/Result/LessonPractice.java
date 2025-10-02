package com.fortyk.studentapp.models.Result;

public class LessonPractice {

   private boolean iscorrect;
   private String lessonpracticeid;
   private String lessonpracticequestionid;
   private String questionid;
   private int tries;

    public LessonPractice(boolean iscorrect, String lessonpracticeid, String lessonpracticequestionid, String questionid, int tries) {
        this.iscorrect = iscorrect;
        this.lessonpracticeid = lessonpracticeid;
        this.lessonpracticequestionid = lessonpracticequestionid;
        this.questionid = questionid;
        this.tries = tries;
    }

    public boolean isIscorrect() {
        return iscorrect;
    }

    public void setIscorrect(boolean iscorrect) {
        this.iscorrect = iscorrect;
    }

    public String getLessonpracticeid() {
        return lessonpracticeid;
    }

    public void setLessonpracticeid(String lessonpracticeid) {
        this.lessonpracticeid = lessonpracticeid;
    }

    public String getLessonpracticequestionid() {
        return lessonpracticequestionid;
    }

    public void setLessonpracticequestionid(String lessonpracticequestionid) {
        this.lessonpracticequestionid = lessonpracticequestionid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
