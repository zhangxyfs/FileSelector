package com.eblog.base.widget.box.ui.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.eblog.base.R
import com.eblog.base.widget.box.SelectionSpec
import com.eblog.base.widget.box.entity.Item
import com.eblog.base.widget.box.model.SelectedItemCollection
import com.eblog.base.widget.box.widget.CheckView
import com.eblog.base.widget.box.widget.MediaLayout
import com.eblog.base.widget.matisse.internal.entity.IncapableCause

/**
 * 文件选择 适配器
 */
class FileAlbumAdapter(
        private var context: Context, private var selectedCollection: SelectedItemCollection
) : RecyclerViewCursorAdapter<RecyclerView.ViewHolder>(null), MediaLayout.OnMediaItemClickListener {

    private var selectionSpec: SelectionSpec = SelectionSpec.getInstance()
    private var layoutInflater: LayoutInflater
    private var isOpenCheck: Boolean
    private var collectionMap: HashMap<String, Int>
    private var mOnMediaClickListener: OnMediaClickListener? = null
    private var mCheckStateListener: CheckStateListener? = null
    private var mSearckKey = ""

    init {
        val ta = context.theme.obtainStyledAttributes(intArrayOf(R.attr.item_placeholder))
        ta.recycle()
        layoutInflater = LayoutInflater.from(context)
        isOpenCheck = false
        collectionMap = HashMap()
    }

    fun destory() {
        mCheckStateListener = null
        mOnMediaClickListener = null
        collectionMap.clear()
    }

    fun setCollectionMap(map: HashMap<String, Int>) {
        collectionMap.putAll(map)
    }

    fun getCollectionMap(): HashMap<String, Int> = collectionMap

    fun setOpenCheck(isOpenCheck: Boolean) {
        this.isOpenCheck = isOpenCheck
        notifyDataSetChanged()
    }

    fun setSearchKey(searchKey: String) {
        mSearckKey = searchKey
    }

    companion object {
        const val VIEW_TYPE_MEDIA = 0X02
        const val VIEW_TYPE_END = 0X03
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_MEDIA -> {
                return MediaViewHolder(layoutInflater.inflate(R.layout.item_file_media, parent, false))
            }
            else -> {
                return EndViewHolder(layoutInflater.inflate(R.layout.widget_pull_to_refresh_end, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, cursor: Cursor, position: Int) {
        holder.apply {
            when (this) {
                is MediaViewHolder -> {
                    val item = Item.valueOf(cursor, position)
                    val info = MediaLayout.PreBindInfo(
                            item.getFileType(),
                            collectionMap[item.filePath] != null,
                            selectionSpec.isCountable(), isOpenCheck, holder, mSearckKey,
                            position + 2 == itemCount)
                    mediaGrid.bindMedia(info, item)
                    mediaGrid.setOnMediaGridClickListener(this@FileAlbumAdapter)
                    setCheckStatus(item, mediaGrid)
                }
            }
        }
    }

    override fun getItemViewType(position: Int, cursor: Cursor) = VIEW_TYPE_MEDIA

    override fun getEndItemViewType(): Int = VIEW_TYPE_END

    /**
     * 初始化选择框选中状态
     */
    private fun setCheckStatus(item: Item, mediaGrid: MediaLayout) {
        if (selectionSpec.isCountable()) {
            val checkedNum = selectedCollection.checkedNumOf(item)

            if (checkedNum > 0) {
                mediaGrid.setCheckedNum(checkedNum)
            } else {
                mediaGrid.setCheckedNum(
                        if (selectedCollection.maxSelectableReached(item)) CheckView.UNCHECKED else checkedNum
                )
            }
        } else {
            mediaGrid.setChecked(selectedCollection.isSelected(item))
        }
    }

    override fun onThumbnailClicked(thumbnail: ImageView?, item: Item?, holder: RecyclerView.ViewHolder?, isFolder: Boolean) {
        mOnMediaClickListener?.onMediaClick(item, holder?.adapterPosition ?: -1, isFolder)
    }

    /**
     * 单选：
     *     a.选中：刷新当前项与上次选择项
     *     b.取消选中：刷新当前项与上次选择项
     *
     * 多选：
     *      1. 按序号计数
     *          a.选中：仅刷新选中的item
     *          b.取消选中：
     *              取消最后一位：仅刷新当前操作的item
     *              取消非最后一位：刷新所有选中的item
     *      2. 无序号计数
     *          a.选中：仅刷新选中的item
     *          b.取消选中：仅刷新选中的item
     */
    override fun onCheckViewClicked(checkView: CheckView?, item: Item?, holder: RecyclerView.ViewHolder?) {
        if (selectionSpec.isSingleChoose()) {
            notifySingleChooseData(item)
        } else {
            notifyMultiChooseData(item)
        }
    }

    /**
     * 单选刷新数据
     */
    private fun notifySingleChooseData(item: Item?) {
        item?.also {
            if (selectedCollection.isSelected(item)) {
                selectedCollection.remove(item)
                notifyItemChanged(item.positionInList)
            } else {
                notifyLastItem()
                if (!addItem(item)) return
                notifyItemChanged(item.positionInList)
            }
            notifyCheckStateChanged()
        }
    }

    private fun notifyLastItem() {
        val itemLists = selectedCollection.asList()
        if (itemLists.size > 0) {
            selectedCollection.remove(itemLists[0])
            notifyItemChanged(itemLists[0].positionInList)
        }
    }

    /**
     * 多选刷新数据
     *      1. 按序号计数
     *          a.选中：仅刷新选中的item
     *          b.取消选中：
     *              取消最后一位：仅刷新当前操作的item
     *              取消非最后一位：刷新所有选中的item
     *      2. 无序号计数
     *          a.选中：仅刷新选中的item
     *          b.取消选中：仅刷新选中的item
     */
    private fun notifyMultiChooseData(item: Item?) {
        item?.also {
            if (selectionSpec.isCountable()) {
                if (notifyMultiCountableItem(item)) return
            } else {
                if (selectedCollection.isSelected(item)) {
                    selectedCollection.remove(item)
                } else {
                    if (!addItem(item)) return
                }

                notifyItemChanged(item.positionInList)
            }

            notifyCheckStateChanged()
        }
    }

    /**
     * @return 是否拦截 true=拦截  false=不拦截
     */
    private fun notifyMultiCountableItem(item: Item): Boolean {
        val checkedNum = selectedCollection.checkedNumOf(item)
        if (checkedNum == CheckView.UNCHECKED) {
            if (!addItem(item)) return true
            notifyItemChanged(item.positionInList)
        } else {
            selectedCollection.remove(item)
            // 取消选中中间序号时，刷新所有选中item
            if (checkedNum != selectedCollection.count() + 1) {
                selectedCollection.asList().forEach {
                    notifyItemChanged(it.positionInList)
                }
            }
            notifyItemChanged(item.positionInList)
        }
        return false
    }

    private fun notifyCheckStateChanged() {
        mCheckStateListener?.onSelectUpdate()
    }

    private fun addItem(item: Item): Boolean {
        if (!assertAddSelection(context, item)) return false
        selectedCollection.add(item)
        return true
    }


    private fun assertAddSelection(context: Context, item: Item): Boolean {
        val cause = selectedCollection.isAcceptable(item)
        IncapableCause.handleCause(context, cause)
        return cause == null
    }

    fun registerCheckStateListener(listener: CheckStateListener) {
        mCheckStateListener = listener
    }

    fun unregisterCheckStateListener() {
        mCheckStateListener = null
    }

    fun registerOnMediaClickListener(listener: OnMediaClickListener) {
        mOnMediaClickListener = listener
    }

    fun unregisterOnMediaClickListener() {
        mOnMediaClickListener = null
    }

    interface CheckStateListener {
        fun onSelectUpdate()
    }


    interface OnMediaClickListener {
        fun onMediaClick(item: Item?, adapterPosition: Int, isFolder: Boolean)
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mediaGrid: MediaLayout = itemView as MediaLayout
    }

    class EndViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}