package com.OrLove.peoplemanager.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.OrLove.peoplemanager.data.local.entity.IdentityEntity

@Dao
abstract class IdentitiesDao: BaseDao<IdentityEntity>() {

    @Query(
        """
            SELECT
              *
            FROM
              Identity
        """
    )
    abstract suspend fun getAll(): List<IdentityEntity>

    @Query(
        """
            DELETE FROM
                Identity
        """
    )
    abstract override suspend fun clear()
}