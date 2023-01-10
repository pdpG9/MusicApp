package com.example.musicapp.presentation.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

interface AppNavigator {
    val  navigationFlow:Flow<NavController.()->Unit>
}