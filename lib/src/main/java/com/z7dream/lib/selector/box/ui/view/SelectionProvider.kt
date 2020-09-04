package com.z7dream.lib.selector.box.ui.view

import com.z7dream.lib.selector.box.model.SelectedItemCollection

interface SelectionProvider {
    fun provideSelectedItemCollection(): SelectedItemCollection

    fun loadDataSucc(hasData: Boolean)
}