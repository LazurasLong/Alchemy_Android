package com.etbrady.alchemy

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val menuDateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd", Locale.US)
    private var selectedDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)

        val currentDateText = menuDateFormat.format(selectedDate.time)

        val dateItem = menu?.findItem(R.id.date_menu_item)
        dateItem?.title = currentDateText

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.date_menu_item -> dateMenuItemClicked(item)
        else -> super.onOptionsItemSelected(item)
    }

    private fun dateMenuItemClicked(item: MenuItem): Boolean {

        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)

            selectedDate = selectedCalendar.time
            val selectedDateText = menuDateFormat.format(selectedCalendar.time)
            item.title = selectedDateText

        }, year, month, dayOfMonth)

        datePickerDialog.show()
        return true
    }
}
