package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionOptionfile  implements Parcelable {
    private int filetype;
    private String filename;
    private String fileext;

    public QuestionOptionfile(int filetype, String filename, String fileext) {
        this.filetype = filetype;
        this.filename = filename;
        this.fileext = fileext;
    }

    protected QuestionOptionfile(Parcel in) {
        filetype = in.readInt();
        filename = in.readString();
        fileext = in.readString();
    }

    public static final Creator<QuestionOptionfile> CREATOR = new Creator<QuestionOptionfile>() {
        @Override
        public QuestionOptionfile createFromParcel(Parcel in) {
            return new QuestionOptionfile(in);
        }

        @Override
        public QuestionOptionfile[] newArray(int size) {
            return new QuestionOptionfile[size];
        }
    };

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileext() {
        return fileext;
    }

    public void setFileext(String fileext) {
        this.fileext = fileext;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(filetype);
        dest.writeString(filename);
        dest.writeString(fileext);
    }
}
