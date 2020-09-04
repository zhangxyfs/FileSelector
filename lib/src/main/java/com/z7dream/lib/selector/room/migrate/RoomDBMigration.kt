package com.z7dream.lib.selector.room.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object RoomDBMigration {
    fun migration_1_2() = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
}