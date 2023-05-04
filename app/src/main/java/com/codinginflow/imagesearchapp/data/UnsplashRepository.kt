package com.codinginflow.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.codinginflow.imagesearchapp.api.UnsplashApi
import org.intellij.lang.annotations.Flow
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val unsplashApi: UnsplashApi
) {
    fun getSearchPhotos(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData
}
