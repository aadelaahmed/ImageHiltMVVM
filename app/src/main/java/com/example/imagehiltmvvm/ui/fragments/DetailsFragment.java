package com.example.imagehiltmvvm.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.imagehiltmvvm.R;
import com.example.imagehiltmvvm.data.UnsplashPhoto;
import com.example.imagehiltmvvm.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    FragmentDetailsBinding binding;
    UnsplashPhoto photoModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoModel = DetailsFragmentArgs.fromBundle(getArguments()).getPhoto();
        if (photoModel != null) {
            Glide.with(this)
                    .load(photoModel.getUrls().getFull())
                    .error(R.drawable.ic_error)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.imageCreator.setVisibility(View.VISIBLE);
                            if (photoModel.getDescription() != null)
                                binding.imageDescription.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(binding.imagePost);

            Uri uri = Uri.parse(photoModel.getUser().getAttributionUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            binding.imageCreator.setText("Photo By " + photoModel.getUser().getName());
            binding.imageCreator.getPaint().setUnderlineText(true);
            binding.imageCreator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fire the implicit intent
                    getContext().startActivity(intent);
                }
            });
        }
        else
            Log.d(TAG, "recieved photo model is null");
    }
}