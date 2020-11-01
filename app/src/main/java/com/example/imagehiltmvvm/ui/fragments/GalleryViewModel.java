package com.example.imagehiltmvvm.ui.fragments;

import android.view.animation.Transformation;

import androidx.arch.core.util.Function;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.example.imagehiltmvvm.data.ImageRepository;
import com.example.imagehiltmvvm.data.UnsplashPhoto;

import javax.inject.Inject;


public class GalleryViewModel extends ViewModel {
    public static final String DEFAULT_QUERY = "messi";
    public static final String STATE_KEY = "STATE_KEY";
    MutableLiveData<String> queryLiveData;
    @Inject
    ImageRepository repository;
    @Inject
    SavedStateHandle savedStateHandle;
    //MutableLiveData queryLiveData  = new MutableLiveData(DEFAULT_QUERY);
    @ViewModelInject
    public GalleryViewModel(ImageRepository repository,@Assisted SavedStateHandle savedStateHandle) {
        this.repository = repository;
        this.savedStateHandle =savedStateHandle;
    }

//    Observable<PagingData<UnsplashPhoto>> livePhotos =
//            Transformations.switchMap(mutablePhotoType, new Function<String, LiveData<Object>>() {
//                @Override
//                public LiveData<Object> apply(String input) {
//                    return null;
//                }
//            })

    public LiveData<PagingData<UnsplashPhoto>> fetchLivePhotos() {
         queryLiveData = savedStateHandle.getLiveData(STATE_KEY,DEFAULT_QUERY);
        return Transformations.switchMap(queryLiveData,
                input -> repository.getPagedPhotos(input)
                );
        //return repository.getPagedPhotos(photoType);
    }

    public void setNewSearchQuery(String query)
    {
        queryLiveData.setValue(query);
    }
}
