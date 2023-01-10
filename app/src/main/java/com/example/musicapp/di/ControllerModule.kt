package com.example.musicapp.di

import com.example.musicapp.presentation.controller.MusicController
import com.example.musicapp.presentation.service.MusicService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ControllerModule {
    @Binds
    fun bindMusicController(imp: MusicService): MusicController
}