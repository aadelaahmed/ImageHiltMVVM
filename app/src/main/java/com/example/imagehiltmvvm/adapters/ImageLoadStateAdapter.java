package com.example.imagehiltmvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagehiltmvvm.R;
import com.example.imagehiltmvvm.databinding.LoadStateFooterBinding;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ImageLoadStateAdapter extends LoadStateAdapter<ImageLoadStateAdapter.ImageLoadStateViewHolder> {
    LoadStateFooterBinding binding;
    RetryOnClickListener listener;
    public ImageLoadStateAdapter( RetryOnClickListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NotNull ImageLoadStateViewHolder imageLoadStateViewHolder, @NotNull LoadState loadState) {
        imageLoadStateViewHolder.bind(loadState);
    }

    @NotNull
    @Override
    public ImageLoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, @NotNull LoadState loadState) {
        binding = LoadStateFooterBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);
        return new ImageLoadStateViewHolder(binding);
    }

    public interface RetryOnClickListener{
        void retryOnclick();
    }

    public class ImageLoadStateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        LoadStateFooterBinding binding;
        public ImageLoadStateViewHolder(LoadStateFooterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.retryBtn.setOnClickListener(this);
        }
        public void bind(LoadState loadState)
        {
            if (loadState instanceof LoadState.Error)
            {
                binding.errorTv.setVisibility(View.VISIBLE);
                binding.retryBtn.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
//                binding.progressBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.errorTv.setVisibility(View.GONE);
                binding.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            listener.retryOnclick();
        }
    }
}
