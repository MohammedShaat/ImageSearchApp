package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import com.codinginflow.imagesearchapp.util.onSubmit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

private const val TAG = "GalleryFragment"

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel: GalleryViewModel by viewModels()

    private var _binding: FragmentGalleryBinding? = null
    private val binding: FragmentGalleryBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)
        val photoAdapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerViewImagesList.apply {
                setHasFixedSize(true)
                adapter = photoAdapter.withLoadStateHeaderAndFooter(
                    header = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() },
                    footer = UnsplashPhotoLoadStateAdapter { photoAdapter.retry() }
                )
            }

            buttonRetry.setOnClickListener {
                photoAdapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) { photoPagingData ->
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, photoPagingData)
        }

        photoAdapter.addLoadStateListener { loadState ->
            binding.apply {
                val refresh = loadState.source.refresh
                recyclerViewImagesList.isVisible = refresh is LoadState.NotLoading
                progressBar.isVisible = refresh is LoadState.Loading
                textViewError.isVisible = refresh is LoadState.Error
                buttonRetry.isVisible = refresh is LoadState.Error
                textViewEmpty.isVisible = refresh is LoadState.NotLoading &&
                        photoAdapter.itemCount < 1 &&
                        loadState.append.endOfPaginationReached
            }
        }

        onCollect(viewModel.photoSharedFlow) { photo ->
            val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_gallery, menu)

        val itemSearch = menu.findItem(R.id.action_search)
        val searchView = itemSearch.actionView as SearchView

        searchView.onSubmit { query ->
            viewModel.onSearchActionSubmit(query)
            searchView.clearFocus()
            binding.recyclerViewImagesList.scrollToPosition(0)
        }
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        viewModel.onPhotoClick(photo)
    }

}

fun <T> Fragment.onCollect(flow: Flow<T>, block: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        flow.collect(block)
    }
}