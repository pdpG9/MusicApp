package com.example.musicapp.presentation.screen.player

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.data.CommandEnum
import com.example.musicapp.data.MusicData
import com.example.musicapp.databinding.ScreenPlayerBinding
import com.example.musicapp.presentation.controller.MusicController
import com.example.musicapp.presentation.controller.MusicManager
import com.example.musicapp.presentation.service.MusicService
import com.example.musicapp.presentation.service.MyMusicPlayer
import com.example.musicapp.utils.changeStatusBarColor
import com.example.musicapp.utils.convertToMMSS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerScreen : Fragment(R.layout.screen_player) {
    private val binding by viewBinding(ScreenPlayerBinding::bind)
    @Inject
    lateinit var musicController: MusicController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().window.changeStatusBarColor(R.color.color_actionbar_player_screen)
        binding.btNextMusic.setOnClickListener { musicController.doneCommand(CommandEnum.NEXT) }
        binding.btPrevMusic.setOnClickListener { musicController.doneCommand(CommandEnum.PREV) }
        binding.btPlay.setOnClickListener { musicController.doneCommand(CommandEnum.MANAGE) }
        MusicManager.playMusic.observe(viewLifecycleOwner, playMusicObserver)
        MusicManager.isPlay.observe(viewLifecycleOwner, isPlayingObserver)
        MusicManager.isCancel.observe(viewLifecycleOwner, isCancellingObserver)
        MusicManager.liveTime.observe(viewLifecycleOwner, currentTimeObserver)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MyMusicPlayer.getMusicPlayer().seekTo(progress)
                    MusicManager.currentTime = progress.toLong()
                    seekBar?.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

    }
    private val playMusicObserver = Observer<MusicData> {
        binding.apply {
            tvTitleSong.text = it.title
            tvSinger.text = it.artist
            tvRightTime.text = it.duration.convertToMMSS()
            seekBar.max = it.duration.toInt()
        }
    }

    private val isPlayingObserver = Observer<Boolean> {
        if (it) binding.btPlayTop.setImageResource(R.drawable.ic_pause2)
        else binding.btPlayTop.setImageResource(R.drawable.ic_play)
    }
    private val isCancellingObserver = Observer<Boolean> {
        binding.apply {
            btPlayTop.setImageResource(R.drawable.ic_play)
            seekBar.progress = 0
            tvLeftTime.text = seekBar.progress.toLong().convertToMMSS()
            ivMusicAvatar.rotation = 0f
        }

    }

    private val currentTimeObserver = Observer<Long> {
        binding.seekBar.progress = MyMusicPlayer.getMusicPlayer().currentPosition
        binding.tvLeftTime.text = MyMusicPlayer.getMusicPlayer().currentPosition.toLong().convertToMMSS()
        animateImageView()
    }

    private fun animateImageView() {

        lifecycleScope.launch {
            ValueAnimator.ofFloat(binding.ivMusicAvatar.rotation, binding.ivMusicAvatar.rotation + 25).apply {
                addUpdateListener {
                    binding.ivMusicAvatar.rotation = it.animatedValue as Float
                }
                duration = 1000
                interpolator = LinearInterpolator()
                start()
            }
           ValueAnimator.ofFloat(0f,5f).apply {
               addUpdateListener {
                   binding.ivLaser.translationY = it.animatedValue as Float
               }
               duration = 1000
               start()
           }
        }

    }

    private fun startMyService(action: CommandEnum) {
        val intent = Intent(requireContext(), MusicService::class.java)
        intent.putExtra("COMMAND", action)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }
}