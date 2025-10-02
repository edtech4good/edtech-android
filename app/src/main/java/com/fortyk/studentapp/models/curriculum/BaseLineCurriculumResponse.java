package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BaseLineCurriculumResponse implements Parcelable {

    @SerializedName("data")
    private BaseLineCurriculum data;
    private boolean error;
    private String errormessage;

    public BaseLineCurriculum getData() {
        return data;
    }

    public void setData(BaseLineCurriculum data) {
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

    protected BaseLineCurriculumResponse(Parcel in) {
        data = in.readParcelable(BaseLineCurriculum.class.getClassLoader());
        error = in.readByte() != 0;
        errormessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(errormessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseLineCurriculumResponse> CREATOR = new Creator<BaseLineCurriculumResponse>() {
        @Override
        public BaseLineCurriculumResponse createFromParcel(Parcel in) {
            return new BaseLineCurriculumResponse(in);
        }

        @Override
        public BaseLineCurriculumResponse[] newArray(int size) {
            return new BaseLineCurriculumResponse[size];
        }
    };
}
