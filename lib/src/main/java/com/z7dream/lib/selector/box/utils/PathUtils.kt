package com.z7dream.lib.selector.box.utils

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File


/**
 * http://stackoverflow.com/a/27271131/4739220
 */
object PathUtils {
    private const val SCHEME_CONTENT = "content"
    const val NOMEDIA = ".nomedia"

    var CONTENT_URL = "external"
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                MediaStore.VOLUME_EXTERNAL_PRIMARY
//            } else {
//                "external"
//            }

    var DATA_TIME = MediaStore.Files.FileColumns.DATE_MODIFIED
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                MediaStore.Files.FileColumns.DATE_TAKEN
//            } else {
//                MediaStore.Files.FileColumns.DATE_MODIFIED
//            }

    var FILE_PARENT =  MediaStore.Files.FileColumns.PARENT
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                MediaStore.Files.FileColumns.RELATIVE_PATH
//            } else {
//                MediaStore.Files.FileColumns.PARENT
//            }

    var FILE_PATH = MediaStore.Files.FileColumns.DATA

    fun getPath(resolver: ContentResolver, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        if (SCHEME_CONTENT == uri.scheme) {
            var cursor: Cursor? = null
            return try {
                cursor = resolver.query(uri, arrayOf(FILE_PATH),
                        null, null, null)
                if (cursor == null || !cursor.moveToFirst()) {
                    null
                } else cursor.getString(cursor.getColumnIndex(FILE_PATH))
            } finally {
                cursor?.close()
            }
        }
        return uri.path
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? { // DocumentProvider
        if (Platform.hasKitKat19() && DocumentsContract.isDocumentUri(context, uri)) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) { // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                } else {
                    contentUri = MediaStore.Files.getContentUri("external")
                }


                val selection = "_id=?"
                val selectionArgs = arrayOf(
                        split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) { // MediaStore (and general)
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) { // File
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
                column
        )
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun deleteNoMediaWithEsPath(vararg paths: String) {
        val fileList = ArrayList<File>()
        for (path in paths) {
            val file = File(path)
            file.listFiles()?.forEach {
                fileList.add(it)
            }
            MediaScan.instance.scan(path)
        }
        for (file in fileList) {
            if (file.name.contains(NOMEDIA)) {
                file.delete()
            } else if (file.isDirectory) {
                deleteNoMediaWithEsPath(file.path)
            } else {
                MediaScan.instance.scan(file.path)
            }
        }
    }


}