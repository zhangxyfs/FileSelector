package com.z7dream.lib.selector.box

import android.content.Context

class SelectionPlugin private constructor() {
    private var mListener: SelectionListener? = null

    private fun init(listener: SelectionListener?) {
        mListener = listener
    }


    fun getListener() = mListener

    interface SelectionListener {
        fun applicationContext(): Context
    }

    companion object {
        val instance: SelectionPlugin by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SelectionPlugin()
        }
    }
}