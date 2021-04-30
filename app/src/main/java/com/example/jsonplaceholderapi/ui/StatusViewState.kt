package com.example.jsonplaceholderapi.ui

import com.example.jsonplaceholderapi.common.Status

class StatusViewState(val status: Status) {

    fun isLoading() = status is Status.Loading

    fun getErrorMessage() = if (status is Status.Error) status.exception.message else ""

    fun shouldShowErrorMessage() = status is Status.Error
}