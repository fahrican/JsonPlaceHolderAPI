package com.example.jsonplaceholderapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.repository.BaseRepository
import com.example.jsonplaceholderapi.repository.PostRepository
import com.example.jsonplaceholderapi.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private var _posts = MutableLiveData<ResultState<ArrayList<PostsItem>>>()
    val posts: LiveData<ResultState<ArrayList<PostsItem>>>
        get() = _posts

    fun fetchPosts() = viewModelScope.launch {
        _posts.postValue(ResultState.InProgress)
        viewModelScope.launch {
            val response = repository.getPosts()
            response.let { postList ->
                when (postList) {
                    is ResultState.Success<*> -> {
                        val posts = (postList.extractData as ArrayList<PostsItem>)
                        _posts.postValue(ResultState.Success(posts))
                        Log.d("fetchPosts()", "response: ${response}")
                    }
                    else -> {
                        _posts.postValue(ResultState.Error(Exception()))
                        Log.e("fetchPosts()", "catch block")
                    }
                }
            }
        }

    }

    fun fetchPostById(id: Int) = viewModelScope.launch {
        try {
            val response = repository.getPostById(id)
            Log.d("fetchPostById()", "response: ${response}")
        } catch (t: Throwable) {
            Log.e("fetchPostById()", "catch block: ${t.message}")
        }
    }

    fun createPostsItem(postsItem: PostsItem) = viewModelScope.launch {
        try {
            val response = repository.postPostsItem(postsItem)
            Log.d("createPostsItem()", "response: ${response}")
        } catch (t: Throwable) {
            Log.e("createPostsItem()", "catch block: ${t.message}")
        }
    }

    fun removePostById(id: Int) = viewModelScope.launch {
        try {
            val response = repository.deletePostById(id)
            Log.d("removePostById()", "response: ${response}")
        } catch (t: Throwable) {
            Log.e("removePostById()", "catch block: ${t.message}")
        }
    }

    fun updatePostById(id: Int, item: PostsItem) = viewModelScope.launch {
        try {
            val response = repository.putPostById(id, item)
            Log.d("updatePostById()", "response: ${response}")
        } catch (t: Throwable) {
            Log.e("updatePostById()", "catch block: ${t.message}")
        }
    }
}