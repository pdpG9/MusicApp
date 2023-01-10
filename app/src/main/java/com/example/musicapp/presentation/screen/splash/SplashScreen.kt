package com.example.musicapp.presentation.screen.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.databinding.ScreenSplashBinding
import com.example.musicapp.presentation.viewmodel.SplashViewModel
import com.example.musicapp.utils.changeStatusBarColor
import com.example.musicapp.utils.checkPermissions
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private val vm: SplashViewModel by viewModels<SplashViewModelImp>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().window.changeStatusBarColor(R.color.color_actionbar_splash_screen)
        requireActivity().checkPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                vm.getMusicFromLocal()
        }
}
}