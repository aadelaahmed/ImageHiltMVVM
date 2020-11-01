package com.example.imagehiltmvvm.data;

import com.example.imagehiltmvvm.api.UnsplashApi;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;
import androidx.paging.rxjava3.PagingRx;


import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

public class ImageRepository {
    @Inject
    UnsplashApi api;

    //    @Singleton
    @Inject
    public ImageRepository(UnsplashApi api) {
        this.api = api;
    }

    public LiveData<PagingData<UnsplashPhoto>> getPagedPhotos(String photoType) {

//        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(viewModel);


        PagingConfig config = new PagingConfig(
                10
        );
        ImagePagingSource imagePagingSource = new ImagePagingSource(api, photoType);
        Pager<Integer, UnsplashPhoto> pager =
                new Pager(
                        config,
                        new Function0<PagingSource>() {
                            @Override
                            public PagingSource invoke() {
                                return imagePagingSource;
                            }
                        }
                );
        LiveData<PagingData<UnsplashPhoto>> liveData = PagingLiveData.getLiveData(pager);
        return liveData;
    }
}
