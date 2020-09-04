package com.z7dream.lib.selector.room.dao.file

import android.database.Cursor
import androidx.room.*
import com.z7dream.lib.selector.room.entity.file.FileStarEntity

@Dao
interface FileStarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity : FileStarEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<FileStarEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: FileStarEntity)

    @Delete
    fun deleteOne(entity: FileStarEntity)

    //删除 根据 用户id 公司id 文件路径列表
    @Query("delete from FileStarEntity where userId=:userId and companyId=:companyId and _data in (:filePath)")
    fun delete(userId: Long, companyId: Long, filePath: Array<String>)

    //删除 根据 用户id 文件路径列表
    @Query("delete from FileStarEntity where userId=:userId and _data in (:filePath)")
    fun delete(userId: Long, filePath: Array<String>)

    //模糊查询星标 根据 用户id 公司id 文件路径
    @Query("select * from FileStarEntity where userId=:userId and _data=:filePath and companyId=:companyId")
    fun selectOne(userId: Long, companyId: Long, filePath: String): FileStarEntity?

    //模糊查询星标 根据 用户id 文件路径
    @Query("select * from FileStarEntity where userId=:userId and _data=:filePath")
    fun selectOne(userId: Long, filePath: String): FileStarEntity?

    //模糊查询星标列表 根据 用户id 公司id 文件路径列表
    @Query("select * from FileStarEntity where userId=:userId and companyId=:companyId and _data in(:filePath)")
    fun selectByPaths(userId: Long, companyId: Long, filePath: Array<String>): List<FileStarEntity>

    //模糊查询星标列表 根据 用户id 文件路径
    @Query("select * from FileStarEntity where userId=:userId and _data in(:filePath)")
    fun selectByPaths(userId: Long, filePath: Array<String>): List<FileStarEntity>

    //模糊查询星标列表 根据 用户id 公司id
    @Query("select * from FileStarEntity where userId=:userId and companyId=:companyId")
    fun findStarList(userId: Long, companyId: Long): List<FileStarEntity>

    //模糊查询星标列表 根据 用户id
    @Query("select * from FileStarEntity where userId=:userId")
    fun findStarList(userId: Long): List<FileStarEntity>

    //模糊查询星标列表 根据 用户id
    @Query("select * from FileStarEntity where userId=:userId and _data like '%'||:searchKey||'%' COLLATE NOCASE")
    fun findStarList(userId: Long, searchKey: String): List<FileStarEntity>

    //模糊查询星标列表 根据 用户id 公司id
    @Query("select * from FileStarEntity where userId=:userId and companyId=:companyId and _data like '%'||:searchKey||'%' COLLATE NOCASE")
    fun findCursorStarList(userId: Long, companyId: Long, searchKey: String): Cursor

    //模糊查询星标列表 根据 用户id
    @Query("select * from FileStarEntity where userId=:userId and _data like '%'||:searchKey||'%' COLLATE NOCASE")
    fun findCursorStarList(userId: Long, searchKey: String): Cursor
}