package ru.netology.nprofflink.repository

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.netology.nprofflink.api.PostApi
import ru.netology.nprofflink.db.AppDb
import ru.netology.nprofflink.entity.PostEntity
import ru.netology.nprofflink.entity.PostRemoteKeyEntity
import ru.netology.nprofflink.entity.toEntity
import ru.netology.nprofflink.error.ApiError
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val application: Application
) : RemoteMediator<Int, PostEntity>() {

    private val postDao = AppDb.getInstance(application).postDao()
    private val postRemoteKeyDao = AppDb.getInstance(application).postRemoteKeyDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    val id = postRemoteKeyDao.max()
                    if (id == null) {
                        PostApi.retrofitService.getLatest(state.config.pageSize)
                    } else {
                        PostApi.retrofitService.getAfter(id, state.config.pageSize)
                    }
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(false)
                }

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    PostApi.retrofitService.getBefore(id, state.config.pageSize)
                }
            }

            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }
            val body = result.body() ?: throw ApiError(result.code(), result.message())

            AppDb.getInstance(application).withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {

                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.AFTER,
                                body.ifEmpty { return@withTransaction }.first().id
                            )
                        )
                        if (postRemoteKeyDao.getAll().isEmpty()) {
                            postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    body.ifEmpty{ return@withTransaction }.last().id
                                )
                            )
                        }
                    }

                    LoadType.PREPEND -> {
                    }

                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id
                            )
                        )
                    }
                }
                postDao.insert(body.map { it.copy() }.toEntity())
            }

            return MediatorResult.Success(body.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
