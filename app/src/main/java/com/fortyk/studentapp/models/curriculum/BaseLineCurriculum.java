package com.fortyk.studentapp.models.curriculum;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseLineCurriculum implements Parcelable {

    private String baseline;
    private boolean baselinepassed;

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public boolean isBaselinepassed() {
        return baselinepassed;
    }

    public void setBaselinepassed(boolean baselinepassed) {
        this.baselinepassed = baselinepassed;
    }

    protected BaseLineCurriculum(Parcel in) {
        baseline = in.readString();
        baselinepassed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(baseline);
        dest.writeByte((byte) (baselinepassed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseLineCurriculum> CREATOR = new Creator<BaseLineCurriculum>() {
        @Override
        public BaseLineCurriculum createFromParcel(Parcel in) {
            return new BaseLineCurriculum(in);
        }

        @Override
        public BaseLineCurriculum[] newArray(int size) {
            return new BaseLineCurriculum[size];
        }
    };
}
