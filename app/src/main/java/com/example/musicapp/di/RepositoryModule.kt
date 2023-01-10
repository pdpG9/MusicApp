package com.example.musicapp.di

import com.example.musicapp.domain.repository.MusicPlayerRepository
import com.example.musicapp.domain.repository.MusicPlayerRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRepository(imp:MusicPlayerRepositoryImp):MusicPlayerRepository
}