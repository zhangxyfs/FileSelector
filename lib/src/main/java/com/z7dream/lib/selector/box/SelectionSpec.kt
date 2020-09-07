package com.z7dream.lib.selector.box

import android.content.pm.ActivityInfo
import androidx.annotation.StyleRes
import com.eblog.base.widget.box.engine.ImageEngine
import com.eblog.base.widget.box.filter.Filter
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.Z7Plugin

class SelectionSpec {
    /**
     * 是否初始化了
     */
    var hasInited = false
    /**
     * 图片展示
     */
    var imageEngine: ImageEngine? = null
    /**
     * 页面风格样式
     */
    @StyleRes
    var themeId = 0
    /**
     * 需要显示的文件类型
     */
    var mimeTypeSet: Set<MimeType>? = null
    @Deprecated("该属性暂时没用")
    var typeExclusive = false                      // 设置单种/多种媒体资源选择 默认支持多种
    @Deprecated("暂时没用")
    var filters: MutableList<Filter>? = null
    /**
     * 最大所有数量
     */
    var maxSelectable = 18
    /**
     * 最大文件数量
     */
    var maxFileSelectable = 9
    /**
     * 最大图片数量
     */
    var maxImageSelectable = 9
    /**
     * 缩略图缩放比例
     */
    @Deprecated("暂时没用")
    var thumbnailScale = 0.5f
    @Deprecated("暂时没用")
    var countable = false
    @Deprecated("暂时没用")
    var gridExpectedSize = 0
    /**
     * adapter 表格模式时候一行显示数量
     */
    var spanCount = 3
    /**
     * 显示方向
     */
    var orientation = 0
    var userId: Long = 0L
    //公司id
    var companyId: Long = 0L
    //标题
    var titleName: String? = null
    //是否开启转发
    var isNeedOpenForward = false
    //单个文件最大值
    var simpleFileSizeMax: Long = -1L
    //wps文件
    var isWps = false
    //qq 文件
    var isQQ = false
    //微信 文件
    var isWX = false
    //星标文件
    var isStar = false
    //是否数独界面
    var isSudioku = false
    //是否最近30天
    var is30Days = false

    var requestCode = 0

    class InstanceHolder {
        companion object {
            val INSTANCE: SelectionSpec = SelectionSpec()
        }
    }

    companion object {

        fun getInstance() = InstanceHolder.INSTANCE

        fun getCleanInstance(): SelectionSpec {
            val selectionSpec = getInstance()
            selectionSpec.reset()
            return selectionSpec
        }
    }

    private fun reset() {
        hasInited = true
        themeId = R.style.Theme_Box
        mimeTypeSet = null
        typeExclusive = false
        orientation = 0
        countable = false
        maxSelectable = 18
        maxImageSelectable = 9
        maxFileSelectable = 9
        filters = null
        spanCount = 3
        gridExpectedSize = 0
        thumbnailScale = 0.5f
        companyId = 0L
        userId = 0L
        titleName = ""
        isNeedOpenForward = false
        simpleFileSizeMax = -1L
        isWX = false
        isQQ = false
        isWps = false
        isStar = false
        isSudioku = false
        is30Days = false
        requestCode = 0
        imageEngine = Z7Plugin.instance.getListener()?.getImageEngine()
    }

    // 是否可计数
    fun isCountable() = countable && !isSingleChoose()

    // 是否可单选
    fun isSingleChoose() =
            maxSelectable == 1 || (maxImageSelectable == 1 && maxFileSelectable == 1)

    // 是否单一资源选择方式
    fun isTypeExclusive() =
            typeExclusive && (maxImageSelectable + maxFileSelectable == 0)

    /**
     * 只展现文件
     */
    fun onlyShowFile() =
            if (mimeTypeSet != null) MimeTypeManager.ofFile().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现图片
     */
    fun onlyShowPic() =
            if (mimeTypeSet != null) MimeTypeManager.ofPic().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现音频
     */
    fun onlyShowAudio() =
            if (mimeTypeSet != null) MimeTypeManager.ofAudio().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现视频
     */
    fun onlyShowVideos() =
            if (mimeTypeSet != null) MimeTypeManager.ofVideo().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现文本
     */
    fun onlyShowTxts() =
            if (mimeTypeSet != null) MimeTypeManager.ofTxt().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现工作表
     */
    fun onlyShowExcels() =
            if (mimeTypeSet != null) MimeTypeManager.ofExcel().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现ppt
     */
    fun onlyShowPpts() =
            if (mimeTypeSet != null) MimeTypeManager.ofPpt().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现word
     */
    fun onlyShowWords() =
            if (mimeTypeSet != null) MimeTypeManager.ofWord().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现pdf
     */
    fun onlyShowPdfs() =
            if (mimeTypeSet != null) MimeTypeManager.ofPdf().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现压缩包
     */
    fun onlyShowZips() =
            if (mimeTypeSet != null) MimeTypeManager.ofZip().containsAll(mimeTypeSet!!) else false

    /**
     * 只展现文件夹
     */
    fun withShowFolder() =
            if (mimeTypeSet != null) MimeTypeManager.ofFolder().containsAll(mimeTypeSet!!) else false

    fun isQQType() = isQQ

    fun isWXType() = isWX

    fun isWPSType() = isWps

    fun isStarType() = isStar

    fun is30DayType() = is30Days

    fun singleSelectionModeEnabled() = !countable && isSingleChoose()

    fun needOrientationRestriction() = orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}