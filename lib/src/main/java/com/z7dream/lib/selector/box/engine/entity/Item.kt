package com.z7dream.lib.selector.box.engine.entity

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.graphics.PathUtils
import com.z7dream.lib.selector.box.MimeTypeManager
import com.z7dream.lib.selector.utils.FileType
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
class Item(
        var id: Long, var parentId: String, var displayName: String, var mimeType: String, var size: Long = 0,
        var dataTime: Long = 0, var filePath: String, var positionInList: Int = -1
) : Parcelable {

    companion object {
        // * 注：资源文件size单位为字节byte
        fun valueOf(cursor: Cursor, positionInList: Int = -1): Item =
                Item(
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                        cursor.getString(cursor.getColumnIndex(PathUtils.FILE_PARENT)) ?: "",
                        cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
                                ?: "",
                        cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))
                                ?: "",
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)),
                        cursor.getString(cursor.getColumnIndex(PathUtils.FILE_PATH)) ?: "",
                        positionInList
                )
    }


    @IgnoredOnParcel
    private var uri: Uri

    @IgnoredOnParcel
    private var isFolder: Boolean

    @IgnoredOnParcel
    private var fileType: Int

    init {
        val contentUri = when {
            isAudio() -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            isVideo() -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }
        uri = ContentUris.withAppendedId(contentUri, id)

        isFolder = File(filePath).isDirectory

        fileType =
                if (!isFolder) {
                    FileType.createFileType(FileUtils.getExtensionName(filePath))
                } else {
                    FileType.FOLDER
                }
    }

    fun isFile() = isAudio() || isVideo() || isTxt() || isExcel() || isPpt() || isWord() || isPdf() || isZip()

    fun isPic() = MimeTypeManager.isPic(mimeType)

    fun isAudio() = MimeTypeManager.isAudio(mimeType)

    fun isVideo() = MimeTypeManager.isVideo(mimeType)

    fun isTxt() = MimeTypeManager.isTxt(mimeType)

    fun isExcel() = MimeTypeManager.isExcel(mimeType)

    fun isPpt() = MimeTypeManager.isPpt(mimeType)

    fun isWord() = MimeTypeManager.isWord(mimeType)

    fun isPdf() = MimeTypeManager.isPdf(mimeType)

    fun isZip() = MimeTypeManager.isZip(mimeType)

    fun isFolder(): Boolean = isFolder

    fun getFileType() = fileType

    fun getContentUri() = uri

    override fun describeContents() = 0

    override fun equals(other: Any?): Boolean {
        if (other !is Item) return false

        val otherItem = other as Item?
        return id == otherItem?.id
                && (parentId == otherItem.parentId)
                && (displayName == otherItem.displayName)
                && (mimeType == otherItem.mimeType)
                && (uri == otherItem.uri)
                && (size == otherItem.size)
                && (dataTime == otherItem.dataTime)
                && (filePath == otherItem.filePath)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + parentId.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + size.toString().hashCode()
        result = 31 * result + dataTime.toString().hashCode()
        result = 31 * result + filePath.hashCode()
        return result
    }

    override fun toString(): String {
        return "Item(id=$id, parentPath='$parentId', displayName='$displayName', mimeType='$mimeType', size=$size, dataTime=$dataTime, filePath='$filePath', positionInList=$positionInList, uri=$uri, isFolder=$isFolder, fileType=$fileType)"
    }
}
