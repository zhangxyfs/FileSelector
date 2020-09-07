package com.z7dream.lib.selector.box.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.z7dream.lib.selector.BuildConfig

/**
 * 文件管理器基类
 */
abstract class BoxBaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnCreate()")
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

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "[lifecycle] >>>>> toOnDestroy()")
    }
}