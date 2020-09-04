package com.eblog.base.widget.box.filter

import android.content.Context
import com.eblog.base.widget.box.MimeType
import com.eblog.base.widget.box.MimeTypeManager
import com.z7dream.lib.selector.box.entity.Item
import com.eblog.base.widget.matisse.internal.entity.IncapableCause

abstract class Filter {
    companion object {

        // Convenient constant for a minimum value
        const val MIN = 0

        // Convenient constant for a maximum value
        const val MAX = Int.MAX_VALUE

        // Convenient constant for 1024
        const val K = 1024
    }

    // Against what mime types this filter applies
    abstract fun constraintTypes(): Set<MimeType>

    /**
     * Invoked for filtering each item
     *
     * @return null if selectable, {@link IncapableCause} if not selectable.
     */
    abstract fun filter(context: Context, item: Item?): IncapableCause?

    // Whether an {@link Item} need filtering
    open fun needFiltering(context: Context, item: Item?): Boolean {
        constraintTypes().forEach {
            if (MimeTypeManager.checkType(
                            context.contentResolver, item?.getContentUri(), it.getValue()
                    )
            ) return true
        }

        return false
    }
}