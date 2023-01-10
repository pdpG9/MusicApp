package com.example.musicapp.domain.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.example.musicapp.data.MusicData
import com.example.musicapp.presentation.controller.MusicManager
import com.example.musicapp.utils.myLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerRepositoryImp @Inject constructor(@ApplicationContext private val context: Context) : MusicPlayerRepository {
    private val audio = ArrayList<MusicData>()

    //        private val audioFilePathList = ArrayList<String>()
    private val listElementsArrayList = ArrayList<String>()
    private val imageSongPathList = ArrayList<Int>()
    private val albumPathList = ArrayList<Int>()
    private val imageSongPathListForAlbums = ArrayList<Int>()
    private val artistPathList = ArrayList<String>()
    private val imageSongPathListForArtists = ArrayList<Int>()
    private val audioFlow = MutableSharedFlow<List<MusicData>>()
    private lateinit var mCursor: Cursor
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
//        MediaStore.Audio.Playlists.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE
    )

    init {
        if (audio.isEmpty()) readAllDataFromStorage()
    }

    override fun getAllMusicsEntity(): Flow<List<MusicData>> = audioFlow
    override fun getDirectors(): Flow<List<String>> = flow {
        emit(listOf("Tracks", "Albums", "Favorites", "Artist", "Playlists"))
    }

    override suspend fun getMusicFromLocal() {
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Audio.Media.IS_MUSIC + "!=0",
            null,
            null
        )
        readAllDataFromStorage()
        cursor?.let {
            MusicManager.cursor = it
            "getMusicFromLocal:${cursor.count}".myLog()
            mCursor = it
        }
    }

    override fun getCursor(): Cursor = mCursor

    private fun readAllDataFromStorage() = flow<Unit> {
        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver?.query(
            uri,  // Uri
            null,
            null,
            null,
            null
        )
        if (cursor == null) {
            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG).show()
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG).show()
        } else {
            val title: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumArt: Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
            val path: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val album: Int = cursor.getColumnIndex(MediaStore.Audio.Playlists.ALBUM)
            val artist: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
//            val id: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
//            val albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
//            val img: Int = cursor.getColumnIndex(MediaStore.Audio.Media.IS_FAVORITE)


            do {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val songTitle: String = cursor.getString(title)
                val albumColumn: String = cursor.getString(album)
                val pathColumn: String = cursor.getString(path)
//                val disColumn: String = cursor.getString(img)
                val artistColumn: String = cursor.getString(artist)
//                val albumArtColumn: String = cursor.getString(album)
                if (File(pathColumn).exists()) {
                    try {
                        while (cursor.moveToNext()) {
                            //get values of columns for a give audio file
                            val id = cursor.getInt(idColumn)
                            var name = cursor.getString(nameColumn)
                            val duration = cursor.getLong(durationColumn)
                            val size = cursor.getInt(sizeColumn)
                            val albumId = cursor.getLong(albumIdColumn)

                            //song uri
                            val songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toLong())

                            //album art uri
                            val albumArtUri = ContentUris.withAppendedId(
                                Uri.parse("content://media/external/audio/albumart"),
                                albumId
                            )

                            //remove .mp3 extension on song's name
                            name = name.substring(0, name.lastIndexOf("."))

                            val song = MusicData(
                                id,
                                name,
                                artistColumn,
                                pathColumn,
                                duration
                            )
                            audio.add(song)
                        }
                    } catch (_: java.lang.Exception) {
                    }
                    listElementsArrayList.add(songTitle)
                    imageSongPathList.add(albumArt)
                    if (!albumPathList.contains(album)) {
                        imageSongPathListForAlbums.add(albumArt)
                        albumPathList.add(album)
                    }
                    if (!artistPathList.contains(artistColumn)) {
                        imageSongPathListForArtists.add(albumArt)
                        artistPathList.add(artistColumn)
                    }
                }
            } while (cursor.moveToNext())
            audioFlow.emit(audio)
        }
    }.flowOn(Dispatchers.IO)
}