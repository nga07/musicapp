package com.ngadt.appmusicplayer.service.notification

import Song
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ngadt.appmusicplayer.R

private const val INTENT_REQUEST_CODE = 100

fun createNotificationChannel(context: Context, notificationManager: NotificationManager?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(R.string.infor_channel_id),
            context.getString(R.string.infor_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(notificationChannel)
    }
}

fun sendNotification(song: Song?, context: Context, isPlay: Boolean?): Notification? {
    val playIntent = Intent(context, MusicReceiver::class.java)
    playIntent.action = context.getString(R.string.intent_play_music)
    val playPendingIntent =
        PendingIntent.getBroadcast(
            context,
            INTENT_REQUEST_CODE,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    val nextIntent = Intent(context, MusicReceiver::class.java)
    nextIntent.action = context.getString(R.string.intent_next_music)
    val nextPendingIntent =
        PendingIntent.getBroadcast(
            context,
            INTENT_REQUEST_CODE,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    val prevIntent = Intent(context, MusicReceiver::class.java)
    prevIntent.action = context.getString(R.string.intent_prev_music)
    val prevPendingIntent =
        PendingIntent.getBroadcast(
            context,
            INTENT_REQUEST_CODE,
            prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    val contentView = RemoteViews(context.packageName, R.layout.current_song_layout)
    contentView.apply {
        setImageViewResource(
            R.id.btnPlayPause,
            if (isPlay == true) R.drawable.ic_baseline_pause_circle_outline_64 else R.drawable.ic_baseline_play_circle_outline_64
        )
        setImageViewResource(R.id.btnNext, R.drawable.ic_baseline_skip_next_64)
        setImageViewResource(R.id.btnPrevious, R.drawable.ic_baseline_skip_previous_64)
        setTextViewText(R.id.tvTitleSong, song?.nameSong)
        setTextViewText(R.id.tvAuthor, song?.author)
        setOnClickPendingIntent(R.id.btnPlayPause, playPendingIntent)
        setOnClickPendingIntent(R.id.btnNext, nextPendingIntent)
        setOnClickPendingIntent(R.id.btnPrevious, prevPendingIntent)
    }
    return NotificationCompat.Builder(context, context.getString(R.string.infor_channel_id))
        .setSmallIcon(R.drawable.ic_music_note)
        .setContentTitle("App Music")
        .setCustomBigContentView(contentView)
        .build()
}
