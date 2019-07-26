/*
 * *
 *  * Created by Abhinay Sharma(abhinay20392@gmail.com) on 26/7/19 8:14 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/7/19 6:41 AM
 *
 */

package com.example.statusview.Modal;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusModel implements Parcelable {
    private int imageUri;
    private String name;
    private String time;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StatusModel(int imageUri, String name, String time, int type) {
        this.imageUri = imageUri;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public int getImageUri() {
        return imageUri;
    }

    public void setImageUri(int imageUri) {
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
        imageUri = in.readInt();
        name = in.readString();
        time = in.readString();
        type = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageUri);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeInt(type);
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

