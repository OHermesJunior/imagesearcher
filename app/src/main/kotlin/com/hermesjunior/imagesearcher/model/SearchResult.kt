package com.hermesjunior.imagesearcher.model

import android.graphics.drawable.Drawable

data class SearchResult(
    val engineTitle: String,
    val searchUrl: String,
    val engineIcon: Drawable
)
