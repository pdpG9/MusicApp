package com.example.musicapp.presentation.controller

import com.example.musicapp.data.CommandEnum

interface MusicController {
    fun doneCommand(command: CommandEnum)
    fun onCleared()
}