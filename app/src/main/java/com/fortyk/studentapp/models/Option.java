package com.fortyk.studentapp.models;

public class Option {

    private String option;
    private String answer;

    public Option(String option, String answer) {
        this.option = option;
        this.answer = answer;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
