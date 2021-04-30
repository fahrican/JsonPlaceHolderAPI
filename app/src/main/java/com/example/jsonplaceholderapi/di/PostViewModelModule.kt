package com.example.jsonplaceholderapi.di

import com.example.jsonplaceholderapi.repository.PostRepository
import com.example.jsonplaceholderapi.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class PostViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: PostRepositoryImpl): PostRepository

}