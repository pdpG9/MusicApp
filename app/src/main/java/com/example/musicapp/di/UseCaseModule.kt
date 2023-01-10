package com.example.musicapp.di

import com.example.musicapp.domain.usecase.CursorReferenceUseCase
import com.example.musicapp.domain.usecase.GetMusicFromLocalUseCase
import com.example.musicapp.domain.usecase.imp.CursorReferenceUseCaseImp
import com.example.musicapp.domain.usecase.imp.GetMusicFromLocalUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetCursorUseCase(imp:CursorReferenceUseCaseImp):CursorReferenceUseCase
    @Binds
    fun bindGetLocalStorageUseCase(imp:GetMusicFromLocalUseCaseImp):GetMusicFromLocalUseCase
}