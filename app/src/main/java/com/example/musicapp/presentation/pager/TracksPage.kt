package com.example.musicapp.presentation.pager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.data.MusicData
import com.example.musicapp.databinding.PageTrackBinding
import com.example.musicapp.presentation.adapter.MusicAdapter

class TracksPage(private val list: List<MusicData>, private val itemClick: ((MusicData) -> Unit)) : Fragment(R.layout.page_track) {
    private val binding by viewBinding(PageTrackBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MusicAdapter(itemClick)
        binding.rvTracks.adapter = adapter
        adapter.submitList(list)
    }
}