package com.z7dream.lib.selector.box.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eblog.base.widget.box.ui.adapter.FileAlbumAdapter
import com.z7dream.lib.selector.box.ui.impl.SelectionImpl
import com.eblog.base.widget.box.ui.view.FragmentImpl
import com.z7dream.lib.selector.box.ui.view.SelectionProvider
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.Z7Plugin
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.model.ArgsBean
import com.z7dream.lib.selector.box.model.FileAlbumCallbacks
import com.z7dream.lib.selector.box.utils.MediaGridInset
import com.z7dream.lib.selector.room.manager.file.FileStarManager
import com.z7dream.lib.selector.utils.FileType
import com.z7dream.lib.selector.utils.SizeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 文件管理器--文件选择基类
 */
abstract class SelectionBaseFragment : BoxBaseFragment(), FileAlbumCallbacks, FileAlbumAdapter.CheckStateListener,
        FileAlbumAdapter.OnMediaClickListener, FragmentImpl, SelectionImpl {
    private var recyclerview: RecyclerView? = null
    private lateinit var adapter: FileAlbumAdapter
    private lateinit var selectionProvider: SelectionProvider
    private lateinit var checkStateListener: FileAlbumAdapter.CheckStateListener
    private lateinit var onMediaClickListener: FileAlbumAdapter.OnMediaClickListener
    private var isSearch: Boolean = false
    private var mArgs = ArgsBean(null, null,
            SelectionSpec.getInstance().userId, SelectionSpec.getInstance().companyId)

    override fun layoutID(): Int = R.layout.box_fragment_media_selection

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SelectionProvider) {
            selectionProvider = context
        } else {
            throw IllegalStateException("Context must implement SelectionProvider.")
        }

        if (context is FileAlbumAdapter.CheckStateListener) checkStateListener = context

        if (context is FileAlbumAdapter.OnMediaClickListener) onMediaClickListener = context

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initArguments()
        recyclerview = view?.findViewById(R.id.recyclerview)

        adapter = FileAlbumAdapter(context!!, selectionProvider.provideSelectedItemCollection())
        adapter.setDisplayEnd(!SelectionSpec.getInstance().onlyShowPic())
        adapter.registerCheckStateListener(this)
        adapter.registerOnMediaClickListener(this)
        recyclerview?.setHasFixedSize(true)

        if (SelectionSpec.getInstance().onlyShowPic()) {
            recyclerview?.layoutManager = GridLayoutManager(context!!, SelectionSpec.getInstance().spanCount)
            val spacing = resources.getDimensionPixelSize(R.dimen.box_media_grid_spacing)
            recyclerview?.addItemDecoration(
                MediaGridInset(SelectionSpec.getInstance().spanCount
                    , spacing, false)
            )
        } else {
            recyclerview?.layoutManager = LinearLayoutManager(context!!)
        }
        recyclerview?.itemAnimator?.changeDuration = 0
        recyclerview?.adapter = adapter

        collectionCreate()
        loadStarMap()
    }

    private fun loadStarMap() {
        SelectionSpec.getInstance().also {
            GlobalScope.launch {
                FileStarManager.instance.clearStarWithFileNotExit(it.userId)
                if (!it.isStarType()) {
                    val list = FileStarManager.instance.findStarList(it.userId, it.companyId)
                    val map = HashMap<String, Int>()
                    for (entity in list) {
                        entity._data?.apply {
                            map[this] = 1
                        }
                    }
                    adapter.setCollectionMap(map)
                }
                if (!isSearch) {
                    recyclerview?.post {
                        collectionFirstLoad()
                    }
                }
            }.start()
        }
    }

    private fun getTooBigSelectItems(): ArrayList<Item> {
        val spec = SelectionSpec.getInstance()
        if (spec.simpleFileSizeMax <= 0) {
            return ArrayList()
        }
        val needRemoveList = ArrayList<Item>()
        selectionProvider.provideSelectedItemCollection().also {
            val mIterator = it.items().iterator()
            while (mIterator.hasNext()) {
                val entry = mIterator.next()
                if (entry.size > spec.simpleFileSizeMax) {
                    needRemoveList.add(entry)
                }
            }
            return needRemoveList
        }
    }

    /**
     * 是否有选择的文件过大
     */
    private fun isSelectItemsSizeTooBig(): Boolean {
        val needRemoveList = getTooBigSelectItems()
        if (needRemoveList.size <= 0) {
            return false
        }
        return true
    }

    /**
     * 获取星标map
     */
    override fun getStarMap() = adapter.getCollectionMap()

    /**
     * 刷新adapter
     */
    override fun refreshMediaGrid() {
        adapter.notifyDataSetChanged()
    }

    /**
     * 打开选择
     */
    override fun openCheck(isOpenCheck: Boolean) {
        adapter.setOpenCheck(isOpenCheck)
    }


    /**
     * 选择全部
     */
    override fun selectAll() {
        if (checkSelctAllNum() || checkSelectFileNum() || checkSelectPicNum()) {
            return
        }
        val alreadySelectMap = createAlreadySelectMap()
        adapter.getCursor()?.apply {
            var nowPosition = 0
            while ((if (nowPosition == 0) moveToFirst() else moveToNext())) {
                //先生成一个临时数据
                val item = Item.valueOf(this, nowPosition)
                if (selectionProvider.provideSelectedItemCollection()
                                .maxSelectableReached(item)) {
                    break
                }
                if (!item.isFolder() && alreadySelectMap[item.filePath] == null) {
                    selectionProvider.provideSelectedItemCollection()
                            .add(item)
                }
                nowPosition++
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 清理选择
     */
    override fun clearSelectAll(isNeedNotify: Boolean) {
        selectionProvider.provideSelectedItemCollection().also {
            if (it.count() == 0) {
                return@also
            }
            it.removeAll()

            if (isNeedNotify) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 删除文件
     */
    override fun removeFiles() {
        selectionProvider.provideSelectedItemCollection().also {
            if (it.count() == 0) {
                return@also
            }
            for (item in it.items()) {
                //删除真实文件
                val file = File(item.filePath)
                if (file.exists()) {
                    file.delete()
                }
                //删除系统数据库中的文件
                context?.contentResolver?.delete(item.getContentUri(), null, null)
            }
            //如果是星标
            if (SelectionSpec.getInstance().isStarType()) {
                GlobalScope.launch {
                    //删除星标表中的文件
                    FileStarManager.instance.removeStarFile(SelectionSpec.getInstance().userId,
                            SelectionSpec.getInstance().companyId, it.items(), null)
                    //将已选的文件全部移除
                    it.removeAll()
                    //刷新adapter
                    collectionResetLoad()
                }.start()
            } else {
                //将已选的文件全部移除
                it.removeAll()
                //刷新adapter
                collectionResetLoad()
            }
        }
    }

    /**
     * 根据文件大小重新选择
     */
    override fun resetSelectItems(): Boolean {
        val spec = SelectionSpec.getInstance()
        val needRemoveList = getTooBigSelectItems()
        if (needRemoveList.size <= 0) {
            return false
        }
        val string = getString(R.string.only_choice_n_filesize_str,
            SizeFormat.formatShortFileSize(context, spec.simpleFileSizeMax).toUpperCase(Locale.getDefault()))
        Toast.makeText(context, string, Toast.LENGTH_SHORT)

        //移除所有的大文件
        for (item in needRemoveList) {
            selectionProvider.provideSelectedItemCollection().remove(item)
        }
        collectionResetLoad()
        onSelectUpdate()
        return true
    }

    /**
     * 发送到im
     */
    override fun forwardItemsToChat(activity: Activity) {
        selectionProvider.provideSelectedItemCollection().items().also {
            Z7Plugin.instance.getListener()?.sendToIM(it)
        }
    }

    /**
     * 发送到调用页面
     */
    override fun forwardItems(activity: Activity) {
        selectionProvider.provideSelectedItemCollection().items().also {
            val intent = Intent()

            if (SelectionSpec.getInstance().onlyShowPic()) {
                val pathArray = Array(it.size) { "" }
                for (index in it.indices) {
                    pathArray[index] = it[index].filePath
                }
                intent.putExtra(FileManager.PIC_INTENT_SELECT_DATA, pathArray)
            } else {
                val modelList = ArrayList<FileManagerModel>()
                for (index in it.indices) {
                    val filePath = it[index].filePath
                    val ext = FileUtils.getExtensionName(filePath)
                    val fileType = FileType.createFileType(ext)
                    modelList.add(FileManagerModel().createModel(filePath,fileType))
                }
                intent.putExtra(FileManager.FILE_INTENT_SELECT_DATA, modelList.toTypedArray())
            }

            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }
    }

    /**
     * 重命名
     */
    override fun rename(context: Context) {
        selectionProvider.provideSelectedItemCollection().also {
            val renameChildView = LayoutInflater.from(context).inflate(R.layout.widget_alert_edit, null)
            val renameEt = renameChildView.findViewById<View>(R.id.et_wae) as EditText
            val renameDialogBuilder = AlertDialog.Builder(context).setView(renameChildView)
                    .setPositiveButton(R.string.confirm_str) { _, _ ->
                        val newName = renameEt.text.toString().trim { it <= ' ' }
                        val file = File(it.items()[0].filePath)
                        val ext = FileUtils.getExtensionName(it.items()[0].filePath)
                        //创建新文件
                        val newFile = File(file.parent + File.separator + newName + "." + ext)
                        //改名
                        file.renameTo(newFile)
                        //更新系统数据库
                        val values = ContentValues()
                        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$newName.$ext")
                        values.put(MediaStore.Files.FileColumns.DATE_MODIFIED, file.lastModified())
                        values.put(PathUtils.FILE_PATH, newFile.path)
                        context.contentResolver?.update(it.items()[0].getContentUri(), values, null, null)

                        //如果是星标
                        if (SelectionSpec.getInstance().isStarType()) {
                            GlobalScope.launch {
                                FileStarManager.instance.rename(newFile, file.path,
                                        SelectionSpec.getInstance().userId, SelectionSpec.getInstance().companyId)
                            }.start()
                        }
                        //清除所选
                        it.removeAll()
                        //刷新数据
                        collectionResetLoad()
                    }.setNegativeButton(R.string.cancel_str) { _, _ ->

                    }
            renameEt.hint = FileUtils.getFolderName(FileUtils.getFileNameNoEx(it.items()[0].filePath))
            renameDialogBuilder.show()
        }
    }

    /**
     * 星标
     */
    override fun starFile(isStar: Boolean) {
        val selec = SelectionSpec.getInstance()
        selectionProvider.provideSelectedItemCollection().items().also {
            GlobalScope.launch {
                //如果是星标类型 那么直接移除
                if (selec.isStarType()) {
                    FileStarManager.instance.removeStarFile(selec.userId, selec.companyId, it, null)

                    recyclerview?.post {
                        clearSelectAll(false)
                        collectionResetLoad()
                        onSelectUpdate()
                    }
                } else {
                    val list = ArrayList<String>()
                    for (item in it) {
                        list.add(item.filePath)
                        if (isStar) {
                            if (getStarMap()[item.filePath] == null) {
                                getStarMap()[item.filePath] = 1
                            }
                        } else {
                            if (getStarMap()[item.filePath] != null) {
                                getStarMap().remove(item.filePath)
                            }
                        }
                    }
                    if (isStar) {
                        FileStarManager.instance.toStarFile(selec.userId, selec.companyId, it, null)
                    } else {
                        FileStarManager.instance.removeStarFile(selec.userId, selec.companyId, it, null)
                    }

                    recyclerview?.post {
                        refreshMediaGrid()
                        onSelectUpdate()
                    }
                }

            }.start()
        }
    }

    /**
     * 分享
     */
    override fun share() {
        selectionProvider.provideSelectedItemCollection().items().also {
            CommonSharingUtils.shareFiles(it[0].filePath, context)
        }
    }

    /**
     * 是否为搜索的fragment
     */
    override fun isFragmentTypeSearch() = isSearch

    /**
     * 获取所有文件数量
     */
    override fun getCursorCount(): Int = adapter.getCursor()?.count ?: 0


    override fun onSelectUpdate() {
        checkStateListener.onSelectUpdate()
    }

    override fun onAlbumStart() {
        // do nothing
    }

    override fun onAlbumLoad(cursor: Cursor) {
        adapter.swapCursor(cursor)
        selectionProvider.loadDataSucc(cursor.count > 0)
    }

    override fun onAlbumReset() {
        adapter.swapCursor(null)
    }

    override fun onDestroyView() {
        adapter.destory()
        super.onDestroyView()
        collectionDestory()
    }


    override fun checkSelectPicNum(): Boolean {
        val couldSelectPicNum = SelectionSpec.getInstance().maxImageSelectable
        if (SelectionSpec.getInstance().onlyShowPic() &&
                selectionProvider.provideSelectedItemCollection().count() >= couldSelectPicNum) {
            return true
        }
        return false
    }

    override fun checkSelectFileNum(): Boolean {
        val couldSelectFileNum = SelectionSpec.getInstance().maxFileSelectable
        if (SelectionSpec.getInstance().onlyShowFile() &&
                selectionProvider.provideSelectedItemCollection().count() >= couldSelectFileNum) {
            return true
        }
        return false
    }

    override fun checkSelctAllNum(): Boolean {
        val couldSelectAllNum = SelectionSpec.getInstance().maxSelectable
        if (SelectionSpec.getInstance().withShowFolder() &&
                selectionProvider.provideSelectedItemCollection().count() >= couldSelectAllNum) {
            return true
        }
        return false
    }

    override fun createAlreadySelectMap(): HashMap<String, Item> {
        val alreadySelectMap = HashMap<String, Item>()
        for (item in selectionProvider.provideSelectedItemCollection().items()) {
            alreadySelectMap[item.filePath] = item
        }
        return alreadySelectMap
    }

    fun getAdapter() = adapter

    fun getOnMediaClickListener() = onMediaClickListener

    fun setIsSearch(isSearch: Boolean) {
        this.isSearch = isSearch
    }

    fun buildArgs(searchKey: String?, mParentPath: String?): ArgsBean {
        mArgs.searchKey = searchKey
        mArgs.parentPath = mParentPath
        return mArgs
    }

    abstract fun initArguments()

    abstract fun collectionCreate()

    abstract fun collectionFirstLoad()

    abstract fun collectionResetLoad()

    abstract fun collectionDestory()

}