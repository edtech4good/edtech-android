package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LevelQuizResponse implements Parcelable {

    @SerializedName("data")
    private LevelQuiz data;
    private boolean error;
    private String errormessage;

    public LevelQuizResponse(LevelQuiz data, boolean error, String errormessage) {
        this.data = data;
        this.error = error;
        this.errormessage = errormessage;
    }

    protected LevelQuizResponse(Parcel in) {
        data = in.readParcelable(LevelQuiz.class.getClassLoader());
        error = in.readByte() != 0;
        errormessage = in.readString();
    }

    public static final Creator<LevelQuizResponse> CREATOR = new Creator<LevelQuizResponse>() {
        @Override
        public LevelQuizResponse createFromParcel(Parcel in) {
            return new LevelQuizResponse(in);
        }

        @Override
        public LevelQuizResponse[] newArray(int size) {
            return new LevelQuizResponse[size];
        }
    };

    public LevelQuiz getData() {
        return data;
    }

    public void setData(LevelQuiz data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(errormessage);
    }
}
