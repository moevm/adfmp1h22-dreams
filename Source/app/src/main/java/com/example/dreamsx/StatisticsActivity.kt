package com.example.dreamsx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView


class StatisticsActivity() : AppCompatActivity() {

    lateinit var countOfDreamTitle : TextView
    lateinit var countOfPositiveDreamsTitle : TextView
    lateinit var countOfMiddleDreamsTitle : TextView
    lateinit var countOfNegativeDreamsTitle : TextView
    lateinit var topTagsTitle : TextView // топ тегов

    lateinit var weekBtn : Button
    lateinit var monthBtn : Button
    lateinit var yearBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Статистика"

        val countOfDreams = intent.getStringExtra("countOfDreams")
        val countOfPositiveDreams = intent.getStringExtra("countOfPositiveDreams")
        val countOfMiddleDreams = intent.getStringExtra("countOfMiddleDreams")
        val countOfNegativeDreams = intent.getStringExtra("countOfNegativeDreams")
        val arrayOfTags: Array<String> = intent.getStringArrayExtra("arrayOfTags") as Array<String> //Теги

        weekBtn = findViewById<Button>(R.id.buttonWeek)
        monthBtn = findViewById<Button>(R.id.buttonMonth)
        yearBtn = findViewById<Button>(R.id.buttonYear)
        countOfDreamTitle = findViewById<TextView>(R.id.textDreamsTotalCount)
        countOfPositiveDreamsTitle = findViewById<TextView>(R.id.textPositiveDreamsCount)
        countOfMiddleDreamsTitle = findViewById<TextView>(R.id.textMiddleDreamsCount)
        countOfNegativeDreamsTitle = findViewById<TextView>(R.id.textNegativeDreamsCount)
        topTagsTitle = findViewById<TextView>(R.id.tagsList)

        countOfDreamTitle.text = countOfDreams
        countOfPositiveDreamsTitle.text = countOfPositiveDreams
        countOfMiddleDreamsTitle.text = countOfMiddleDreams
        countOfNegativeDreamsTitle.text = countOfNegativeDreams



    }


}