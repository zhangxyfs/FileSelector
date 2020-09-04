package com.eblog.base.widget.box.engine

import android.widget.ImageView

interface ImageEngine {

    fun loadThumbnail(imageView: ImageView, path: String?, overrideWith: Int, overrideHeight: Int, holdResId: Int)
}