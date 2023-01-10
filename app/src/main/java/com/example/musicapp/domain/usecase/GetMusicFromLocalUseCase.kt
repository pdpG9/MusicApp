package com.example.musicapp.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetMusicFromLocalUseCase {
    fun execute(): Flow<Unit>
}