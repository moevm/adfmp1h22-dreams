package com.example.dreamdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingsActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_activty)

        val actionBar = supportActionBar

        actionBar!!.title = "Settings Activity"

        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}