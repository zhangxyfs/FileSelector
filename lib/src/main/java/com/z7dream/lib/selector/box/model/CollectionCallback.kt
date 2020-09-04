package com.z7dream.lib.selector.box.model

import androidx.fragment.app.FragmentActivity

interface CollectionCallback {
    fun onCreate(context: FragmentActivity, callbacks: FileAlbumCallbacks)

    fun onDestroy()

    fun load(args: ArgsBean)

    fun load()

    fun resetLoad()
}