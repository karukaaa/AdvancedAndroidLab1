package com.example.lab1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.IOException


class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var lastPosition = 0
    private val CHANNEL_ID = "music_service_channel"
    private var currentSongIndex = 0 // Keeps track of the current song


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> playMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
        }
        return START_STICKY
    }


    private fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            try {
                val assetFileDescriptor = assets.openFd("akeboshi_wind.mp3")

                mediaPlayer?.apply {
                    reset()
                    setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                    prepare()
                    seekTo(lastPosition) // Resume from last position if it was paused
                    start()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            if (isPaused) {
                mediaPlayer?.seekTo(lastPosition)
                mediaPlayer?.start()
                isPaused = false
            }
        }

        startForeground(1, createNotification())
    }



    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        lastPosition = 0
        isPaused = false
        stopForeground(true)
        stopSelf()
    }


    private fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            lastPosition = mediaPlayer?.currentPosition ?: 0
            isPaused = true
            startForeground(1, createNotification())
        }
    }


    private fun createNotification(): Notification {
        val playIntent = Intent(this, MusicPlayerService::class.java).apply { action = "PLAY" }
        val pauseIntent = Intent(this, MusicPlayerService::class.java).apply { action = "PAUSE" }
        val stopIntent = Intent(this, MusicPlayerService::class.java).apply { action = "STOP" }

        val playPending =
            PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val pausePending =
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopPending =
            PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE)


        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Wind - Akeboshi")
            .setSmallIcon(R.drawable.ic_music)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.drawable.ic_play, "Play", playPending)
            .addAction(R.drawable.ic_pause, "Pause", pausePending)
            .addAction(R.drawable.ic_stop, "Stop", stopPending)
            .setOngoing(true)
            .build()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

    }


    override fun onDestroy() {
        stopMusic()
        super.onDestroy()
    }


}
