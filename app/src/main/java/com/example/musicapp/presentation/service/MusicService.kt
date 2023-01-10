package com.example.musicapp.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.musicapp.R
import com.example.musicapp.data.CommandEnum
import com.example.musicapp.data.MusicData
import com.example.musicapp.data.mapper.getMusicDataByPosition
import com.example.musicapp.presentation.controller.MusicController
import com.example.musicapp.presentation.controller.MusicManager
import com.example.musicapp.utils.myLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
@Singleton
class MusicService @Inject constructor() : Service(), MusicController {
    override fun onBind(intent: Intent?): IBinder? = null
    private val channelId = "MusicApp"
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var job: Job? = null
    private var manager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startMyService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        startMyService()
        var command = CommandEnum.PLAY
        when (intent!!.extras?.getInt("COMMAND")) {
            CommandEnum.PLAY.pos -> command = CommandEnum.PLAY
            CommandEnum.NEXT.pos -> command = CommandEnum.NEXT
            CommandEnum.PREV.pos -> command = CommandEnum.PREV
            CommandEnum.CANCEL.pos -> command = CommandEnum.CANCEL
            CommandEnum.MANAGE.pos -> command = CommandEnum.MANAGE
            CommandEnum.PAUSE.pos -> command = CommandEnum.PAUSE
        }
        doneCommand(command)
        return START_NOT_STICKY
    }

    private fun startMyService() {

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo2)
            .setContentTitle("..Way of Music..")
            .setContentIntent(createPendingIntent(MusicManager.lastCommand))
            .setCustomContentView(createSmallRemoteView())
            .setCustomBigContentView(createLargeRemoteView())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()
        startForeground(1, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "Music notification chanel", NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createSmallRemoteView(): RemoteViews {
        return RemoteViews(this.packageName, R.layout.notification_small).apply {
            MusicManager.cursor?.getMusicDataByPosition(MusicManager.selectMusicPosition)?.let { musicData ->
                setTextViewText(R.id.tvTitleNotification, musicData.title)
                setTextViewText(R.id.tvArtistNotification, musicData.artist)
                setOnClickPendingIntent(R.id.btnCancel, createPendingIntent(CommandEnum.CANCEL))
            }
        }
    }

    private fun createLargeRemoteView(): RemoteViews {
        return RemoteViews(this.packageName, R.layout.notification_large).apply {
            MusicManager.cursor?.getMusicDataByPosition(MusicManager.selectMusicPosition)?.let { musicData ->
                setTextViewText(R.id.tvTitleNotification, musicData.title)
                setTextViewText(R.id.tvArtistNotification, musicData.artist)
                if (MyMusicPlayer.getMusicPlayer().isPlaying)
                    setImageViewResource(R.id.btPlay, R.drawable.ic_pause2)
                else
                    setImageViewResource(R.id.btPlay, R.drawable.ic_play)
                setOnClickPendingIntent(R.id.btPrevNotification, createPendingIntent(CommandEnum.PREV))
                setOnClickPendingIntent(R.id.btNextNotification, createPendingIntent(CommandEnum.NEXT))
                setOnClickPendingIntent(R.id.btnCancel, createPendingIntent(CommandEnum.CANCEL))
                setOnClickPendingIntent(R.id.btPlay, createPendingIntent(CommandEnum.MANAGE))
            }
        }
    }

    private fun createPendingIntent(commandEnum: CommandEnum): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("COMMAND", commandEnum.pos)
        return PendingIntent.getService(this, commandEnum.pos, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun doneCommand(command: CommandEnum) {
        scope.launch {
            "doneCommand:$command".myLog()
            val data: MusicData = MusicManager.cursor?.getMusicDataByPosition(MusicManager.selectMusicPosition)!!
            when (command) {
                CommandEnum.MANAGE -> {
                    if (MyMusicPlayer.getMusicPlayer().isPlaying) doneCommand(CommandEnum.PAUSE)
                    else doneCommand(CommandEnum.PLAY)
                }
                CommandEnum.PLAY -> {
                    if (MyMusicPlayer.getMusicPlayer().isPlaying)
                        MyMusicPlayer.getMusicPlayer().reset()
                    MusicManager.isPlay.postValue(true)
                    MusicManager.playMusic.postValue(data)
                    MyMusicPlayer.create(this@MusicService, Uri.parse(data.data))
                    MusicManager.fullTime = data.duration
                    MyMusicPlayer.getMusicPlayer().start()
                    MyMusicPlayer.getMusicPlayer().setOnCompletionListener { doneCommand(CommandEnum.NEXT) }
                    MyMusicPlayer.getMusicPlayer().seekTo(MusicManager.currentTime.toInt())
                    job?.cancel()
                    job = scope.launch {
                        changeProgress().collectLatest {
                            MusicManager.liveTime.postValue(it)
                        }
                    }
                    startMyService()
                }
                CommandEnum.PAUSE -> {
                    MusicManager.currentTime = MyMusicPlayer.getMusicPlayer().currentPosition.toLong()
                    MyMusicPlayer.getMusicPlayer().stop()
                    job?.cancel()
                    MusicManager.isPlay.postValue(false)
                    startMyService()
                }

                CommandEnum.NEXT -> {
                    MusicManager.currentTime = 0
                    if (MusicManager.selectMusicPosition + 1 == MusicManager.cursor!!.count) MusicManager.selectMusicPosition = 0
                    else MusicManager.selectMusicPosition++
                    doneCommand(CommandEnum.PLAY)
                }

                CommandEnum.PREV -> {
                    MusicManager.currentTime = 0
                    if (MusicManager.selectMusicPosition == 0) MusicManager.selectMusicPosition = MusicManager.cursor!!.count - 1
                    else MusicManager.selectMusicPosition--
                    doneCommand(CommandEnum.PLAY)
                }
                CommandEnum.CANCEL -> {
                    MyMusicPlayer.getMusicPlayer().stop()
                    MusicManager.isCancel.postValue(true)
                    stopSelf()
                }
            }
        }
    }

    private fun changeProgress(): Flow<Long> = flow {
        val fullTime = MyMusicPlayer.getMusicPlayer().duration
        for (i in MusicManager.currentTime until fullTime step 1000) {
            delay(1000)
            emit(MusicManager.currentTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onCleared() {
        scope.cancel()
        MyMusicPlayer.getMusicPlayer().release()
        MyMusicPlayer.getMusicPlayer().stop()
    }
}