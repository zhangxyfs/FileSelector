package com.z7dream.lib.selector.box

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.eblog.base.widget.box.engine.ImageEngine
import com.eblog.base.widget.box.filter.Filter
import java.util.*


class SelectionCreator
/**
 * 在上下文中构造新的规范生成器。
 *
 * @param box   请求者上下文包装器。
 * @param mimeTypes MIME类型设置为select。
 */
(val mBox: Box, mimeTypes: Set<MimeType>?, mediaTypeExclusive: Boolean) {
    private var mSelectionSpec: SelectionSpec

    init {
        mSelectionSpec = SelectionSpec.getCleanInstance()
        mSelectionSpec.mimeTypeSet = mimeTypes
        mSelectionSpec.typeExclusive = mediaTypeExclusive
        mSelectionSpec.orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @IntDef(
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_USER,
            ActivityInfo.SCREEN_ORIENTATION_BEHIND,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR,
            ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
    )
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class ScreenOrientation

    /**
     * 媒体选择活动主题。
     *
     *
     * 有两个内置主题:
     * 1. R.style.Matisse_Zhihu;
     * 2. R.style.Matisse_Dracula
     * 您可以定义从上述主题或其他主题派生的自定义主题。
     *
     * @param themeId 主题资源id。 Default value is R.style.Matisse_Zhihu.
     * @return [SelectionCreator] for fluent API.
     */
    fun theme(@StyleRes themeId: Int): SelectionCreator {
        mSelectionSpec.themeId = themeId
        return this
    }

    /**
     * 当用户选择媒体时，显示一个自动增加的数字或一个复选标记。
     *
     * @param countable 对自动增加的数字为真，对复选标记为假。默认值为false。
     * @return [SelectionCreator] for fluent API.
     */
    fun countable(countable: Boolean): SelectionCreator {
        mSelectionSpec.countable = countable
        return this
    }

    /**
     * 最大的选择计算。
     *
     * @param maxSelectable 最大的选择计算。默认值是1。
     * @return [SelectionCreator] for fluent API.
     */
    fun maxSelectable(maxSelectable: Int): SelectionCreator {
        require(maxSelectable >= 1) { "maxSelectable must be greater than or equal to one" }
        mSelectionSpec.also {
            check(maxSelectable <= it.maxImageSelectable + it.maxFileSelectable) {
                "maxSelectable must be less than maxImageSelectable and maxFileSelectable sum"
            }
            it.maxSelectable = maxSelectable
        }
        return this
    }

    /**
     * 只有在 [SelectionSpec.mediaTypeExclusive] 时才有用。设置为真，你想为图像和文件类型设置不同的最大可选择文件。
     *
     * @param maxImageSelectable 图像的最大可选计数。
     * @param maxFileSelectable 文件的最大可选计数。
     * @return [SelectionCreator] for fluent API.
     */
    fun maxSelectablePerMediaType(maxImageSelectable: Int, maxFileSelectable: Int): SelectionCreator {
        mSelectionSpec.maxImageSelectable = maxImageSelectable
        mSelectionSpec.maxFileSelectable = maxFileSelectable
        return this
    }

    /**
     * 添加筛选器来筛选每个选择项。
     *
     * @param filter [Filter]
     * @return [SelectionCreator] for fluent API.
     */
    fun addFilter(filter: Filter): SelectionCreator {
        if (mSelectionSpec.filters == null) {
            mSelectionSpec.filters = ArrayList()
        }
        requireNotNull(filter) { "filter cannot be null" }
        mSelectionSpec.filters?.add(filter)
        return this
    }

    /**
     * 设置此活动的预期方向。
     *
     * @param orientation 在 [ScreenOrientation] 中使用的方向常数。
     * Default value is [android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT].
     * @return [SelectionCreator] for fluent API.
     * @see Activity.setRequestedOrientation
     */
    fun restrictOrientation(@ScreenOrientation orientation: Int): SelectionCreator {
        mSelectionSpec.orientation = orientation
        return this
    }

    /**
     * 为媒体网格设置固定的跨度计数。对于不同的屏幕方向是相同的。
     *
     * 只针对图片选择页面
     *
     * @param spanCount 要求跨数。
     * @return [SelectionCreator] for fluent API.
     */
    fun spanCount(spanCount: Int): SelectionCreator {
        require(spanCount >= 1) { "spanCount cannot be less than 1" }
        mSelectionSpec.spanCount = spanCount
        return this
    }

    /**
     * 设置媒体网格的期望大小，以适应不同的屏幕大小。这不一定要应用，因为媒体网格应该填充视图容器。
     * 测量的媒体网格的大小将尽可能接近这个值。
     *
     * @param size 期望的媒体网格大小(以像素为单位)。
     * @return [SelectionCreator] for fluent API.
     */
    @Deprecated("没用上")
    fun gridExpectedSize(size: Int): SelectionCreator {
        mSelectionSpec.gridExpectedSize = size
        return this
    }

    /**
     * 照片缩略图的比例与视图的大小。它应该是一个浮点值(0.0, 1.0].
     *
     * @param scale Thumbnail's scale in (0.0, 1.0]. Default value is 0.5.
     * @return [SelectionCreator] for fluent API.
     */
    @Deprecated("没用上")
    fun thumbnailScale(scale: Float): SelectionCreator {
        require(!(scale <= 0f || scale > 1f)) { "Thumbnail scale must be between (0.0, 1.0]" }
        mSelectionSpec.thumbnailScale = scale
        return this
    }

    /**
     * 提供一个映像引擎。
     *
     *
     * 有两个内置的图像引擎:
     * 1. [com.zhihu.matisse.engine.impl.GlideEngine]
     * 2. [com.zhihu.matisse.engine.impl.PicassoEngine]
     * 你可以实现你自己的图像引擎。
     *
     * @param imageEngine [ImageEngine]
     * @return [SelectionCreator] for fluent API.
     */
    fun imageEngine(imageEngine: ImageEngine): SelectionCreator {
        mSelectionSpec.imageEngine = imageEngine
        return this
    }

    /**
     * 开始选择媒体，等待结果。
     *
     * @param requestCode 请求活动或片段的标识。
     */
    fun forResult(requestCode: Int) {
        val activity: Activity = mBox.activity ?: return
        val fragment: Fragment? = mBox.fragment

        val intent: Intent
        if (mSelectionSpec.isSudioku) {
            intent = Intent(activity, SudokuActivity::class.java)
        } else {
            intent = Intent(activity, BoxActivity::class.java)
        }
        mSelectionSpec.requestCode = requestCode

        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 设置toolbar标题
     */
    fun setToolbarTitle(title: String): SelectionCreator {
        mSelectionSpec.titleName = title
        return this
    }

    /**
     * 单个文件最大可选的数值 单位: bit
     */
    fun simpleFileSizeMax(max: Long?): SelectionCreator {
        mSelectionSpec.simpleFileSizeMax = max ?: -1L
        return this
    }

    /**
     * 是否可以转发 true: 可以  false 不可以
     */
    fun isNeedForward(isNeedForward: Boolean): SelectionCreator {
        mSelectionSpec.isNeedOpenForward = isNeedForward
        return this
    }

    /**
     * 设置用户id 和公司id
     */
    fun setId(userId: Long, companyId: Long?): SelectionCreator {
        mSelectionSpec.userId = userId
        mSelectionSpec.companyId = companyId ?: 0L
        return this
    }

    /**
     * 显示qq文件
     */
    fun setSearchByQQ(): SelectionCreator {
        mSelectionSpec.isQQ = true
        return this
    }

    /**
     * 显示最近30天
     */
    fun setSearchBy30Day(): SelectionCreator{
        mSelectionSpec.is30Days = true
        return this
    }

    /**
     * 显示微信文件
     */
    fun setSearchByWX(): SelectionCreator {
        mSelectionSpec.isWX = true
        return this
    }

    /**
     * 显示wps文件
     */
    fun setSearchByWPS(): SelectionCreator {
        mSelectionSpec.isWps = true
        return this
    }

    /**
     * 显示星标文件
     */
    fun setSearchByStar(): SelectionCreator {
        mSelectionSpec.isStar = true
        return this
    }

    /**
     * 显示九宫格界面
     */
    fun setSudioku(): SelectionCreator {
        mSelectionSpec.isSudioku = true
        return this
    }
}