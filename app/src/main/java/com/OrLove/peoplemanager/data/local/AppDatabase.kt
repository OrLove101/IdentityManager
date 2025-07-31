package com.OrLove.peoplemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.OrLove.peoplemanager.data.local.AppDatabase.Companion.DATABASE_VERSION
import com.OrLove.peoplemanager.data.local.dao.IdentitiesDao
import com.OrLove.peoplemanager.data.local.entity.IdentityEntity

@Database(
    entities = [
        IdentityEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun identitiesDao(): IdentitiesDao

    companion object {
        const val DATABASE_VERSION = 1

        fun initDatabase(
            context: Context
        ): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_db"
            ).build()
        }
    }
}