package com.example.musicapp.presentation.viewmodel

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.example.musicapp.data.MusicData
import kotlinx.coroutines.flow.StateFlow

interface MainViewModel {
    val directorsFlow:StateFlow<List<String>>
    val cursorFlow:StateFlow<Cursor?>

    fun clickNext()
    fun clickPrev()
    fun clickPlay()
    fun clickItemPlayer()
    fun selectedMusic(position:Int)
}