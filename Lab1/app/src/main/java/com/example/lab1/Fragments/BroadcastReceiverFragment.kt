package com.example.lab1.Fragments

import android.content.BroadcastReceiver
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.lab1.AirplaneModeReceiver
import com.example.lab1.R


class BroadcastReceiverFragment : Fragment() {

    private lateinit var receiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = AirplaneModeReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_broadcast_receiver, container, false)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")
        val flag = ContextCompat.RECEIVER_EXPORTED
        ContextCompat.registerReceiver(requireContext(), receiver, intentFilter, flag)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }

    companion object {
        fun newInstance() = BroadcastReceiverFragment()
    }
}