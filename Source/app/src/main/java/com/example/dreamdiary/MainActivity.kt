package com.example.dreamdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secondActivityBtn: Button = findViewById (R.id.secondActivityBtn)

        secondActivityBtn.setOnClickListener{
            val intent = Intent(this, SettingsActivty::class.java)
            startActivity(intent)
        }
    }
}