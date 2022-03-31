package com.codequark.yelp.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.codequark.yelp.room.models.LocalBusiness

@Dao
interface LocalBusinessDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localBusiness: LocalBusiness)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localBusinesses: List<LocalBusiness>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(localBusiness: LocalBusiness)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(localBusinesses: List<LocalBusiness>)

    @Update
    fun update(localBusiness: LocalBusiness)

    @Update
    fun update(localBusinesses: List<LocalBusiness>)

    @Delete
    fun delete(localBusiness: LocalBusiness)

    @Delete
    fun delete(localBusinesses: List<LocalBusiness>)

    @Query("DELETE FROM tableLocalBusiness")
    fun deleteAll()

    @Query("SELECT * FROM tableLocalBusiness ORDER BY id")
    fun getLocalBusinesses(): PagingSource<Int, LocalBusiness>
}