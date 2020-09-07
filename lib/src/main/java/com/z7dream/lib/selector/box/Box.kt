package com.z7dream.lib.selector.box

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class Box private constructor(val activity: Activity?, val fragment: Fragment?) {
    private var mContext: WeakReference<Activity>? =
        if (activity != null) {
            WeakReference(activity)
        } else {
            null
        }
    private var mFragment: WeakReference<Fragment>? =
        if (fragment != null) {
            WeakReference(fragment)
        } else {
            null
        }

    private constructor(activity: Activity?) : this(activity, null)
    private constructor(fragment: Fragment?) : this(null, fragment)


    /**
     * 选择mime类型约束
     *
     * @param mimeTypes 用户可以选择设置的MIME类型.
     * @return [SelectionCreator] 构建选择规范.
     * @see MimeType
     *
     * @see SelectionCreator
     */
    fun choose(mimeTypes: Set<MimeType>?): SelectionCreator {
        return choose(mimeTypes, true)
    }

    /**
     * 选择mime类型约束
     *
     *
     * @param mimeTypes          用户可以选择设置的MIME类型.
     * @param mediaTypeExclusive 是否可以在一个单一的选择过程中同时选择图像和视频。
     * true表示不能同时选择图像和视频，而false表示可以同时选择图像和视频。 (该功能暂时没用)
     * @return [SelectionCreator] 构建选择规范.
     * @see MimeType
     *
     * @see SelectionCreator
     */
    @Deprecated(
        "暂时没用",
        ReplaceWith(
            "SelectionCreator(this, mimeTypes, mediaTypeExclusive)",
            "com.eblog.base.widget.box.SelectionCreator"
        )
    )
    fun choose(mimeTypes: Set<MimeType>?, mediaTypeExclusive: Boolean): SelectionCreator {
        return SelectionCreator(this, mimeTypes, mediaTypeExclusive)
    }


    companion object {
        const val FILE_MANAGER_REQUEST_CODE = 9166
        const val PIC_INTENT_SELECT_DATA = "pic_select_data"
        const val FILE_INTENT_SELECT_DATA = "file_select_data"

        fun from(activity: Activity?): Box {
            return Box(activity)
        }

        fun from(fragment: Fragment?): Box {
            return Box(fragment)
        }
    }
}