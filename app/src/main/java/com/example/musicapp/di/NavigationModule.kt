package com.example.musicapp.di

import com.example.musicapp.presentation.navigation.AppNavigationManager
import com.example.musicapp.presentation.navigation.AppNavigator
import com.example.musicapp.presentation.navigation.NavigationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigator(imp: AppNavigationManager): AppNavigator

    @Binds
    fun bindNavigationHandler(imp: AppNavigationManager): NavigationHandler
}