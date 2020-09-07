package com.z7dream.lib.selector.room.manager.file

import android.annotation.SuppressLint
import android.database.Cursor
import android.text.TextUtils
import android.util.Log
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.room.BoxRoomDB
import com.z7dream.lib.selector.room.dao.file.FileStarDao
import com.z7dream.lib.selector.room.entity.file.FileStarEntity
import com.z7dream.lib.selector.utils.callback.Z7Callback
import com.z7dream.lib.selector.utils.rx.RxSchedulersHelper
import io.reactivex.rxjava3.core.Observable
import java.io.File
import kotlin.math.floor

@SuppressLint("CheckResult")
class FileStarManager private constructor() {

    fun rename(file: File, oldPath: String, userId: Long, companyId: Long?) {
        val entity: FileStarEntity?
        if (companyId == null || companyId <= 0) {
            entity = dao().selectOne(userId, oldPath)
        } else {
            entity = dao().selectOne(userId, companyId, oldPath)
        }
        entity?.apply {
            entity._data = file.path
            entity._display_name = file.name
            entity.date_modified = file.lastModified()
            dao().update(entity)
        }
    }


    fun toStarFile(
        userId: Long?,
        companyId: Long?,
        fileItemList: List<Item>,
        callback: Z7Callback.Callback<String>?
    ) {
        val fileHasMap = HashMap<String, Item>()
        val filePathList = ArrayList<String>()
        for (item in fileItemList) {
            fileHasMap[item.filePath] = item
            filePathList.add(item.filePath)
        }

        val listArrays = createListArray(filePathList, 900)
        val needList = ArrayList<FileStarEntity>()
        if (userId == null) {
            callback?.event("succ")
        } else {
            Observable.just(1)
                .map {
                    if (companyId != null && companyId > 0) {
                        for (array in listArrays) {
                            needList.addAll(dao().selectByPaths(userId, companyId, array))
                        }
                    } else {
                        for (array in listArrays) {
                            needList.addAll(dao().selectByPaths(userId, array))
                        }
                    }
                }
                .map {
                    if (needList.size != filePathList.size) {
                        val needMap = HashMap<String, FileStarEntity>()
                        for (entity in needList) {
                            entity._data?.apply {
                                needMap[this] = entity
                            }
                        }
                        val needInsertList = ArrayList<FileStarEntity>()
                        for (entry in fileHasMap.entries) {
                            if (needMap[entry.key] == null) {
                                val item = entry.value
                                val entity = FileStarEntity()
                                entity.userId = userId
                                if (companyId != null && companyId > 0) {
                                    entity.companyId = companyId
                                }
                                entity._data = item.filePath
                                entity._display_name = item.displayName
                                entity._size = item.size
                                entity.date_modified = item.dataTime
                                entity.mime_type = item.mimeType
                                entity.parent = item.parentId
                                entity.relative_path = item.parentId
                                needInsertList.add(entity)
                            }
                        }

                        if (needInsertList.size > 0) {
                            dao().insert(needInsertList)
                        }
                    }
                }
                .compose(RxSchedulersHelper.io())
                .subscribe(
                    {
                        callback?.event("succ")
                    },
                    {
                        it.printStackTrace()
                        callback?.event("succ")
                    }
                )
        }
    }

    fun removeStarFile(
        userId: Long?,
        companyId: Long?,
        fileItemList: List<Item>,
        callback: Z7Callback.Callback<String>?
    ) {
        val fileHasMap = HashMap<String, Item>()
        val filePathList = ArrayList<String>()
        for (item in fileItemList) {
            fileHasMap[item.filePath] = item
            filePathList.add(item.filePath)
        }

        val listArrays = createListArray(filePathList, 900)
        if (userId == null) {
            callback?.event("succ")
        } else {
            Observable.fromIterable(listArrays)
                .map {
                    if (companyId != null && companyId > 0L) {
                        dao().delete(userId, companyId, it)
                    } else {
                        dao().delete(userId, it)
                    }
                }
                .compose(RxSchedulersHelper.io())
                .subscribe({
                    callback?.event("succ")
                }, {
                    it.printStackTrace()
                    callback?.event("succ")
                })
        }
    }

    fun findStarList(userId: Long?, companyId: Long?): List<FileStarEntity> {
        userId?.apply {
            if (companyId != null && companyId > 0) {
                val arrayList = ArrayList(dao().findStarList(userId, companyId))
                val it = arrayList.iterator()
                while (it.hasNext()) {
                    val info = it.next()
                    if (TextUtils.isEmpty(info._data) || !File(info._data).isFile) {
                        it.remove()
                    }
                }
                return arrayList
            } else {
                return findStarList(userId)
            }
        }
        return ArrayList()
    }

    fun findStarList(userId: Long?): List<FileStarEntity> {
        userId?.apply {
            val arrayList = ArrayList(dao().findStarList(userId))
            val it = arrayList.iterator()
            while (it.hasNext()) {
                val info = it.next()
                if (TextUtils.isEmpty(info._data) || !File(info._data).isFile) {
                    it.remove()
                }
            }
            return arrayList
        }
        return ArrayList()
    }

    fun findCursorStarList(userId: Long?, companyId: Long?, searchKey: String): Cursor? {
        if (userId == null) {
            return null
        }
        if (companyId != null && companyId > 0) {
            return dao().findCursorStarList(userId, companyId, searchKey)
        } else {
            return dao().findCursorStarList(userId, searchKey)
        }
    }

    fun clearStarWithFileNotExit(userId: Long?) {
        userId?.apply {
            val list = dao().findStarList(this)
            val it = list.iterator()
            while (it.hasNext()) {
                val info = it.next()
                if (TextUtils.isEmpty(info._data) || !File(info._data).exists()) {
                    dao().deleteOne(info)
                }
            }
        }
        Log.e("tag", "...........")
    }


    private fun createListArray(needList: List<String>, maxLength: Int): ArrayList<Array<String>> {
        val list = ArrayList<Array<String>>()

        val maxSize = needList.size
        if (maxSize > maxLength) {
            var size = floor((needList.size / maxLength).toDouble())
            if (maxSize.toFloat() / maxLength.toFloat() > size.toFloat()) {
                size += 1
            }
            for (i in 0 until size.toInt()) {
                val start = i * maxLength
                var end = start + maxLength
                if (end > needList.size) {
                    end = needList.size
                }
                list.add(needList.subList(start, end).toTypedArray())
            }
        } else {
            list.add(needList.toTypedArray())
        }
        return list
    }

    private fun dao(): FileStarDao {
        if (mDao == null) {
            mDao = BoxRoomDB.get().fileStartDao()
        }
        return mDao!!
    }



    companion object {
        var mDao: FileStarDao? = null

        val instance: FileStarManager by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FileStarManager()
        }
    }
}