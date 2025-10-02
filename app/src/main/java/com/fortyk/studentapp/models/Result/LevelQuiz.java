package com.fortyk.studentapp.models.Result;

public class LevelQuiz {

    private boolean iscorrect;
    private String levelid;
    private String levelquizquestionid;
    private String questionid;

    public LevelQuiz(boolean iscorrect, String levelid, String levelquizquestionid, String questionid) {
        this.iscorrect = iscorrect;
        this.levelid = levelid;
        this.levelquizquestionid = levelquizquestionid;
        this.questionid = questionid;
    }

    public boolean isIscorrect() {
        return iscorrect;
    }

    public void setIscorrect(boolean iscorrect) {
        this.iscorrect = iscorrect;
    }

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public String getLevelquizquestionid() {
        return levelquizquestionid;
    }

    public void setLevelquizquestionid(String levelquizquestionid) {
        this.levelquizquestionid = levelquizquestionid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}
