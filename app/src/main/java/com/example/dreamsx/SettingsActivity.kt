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
    lateinit var broadcast :PendingIntent
    var hour = 9
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

        val switch = findViewById<Switch>(R.id.switch1)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onChecked()
            }
            else{
                onNotChecked()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {}

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        Log.d("timeTag", p1.toString())
        Log.d("timeTag", p2.toString())
        val p1format = p1.toString().padStart(2, '0')
        val p2format = p2.toString().padStart(2, '0')

        val textTime = findViewById<TextView>(R.id.textTime)
        textTime.text = "$p1format:$p2format"

        hour = p1
        minutes = p2

        val switch = findViewById<Switch>(R.id.switch1)
        if(switch.isChecked)
            onChecked()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun onChecked(){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent("android.media.action.DISPLAY_NOTIFICATION")
        notificationIntent.addCategory("android.intent.category.DEFAULT")

        broadcast = PendingIntent.getBroadcast(
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

    fun onNotChecked(){
        broadcast.cancel()
    }
}