package com.example.musicapp.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.GetMusicFromLocalUseCase
import com.example.musicapp.presentation.navigation.NavigationHandler
import com.example.musicapp.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImp @Inject constructor(
    private val navigationHandler: NavigationHandler,
    private val getMusicFromLocalUseCase: GetMusicFromLocalUseCase
) : ViewModel(), SplashViewModel {

    override fun getMusicFromLocal() {
        getMusicFromLocalUseCase.execute().onEach {
            delay(1000)
            navigationHandler.navigationTo(SplashScreenDirections.actionSplashScreenToMainScreen())
        }.launchIn(viewModelScope)
    }
}