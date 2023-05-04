package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.data.UnsplashRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @ViewModelInject constructor(
    private val repository : UnsplashRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        const val DEFAULT_QUERY = "cats"
    }

    private val _currentQuery = state.getLiveData("query", DEFAULT_QUERY)
    val currentQuery: LiveData<String>
        get() = _currentQuery

    val photos = _currentQuery.switchMap {
        repository.getSearchPhotos(it).cachedIn(viewModelScope)
    }

    private val _photoSharedFlow = MutableSharedFlow<UnsplashPhoto>()
    val photoSharedFlow = _photoSharedFlow.asSharedFlow()

    fun searchPhotos(query: String) {
        _currentQuery.value = query
    }

    fun onSearchActionSubmit(query: String) {
        searchPhotos(query)
    }

    fun onPhotoClick(photo: UnsplashPhoto) = viewModelScope.launch {
        _photoSharedFlow.emit(photo)
    }
}