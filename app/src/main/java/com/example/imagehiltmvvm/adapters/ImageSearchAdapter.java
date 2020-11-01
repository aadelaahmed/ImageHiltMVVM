package com.example.imagehiltmvvm.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.imagehiltmvvm.R;
import com.example.imagehiltmvvm.data.UnsplashPhoto;
import com.example.imagehiltmvvm.databinding.UnsplashItemBinding;

public class ImageSearchAdapter extends PagingDataAdapter<UnsplashPhoto, ImageSearchAdapter.UnsplashViewHolder> {
    UnsplashItemBinding binding;
    OnItemClickListener listener;
    public static DiffUtil.ItemCallback<UnsplashPhoto> diffCallback = new DiffUtil.ItemCallback<UnsplashPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull UnsplashPhoto oldItem, @NonNull UnsplashPhoto newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull UnsplashPhoto oldItem, @NonNull UnsplashPhoto newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ImageSearchAdapter(OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(UnsplashPhoto photo);
    }

    @NonNull
    @Override
    public UnsplashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unsplash_item,parent,false);
        binding = UnsplashItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UnsplashViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UnsplashViewHolder holder, int position) {
        UnsplashPhoto model = getItem(position);
        if (model != null)
            holder.bind(model);
    }

    public class UnsplashViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UnsplashItemBinding binding;

        public UnsplashViewHolder(UnsplashItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(UnsplashPhoto model) {
            Glide.with(binding.getRoot())
                    .load(model.getUrls().getRegular())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(binding.imageItem);
            binding.usernameItemTv.setText(model.getUser().getUsername());
        }

        @Override
        public void onClick(View v) {
            if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                UnsplashPhoto photo = getItem(getBindingAdapterPosition());
                if (photo != null)
                    listener.onItemClick(photo);
            }

        }
    }
}
