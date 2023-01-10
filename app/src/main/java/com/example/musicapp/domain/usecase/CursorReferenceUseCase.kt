package com.example.musicapp.domain.usecase

import android.database.Cursor
import kotlinx.coroutines.flow.Flow

interface CursorReferenceUseCase {
    fun execute(): Flow<Cursor>
}