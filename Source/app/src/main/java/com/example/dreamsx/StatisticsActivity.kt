package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Статистика"



    }
}