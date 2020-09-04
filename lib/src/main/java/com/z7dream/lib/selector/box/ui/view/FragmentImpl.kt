package com.eblog.base.widget.box.ui.view

import android.app.Activity
import android.content.Context

interface FragmentImpl {
    fun searchByKeyword(key: String?)

    fun getStarMap(): HashMap<String, Int>

    fun refreshMediaGrid()

    fun openCheck(isOpenCheck: Boolean)

    fun selectAll()

    fun clearSelectAll(isNeedNotify: Boolean)

    fun removeFiles()

    fun resetSelectItems(): Boolean

    fun forwardItemsToChat(activity: Activity)

    fun forwardItems(activity: Activity)

    fun rename(context: Context)

    fun starFile(isStar: Boolean)

    fun share()

    fun isFragmentTypeSearch(): Boolean

    fun toBackParentFolder(): Boolean

    fun getCursorCount(): Int


}