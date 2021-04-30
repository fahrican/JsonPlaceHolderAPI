package com.example.jsonplaceholderapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.jsonplaceholderapi.common.Status
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.repository.PostRepository
import com.example.jsonplaceholderapi.ui.StatusViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private var _posts = MutableLiveData<ArrayList<PostsItem>>()
    val posts: LiveData<ArrayList<PostsItem>>
        get() = _posts

    private val _status = MutableLiveData<StatusViewState>()
    val status: LiveData<StatusViewState> = _status


    fun fetchPosts() = viewModelScope.launch {
        _status.value = StatusViewState(Status.Loading)
        try {
            val response: ArrayList<PostsItem> = repository.getPosts()
            _posts.value = response
            Log.d("fetchPosts()", "response: ${response}")
            _status.value = StatusViewState(Status.Content)
        } catch (t: Throwable) {
            _status.value = StatusViewState(Status.Error(t))
            Log.e("fetchPosts()", "catch block: ${t.message}")
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