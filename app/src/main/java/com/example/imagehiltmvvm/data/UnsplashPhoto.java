package com.example.imagehiltmvvm.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UnsplashPhoto implements Parcelable {

    private String id;
    private String description;
    private UnsplashUrl urls;
    private UnsplashUser user;

    public UnsplashPhoto(String id, String description, UnsplashUrl urls, UnsplashUser user) {
        this.id = id;
        this.description = description;
        this.urls = urls;
        this.user = user;
    }

    protected UnsplashPhoto(Parcel in) {
        id = in.readString();
        description = in.readString();
    }

    public static final Creator<UnsplashPhoto> CREATOR = new Creator<UnsplashPhoto>() {
        @Override
        public UnsplashPhoto createFromParcel(Parcel in) {
            return new UnsplashPhoto(in);
        }

        @Override
        public UnsplashPhoto[] newArray(int size) {
            return new UnsplashPhoto[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnsplashUrl getUrls() {
        return urls;
    }

    public void setUrls(UnsplashUrl urls) {
        this.urls = urls;
    }

    public UnsplashUser getUser() {
        return user;
    }

    public void setUser(UnsplashUser user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
    }


    //    @SerializedName("total")
//    @Expose
//    private Integer total;
//    @SerializedName("total_pages")
//    @Expose
//    private Integer totalPages;
//    @SerializedName("results")
//    @Expose
//    private List<Result> results = null;

//    public Integer getTotal() {
//        return total;
//    }
//
//    public void setTotal(Integer total) {
//        this.total = total;
//    }
//
//    public Integer getTotalPages() {
//        return totalPages;
//    }
//
//    public void setTotalPages(Integer totalPages) {
//        this.totalPages = totalPages;
//    }

//    public List<Result> getResults() {
//        return results;
//    }
//
//    public void setResults(List<Result> results) {
//        this.results = results;
//    }

}