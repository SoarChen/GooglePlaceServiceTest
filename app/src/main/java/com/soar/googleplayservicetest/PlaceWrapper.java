package com.soar.googleplayservicetest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016/1/4.
 */
public class PlaceWrapper implements Parcelable {
    private String mName = null;
    private String mAddress = null;
    private String mPhoneNumber = null;

    public PlaceWrapper() {

    }

    protected PlaceWrapper(Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mPhoneNumber = in.readString();
    }

    public static final Creator<PlaceWrapper> CREATOR = new Creator<PlaceWrapper>() {
        @Override
        public PlaceWrapper createFromParcel(Parcel in) {
            return new PlaceWrapper(in);
        }

        @Override
        public PlaceWrapper[] newArray(int size) {
            return new PlaceWrapper[size];
        }
    };

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String location) {
        mAddress = location;
    }

    public void setPhoneNumber(String phone) {
        mPhoneNumber = phone;
    }


    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getAddress() {
        return mAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeString(mPhoneNumber);
    }
}
