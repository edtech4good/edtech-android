package com.fortyk.studentapp.models.lessons;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LessonResponse implements Parcelable {

    @SerializedName("data")
    private LessonChapters data;
    private boolean error;
    private String errormessage;

    public LessonResponse(LessonChapters data, boolean error, String errormessage) {
        this.data = data;
        this.error = error;
        this.errormessage = errormessage;
    }

    public LessonChapters getData() {
        return data;
    }

    public void setData(LessonChapters data) {
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

    public static Creator<LessonResponse> getCREATOR() {
        return CREATOR;
    }

    protected LessonResponse(Parcel in) {
        data = in.readParcelable(LessonChapters.class.getClassLoader());
        error = in.readByte() != 0;
        errormessage = in.readString();
    }

    public static final Creator<LessonResponse> CREATOR = new Creator<LessonResponse>() {
        @Override
        public LessonResponse createFromParcel(Parcel in) {
            return new LessonResponse(in);
        }

        @Override
        public LessonResponse[] newArray(int size) {
            return new LessonResponse[size];
        }
    };

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
