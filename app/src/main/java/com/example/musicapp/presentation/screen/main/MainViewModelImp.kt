package com.example.musicapp.presentation.screen.main

import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.CommandEnum
import com.example.musicapp.domain.usecase.CursorReferenceUseCase
import com.example.musicapp.presentation.controller.MusicController
import com.example.musicapp.presentation.controller.MusicManager
import com.example.musicapp.presentation.navigation.NavigationHandler
import com.example.musicapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModelImp @Inject constructor(
    private val musicController: MusicController,
    private val navigationHandler: NavigationHandler,
    private val useCase: CursorReferenceUseCase
) : ViewModel(), MainViewModel {
    override val directorsFlow = MutableStateFlow<List<String>>(emptyList())
    override val cursorFlow = MutableStateFlow<Cursor?>(null)
    override fun clickNext() {
        musicController.doneCommand(CommandEnum.NEXT)
    }

    override fun clickPrev() {
        musicController.doneCommand(CommandEnum.PREV)
    }

    override fun clickPlay() {
        musicController.doneCommand(CommandEnum.MANAGE)
    }

    override fun clickItemPlayer() {
        viewModelScope.launch {
            navigationHandler.navigationTo(MainScreenDirections.actionMainScreenToPlayerScreen())
        }
    }

    override fun selectedMusic(position: Int) {
        MusicManager.selectMusicPosition = position
    }

    init {
        useCase.execute().onEach { cursorFlow.emit(it) }.launchIn(viewModelScope)
    }

}