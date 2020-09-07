package com.z7dream.lib.selector.box.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.z7dream.lib.selector.BuildConfig

/**
 * 文件管理器基类
 */
abstract class BoxBaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnAttach()")
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnCreateView() viewID=${layoutID()}")
        return inflater.inflate(layoutID(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnActivityCreated()")
    }

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnStart()")
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnResume()")
    }

    override fun onPause() {
        super.onPause()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnPause()")
    }

    override fun onStop() {
        super.onStop()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnDestroyView()")
    }

    abstract fun layoutID(): Int

}