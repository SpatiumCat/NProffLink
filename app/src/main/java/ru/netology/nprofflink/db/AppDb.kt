package ru.netology.nprofflink.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.InternalCoroutinesApi
import ru.netology.nprofflink.converter.LikeOwnerIdsConverter
import ru.netology.nprofflink.converter.MentionsIdsConverter
import ru.netology.nprofflink.dao.PostDao
import ru.netology.nprofflink.dao.PostRemoteKeyDao
import ru.netology.nprofflink.entity.PostEntity
import ru.netology.nprofflink.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 1)
@TypeConverters(LikeOwnerIdsConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao

    companion object {
        @Volatile
        private var instance: AppDb? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): AppDb {
            return instance ?: kotlinx.coroutines.internal.synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()

    }
}