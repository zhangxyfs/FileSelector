package com.z7dream.lib.selector.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.z7dream.lib.selector.Z7Plugin
import com.z7dream.lib.selector.room.dao.file.FileStarDao
import com.z7dream.lib.selector.room.entity.file.FileStarEntity
import com.z7dream.lib.selector.room.migrate.RoomDBMigration

@Database(
    version = 1, exportSchema = false, entities = [
        FileStarEntity::class
    ]
)
abstract class RoomDB() : RoomDatabase() {

    /**
     * 星标文件列表
     */
    abstract fun fileStartDao(): FileStarDao

    companion object {
        private var instance: RoomDB? = null

        @Synchronized
        fun get(): RoomDB = instance ?: synchronized(this) {
            val context = Z7Plugin.instance.getListener()!!.applicationContext()
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java, "RoomDatabase.db"
            ).addMigrations(
                RoomDBMigration.migration_1_2()
            ).build()

    }
}