package com.eblog.base.widget.box.ui.adapter

import android.database.Cursor
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView

/**
 * 文件选择适配器 基类
 */
abstract class RecyclerViewCursorAdapter<VH : RecyclerView.ViewHolder>(c: Cursor?) :
        RecyclerView.Adapter<VH>() {
    private var cursor: Cursor? = null
    private var rowIDColumn = 0
    private var isNeedEnd = true

    init {
        setHasStableIds(true)
        swapCursor(c)
    }

    fun setDisplayEnd(isNeedEnd: Boolean) {
        this.isNeedEnd = isNeedEnd
    }

    abstract fun onBindViewHolder(holder: VH, cursor: Cursor, position: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (isNeedEnd && isPositionEnd(position)) {
            return
        }

        check(isDataValid(cursor)) {
            "Cannot bind view holder when cursor is in invalid state."
        }

        check(cursor?.moveToPosition(position)!!) {
            "Could not move cursor to position $position when trying to bind view holder"
        }

        onBindViewHolder(holder, cursor!!, position)
    }

    override fun getItemViewType(position: Int): Int {
        if (isNeedEnd && isPositionEnd(position)) {
            return getEndItemViewType()
        }
        check(cursor?.moveToPosition(position)!!) {
            "Could not move cursor to position $position when trying to get item view type."
        }
        return getItemViewType(position, cursor!!)
    }

    fun isPositionEnd(position: Int) = position + 1 == itemCount

    abstract fun getItemViewType(position: Int, cursor: Cursor): Int

    abstract fun getEndItemViewType(): Int

    override fun getItemCount(): Int {
        return if (isDataValid(cursor)) cursor?.count!! + (if (isNeedEnd) 1 else 0) else 0
    }

    override fun getItemId(position: Int): Long {
        if (isNeedEnd && isPositionEnd(position)) {
            return -100
        }
        check(isDataValid(cursor)) { "Cannot lookup item id when cursor is in invalid state." }
        check(cursor?.moveToPosition(position)!!) {
            "Could not move cursor to position $position when trying to get an item id"
        }

        return cursor?.getLong(rowIDColumn) ?: 0
    }

    fun swapCursor(newCursor: Cursor?) {
        if (newCursor == cursor) return

        if (newCursor == null) {
            notifyItemRangeRemoved(0, itemCount)
            cursor = null
            rowIDColumn = -1
        } else {
            cursor = newCursor
            rowIDColumn =
                    cursor?.getColumnIndexOrThrow(
                            MediaStore.Files.FileColumns._ID
                    ) ?: 0
            // notify the observers about the new cursor
            notifyDataSetChanged()
        }
    }

    private fun isDataValid(cursor: Cursor?) = cursor != null && !cursor.isClosed

    fun getCursor() = cursor
}