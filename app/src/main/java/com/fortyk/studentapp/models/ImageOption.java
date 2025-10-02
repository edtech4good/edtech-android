package com.fortyk.studentapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageOption implements Parcelable {
    private String image;
    private String description;
    private String answer;
    private boolean isSelected;
    private int id;

    public ImageOption(String image, String description, String answer, boolean isSelected, int id) {
        this.image = image;
        this.description = description;
        this.answer = answer;
        this.isSelected = isSelected;
        this.id = id;
    }

    protected ImageOption(Parcel in) {
        image = in.readString();
        description = in.readString();
        answer = in.readString();
        isSelected = in.readByte() != 0;
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(description);
        dest.writeString(answer);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageOption> CREATOR = new Creator<ImageOption>() {
        @Override
        public ImageOption createFromParcel(Parcel in) {
            return new ImageOption(in);
        }

        @Override
        public ImageOption[] newArray(int size) {
            return new ImageOption[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
