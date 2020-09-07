package com.z7dream.lib.selector.box.loader

import android.content.Context
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.utils.PathUtils
import com.z7dream.lib.selector.utils.CacheManager
import com.z7dream.lib.selector.utils.FileUtils
import com.z7dream.lib.selector.utils.WPSUtils
import java.io.*

//文件目录列表(包括文件)loader
class FolderDataLoader(
    context: Context, selection: String, selectionArgs: Array<out String>?
) : CursorLoader(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY) {

    companion object {
        private val QUERY_URI = MediaStore.Files.getContentUri(PathUtils.CONTENT_URL)
        private var ORDER_BY = "lower(${PathUtils.FILE_PARENT}) ASC, ${PathUtils.DATA_TIME} ASC"
        private var LOWER_MIME = "lower(${MediaStore.Files.FileColumns.MEDIA_TYPE})"


        val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID,
            PathUtils.FILE_PARENT,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            PathUtils.FILE_PATH
        )

        private fun select(parentPath: String?, searchValue: String?): String {
            val isRoot = parentPath == "/"
            val searchSelection = StringBuilder()
            if (searchValue.isNullOrEmpty()) {
                if (isRoot) {
                    searchSelection.append(getRootFileListSelection())
                } else {
                    searchSelection.append(getChildFileListSelection(parentPath))
                }
            } else {
                searchSelection
                    .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    .append(" like ")
                    .append("'%")
                    .append(searchValue)
                    .append("%'")
            }
            if (searchSelection.isNotEmpty()) {
                searchSelection.append(" AND ")
            }
            searchSelection
                .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                .append(" is not null")
                .append(" AND ")
                .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                .append(" not like '.%'")
                .append(" AND ")
                .append(MediaStore.Files.FileColumns.DISPLAY_NAME)
                .append(" not like '|_%' escape '|'")

            return searchSelection.toString()
        }

        /**
         * 当为根目录时候生成查询信息
         */
        private fun getRootFileListSelection(): String {
            val fileList = File(CacheManager.getSaveFilePath())
            val searchSelection = StringBuilder()
            fileList.listFiles()?.apply {
                searchSelection.append("(")
                forEachIndexed { index, file ->
                    searchSelection.append(PathUtils.FILE_PATH)
                        .append("='").append(file.path).append("'")
                    if (index < size - 1) {
                        searchSelection.append(" OR ")
                    }
                }
                searchSelection.append(")")
            }
            return searchSelection.toString()
        }

        /**
         * 获取子目录的文件列表
         */
        private fun getChildFileListSelection(parentPath: String?): String {
            val relativeParentPath = CacheManager.getSaveFilePath() + File.separator
            val needPath =
                if (parentPath?.startsWith(relativeParentPath) == false) {
                    relativeParentPath + parentPath
                } else {
                    parentPath
                }
            val fileArray = File(needPath).listFiles()
            val searchSelection = StringBuilder()
            if (fileArray.isNullOrEmpty()) {
                searchSelection
                    .append(PathUtils.FILE_PATH)
                    .append(" like '").append(needPath).append("%' AND ")
                    .append(PathUtils.FILE_PATH)
                    .append("!='").append(needPath).append("'")
            } else {
                searchSelection.append("(")
                fileArray.forEachIndexed { index, file ->
                    searchSelection.append(PathUtils.FILE_PATH)
                        .append("='").append(file.path).append("'")
                    if (index < fileArray.size - 1) {
                        searchSelection.append(" OR ")
                    }
                }
                searchSelection.append(")")
            }
            return searchSelection.toString()
        }


        private fun selectByType(parentPath: String?): String {
            val sb = StringBuilder().append(parentPath)
            val nowTime = System.currentTimeMillis() / 1000L
            val lestTime = nowTime - 30 * 24 * 60 * 60
            when {
                SelectionSpec.getInstance().isQQ -> {
                    sb.append(" AND (")
                        .append(PathUtils.FILE_PATH)
                        .append(" like '%/QQ_Images%' OR ")
                        .append(PathUtils.FILE_PATH)
                        .append(" like '%/QQfile_recv%')")
                }
                SelectionSpec.getInstance().isWX -> {
                    sb.append(" AND (")
                        .append(PathUtils.FILE_PATH)
                        .append(" like '%/MicroMsg/WeiXin%' OR ")
                        .append(PathUtils.FILE_PATH)
                        .append(" like '%/MicroMsg/Download%')")
                }
                SelectionSpec.getInstance().isWps -> {
                    val list = getWPSFileNameList()
                    sb.append(" AND (")
                    if (list.size != 0) {
                        for (index in list.indices) {
                            if (index > 0) {
                                sb.append(" OR ")
                            }
                            sb.append(PathUtils.FILE_PATH)
                                .append(" like ")
                                .append("'%${list[index]}%'")
                        }
                    } else {
                        sb.append(PathUtils.FILE_PATH)
                            .append("='NONE'")
                    }
                    sb.append(") ")
                }
                SelectionSpec.getInstance().is30Days -> {
                    sb.append(" AND ")
                        .append(MediaStore.Files.FileColumns.DATE_MODIFIED)
                        .append(">")
                        .append(lestTime)
                        .append(" AND ")
                        .append(MediaStore.Files.FileColumns.DATE_MODIFIED)
                        .append("<")
                        .append(nowTime)
                }
            }
            return sb.toString()
        }

        private fun getWPSFileNameList(): ArrayList<String> {
            val wpsPath = CacheManager.getSaveFilePath() + File.separator +
                    "/Android/Data/cn.wps.moffice_eng/.cache/KingsoftOffice/.history/attach_mapping_v1.json"
            val file = File(wpsPath)
            val stringBuffer = StringBuilder()
            if (file.isFile() && file.exists() && !file.isDirectory()) {
                var line: String? = null
                var fileInputStream: FileInputStream? = null
                var inputStreamReader: InputStreamReader? = null
                var reader: BufferedReader? = null

                try {
                    fileInputStream = FileInputStream(file)
                    inputStreamReader = InputStreamReader(fileInputStream, "UTF-8")
                    reader = BufferedReader(inputStreamReader)
                    while (((reader.readLine())?.also { line = it }) != null) {
                        stringBuffer.append(line)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        reader?.close()
                        inputStreamReader?.close()
                        fileInputStream?.close()
                    } catch (exce: IOException) {
                        exce.printStackTrace()
                    }
                }
            }
            val list = ArrayList<String>()
            if (stringBuffer.isNotEmpty()) {
                val l = Gson().fromJson<List<WPSUtils.WPSJSONBean>>(
                    stringBuffer.toString(),
                    object : TypeToken<List<WPSUtils.WPSJSONBean>>() {
                    }.type
                )
                for (index in l.indices) {
                    if (index > 29) {
                        break
                    }
                    val path = l[index].filePath
                    val file = File(path)
                    if (!file.exists()) {
                        continue
                    }
                    list.add(FileUtils.getFileNameNoEx(file.name))
                }
            }
            return list
        }

        fun selectionByMimeType(): String {
            val sb = StringBuilder()
            sb.append("(")
                .append(LOWER_MIME)
                .append(" like '%image/%' or ")
                .append(LOWER_MIME)
                .append(" like '%audio/%' or ")
                .append(LOWER_MIME)
                .append(" like '%video/%' or ")
                .append(LOWER_MIME)
                .append(" like '%text/%' or ")
                .append(LOWER_MIME)
                .append(" like '%application/%' or ")
                .append(LOWER_MIME)
                .append(" is null")
                .append(" )")
            return sb.toString()
        }

        fun newInstance(context: Context, parentPath: String?, searchValue: String?): CursorLoader {
            val selection = selectByType(select(parentPath, searchValue))
            return FolderDataLoader(context, selection, null)
        }
    }
}