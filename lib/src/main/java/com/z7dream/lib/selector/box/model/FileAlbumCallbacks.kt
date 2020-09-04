package com.z7dream.lib.selector.box.model

import android.database.Cursor

interface FileAlbumCallbacks {
    fun onAlbumStart()
    fun onAlbumLoad(cursor: Cursor)
    fun onAlbumReset()
}