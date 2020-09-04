package com.z7dream.lib.selector.box.ui

import android.os.Bundle
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.model.ArgsBean
import com.z7dream.lib.selector.box.model.FileAlbumCollection

/**
 * 按类型显示 fragment
 */
class FileDatabaseSelectionFragment : SelectionBaseFragment() {
    private var albumMediaCollection = FileAlbumCollection()

    companion object {
        const val MEDIA_FRAGMENT_LIST = "FileDatabaseSelectionFragment"
        const val MEDIA_FRAGMENT_SEARCH = "FileSearchSelectionFragment"

        private val IS_SEARCH = "isSearch"

        fun newInstance(isSearch: Boolean): FileDatabaseSelectionFragment {
            val fragment = FileDatabaseSelectionFragment()
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
        val args = ArgsBean("", SelectionSpec.getInstance().userId,
                SelectionSpec.getInstance().companyId)
        albumMediaCollection.load(args)
    }

    override fun collectionResetLoad() {
        albumMediaCollection.resetLoad()
    }

    override fun collectionDestory() {
        albumMediaCollection.onDestroy()
    }

    override fun searchByKeyword(key: String?) {
        if (key.isNullOrEmpty()) {
            return
        }
        getAdapter().setSearchKey(key)
        albumMediaCollection.load(buildArgs(key, null))
    }


    override fun onMediaClick(item: Item?, adapterPosition: Int, isFolder: Boolean) {
        getOnMediaClickListener().onMediaClick(item, adapterPosition, isFolder)
    }

    override fun toBackParentFolder(): Boolean = false
}