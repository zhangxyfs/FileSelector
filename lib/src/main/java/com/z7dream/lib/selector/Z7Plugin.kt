package com.z7dream.lib.selector

import android.content.Context
import com.z7dream.lib.selector.box.entity.Item

class Z7Plugin private constructor() {
    private var mListener: SelectionListener? = null

    private fun init(listener: SelectionListener?) {
        mListener = listener
    }


    fun getListener() = mListener

    interface SelectionListener {
        fun applicationContext(): Context

        fun sendToIM(list: List<Item>)
    }

    companion object {
        val instance: Z7Plugin by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Z7Plugin()
        }
    }
}