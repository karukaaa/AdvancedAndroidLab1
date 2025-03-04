package com.example.lab1.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lab1.MusicPlayerService
import com.example.lab1.R


class ForegroundServiceFragment : Fragment() {
    private lateinit var songTitleTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_foreground_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        val playButton: Button = view.findViewById(R.id.btnPlay)
        val stopButton: Button = view.findViewById(R.id.btnStop)
        val pauseButton: Button = view.findViewById(R.id.btnPause)

        playButton.setOnClickListener {
            sendMusicCommand("PLAY")
        }

        stopButton.setOnClickListener {
            sendMusicCommand("STOP")
        }

        pauseButton.setOnClickListener {
            sendMusicCommand("PAUSE")
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun sendMusicCommand(action: String) {
        val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
            this.action = action
        }
        requireContext().startService(intent)
    }

}
