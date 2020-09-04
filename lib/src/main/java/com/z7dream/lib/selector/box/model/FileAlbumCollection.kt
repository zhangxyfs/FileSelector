package com.z7dream.lib.selector.box.model

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.z7dream.lib.selector.box.loader.FileDataLoader
import java.lang.ref.WeakReference

class FileAlbumCollection : LoaderManager.LoaderCallbacks<Cursor>, CollectionCallback {
    companion object {
        const val LOADER_ID = 2
        const val ARGS_SEARCH = "args_search"
        const val ARGS_COMID = "args_comId"
    }

    private var context: WeakReference<Context>? = null
    private var loaderManager: LoaderManager? = null
    private var callbacks: FileAlbumCallbacks? = null
    private var mBundle: Bundle? = null

    override fun onCreate(context: FragmentActivity, callbacks: FileAlbumCallbacks) {
        this.context = WeakReference(context)
        loaderManager = LoaderManager.getInstance(context)
        this.callbacks = callbacks
    }

    override fun onDestroy() {
        loaderManager?.destroyLoader(LOADER_ID)
        if (callbacks != null) callbacks = null
    }

    override fun load() {
        loaderManager?.initLoader(LOADER_ID, null, this)
    }

    override fun load(args: ArgsBean) {
        val isFrist = mBundle == null

        mBundle = Bundle()
        mBundle?.putString(ARGS_SEARCH, args.searchKey)
        mBundle?.putLong(ARGS_COMID, args.companyId)
        if (isFrist) {
            loaderManager?.initLoader(LOADER_ID, mBundle, this)
        } else {
            loaderManager?.restartLoader(LOADER_ID, mBundle, this)
        }
    }

    override fun resetLoad() {
        loaderManager?.restartLoader(LOADER_ID, mBundle, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return FileDataLoader.newInstance(context?.get()!!, args?.getString(ARGS_SEARCH), args?.getLong(
            ARGS_COMID
        ))
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (context?.get() == null || data == null) return
        callbacks?.onAlbumLoad(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        callbacks?.onAlbumReset()
    }
}