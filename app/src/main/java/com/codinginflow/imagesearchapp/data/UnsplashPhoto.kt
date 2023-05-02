package com.codinginflow.imagesearchapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser,
) : Parcelable {
    @Parcelize
    class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    class UnsplashUser(
        val username: String,
        val name: String,
        val x: String
    ) : Parcelable {
        val attributionUrl: String
            get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }
}