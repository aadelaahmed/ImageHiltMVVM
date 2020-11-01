package com.example.imagehiltmvvm.data;

import android.util.Log;

import androidx.paging.PagingSource;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.imagehiltmvvm.api.UnsplashApi;
import com.example.imagehiltmvvm.api.UnsplashResponse;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImagePagingSource extends RxPagingSource<Integer, UnsplashPhoto> {
    private static final String TAG = "ImagePagingSource";
    public static final int PAGE_START_INDEX = 1;
    @Inject
    UnsplashApi unsplashApi;
    String photoType;
    Integer currentPosition, prevPosition;

    public ImagePagingSource(UnsplashApi unsplashApi, String photoType) {
        this.unsplashApi = unsplashApi;
        this.photoType = photoType;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, UnsplashPhoto>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        currentPosition = (loadParams.getKey() == null) ? PAGE_START_INDEX : loadParams.getKey();
        Log.d(TAG, "loadSingle: current Position -->" + currentPosition);
        prevPosition = (currentPosition == PAGE_START_INDEX) ? null : currentPosition - 1;
        Log.d(TAG, "loadSingle: prev Position -->" + prevPosition);
        Single<UnsplashResponse> single = unsplashApi.fetchSearchPhotos(
                photoType,
                currentPosition,
                loadParams.getLoadSize()
        );
        return single
                .subscribeOn(Schedulers.io())
                .map(new Function<UnsplashResponse, LoadResult<Integer, UnsplashPhoto>>() {
                    @Override
                    public LoadResult<Integer, UnsplashPhoto> apply(UnsplashResponse unsplashResponse) throws Throwable {
                        Log.d(TAG, "apply: success with paging source");
                        return new LoadResult.Page<Integer, UnsplashPhoto>(
                                unsplashResponse.getListPhoto(),
                                prevPosition,
                                (unsplashResponse.getListPhoto().isEmpty()) ? null : currentPosition + 1,
                                LoadResult.Page.COUNT_UNDEFINED,
                                LoadResult.Page.COUNT_UNDEFINED
                        );
                    }
                })
                .onErrorReturn(new Function<Throwable, LoadResult<Integer, UnsplashPhoto>>() {
                    @Override
                    public LoadResult<Integer, UnsplashPhoto> apply(Throwable throwable) throws Throwable {
                        Log.d(TAG, "apply: error with paging source -->" + throwable.getLocalizedMessage());
                        return new LoadResult.Error<Integer, UnsplashPhoto>(throwable);
                    }
                });
    }
}
