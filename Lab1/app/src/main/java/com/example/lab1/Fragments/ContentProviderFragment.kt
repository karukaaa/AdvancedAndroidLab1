package com.example.lab1.Fragments

import android.content.pm.PackageManager
import android.database.Cursor
import java.util.*
import java.text.SimpleDateFormat
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
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.lab1.CalendarEvent
import com.example.lab1.R

class ContentProviderFragment : Fragment() {

    companion object {
        private const val CALENDAR_ID = "7"
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchAndPrintEvents()
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
                fetchAndPrintEvents()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_CALENDAR)
            }
        }

        return view
    }


    private fun fetchAndPrintEvents() {

        val eventList = mutableListOf<CalendarEvent>()

        val currentTime = System.currentTimeMillis()
        val oneWeekLater = currentTime + (7 * 24 * 60 * 60 * 1000) // 7 days from now
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
        )

        //For non recurring events
        val selection =
            "${CalendarContract.Events.CALENDAR_ID} = ? AND ${CalendarContract.Events.DTSTART} >= ?"
        val selectionArgs = arrayOf(CALENDAR_ID, currentTime.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val cursor: Cursor? = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            while (it.moveToNext()) {
                val eventID = it.getLong(0)
                val title = it.getStringOrNull(1) ?: "No title"
                val dtStart = it.getLongOrNull(2) ?: continue

                eventList.add(CalendarEvent(eventID, title, dtStart))
            }
        } ?: println("No non recurring events!")


        //For recurring events
        val recurringProjection = arrayOf(
            CalendarContract.Instances._ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
        )

        val recurringSelection = "${CalendarContract.Instances.CALENDAR_ID} = ?"
        val recurringSelectionArgs = arrayOf(CALENDAR_ID)
        val recurringSortOrder = "${CalendarContract.Instances.BEGIN} ASC"

        // Build the URI with the time range
        val uri = CalendarContract.Instances.CONTENT_URI
            .buildUpon()
            .appendPath(currentTime.toString())
            .appendPath(oneWeekLater.toString())
            .build()

        val recurringCursor: Cursor? = requireContext().contentResolver.query(
            uri,
            recurringProjection,null, null,
//            recurringSelection,
//            recurringSelectionArgs,
            recurringSortOrder
        )

        recurringCursor?.use {
            while (it.moveToNext()) {
                val eventID = it.getLong(0)
                val title = it.getStringOrNull(1) ?: "No title"
                val startTime = it.getLongOrNull(2) ?: continue

                eventList.add(CalendarEvent(eventID, title, startTime))
            }
        } ?: println("No recurring events!")


        eventList.sortBy { it.startTime }
        for (event in eventList) {
            val formattedDate = dateFormat.format(Date(event.startTime))
            println("Found Event ID: ${event.id}, Title: ${event.title}, Start Time: $formattedDate")
        }
    }

}