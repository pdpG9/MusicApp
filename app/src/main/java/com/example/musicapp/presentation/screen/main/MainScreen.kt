package com.example.musicapp.presentation.screen.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.data.CommandEnum
import com.example.musicapp.data.MusicData
import com.example.musicapp.databinding.ScreenMainBinding
import com.example.musicapp.presentation.adapter.MusicCursorAdapter
import com.example.musicapp.presentation.controller.MusicManager
import com.example.musicapp.presentation.service.MusicService
import com.example.musicapp.presentation.viewmodel.MainViewModel
import com.example.musicapp.utils.myLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val vm: MainViewModel by viewModels<MainViewModelImp>()
    private val adapter = MusicCursorAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            rvMusics.layoutManager = LinearLayoutManager(requireContext())
            rvMusics.adapter = adapter
            itemPlayerLayout.setOnClickListener { vm.clickItemPlayer() }
            btNext.setOnClickListener { vm.clickNext() }
            btPrev.setOnClickListener { vm.clickPrev() }
            btPlay.setOnClickListener { vm.clickPlay() }
        }
        adapter.setSelectMusicPositionListener {
//            val music = MyAppManager.cursor?.getMusicDataByPosition(pos = it)
            "setSelectMusicPositionListener:$it".myLog()
            vm.selectedMusic(it)
            startMyService()

        }
        vm.cursorFlow.onEach { adapter.cursor = it }.launchIn(lifecycleScope)
        MusicManager.playMusic.observe(viewLifecycleOwner) {
            " vm.musicFlow.observe(viewLifecycleOwner)".myLog()
            if (it != null) {
                setMusicData(it)
            }
        }
        MusicManager.isPlay.observe(viewLifecycleOwner) {
            changePlayButton(it)
        }
    }

    private fun startMyService() {
        val intent = Intent(requireContext(), MusicService::class.java)
        MusicManager.lastCommand = CommandEnum.PLAY
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }

    private fun setMusicData(data: MusicData) {
        binding.apply {
            itemPlayerLayout.visibility = View.VISIBLE
            tvTitleSongItemPlayer.text = data.title
            tvAuthorSongItemPlayer.text = data.artist
        }
    }

    private fun changePlayButton(isPlay: Boolean) {
        if (isPlay) binding.btPlay.setImageResource(R.drawable.ic_pause2)
        else binding.btPlay.setImageResource(R.drawable.ic_play)
    }

}