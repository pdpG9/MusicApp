package com.example.musicapp.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.data.MusicData
import com.example.musicapp.databinding.ItemMusicBinding
import com.example.musicapp.utils.myInflate

class MusicAdapter(private val itemClick:((MusicData)->Unit)) : ListAdapter<MusicData, MusicAdapter.MusicViewHolder>(MusicDiffUtil) {

    object MusicDiffUtil : DiffUtil.ItemCallback<MusicData>() {
        override fun areItemsTheSame(oldItem: MusicData, newItem: MusicData): Boolean { return oldItem.id == newItem.id }
        override fun areContentsTheSame(oldItem: MusicData, newItem: MusicData): Boolean { return oldItem == newItem }
    }

    inner class MusicViewHolder(binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root){
        private val title = binding.tvTitleSongItemPlayer
        private val artist = binding.tvAuthorSongItemPlayer
        init { itemView.setOnClickListener { itemClick.invoke(currentList[bindingAdapterPosition])} }
        fun bind(data: MusicData){
            title.text = data.title
            artist.text = data.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(ItemMusicBinding.bind(parent.myInflate(R.layout.item_music)))
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) { holder.bind(currentList[position]) }
}