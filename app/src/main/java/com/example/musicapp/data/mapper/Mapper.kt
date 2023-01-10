package com.example.musicapp.data.mapper

import android.database.Cursor
import com.example.musicapp.data.MusicData

fun Cursor.getMusicDataByPosition(pos: Int): MusicData? {
    if (this.moveToPosition(pos))
        return MusicData(
            this.getInt(0),
            this.getString(1),
            this.getString(2),
            this.getString(3),
            this.getLong(4)
        )
    return null
}