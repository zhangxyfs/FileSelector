package com.z7dream.lib.selector.box.ui

import android.os.Bundle
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.model.StarCollection

/**
 * 星标 fragment
 */
class StarSelectionFrament : SelectionBaseFragment() {
    private val albumMediaCollection = StarCollection()

    companion object {
        const val STAR_FRAGMENT_LIST = "StarListSelectionFragment"
        const val STAR_FRAGMENT_SEARCH = "StarSearchSelectionFragment"

        private val IS_SEARCH = "isSearch"

        fun newInstance(isSearch: Boolean): StarSelectionFrament {
            val fragment = StarSelectionFrament()
            val args = Bundle()
            args.putBoolean(IS_SEARCH, isSearch)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initArguments() {
        setIsSearch(arguments?.getBoolean(IS_SEARCH, false) ?: false)
    }

    override fun collectionCreate() {
        albumMediaCollection.onCreate(activity!!, this)
    }

    override fun collectionFirstLoad() {
        albumMediaCollection.load(buildArgs(null, null))
    }

    override fun collectionResetLoad() {
        albumMediaCollection.resetLoad()
    }

    override fun collectionDestory() {
        albumMediaCollection.onDestroy()
    }

    override fun onMediaClick(item: Item?, adapterPosition: Int, isFolder: Boolean) {
        getOnMediaClickListener().onMediaClick(item, adapterPosition, isFolder)
    }

    override fun searchByKeyword(key: String?) {
        if (key.isNullOrEmpty()) {
            return
        }
        getAdapter().setSearchKey(key)
        albumMediaCollection.load(buildArgs(key, null))
    }

    override fun toBackParentFolder(): Boolean = false
}