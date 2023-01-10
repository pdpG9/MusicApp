package com.example.musicapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.data.MusicData
import com.example.musicapp.presentation.pager.TracksPage

class DirectoryAdapter(mFragmentActivity: FragmentActivity,private val list:List<String>):FragmentStateAdapter(mFragmentActivity) {
    private var tracks = ArrayList<MusicData>()
    private var trackLickListener:((MusicData)->Unit)? = null

    fun setListener(block:(MusicData)->Unit){
        trackLickListener = block
    }

    fun setTracks(list:List<MusicData>){
        tracks.clear()
        tracks.addAll(list)
    }

    override fun getItemCount(): Int = list.size


    override fun createFragment(position: Int): Fragment {
        return when(list[position]){
            "Tracks"->{TracksPage(tracks, trackLickListener!!)}
            else -> {TracksPage(tracks, trackLickListener!!)}
        }
    }
}