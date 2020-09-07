package com.z7dream.lib.selector

import android.content.Context
import com.eblog.base.widget.box.engine.ImageEngine
import com.z7dream.lib.selector.box.entity.Item

class Z7Plugin private constructor() {
    private var mListener: SelectionListener? = null

    fun init(listener: SelectionListener?) {
        mListener = listener
    }


    fun getListener() = mListener

    interface SelectionListener {
        fun applicationContext(): Context

        fun sendToIM(list: List<Item>)

        fun share(context: Context?, filePath: String?)

        fun getImageEngine() : ImageEngine?
    }

    companion object {
        val instance: Z7Plugin by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Z7Plugin()
        }
    }
}