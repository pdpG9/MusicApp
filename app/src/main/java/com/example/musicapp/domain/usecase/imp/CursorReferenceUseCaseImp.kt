package com.example.musicapp.domain.usecase.imp

import android.database.Cursor
import com.example.musicapp.domain.repository.MusicPlayerRepository
import com.example.musicapp.domain.usecase.CursorReferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CursorReferenceUseCaseImp @Inject constructor(private val repository: MusicPlayerRepository) : CursorReferenceUseCase {
    override fun execute(): Flow<Cursor>  = flow{ emit(repository.getCursor()) }
}