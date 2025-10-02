package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CurriculumResponse implements Parcelable {

    @SerializedName("data")
    private Curriculum data;
    private boolean error;
    private String errormessage;

    public CurriculumResponse(Curriculum data, boolean error, String errormessage) {
        this.data = data;
        this.error = error;
        this.errormessage = errormessage;
    }

    protected CurriculumResponse(Parcel in) {
        data = in.readParcelable(Curriculum.class.getClassLoader());
        error = in.readByte() != 0;
        errormessage = in.readString();
    }

    public static final Creator<CurriculumResponse> CREATOR = new Creator<CurriculumResponse>() {
        @Override
        public CurriculumResponse createFromParcel(Parcel in) {
            return new CurriculumResponse(in);
        }

        @Override
        public CurriculumResponse[] newArray(int size) {
            return new CurriculumResponse[size];
        }
    };

    public Curriculum getData() {
        return data;
    }

    public void setData(Curriculum data) {
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(data, i);
        parcel.writeByte((byte) (error ? 1 : 0));
        parcel.writeString(errormessage);
    }
}
