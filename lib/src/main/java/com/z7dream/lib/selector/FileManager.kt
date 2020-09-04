package com.z7dream.lib.selector

import android.app.Activity
import android.content.Context
import com.z7dream.lib.selector.box.Box
import com.z7dream.lib.selector.options.FileManagerOptions
import com.z7dream.lib.selector.utils.FileType

class FileManager private constructor() {
    /**
     * 文件选择
     */
    fun nextFileSelect() = FileOptions()

    inner class FileOptions {
        //公司id
        private var companyId: Long? = 0L

        //起始位置
        private var startPath: String? = null

        //标题
        private var titleName: String? = null

        //文件类型
        private var fileType = FileType.ES_ALL

        //最大图片数量
        private var picMaxNum = 9

        //最大文件数量
        private var fileMaxNum = 9

        //最大所有数量
        private var allMaxNum = 18

        //最大文件大小
        private var fileSizeMax: Long? = -1L

        //类型
        private var fuction = 0

        private var isDisplayPicIco: Boolean = false

        //是否开启转发
        private var isNeedOpenForward = false

        //是否需要压缩
        private var isNeedZip = true

        //是否需要裁剪
        private var isNeedCrop = false

        //是否可以选择图片
        private var isCouldSelectPic = true

        //是否有水印
        private var isHasWaterFilter = false

        private var fileRxKey: String? = null

        private var picRxKey: String? = null

        private var requestCode = FILE_MANAGER_REQUEST_CODE


        fun companyId(c: Long?): FileOptions {
            companyId = c
            return this
        }

        fun startPath(s: String?): FileOptions {
            startPath = s
            return this
        }

        fun titleName(s: String?): FileOptions {
            titleName = s
            return this
        }

        fun titleName(resId: Int): FileOptions {
            val context = Z7Plugin.instance.getListener()?.applicationContext()
            titleName = context?.getString(resId) ?: ""
            return this
        }

        fun fileType(type: Int): FileOptions {
            fileType = type
            return this
        }

        fun picMaxNum(n: Int?): FileOptions {
            picMaxNum = n ?: 9
            return this
        }

        fun fileMaxNum(n: Int?): FileOptions {
            fileMaxNum = n ?: 9
            return this
        }

        fun allMaxNum(n: Int?): FileOptions {
            allMaxNum = n ?: 9
            return this
        }

        fun fileSizeMax(max: Long): FileOptions {
            fileSizeMax = max
            return this
        }

        fun fuction(f: Int): FileOptions {
            fuction = f
            return this
        }

        fun isDisplayPicIco(d: Boolean): FileOptions {
            isDisplayPicIco = d
            return this
        }

        fun isNeedOpenForward(b: Boolean): FileOptions {
            isNeedOpenForward = b
            return this
        }

        fun isNeedZip(b: Boolean): FileOptions {
            isNeedZip = b
            return this
        }

        fun isNeedCrop(b: Boolean): FileOptions {
            isNeedCrop = b
            return this
        }

        fun isCouldSelectPic(b: Boolean): FileOptions {
            isCouldSelectPic = b
            return this
        }

        fun isHasWaterFilter(b: Boolean): FileOptions {
            isHasWaterFilter = b
            return this
        }

        fun fileRxKey(key: String?): FileOptions {
            fileRxKey = key
            return this
        }

        fun picRxKey(key: String?): FileOptions {
            picRxKey = key
            return this
        }

        fun requestCode(code: Int): FileOptions {
            requestCode = code
            return this
        }


        private fun createOptions(): FileManagerOptions {
            val options = FileManagerOptions()
            options.companyId = companyId
            options.startPath = startPath
            options.titleName = titleName
            options.fileType = fileType
            options.picMaxNum = picMaxNum
            options.fileMaxNum = fileMaxNum
            options.allMaxNum = allMaxNum
            options.fileSizeMax = fileSizeMax
            options.fuction = fuction
            options.isDisplayPicIco = isDisplayPicIco
            options.isNeedOpenForward = isNeedOpenForward
            options.isNeedZip = isNeedZip
            options.isNeedCrop = isNeedCrop
            options.isCouldSelectPic = isCouldSelectPic
            options.isHasWaterFilter = isHasWaterFilter
            options.fileRxKey = fileRxKey
            options.picRxKey = picRxKey
            options.requestCode = requestCode
            return options
        }

        fun build(activity: Activity?) {
            ARouter.getInstance().build(ROUTER_URL.FILE.FILE_BASE)
                .withSerializable(PARAMS.INTENT_BODY, createOptions())
                .navigation(activity, requestCode)
        }

        fun build(context: Context?) {
            ARouter.getInstance().build(ROUTER_URL.FILE.FILE_BASE)
                .withSerializable(PARAMS.INTENT_BODY, createOptions())
                .navigation(context)
        }

        fun build(activity: Activity?, roteUrl: String) {
            ARouter.getInstance().build(roteUrl)
                .withSerializable(PARAMS.INTENT_BODY, createOptions())
                .navigation(activity, requestCode)
        }
    }

    companion object {
        val instance: FileManager by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FileManager()
        }

        const val FILE_MANAGER_REQUEST_CODE = Box.FILE_MANAGER_REQUEST_CODE
        const val PIC_INTENT_SELECT_DATA = Box.PIC_INTENT_SELECT_DATA
        const val FILE_INTENT_SELECT_DATA = "file_select_data"

        /**
         * 类型为强制更新
         */
        const val DIALOG_TYPE_UPDATE = 1

        /**
         * 其他类型
         */
        const val DIALOG_TYPE_OTHER = 2

        const val SCAN_RESULT = "scan_result"

        //二维码扫码 -- 获得文本
        const val SCAN_TYPE_TXT = 100

        //二维码扫码 -- 默认
        const val SCAN_TYPE_NONE = -100

        private var lastOpenCameraTime = 0L
        private const val DEFAULT_ZOOM = 16

        private fun isAlreadyOpenCamera(): Boolean {
            val poor = System.currentTimeMillis() - lastOpenCameraTime
            lastOpenCameraTime = System.currentTimeMillis()
            return poor <= 200
        }
    }
}