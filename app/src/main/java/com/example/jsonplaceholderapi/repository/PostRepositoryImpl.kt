package com.example.jsonplaceholderapi.repository

import android.util.Log
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.network.JsonPlaceHolderApi
import com.example.jsonplaceholderapi.util.ResultState
import retrofit2.HttpException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val webService: JsonPlaceHolderApi
) : BaseRepository() {

    override suspend fun getPosts(): ResultState<ArrayList<PostsItem>> {
        var result: ResultState<ArrayList<PostsItem>> = handleSuccess(ArrayList())
        try {
            val response = webService.getPosts()
            response.let {
                it.body()?.let { posts ->
                    result = handleSuccess(posts)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleException(errorCode)
                        }
                    } else result = handleException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            Log.e("PostRepositoryImpl", "Error: ${error.message}")
            return handleException(error.code())
        }
        return result
    }

    override suspend fun getPostById(id: Int): PostsItem = webService.getPostById(id)

    override suspend fun postPostsItem(postsItem: PostsItem): PostsItem {
        return webService.postPostsItem(postsItem)
    }

    override suspend fun deletePostById(id: Int): PostsItem = webService.deletePostById(id)

    override suspend fun putPostById(id: Int, item: PostsItem): PostsItem {
        return webService.putPostById(id, item)
    }
}