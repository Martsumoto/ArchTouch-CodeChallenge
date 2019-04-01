package com.arctouch.codechallenge.util

import android.content.Context
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setPosterUrl(context: Context, url : String?) {
    this.setUrl(context, url, true)
}

fun ImageView.setBackdropUrl(context: Context, url : String?) {
    this.setUrl(context, url, false)
}

fun ImageView.setUrl(context: Context, rawUrl : String?, isPoster: Boolean) {
    val url = rawUrl?.let {
        if (isPoster) MovieImageUrlBuilder().buildPosterUrl(it)
        else MovieImageUrlBuilder().buildBackdropUrl(it)
    }

    Glide.with(context)
        .load(url)
        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
        .into(this)

}

