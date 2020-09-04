package com.eblog.base.widget.box.loader

import android.content.Context
import android.database.Cursor
import androidx.core.os.CancellationSignal
import androidx.core.os.OperationCanceledException
import androidx.loader.content.AsyncTaskLoader
import com.z7dream.lib.selector.room.manager.file.FileStarManager
import java.io.FileDescriptor
import java.io.PrintWriter

class StarDataLoader(context: Context, var userId: Long?, var companyId: Long?, var searchValue: String) : AsyncTaskLoader<Cursor>(context) {
    var mObserver: ForceLoadContentObserver? = null
    var mCursor: Cursor? = null
    var mCancellationSignal: CancellationSignal? = null

    init {
        mObserver = ForceLoadContentObserver()
    }


    override fun loadInBackground(): Cursor? {
        synchronized(this) {
            if (isLoadInBackgroundCanceled) {
                throw OperationCanceledException()
            }
            mCancellationSignal = CancellationSignal()
        }
        return try {
            val cursor = FileStarManager.instance.findCursorStarList(userId, companyId, searchValue)
            if (cursor != null) {
                try { // Ensure the cursor window is filled.
                    cursor.count
                    cursor.registerContentObserver(mObserver)
                } catch (ex: RuntimeException) {
                    cursor.close()
                    throw ex
                }
            }
            cursor
        } finally {
            synchronized(this) { mCancellationSignal = null }
        }
    }

    override fun cancelLoadInBackground() {
        super.cancelLoadInBackground()
        synchronized(this) {
            if (mCancellationSignal != null) {
                mCancellationSignal!!.cancel()
            }
        }
    }

    /* Runs on the UI thread */
    override fun deliverResult(cursor: Cursor?) {
        if (isReset) { // An async query came in while the loader is stopped
            cursor?.close()
            return
        }
        val oldCursor = mCursor
        mCursor = cursor
        if (isStarted) {
            super.deliverResult(cursor)
        }
        if (oldCursor != null && oldCursor !== cursor && !oldCursor.isClosed) {
            oldCursor.close()
        }
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     *
     * Must be called from the UI thread
     */
    override fun onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor)
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad()
        }
    }

    /**
     * Must be called from the UI thread
     */
    override fun onStopLoading() { // Attempt to cancel the current load task if possible.
        cancelLoad()
    }

    override fun onCanceled(cursor: Cursor?) {
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
    }

    override fun onReset() {
        super.onReset()
        // Ensure the loader is stopped
        onStopLoading()
        if (mCursor != null && !mCursor!!.isClosed) {
            mCursor!!.close()
        }
        mCursor = null
    }

    //var userId: Long?, var companyId: Long?, var searchValue: String

    @Deprecated("")
    override fun dump(prefix: String?, fd: FileDescriptor?, writer: PrintWriter, args: Array<String?>?) {
        super.dump(prefix, fd, writer, args)
        writer.print(prefix)
        writer.print("userId=")
        writer.println(userId)
        writer.print(prefix)
        writer.print("companyId=")
        writer.println(companyId)
        writer.print(prefix)
        writer.print("searchValue=")
        writer.println(searchValue)
    }

    companion object {
        fun newInstance(context: Context, userId: Long?, companyId: Long?, searchValue: String?) =
                StarDataLoader(context, userId, companyId, searchValue ?: "")
    }
}