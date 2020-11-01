package com.example.imagehiltmvvm.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.imagehiltmvvm.R;
import com.example.imagehiltmvvm.adapters.ImageLoadStateAdapter;
import com.example.imagehiltmvvm.adapters.ImageSearchAdapter;
import com.example.imagehiltmvvm.data.UnsplashPhoto;
import com.example.imagehiltmvvm.databinding.FragmentGalleryBinding;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@AndroidEntryPoint
public class GalleryFragment extends Fragment implements ImageLoadStateAdapter.RetryOnClickListener, ImageSearchAdapter.OnItemClickListener {
    private static final String TAG = "GalleryFragment";
    FragmentGalleryBinding binding;
    ImageSearchAdapter adapter;
    RecyclerView imageRV;
    LiveData<PagingData<UnsplashPhoto>> livePhotos;
    //Flowable<PagingData<UnsplashPhoto>> observable;
    GalleryViewModel viewModel;
    NavController navController;

    public GalleryFragment() {
    }


    @Override
    public void retryOnclick() {
        adapter.retry();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.fragment_gallery, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new ImageSearchAdapter(this);
        imageRV = binding.unsplashImageList;
        //observable = viewModel.fetchLivePhotos("cats");
        imageRV.setHasFixedSize(true);
        imageRV.setLayoutManager(new GridLayoutManager(getActivity(),2));
        imageRV.setAdapter(adapter);
        setLoadStateListener();
        setHeaderAndFooter();
        fetchImageData();
        binding.retrySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.retry();
            }
        });
        navController = Navigation.findNavController(view);
    }

    private void setHeaderAndFooter() {
        adapter.withLoadStateHeaderAndFooter(
                new ImageLoadStateAdapter(this),
                new ImageLoadStateAdapter(this)
        );
    }

    private void setLoadStateListener() {
        adapter.addLoadStateListener(loadstate -> {
            if (loadstate.getSource().getRefresh() instanceof LoadState.Loading) {
                binding.progressNewSearch.setVisibility(View.VISIBLE);
                binding.errorTv.setVisibility(View.GONE);
                binding.emptyTv.setVisibility(View.GONE);
                binding.retrySearchBtn.setVisibility(View.GONE);

            } else if (loadstate.getSource().getRefresh() instanceof LoadState.NotLoading) {
                binding.unsplashImageList.setVisibility(View.VISIBLE);
                binding.errorTv.setVisibility(View.GONE);
                binding.emptyTv.setVisibility(View.GONE);
                binding.retrySearchBtn.setVisibility(View.GONE);
                binding.progressNewSearch.setVisibility(View.GONE);
            } else {
                binding.progressNewSearch.setVisibility(View.GONE);
                //in case of error state
                binding.emptyTv.setVisibility(View.GONE);
                binding.errorTv.setVisibility(View.VISIBLE);
                binding.retrySearchBtn.setVisibility(View.VISIBLE);
            }
            if (loadstate.getSource().getRefresh() instanceof LoadState.NotLoading
                    && loadstate.getAppend().getEndOfPaginationReached()
                    && adapter.getItemCount() < 1) {
                binding.emptyTv.setVisibility(View.VISIBLE);
                binding.errorTv.setVisibility(View.GONE);
                binding.retrySearchBtn.setVisibility(View.GONE);
            }
            return Unit.INSTANCE;
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    viewModel.setNewSearchQuery(query);
                    fetchImageData();
                    binding.unsplashImageList.scrollToPosition(0);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchImageData() {
        livePhotos = viewModel.fetchLivePhotos();
        livePhotos.observe(getActivity(), new androidx.lifecycle.Observer<PagingData<UnsplashPhoto>>() {
            @Override
            public void onChanged(PagingData<UnsplashPhoto> unsplashPhotoPagingData) {
                adapter.submitData(getViewLifecycleOwner().getLifecycle(), unsplashPhotoPagingData);
            }
        });
    }


    @Override
    public void onItemClick(UnsplashPhoto photo) {
        GalleryFragmentDirections.ActionGalleryFragmentToDetailsFragment action =
                GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo);
        navController.navigate(action);
    }
}