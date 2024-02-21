package ru.netology.nprofflink.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nprofflink.api.PostApi
import ru.netology.nprofflink.dto.Post
import ru.netology.nprofflink.error.ApiError
import java.io.IOException

class PostPagingSource(): PagingSource<Long, Post>() {

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try{
            val result = when (params) {
                is LoadParams.Refresh -> {
                    PostApi.retrofitService.getLatest(params.loadSize)
                }
                is LoadParams.Append -> {
                    PostApi.retrofitService.getBefore(
                        id = params.key,
                        count = params.loadSize
                    )
                }
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(), nextKey = null, prevKey = params.key
                )
            }
            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }
            val data = result.body().orEmpty()
            return LoadResult.Page(
                data = data,
                prevKey = params.key,
                nextKey = data.lastOrNull()?.id
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}