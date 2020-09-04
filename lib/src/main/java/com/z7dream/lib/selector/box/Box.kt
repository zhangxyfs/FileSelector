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


    companion object {
        fun from(activity: Activity?): Box {
            return Box(activity)
        }

        fun from(fragment: Fragment?): Box {
            return Box(fragment)
        }
    }
}