package com.ngadt.appmusicplayer.service

import Song
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.ngadt.appmusicplayer.service.notification.createNotificationChannel
import com.ngadt.appmusicplayer.service.notification.sendNotification

private const val NOTIFICATION_ID = 1

class MusicService : Service() {

    private var musicPlayer: MediaPlayer? = null
    private var songList: MutableList<Song>? = null
    private var notificationManager: NotificationManager? = null
    private var currentSong = 0
    override fun onBind(p0: Intent?): IBinder? = MusicBinder(this)
    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        createNotificationChannel(applicationContext, notificationManager)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        pushNotification()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
        musicPlayer?.release()
        stopSelf()
    }

    fun pushNotification() {
        notificationManager.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(
                    NOTIFICATION_ID,
                    sendNotification(
                        songList?.get(currentSong),
                        applicationContext,
                        musicPlayer?.isPlaying
                    )
                )
            } else this?.notify(
                NOTIFICATION_ID,
                sendNotification(
                    songList?.get(currentSong),
                    applicationContext,
                    musicPlayer?.isPlaying
                )
            )
        }
    }

    fun setSongList(songList: MutableList<Song>) {
        this.songList = songList
    }

    fun switchMusic(index: Int) {
        currentSong = index
        if (musicPlayer?.isPlaying == true) {
            musicPlayer?.release()
        }
        if (songList == null) {
            return
        }
        musicPlayer = MediaPlayer.create(applicationContext, songList!![index].linkSong.toUri())
        playMusic()
    }

    fun getIntent() = Intent(applicationContext, MusicService::class.java)
    fun isMusicPlaying() = musicPlayer?.isPlaying
    fun playMusic() = musicPlayer?.start()
    fun pauseMusic() = musicPlayer?.pause()
    fun resumeMusic() {
        musicPlayer?.currentPosition?.let {
            musicPlayer?.seekTo(it)
            musicPlayer?.start()
        }
    }

    private fun cancelNotification() {
        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }

    class MusicBinder(private val service: MusicService) : Binder() {
        fun getService() = service
    }
}

