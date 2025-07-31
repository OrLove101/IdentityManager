package com.OrLove.peoplemanager.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

abstract class BaseDao<in T> {

    private val ignoredId = -1L

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(value: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(values: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(value: T)

    @Update
    abstract suspend fun update(values: List<T>)

    @Delete
    abstract suspend fun delete(value: T)

    @Delete
    abstract suspend fun delete(value: List<T>)

    abstract suspend fun clear()

    open suspend fun insertOrUpdate(value: T) {
        val id = insert(value)
        if (id == ignoredId) {
            update(value)
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(list: List<T>) {
        list.forEach {
            insertOrUpdate(it)
        }
    }
}