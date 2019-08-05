package com.example.fixit.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FixitUser implements Parcelable {
    private String name;
    private String email;
    private String photoUrl;

    public FixitUser(){}

    public FixitUser(String name, String email, String photoUrl){
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    protected FixitUser(Parcel in) {
        name = in.readString();
        email = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<FixitUser> CREATOR = new Creator<FixitUser>() {
        @Override
        public FixitUser createFromParcel(Parcel in) {
            return new FixitUser(in);
        }

        @Override
        public FixitUser[] newArray(int size) {
            return new FixitUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(photoUrl);
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
