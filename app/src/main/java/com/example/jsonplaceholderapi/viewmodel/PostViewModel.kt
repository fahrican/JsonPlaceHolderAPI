package com.example.jsonplaceholderapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.repository.PostRepository
import com.example.jsonplaceholderapi.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private var _posts = MutableLiveData<ResultState<ArrayList<PostsItem>>>()
    val posts: LiveData<ResultState<ArrayList<PostsItem>>>
        get() = _posts

    fun fetchPosts() {
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
                        _posts.postValue(postList)
                        Log.e("fetchPosts()", "catch block")
                    }
                }
            }
        }
    }
}