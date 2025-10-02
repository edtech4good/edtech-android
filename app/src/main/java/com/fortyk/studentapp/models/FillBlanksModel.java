package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FillBlanksModel implements Parcelable {
    private String answer;
    private boolean isBlank;
    private int id;
    private int sequence;

    public FillBlanksModel(String answer, boolean isBlank, int id, int sequence) {
        this.answer = answer;
        this.isBlank = isBlank;
        this.id = id;
        this.sequence = sequence;
    }

    protected FillBlanksModel(Parcel in) {
        answer = in.readString();
        isBlank = in.readByte() != 0;
        id = in.readInt();
        sequence = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(answer);
        dest.writeByte((byte) (isBlank ? 1 : 0));
        dest.writeInt(id);
        dest.writeInt(sequence);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FillBlanksModel> CREATOR = new Creator<FillBlanksModel>() {
        @Override
        public FillBlanksModel createFromParcel(Parcel in) {
            return new FillBlanksModel(in);
        }

        @Override
        public FillBlanksModel[] newArray(int size) {
            return new FillBlanksModel[size];
        }
    };

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isBlank() {
        return isBlank;
    }

    public void setBlank(boolean blank) {
        isBlank = blank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
