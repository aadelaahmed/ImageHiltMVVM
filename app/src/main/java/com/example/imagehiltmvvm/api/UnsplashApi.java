package com.example.imagehiltmvvm.api;

import com.example.imagehiltmvvm.api.UnsplashResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UnsplashApi {
    public static final String BASE_URL = "https://api.unsplash.com/";
    public static final String API_KEY = "g0rx8KX4UFBxNVqJyCX2Ie6eNwb1f3fiVUJ9L6cF_p8";

    @Headers({"Accept-Version: v1","Authorization: Client-ID "+API_KEY})
    @GET("search/photos")
    Single<UnsplashResponse> fetchSearchPhotos(
            @Query("query") String query,
            @Query("page") Integer page,
            @Query("per_page") Integer perPage
    );
}
