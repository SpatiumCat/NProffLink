package ru.netology.nprofflink.repository

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.netology.nprofflink.api.PostApi
import ru.netology.nprofflink.dao.PostDao
import ru.netology.nprofflink.dto.Post
import ru.netology.nprofflink.entity.PostEntity
import ru.netology.nprofflink.entity.toDto
import ru.netology.nprofflink.entity.toEntity
import ru.netology.nprofflink.error.ApiError
import ru.netology.nprofflink.error.NetworkError
import ru.netology.nprofflink.error.UnknownError
import java.io.IOException
import java.lang.Exception

class PostRepositoryImpl(
    private val application: Application,
    private val postDao: PostDao
) : PostRepository {

    private var posts = emptyList<Post>()

//    override val data: Flow<List<Post>> = postDao.getAll()
//        .map { it.toDto() }
//        .onEach { posts = it }
//        .flowOn(Dispatchers.Default)

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 25, enablePlaceholders = false),
        pagingSourceFactory = { postDao.getPagingSource() },
        remoteMediator = PostRemoteMediator(
           application
        )
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto)
    }

    override suspend fun showAll() {
    }

    override suspend fun getAll() {
    }

    override suspend fun likeById(id: Long) {
    }

    override suspend fun deleteLikeById(id: Long) {
    }

    override suspend fun shareById(id: Long) {
    }

    override suspend fun removeById(id: Long) {
    }

    override suspend fun save(post: Post) {
    }

    override suspend fun retrySaving(post: Post) {
    }

    override suspend fun getLatest(count: Int) {
        try {
            val response = PostApi.retrofitService.getLatest(count)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError
        }
    }
}