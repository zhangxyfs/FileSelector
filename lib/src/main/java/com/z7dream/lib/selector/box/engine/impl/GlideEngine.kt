package com.eblog.base.widget.box.engine.impl

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eblog.base.widget.box.engine.ImageEngine


class GlideEngine : ImageEngine {
    override fun loadThumbnail(imageView: ImageView, path: String?, overrideWith: Int, overrideHeight: Int, holdResId: Int) {
        var requestOptions = RequestOptions().override(overrideWith, overrideHeight).centerCrop()
        if (holdResId > 0) {
            requestOptions = requestOptions.placeholder(holdResId)
        }
        Glide.with(imageView).asBitmap().load(path).apply(requestOptions).into(imageView)
    }
}