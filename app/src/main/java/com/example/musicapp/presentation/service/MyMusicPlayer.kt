package com.example.musicapp.presentation.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.example.musicapp.utils.myLog

class MyMusicPlayer {
    companion object {
        private var player = MediaPlayer()
        fun getMusicPlayer() = player

        fun create(context: Context, uri: Uri) {
            "context:$context".myLog()
            "uri:$uri".myLog()
            player.reset()
            player = MediaPlayer.create(context, uri)
        }
    }
}