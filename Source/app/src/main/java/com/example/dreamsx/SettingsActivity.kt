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

        val button = findViewById<Button>(R.id.btnPick)
        button.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR]
            val minute = c[Calendar.MINUTE]
            val timePickerDialog = TimePickerDialog(
                this,
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(this)
            )
            timePickerDialog.show()
        })

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {}

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {}

    @SuppressLint("UnspecifiedImmutableFlag")
    fun onChecked(){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent("android.media.action.DISPLAY_NOTIFICATION")
        notificationIntent.addCategory("android.intent.category.DEFAULT")

        val broadcast = PendingIntent.getBroadcast(
            this,
            100,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR, hour)
        cal.set(Calendar.MINUTE, minutes)
        cal.set(Calendar.SECOND, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
    }
}