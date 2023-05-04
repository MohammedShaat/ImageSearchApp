package com.codinginflow.imagesearchapp.util

import androidx.appcompat.widget.SearchView

fun SearchView.onSubmit(block: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            block(query ?: "")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}