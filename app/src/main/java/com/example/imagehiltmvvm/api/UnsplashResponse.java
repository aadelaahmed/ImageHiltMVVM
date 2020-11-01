package com.example.imagehiltmvvm.api;

import com.example.imagehiltmvvm.data.UnsplashPhoto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnsplashResponse {
    @SerializedName("results")
    private List<UnsplashPhoto> listPhoto;

    public UnsplashResponse(List<UnsplashPhoto> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public List<UnsplashPhoto> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(List<UnsplashPhoto> listPhoto) {
        this.listPhoto = listPhoto;
    }
}
