package com.example.musicapp.presentation.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.data.mapper.getMusicDataByPosition
import com.example.musicapp.databinding.ItemMusicBinding

class MusicCursorAdapter : RecyclerView.Adapter<MusicCursorAdapter.MyCursorViewHolder>() {
    var cursor: Cursor? = null
    private var selectMusicPositionListener: ((Int) -> Unit)? = null

    inner class MyCursorViewHolder(private val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                selectMusicPositionListener?.invoke(absoluteAdapterPosition)
            }
        }

        fun bind() {
            cursor?.getMusicDataByPosition(absoluteAdapterPosition)?.let {
                binding.tvTitleSongItemPlayer.text = it.title
                binding.tvAuthorSongItemPlayer.text = it.artist
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyCursorViewHolder(ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyCursorViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    fun setSelectMusicPositionListener(block: (Int) -> Unit) {
        selectMusicPositionListener = block
    }
}