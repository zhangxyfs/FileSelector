package com.z7dream.lib.selector.box.model

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.eblog.base.widget.box.loader.StarDataLoader
import java.lang.ref.WeakReference

class StarCollection : LoaderManager.LoaderCallbacks<Cursor>, CollectionCallback {
    companion object {
        const val LOADER_ID = 3
        const val ARGS_SEARCH = "args_search"
        const val ARGS_USER_ID = "args_user_id"
        const val ARGS_COMPANY_ID = "args_company_id"
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

    }

    override fun load(args: ArgsBean) {
        val isFrist = mBundle == null
        mBundle = Bundle()
        args.searchKey?.apply {
            mBundle?.putString(ARGS_SEARCH, this)
        }
        mBundle?.putLong(ARGS_USER_ID, args.userId)
        mBundle?.putLong(ARGS_COMPANY_ID, args.companyId)

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
        val userId = args?.getLong(ARGS_USER_ID)
        val companyId = args?.getLong(ARGS_COMPANY_ID)
        val searchValue = args?.getString(ARGS_SEARCH)
        return StarDataLoader.newInstance(context?.get()!!, userId, companyId, searchValue)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (context?.get() == null || data == null) return
        callbacks?.onAlbumLoad(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        callbacks?.onAlbumReset()
    }
}