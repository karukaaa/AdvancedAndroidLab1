package com.example.lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver(private val onAirplaneModeChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "android.intent.action.AIRPLANE_MODE"){

           val isAirplaneModeOn = p1.getBooleanExtra("state", false)
            onAirplaneModeChanged(isAirplaneModeOn)
        }
    }
}