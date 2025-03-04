package com.example.lab1

import android.app.Service
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.IOException

class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "music_service_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())

        when (intent?.action) {
            "PLAY" -> playMusic()
            "STOP" -> stopMusic()
        }

        return START_STICKY
    }


    private fun playMusic() {
        stopMusic()
        mediaPlayer = MediaPlayer()
        try {
            val assetFileDescriptor = assets.openFd("akeboshi_wind.mp3")
            mediaPlayer?.apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }


    private fun createNotification(): Notification {
        val playIntent = Intent(this, MusicPlayerService::class.java).apply { action = "PLAY" }
        val stopIntent = Intent(this, MusicPlayerService::class.java).apply { action = "STOP" }

        val playPending = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopPending = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setSmallIcon(R.drawable.ic_music)
            .addAction(R.drawable.ic_play, "Play", playPending)
            .addAction(R.drawable.ic_stop, "Stop", stopPending)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
