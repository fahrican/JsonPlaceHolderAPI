package com.example.jsonplaceholderapi.repository

import com.example.jsonplaceholderapi.util.ResultState

abstract class BaseRepository : PostRepository {

    companion object {
        const val GENERAL_ERROR_CODE = 499
        private const val UNAUTHORIZED = "Unauthorized"
        private const val NOT_FOUND = "Not found"
        const val SOMETHING_WRONG = "Something went wrong"

        fun <T : Any> handleSuccess(data: T): ResultState<T> {
            return ResultState.Success(data)
        }

        fun <T : Any> handleException(code: Int): ResultState<T> {
            val exception = getErrorMessage(code)
            return ResultState.Error(Exception(exception))
        }

        private fun getErrorMessage(httpCode: Int): String {
            return when (httpCode) {
                401 -> UNAUTHORIZED
                404 -> NOT_FOUND
                else -> SOMETHING_WRONG
            }
        }
    }
}