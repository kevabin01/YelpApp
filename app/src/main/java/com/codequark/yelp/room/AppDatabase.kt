package com.codequark.yelp.room

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codequark.yelp.room.dao.LocalBusinessDao
import com.codequark.yelp.room.models.LocalBusiness

@Database(
    entities = [LocalBusiness::class],
    version = RoomConstants.databaseVersion
)
abstract class AppDatabase: RoomDatabase() {
    @NonNull
    abstract fun localBusinessDao(): LocalBusinessDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(@NonNull context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val it = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    RoomConstants.databaseName
                ).build()

                instance = it
                it
            }
        }
    }
}