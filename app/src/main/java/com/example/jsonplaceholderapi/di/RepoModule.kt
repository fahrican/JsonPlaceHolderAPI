package com.example.jsonplaceholderapi.di

import com.example.jsonplaceholderapi.network.JsonPlaceHolderApi
import com.example.jsonplaceholderapi.network.JsonPlaceHolderWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideNewsApiWebService(): JsonPlaceHolderApi = JsonPlaceHolderWebService.getApiClient()

}