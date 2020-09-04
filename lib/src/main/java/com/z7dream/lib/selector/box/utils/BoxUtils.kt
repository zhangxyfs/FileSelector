package com.z7dream.lib.selector.box.utils

import android.content.Context
import android.provider.MediaStore
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.Z7Plugin
import com.z7dream.lib.selector.box.MimeType
import com.z7dream.lib.selector.box.MimeTypeManager
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.entity.IncapableCause
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.loader.FileDataLoader
import com.z7dream.lib.selector.utils.callback.Callback
import com.z7dream.lib.selector.utils.rx.RxSchedulersHelper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.ArrayList

object BoxUtils {

    fun getAllFileCount(callback: Callback.Callback10<Int, Int, ArrayList<Disposable>>) {
        val context = Z7Plugin.instance.getListener()?.applicationContext()
        val list = ArrayList<Disposable>()
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofPic())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(0, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofAudio())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(1, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofVideo())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(2, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofTxt())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(3, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofExcel())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(4, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofPpt())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(5, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofWord())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(6, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofPdf())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(7, it)
                        },{
                            it.printStackTrace()
                        })
        )
        list.add(
                Observable.just(1)
                        .map {
                            return@map getFileCountByMimeType(context, MimeTypeManager.ofZip())
                        }
                        .compose(RxSchedulersHelper.io())
                        .subscribe({
                            callback.event(8, it)
                        },{
                            it.printStackTrace()
                        })
        )
        callback.event1(list)
    }


    fun getFileCountByMimeType(context: Context?, mimeType: Set<MimeType>?): Int {
        val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
            PathUtils.FILE_PARENT,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
            PathUtils.FILE_PATH
        )
        val orderBy = "${PathUtils.DATA_TIME} DESC"

        val selection: String
        val selectionArgs: Array<String>

        when (mimeType) {
            MimeTypeManager.ofPic() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, "")
                selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
            }
            MimeTypeManager.ofAudio() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, "")
                selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
            }
            MimeTypeManager.ofVideo() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE, "")
                selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
            }
            MimeTypeManager.ofTxt() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofTxt()), "")
                selectionArgs = MimeTypeManager.txtType()
            }
            MimeTypeManager.ofExcel() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofExcel()), "")
                selectionArgs = MimeTypeManager.excelType()
            }
            MimeTypeManager.ofPpt() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofPpt()), "")
                selectionArgs = MimeTypeManager.pptType()
            }
            MimeTypeManager.ofWord() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofWord()), "")
                selectionArgs = MimeTypeManager.wordType()
            }
            MimeTypeManager.ofPdf() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofPdf()), "")
                selectionArgs = MimeTypeManager.pdfType()
            }
            MimeTypeManager.ofZip() -> {
                selection = FileDataLoader.appendSearchValue(FileDataLoader.selectionByMimeType(MimeTypeManager.ofZip()), "")
                selectionArgs = MimeTypeManager.zipType()
            }
            else -> {
                selection = ""
                selectionArgs = MimeTypeManager.picType()
            }
        }
        val cursor = context?.contentResolver?.query(
                MediaStore.Files.getContentUri(PathUtils.CONTENT_URL),
                projection,
                selection,
                selectionArgs,
                orderBy
        )

        return if (cursor == null) {
            return 0
        } else {
            return cursor.count
        }
    }


   fun isAcceptable(context: Context, item: Item?): IncapableCause? {
        if (!isSelectableType(context, item)) {
            return IncapableCause(context.getString(R.string.error_file_type))
        }
       if (SelectionSpec.getInstance().filters != null) {
           SelectionSpec.getInstance().filters?.forEach {
               return it.filter(context, item)
           }
       }
        return null
    }

    private fun isSelectableType(context: Context?, item: Item?): Boolean {
        val mimeTypeSet = SelectionSpec.getInstance().mimeTypeSet

        if (context == null || mimeTypeSet == null) return false

        val resolver = context.contentResolver
        for (type in mimeTypeSet) {
            if (MimeTypeManager.checkType(resolver, item?.getContentUri(), type.getValue())) {
                return true
            }
        }
        return false
    }

}