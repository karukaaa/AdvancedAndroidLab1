package com.example.lab1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

        val song = assets.openFd("akeboshi_wind.mp3")
        mediaPlayer?.setDataSource(song.fileDescriptor, song.startOffset, song.length)
        mediaPlayer?.prepare()
        mediaPlayer?.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            "START" -> startForegroundService()
            "PAUSE" -> mediaPlayer?.pause()
            "STOP" -> {
                stopSelf()
                mediaPlayer?.stop()
            }
        }

        return START_STICKY
    }

    private fun startForegroundService() {
        val channelId = "music_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Music Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Playing")
            .setSmallIcon(R.drawable.ic_music)
            .addAction(R.drawable.ic_pause, "Pause", getPendingIntent("PAUSE"))
            .addAction(R.drawable.ic_stop, "Stop", getPendingIntent("STOP"))
            .setOngoing(true)
            .build()

        startForeground(1, notification)
        mediaPlayer?.start()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply { this.action = action }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}