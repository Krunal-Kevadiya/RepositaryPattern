package com.example.ownrepositarypatternsample.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ownrepositarypatternsample.data.Api

object TextViewBind {
    /*@JvmStatic
    @BindingAdapter("movie_url")
    fun setImageViewToLoadMovie(imageView: ImageView, url: String?) {
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
    }*/

    @JvmStatic
    @BindingAdapter("person_url")
    fun setImageViewToLoadPerson(imageView: ImageView, url: String?) {
        url?.let {
            val context = imageView.context
            Glide.with(context)
                .load(Api.getPosterPath(it))
                .apply(RequestOptions().circleCrop())
                .into(imageView)
        }
    }

    /*@JvmStatic
    @BindingAdapter("tv_url")
    fun setImageViewToLoadTv(imageView: ImageView, url: String?) {
        url?.let {
            val context = imageView.context
            Glide.with(context)
                .load(Api.getPosterPath(it))
                .listener(GlidePalette.with(Api.getPosterPath(it))
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(imageView)
                    .crossfade(true))
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("video_url")
    fun setImageViewToLoadVideo(imageView: ImageView, url: String?) {
        url?.let {
            val context = imageView.context
            Glide.with(context)
                .load(Api.getYoutubeThumbnailPath(url))
                .listener(GlidePalette.with(Api.getYoutubeThumbnailPath(url))
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(imageView)
                    .crossfade(true))
                .into(imageView)
        }
    }*/
}