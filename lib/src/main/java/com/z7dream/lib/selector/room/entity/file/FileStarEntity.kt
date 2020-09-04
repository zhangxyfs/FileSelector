package com.z7dream.lib.selector.room.entity.file

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 星标文件 对应 MediaStore.File
 */
@Entity
class FileStarEntity {
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0L

    /**
     * 父path
     * @link android.provider.MediaStore.Files.FileColumns.RELATIVE_PATH
     */
    var relative_path: String? = null
    /**
     * 父path
     * @link android.provider.MediaStore.Files.FileColumns.PARENT
     */
    var parent: String? = null
    /**
     * 显示的名字
     */
    var _display_name: String? = null

    /**
     * 文件类型
     */
    var mime_type: String? = null

    /**
     * 文件大小
     */
    var _size: Long = 0

    /**
     * 修改时间
     */
    var date_modified: Long = 0

    /**
     * 文件路径
     */
    var _data: String? = null

    //用户id
    var userId: Long? = null
    //公司id
    var companyId: Long? = null
}