package com.example.lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "android.intent.action.AIRPLANE_MODE"){

            val msg = "airplane mode changed: ${p1.getBooleanExtra("state", false)} intent: ${p1}"

            Toast.makeText(p0, msg, Toast.LENGTH_SHORT).show()
        }
    }
}