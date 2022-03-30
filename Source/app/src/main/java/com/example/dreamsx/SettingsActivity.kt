package com.example.dreamsx

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SettingsActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @SuppressLint("RestrictedApi", "UseSwitchCompatOrMaterialCode", "UnspecifiedImmutableFlag")

    var hour = 0
    var minutes = 0
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Настройки"

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {}

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {}
}