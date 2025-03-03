package com.example.lab1.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lab1.MusicPlayerService
import com.example.lab1.R


class ForegroundServiceFragment : Fragment() {
    private val track = "akeboshi_wind.mp3"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_foreground_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton: Button = view.findViewById(R.id.btnStart)
        val stopButton: Button = view.findViewById(R.id.btnStop)


        playButton.setOnClickListener {
            }

        stopButton.setOnClickListener {
             }
    }

}
