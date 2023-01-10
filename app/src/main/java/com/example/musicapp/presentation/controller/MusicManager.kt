package com.example.musicapp.presentation.controller

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.CommandEnum
import com.example.musicapp.data.MusicData

object MusicManager {
    var selectMusicPosition: Int = -1
    var lastCommand: CommandEnum = CommandEnum.PLAY
    var cursor: Cursor? = null
    var currentTime: Long = 0L
    var fullTime: Long = 0L
    var audioSession:Int = 0

    val playMusic = MutableLiveData<MusicData>()
    val isPlay = MutableLiveData<Boolean>()
    val liveTime = MutableLiveData<Long>()
    val isCancel = MutableLiveData<Boolean>()
}