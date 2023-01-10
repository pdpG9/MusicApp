package com.example.musicapp.domain.usecase.imp

import com.example.musicapp.domain.repository.MusicPlayerRepository
import com.example.musicapp.domain.usecase.GetMusicFromLocalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMusicFromLocalUseCaseImp @Inject constructor(private val repository: MusicPlayerRepository) : GetMusicFromLocalUseCase {
    override fun execute(): Flow<Unit> = flow { emit(repository.getMusicFromLocal()) }
}