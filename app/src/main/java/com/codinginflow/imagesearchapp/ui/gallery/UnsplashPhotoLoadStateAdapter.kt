package com.codinginflow.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.imagesearchapp.databinding.UnsplashPhotoLoadStateBinding

class UnsplashPhotoLoadStateAdapter(private val onRetryClick: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter.PhotoLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotoLoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class PhotoLoadStateViewHolder(private val binding: UnsplashPhotoLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                onRetryClick()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                textViewError.isVisible = loadState is LoadState.Error
                buttonRetry.isVisible = loadState is LoadState.Error
            }
        }

    }
}