package com.example.jsonplaceholderapi.network

import com.example.jsonplaceholderapi.model.PostsItem
import retrofit2.Response
import retrofit2.http.*

interface JsonPlaceHolderApi {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    suspend fun getPosts(): Response<ArrayList<PostsItem>>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): PostsItem

    @POST("posts")
    suspend fun postPostsItem(@Body postsItem: PostsItem): PostsItem

    @DELETE("posts/{id}")
    suspend fun deletePostById(@Path("id") id: Int): PostsItem

    @PUT("posts/{id}")
    suspend fun putPostById(@Path("id") id: Int, @Body postsItem: PostsItem): PostsItem
}