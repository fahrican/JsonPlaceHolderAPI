package com.example.jsonplaceholderapi.repository

import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.util.ResultState

interface PostRepository {

    suspend fun getPosts(): ResultState<ArrayList<PostsItem>>

    suspend fun getPostById(id: Int): PostsItem

    suspend fun postPostsItem(postsItem: PostsItem): PostsItem

    suspend fun deletePostById(id: Int): PostsItem

    suspend fun putPostById(id: Int, item: PostsItem): PostsItem
}