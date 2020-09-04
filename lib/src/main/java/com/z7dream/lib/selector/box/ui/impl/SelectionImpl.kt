package com.z7dream.lib.selector.box.ui.impl

import com.z7dream.lib.selector.box.entity.Item


interface SelectionImpl {
    fun checkSelectPicNum(): Boolean

    fun checkSelectFileNum(): Boolean

    fun checkSelctAllNum():Boolean

    fun createAlreadySelectMap(): HashMap<String, Item>
}