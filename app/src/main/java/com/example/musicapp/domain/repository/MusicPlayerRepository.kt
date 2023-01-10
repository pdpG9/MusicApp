package com.example.musicapp.domain.repository

import android.database.Cursor
import com.example.musicapp.data.MusicData
import kotlinx.coroutines.flow.Flow

interface MusicPlayerRepository {
    fun getAllMusicsEntity(): Flow<List<MusicData>>
    fun getDirectors(): Flow<List<String>>

    suspend fun getMusicFromLocal()
    fun getCursor(): Cursor
}