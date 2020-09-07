package com.z7dream.lib.selector.box.ui

import android.os.Bundle
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.model.FolderCollection
import com.z7dream.lib.selector.utils.CacheManager
import com.z7dream.lib.selector.utils.FileUtils
import java.io.File

/**
 * 文件夹 fragment
 */
class FolderSelectionFragment : SelectionBaseFragment() {
    private var albumMediaCollection = FolderCollection()
    private var mParentPath: String = ""//这个值其实是用于android 29的, 暂时保留
    private var mRootPath: String = ""
    private var mSearchKey: String = ""

    companion object {
        const val FOLDER_FRAGMENT_LIST = "FolderListSelectionFragment"
        const val FOLDER_FRAGMENT_SEARCH = "FolderSearchSelectionFragment"

        private val IS_SEARCH = "isSearch"
        private val PARENT_PATH = "parentPath"

        fun newInstance(parentPath: String, isSearch: Boolean): FolderSelectionFragment {
            val fragment = FolderSelectionFragment()
            val args = Bundle()
            args.putString(PARENT_PATH, parentPath)
            args.putBoolean(IS_SEARCH, isSearch)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initArguments() {
        setIsSearch(arguments?.getBoolean(IS_SEARCH, false) ?: false)
        mParentPath = arguments?.getString(PARENT_PATH) ?: ""
        mRootPath = mParentPath
    }

    override fun collectionCreate() {
        albumMediaCollection.onCreate(activity!!, this)
    }

    override fun collectionFirstLoad() {
        albumMediaCollection.load(buildArgs(null, mParentPath))
    }

    override fun collectionResetLoad() {
        albumMediaCollection.resetLoad()
    }

    override fun collectionDestory() {
        albumMediaCollection.onDestroy()
    }

    /**
     * 搜索
     */
    override fun searchByKeyword(key: String?) {
        if (key.isNullOrEmpty()) {
            return
        }
        mSearchKey = key
        getAdapter().setSearchKey(mSearchKey)
        albumMediaCollection.load(buildArgs(mSearchKey, mRootPath))
    }

    override fun onMediaClick(item: Item?, adapterPosition: Int, isFolder: Boolean) {
        if (isFolder) {
            item?.apply {
                val relativeParentPath = CacheManager.getSaveFilePath() + File.separator
                mParentPath = item.filePath.replace(relativeParentPath, "") + File.separator
                albumMediaCollection.load(buildArgs(mSearchKey, mParentPath))
            }
        } else {
            getOnMediaClickListener().onMediaClick(item, adapterPosition, false)
        }
    }

    override fun toBackParentFolder(): Boolean {
        if (mParentPath == mRootPath) {
            return false
        }
        val oldPath = mParentPath
        mParentPath = FileUtils.getParentFolder(mParentPath)
        albumMediaCollection.load(buildArgs(mSearchKey, mParentPath))
        return mParentPath != oldPath
    }
}