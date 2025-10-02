package com.fortyk.studentapp.models;

public class SelectedFillBlanks {
    private int position;
    private int sequence;
    private String text;

    public SelectedFillBlanks(int position, int sequence, String text) {
        this.position = position;
        this.sequence = sequence;
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
