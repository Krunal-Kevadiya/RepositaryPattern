package com.example.ownrepositarypatternsample.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ownrepositarypatternsample.data.Api

object TextViewBind {
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
}