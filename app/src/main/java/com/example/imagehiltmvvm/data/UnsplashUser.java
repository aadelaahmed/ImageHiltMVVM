package com.example.imagehiltmvvm.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UnsplashUser implements Parcelable {
    private String name;
    private String username;
    //private String attrributionUrl;

    public UnsplashUser(String name, String username) {
        this.name = name;
        this.username = username;
        //this.attrributionUrl = "http://unsplash.com/"+this.username+"?utm_source=ImageHiltMVVM&utm_medium=referral";
    }


    protected UnsplashUser(Parcel in) {
        name = in.readString();
        username = in.readString();
    }

    public static final Creator<UnsplashUser> CREATOR = new Creator<UnsplashUser>() {
        @Override
        public UnsplashUser createFromParcel(Parcel in) {
            return new UnsplashUser(in);
        }

        @Override
        public UnsplashUser[] newArray(int size) {
            return new UnsplashUser[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getAttrributionUrl() {
//        return attrributionUrl;
//    }
//
//    public void setAttrributionUrl(String attrributionUrl) {
//        this.attrributionUrl = attrributionUrl;
//    }

   public String getAttributionUrl()
   {
       return "http://unsplash.com/"+this.username+"?utm_source=ImageHiltMVVM&utm_medium=referral";
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
    }
}
