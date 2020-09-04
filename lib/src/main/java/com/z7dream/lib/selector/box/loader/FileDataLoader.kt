package com.z7dream.lib.selector.box.loader

import android.content.Context
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.z7dream.lib.selector.box.MimeType
import com.z7dream.lib.selector.box.MimeTypeManager
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.utils.PathUtils
import com.z7dream.lib.selector.utils.CacheManager
import java.util.*

class FileDataLoader(
    context: Context, selection: String, selectionArgs: Array<out String>
) : CursorLoader(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY) {


    companion object {
        private val QUERY_URI = MediaStore.Files.getContentUri(PathUtils.CONTENT_URL)
        private var ORDER_BY = "${PathUtils.DATA_TIME} DESC"

        val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID,
            PathUtils.FILE_PARENT,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            PathUtils.FILE_PATH
        )

        val SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE =
            StringBuilder()
                .append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                .append("=?").append(" AND ")
                .append(MediaStore.Files.FileColumns.SIZE)
                .append(">0")
                .toString()

        fun appendSearchValue(startStr: String, searchValue: String?): String {
            val searchSelection = StringBuilder().append(startStr)
            if (!searchValue.isNullOrEmpty()) {
                searchSelection.append(" AND ")
                    .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    .append(" like ")
                    .append("'%")
                    .append(searchValue)
                    .append("%'")
            }
            return searchSelection.toString()
        }

        fun appendByCompanyId(startStr: String, companyId: Long?): String {
            val searchSelection = StringBuilder().append(startStr)
            if (companyId != null && companyId > 0) {
                searchSelection.append(" AND ")
                    .append(MediaStore.Files.FileColumns.DATA)
                    .append(" like ")
                    .append("'%")
                    .append(CacheManager.getRelativePath(CacheManager.ES, companyId.toString()))
                    .append("%'")
            }
            return searchSelection.toString()
        }

        fun selectionByMimeType(set: EnumSet<MimeType>): String {
            val sb = StringBuilder()
            sb.append("(")
            //不判断文件扩展名
            set.forEach {
                sb.append(MediaStore.Files.FileColumns.MIME_TYPE)
                    .append("=? OR ")
            }
            if (sb.endsWith("OR ")) {
                sb.delete(sb.length - 3, sb.length)
            }
            sb.append(") AND ")
                .append(MediaStore.Files.FileColumns.SIZE)
                .append(">0 AND ")
                .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                .append(" is not null AND ")
                .append(MediaStore.Files.FileColumns.MIME_TYPE)
                .append(" is not null")
            return sb.toString()
        }

        fun newInstance(context: Context, searchValue: String?, companyId: Long?): CursorLoader {
            val selection: String
            val selectionArgs: Array<String>

            when {
                SelectionSpec.getInstance().onlyShowPic() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, searchValue
                        ), companyId
                    )
                    selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                }
                SelectionSpec.getInstance().onlyShowAudio() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, searchValue
                        ), companyId
                    )
                    selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
                }
                SelectionSpec.getInstance().onlyShowVideos() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, searchValue
                        ), companyId
                    )
                    selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                }
                SelectionSpec.getInstance().onlyShowTxts() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(
                                MimeTypeManager.ofTxt()
                            ), searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.txtType()
                }
                SelectionSpec.getInstance().onlyShowExcels() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(MimeTypeManager.ofExcel()),
                            searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.excelType()
                }
                SelectionSpec.getInstance().onlyShowPpts() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(MimeTypeManager.ofPpt()),
                            searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.pptType()
                }
                SelectionSpec.getInstance().onlyShowWords() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(MimeTypeManager.ofWord()),
                            searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.wordType()
                }
                SelectionSpec.getInstance().onlyShowPdfs() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(MimeTypeManager.ofPdf()),
                            searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.pdfType()
                }
                SelectionSpec.getInstance().onlyShowZips() -> {
                    selection = appendByCompanyId(
                        appendSearchValue(
                            selectionByMimeType(MimeTypeManager.ofZip()),
                            searchValue
                        ), companyId
                    )
                    selectionArgs = MimeTypeManager.zipType()
                }
                else -> {
                    selection = ""
                    selectionArgs = Array(0) { "" }
                }
            }
            return FileDataLoader(context, selection, selectionArgs)
        }
    }
}