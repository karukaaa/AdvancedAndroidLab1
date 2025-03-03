package com.example.lab1.Fragments

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lab1.AirplaneModeReceiver
import com.example.lab1.R
import android.provider.Settings


class BroadcastReceiverFragment : Fragment() {


    companion object {
        fun newInstance() = BroadcastReceiverFragment()
    }

    private lateinit var receiver: AirplaneModeReceiver
    private lateinit var airplaneModeOnLayout: LinearLayout
    private lateinit var airplaneModeOffLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = AirplaneModeReceiver { isAirplaneModeOn ->
            updateUI(isAirplaneModeOn)
        }

        val intentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")
        val flag = ContextCompat.RECEIVER_EXPORTED
        ContextCompat.registerReceiver(requireContext(), receiver, intentFilter, flag)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_broadcast_receiver, container, false)

        airplaneModeOnLayout = view.findViewById(R.id.airplane_mode_on)
        airplaneModeOffLayout = view.findViewById(R.id.airplane_mode_off)


        val isAirplaneModeOn = Settings.Global.getInt(
            requireContext().contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
        updateUI(isAirplaneModeOn)

        return view
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


    private fun updateUI(isAirplaneModeOn: Boolean) {
        if (isAirplaneModeOn) {
            airplaneModeOnLayout.visibility = View.VISIBLE
            airplaneModeOffLayout.visibility = View.GONE
        } else {
            airplaneModeOnLayout.visibility = View.GONE
            airplaneModeOffLayout.visibility = View.VISIBLE
        }
    }

}