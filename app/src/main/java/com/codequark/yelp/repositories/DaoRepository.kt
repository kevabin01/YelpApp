package com.codequark.yelp.repositories

import android.content.Context
import androidx.annotation.NonNull
import androidx.paging.Pager
import androidx.paging.PagingData
import com.codequark.yelp.executors.ioThread
import com.codequark.yelp.room.AppDatabase
import com.codequark.yelp.room.dao.LocalBusinessDao
import com.codequark.yelp.room.models.LocalBusiness
import kotlinx.coroutines.flow.Flow

open class DaoRepository(@NonNull context: Context): BaseRepository() {
    @NonNull
    private val database = AppDatabase.getInstance(context)

    @NonNull
    private val localBusinessDao: LocalBusinessDao = database.localBusinessDao()

    @NonNull
    fun getLocalBusinesses(): Flow<PagingData<LocalBusiness>> {
        return Pager(pagingConfig) {
            localBusinessDao.getLocalBusinesses()
        }.flow
    }

    fun insert(@NonNull model: LocalBusiness) = ioThread {
        this.localBusinessDao.insert(model)
    }

    fun insert(@NonNull list: List<LocalBusiness>) = ioThread {
        this.localBusinessDao.insert(list)
    }

    fun replace(@NonNull model: LocalBusiness) = ioThread {
        this.localBusinessDao.replace(model)
    }

    fun replace(@NonNull list: List<LocalBusiness>) = ioThread {
        this.localBusinessDao.replace(list)
    }

    fun update(@NonNull model: LocalBusiness) = ioThread {
        this.localBusinessDao.update(model)
    }

    fun update(@NonNull list: List<LocalBusiness>) = ioThread {
        this.localBusinessDao.update(list)
    }

    fun delete(@NonNull model: LocalBusiness) = ioThread {
        this.localBusinessDao.delete(model)
    }

    fun delete(@NonNull list: List<LocalBusiness>) = ioThread {
        this.localBusinessDao.delete(list)
    }

    fun deleteAll() = ioThread {
        this.localBusinessDao.deleteAll()
    }
}