package ru.netology.nprofflink.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nprofflink.dto.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>

    suspend fun showAll()
    suspend fun getAll()
    suspend fun getLatest(count: Int)
    suspend fun likeById(id: Long)
    suspend fun deleteLikeById (id: Long)
    suspend fun shareById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    //suspend fun saveWithAttachment(post: Post, photoModel: PhotoModel)
    suspend fun retrySaving(post: Post)

}