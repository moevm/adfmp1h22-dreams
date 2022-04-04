package com.example.dreamsx

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class StatisticsActivity() : AppCompatActivity() {

    lateinit var countOfDreamTitle : TextView
    lateinit var countOfPositiveDreamsTitle : TextView
    lateinit var countOfMiddleDreamsTitle : TextView
    lateinit var countOfNegativeDreamsTitle : TextView
    lateinit var topTagsTitle : TextView // топ тегов

    lateinit var weekBtn : Button
    lateinit var monthBtn : Button
    lateinit var yearBtn : Button

    lateinit var pieChart: PieChart


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
        pieChart = findViewById<PieChart>(R.id.pieChart)

        countOfDreamTitle.text = countOfDreams
        countOfPositiveDreamsTitle.text = countOfPositiveDreams
        countOfMiddleDreamsTitle.text = countOfMiddleDreams
        countOfNegativeDreamsTitle.text = countOfNegativeDreams

        createPieChart()

    }

    //private fun getPercantage :

    //Отрисова диаграммы статистики
    private fun createPieChart() {

        val pieChart: PieChart = this.pieChart
        pieChart.description.isEnabled = false

        pieChart.dragDecelerationFrictionCoef = 0.99f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.parseColor("#533D2B"))
        pieChart.transparentCircleRadius = 60f
        pieChart.centerText = "Good Job"
        pieChart.setCenterTextColor(Color.GREEN)
        pieChart.setCenterTextSize(50f)

        //Размер и количесво дуг
        val pieData = mutableListOf<PieEntry>()
        pieData.add(PieEntry(40f ))
        pieData.add(PieEntry(30f ))
        pieData.add(PieEntry(30f ))


        // Подписи к диаграмме
        val dataSet = PieDataSet(pieData, "Hello")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toMutableList()


        pieChart.data = PieData(dataSet)
        //Подписи к дугам
//        pieChart.data.setValueTextColor(Color.RED)
//        pieChart.data.setValueTextSize(10f)
        pieChart.data.setValueTextSize(0f)

        pieChart.animateXY(2000,2000)
    }


}