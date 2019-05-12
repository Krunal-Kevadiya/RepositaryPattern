package com.example.ownrepositarypatternsample.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.data.Api
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

object TextViewBind {
    @JvmStatic
    @BindingAdapter("image_url")
    fun setImageViewToLoad(imageView: ImageView, url: String?) {
        url?.let {
            val context = imageView.context
            Glide.with(context)
                .load(Api.getPosterPath(it))
                .listener(GlidePalette.with(Api.getPosterPath(it))
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(imageView)
                    .crossfade(true)
                ).into(imageView)
        }
    }
}