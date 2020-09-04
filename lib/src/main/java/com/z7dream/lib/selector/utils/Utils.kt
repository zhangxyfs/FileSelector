package com.z7dream.lib.selector.utils

import android.content.Context

object Utils {
    /**
     * 获取屏幕的宽度
     *
     * @param context context
     * @return int
     */
    fun getScreenWidth(context: Context): Int {
        return context.applicationContext.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param ctx context
     * @return int
     */
    fun getScreenHeight(ctx: Context): Int {
        return ctx.applicationContext.resources.displayMetrics.heightPixels
    }
}