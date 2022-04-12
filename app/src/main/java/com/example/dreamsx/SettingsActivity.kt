package com.example.dreamsx

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*


val PREFS_NAME : String = "MyPrefsFile"

class SettingsActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    private var hour: Int = 9
    private var minutes: Int = 0
    private var textTime: String = "09:00"
    private var checked: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = getSharedPreferences(PREFS_NAME, 0)
        hour = settings.getInt("hour", hour)
        minutes = settings.getInt("minutes", minutes)
        textTime = settings.getString("textTime", textTime).toString()
        checked = settings.getBoolean("checked", checked)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Настройки"
        createNotificationChannel()
        val button = findViewById<Button>(R.id.btnPick)
        button.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR_OF_DAY]
            val minute = c[Calendar.MINUTE]
            val timePickerDialog = TimePickerDialog(
                this,
                this,
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        })

        val textTime = findViewById<TextView>(R.id.textTime)
        textTime.text = this.textTime

        val switch = findViewById<Switch>(R.id.switch1)

        if (checked and !switch.isChecked) {
            switch.toggle()
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            Log.d("tag", "checked: $checked")
            Log.d("tag", "isChecked: ${switch.isChecked}")
            if(switch.isChecked){
                Log.d("tag", "onChecked!")
                checked = true
                onChecked()
            } else {
                Log.d("tag", "onNotChecked!")
                checked = false
                onNotChecked()
            }
        }
    }

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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onChecked() {
        val title = "Утреннее уведомление"
        val message = "Не забудь записать сон"
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = getTime()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun onNotChecked() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun getTime(): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, this.hour)
        cal.set(Calendar.MINUTE, this.minutes)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }
}