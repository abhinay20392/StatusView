package com.example.statusview.Modal;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusModel implements Parcelable {
    public String imageUri;
    public String name;
    public String time;

    public StatusModel(String imageUri, String name, String time) {
        this.imageUri = imageUri;
        this.name = name;
        this.time = time;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Creator<StatusModel> getCREATOR() {
        return CREATOR;
    }

    protected StatusModel(Parcel in) {
        imageUri = in.readString();
        name = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUri);
        dest.writeString(name);
        dest.writeString(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StatusModel> CREATOR = new Parcelable.Creator<StatusModel>() {
        @Override
        public StatusModel createFromParcel(Parcel in) {
            return new StatusModel(in);
        }

        @Override
        public StatusModel[] newArray(int size) {
            return new StatusModel[size];
        }
    };
}

