package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

lateinit var countOfDreamTitle : TextView

//textDreamsTotalCount
class StatisticsActivity() : AppCompatActivity() {

    private var countOfDreams: Int = 0
    init {
        //this.countOfDreams = countOfDreams
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Статистика"

        var x = getIntent().getStringExtra("countOfDreams")

        countOfDreamTitle = findViewById<TextView>(R.id.textDreamsTotalCount)

        countOfDreamTitle.text = x
        //countOfDreamTitle.text = countOfDreams.toString()

    }
}