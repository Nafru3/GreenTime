package com.example.greentime.di

import com.example.greentime.domain.repository.AppRepositoryImpl
import com.example.greentime.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository
}