package com.example.jsonplaceholderapi.repository

import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.network.JsonPlaceHolderApi
import retrofit2.Call
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val webService: JsonPlaceHolderApi
) : PostRepository {

    override suspend fun getPosts(): ArrayList<PostsItem> = webService.getPosts()

    override suspend fun getPostById(id: Int): PostsItem = webService.getPostById(id)

    override suspend fun postPostsItem(postsItem: PostsItem): PostsItem {
        return webService.postPostsItem(postsItem)
    }

    override suspend fun deletePostById(id: Int): PostsItem = webService.deletePostById(id)

    override suspend fun putPostById(id: Int, item: PostsItem): PostsItem {
        return webService.putPostById(id, item)
    }
}