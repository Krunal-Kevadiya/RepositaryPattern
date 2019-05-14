package com.example.ownrepositarypatternsample.utils.extension

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kotlinlibrary.utils.ktx.circularRevealedAtCenter

fun Activity.requestGlideListener(view: View): RequestListener<Drawable> {
    return object: RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            this@requestGlideListener.circularRevealedAtCenter(view)
            return false
        }
    }
}
