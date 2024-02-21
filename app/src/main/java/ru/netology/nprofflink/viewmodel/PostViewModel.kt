package ru.netology.nprofflink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nprofflink.db.AppDb
import ru.netology.nprofflink.dto.Post
import ru.netology.nprofflink.repository.PostRepository
import ru.netology.nprofflink.repository.PostRepositoryImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        application,
        AppDb.getInstance(application).postDao()
    )

    private val cached = repository.data.cachedIn(viewModelScope)

    val data = cached


    fun loadPosts() {
        viewModelScope.launch {
            try {
                repository.getLatest(10)
            } catch (e: Exception) {
                error("Loading failed")
            }
        }
    }
    init {
//        loadPosts()
    }
}