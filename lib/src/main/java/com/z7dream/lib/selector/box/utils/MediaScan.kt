package com.eblog.base.widget.box.utils

import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import com.eblog.base.Appli
import java.util.*

class MediaScan private constructor() {
    private val mStack = Stack<String>()

    init {
        mClient = object : MediaScannerConnection.MediaScannerConnectionClient {
            override fun onMediaScannerConnected() {
                scanFile()
            }

            override fun onScanCompleted(path: String?, uri: Uri?) {
                path?.apply {
                    Log.e(MediaScan::class.java.simpleName, path)
                }
                uri?.apply {
                    Log.e(MediaScan::class.java.simpleName, uri.toString())
                }
                scanFile()
            }
        }

        mConnection = MediaScannerConnection(Appli.getContext(), mClient)
    }

    private fun scanFile() {
        if (mStack.empty()) {
            mConnection.disconnect()
        } else {
            mConnection.scanFile(mStack.pop(), null)
        }
    }

    fun scan(filePath: String) {
        mStack.add(filePath)
        if (!mConnection.isConnected) {
            mConnection.connect()
        }
    }

    companion object {
        private lateinit var mConnection: MediaScannerConnection
        private lateinit var mClient: MediaScannerConnection.MediaScannerConnectionClient


        val instance: MediaScan by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MediaScan()
        }
    }
}