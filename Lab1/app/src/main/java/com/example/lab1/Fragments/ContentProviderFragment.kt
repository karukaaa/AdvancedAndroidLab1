package com.example.lab1.Fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.lab1.R

class ContentProviderFragment : Fragment() {

    companion object {
        private val EVENT_PROJECTION = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        private const val PROJECTION_DISPLAY_NAME_INDEX = 1
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCalendars()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_content_provider, container, false)

        val button: Button = view.findViewById(R.id.button_calendar)
        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getCalendars()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_CALENDAR)
            }
        }

        return view
    }


    private fun getCalendars() {
        val uri = CalendarContract.Calendars.CONTENT_URI

        val cur = requireActivity().contentResolver.query(
            uri,
            EVENT_PROJECTION,
            null,
            null,
            null
        )

        while (cur?.moveToNext()==true){
            val displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
            println("Calendar found: $displayName")
        }
        cur?.close()
    }
}