package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.data.UnsplashRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @ViewModelInject constructor(
    private val repository : UnsplashRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = "cats"
    }

    private val currentQuery = MutableStateFlow(DEFAULT_QUERY)
    val photos = currentQuery.flatMapLatest {
        repository.getSearchPhotos(it).cachedIn(viewModelScope)
    }

    private val _photoSharedFlow = MutableSharedFlow<UnsplashPhoto>()
    val photoSharedFlow = _photoSharedFlow.asSharedFlow()

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    fun onSearchActionSubmit(query: String) {
        searchPhotos(query)
    }

    fun onPhotoClick(photo: UnsplashPhoto) = viewModelScope.launch {
        _photoSharedFlow.emit(photo)
    }
}