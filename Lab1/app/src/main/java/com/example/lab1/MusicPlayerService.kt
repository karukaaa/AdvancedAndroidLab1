package com.example.lab1

import android.app.Service
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
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
        if (intent?.action == "PLAY") {
            startForeground(1, createNotification()) // Ensure foreground mode starts
            playMusic()
        } else if (intent?.action == "STOP") {
            stopMusic()
        }
        return START_STICKY
    }



    private fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            try {
                val assetFileDescriptor = assets.openFd("akeboshi_wind.mp3") // Change to your file name
                mediaPlayer?.apply {
                    setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                    prepare() // Prepare MediaPlayer
                    start()
                    setOnCompletionListener {
                        stopMusic()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            mediaPlayer?.start()
        }
    }




    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(true) // ✅ Remove notification
        stopSelf()
    }


    private fun createNotification(): Notification {
        val playIntent = Intent(this, MusicPlayerService::class.java).apply { action = "PLAY" }
        val stopIntent = Intent(this, MusicPlayerService::class.java).apply { action = "STOP" }

        val playPending = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopPending = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Playing: music")
            .setSmallIcon(R.drawable.ic_music)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 🚀 Try HIGH priority
            .addAction(R.drawable.ic_play, "Play", playPending)
            .addAction(R.drawable.ic_stop, "Stop", stopPending)
            .setOngoing(true)
            .build()
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_HIGH // 🚀 Change to HIGH
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
