package com.example.dreamsx

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.view.size
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class StatisticsActivity() : AppCompatActivity() {

    private lateinit var countOfDreamTitle : TextView
    private lateinit var countOfPositiveDreamsTitle : TextView
    private lateinit var countOfMiddleDreamsTitle : TextView
    private lateinit var countOfNegativeDreamsTitle : TextView
    private lateinit var topTagsTitle : TextView // топ тегов
    private lateinit var topTags : TextView

    private lateinit var weekBtn : Button
    private lateinit var monthBtn : Button
    private lateinit var yearBtn : Button

    private lateinit var pieChart: PieChart


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
        topTagsTitle = findViewById<TextView>(R.id.textTagsType)
        topTags = findViewById<TextView>(R.id.tagsList)
        pieChart = findViewById<PieChart>(R.id.pieChart)

        countOfDreamTitle.text = countOfDreams
        countOfPositiveDreamsTitle.text = countOfPositiveDreams
        countOfMiddleDreamsTitle.text = countOfMiddleDreams
        countOfNegativeDreamsTitle.text = countOfNegativeDreams


        //Подготовка данных для диаграммы
        var goodPct : Float = getPercentage(countOfPositiveDreams!!.toInt(), countOfDreams!!.toInt())
        var middlePct : Float = getPercentage(countOfMiddleDreams!!.toInt(), countOfDreams!!.toInt())
        var badPct : Float = getPercentage(countOfNegativeDreams!!.toInt(), countOfDreams!!.toInt())

        createPieChart(goodPct, middlePct, badPct)

        getTopTegs(arrayOfTags)

    }

    private fun getPercentage(current: Int, total: Int) : Float {
        return ((current*100) / total ).toFloat()
    }

    //Отрисовка диаграммы статистики
    private fun createPieChart(goodPct: Float, middlePct: Float, badPct: Float) {

        val pieChart: PieChart = this.pieChart
        pieChart.description.isEnabled = false

        pieChart.dragDecelerationFrictionCoef = 0.99f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.rgb(98, 0, 238))
        pieChart.transparentCircleRadius = 24f
        pieChart.holeRadius = 27f
        pieChart.centerText = "Good Job"
        //pieChart.setCenterTextColor(Color.GREEN)
        pieChart.setCenterTextSize(0f)

        //Размер и количесво дуг
        val pieData = mutableListOf<PieEntry>()
        pieData.add(PieEntry(goodPct))
        pieData.add(PieEntry(middlePct))
        pieData.add(PieEntry(badPct))

        // Подписи к диаграмме
        val dataSet = PieDataSet(pieData, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        var customColors: List<Int> = listOf(
            Color.rgb(69, 243, 6),
            Color.rgb(234, 203, 6),
            Color.rgb(243, 6, 6)
        )
        dataSet.colors = customColors



        pieChart.data = PieData(dataSet)
        pieChart.size
        //Подписи к дугам
//        pieChart.data.setValueTextColor(Color.RED)
//        pieChart.data.setValueTextSize(10f)
        pieChart.data.setValueTextSize(0f)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        pieChart.animateXY(2000,2000)
    }

    private fun getTopTegs(tegs: Array<String>){
        val countTegs = mutableMapOf<String, Int>()
        for (teg in tegs){
            if (!countTegs.containsKey(teg))
                countTegs[teg] = 1
            else
                countTegs[teg] = countTegs.getValue(teg) + 1
        }

        val values: MutableCollection<Int> = countTegs.values
        val maxValue = values.maxOrNull()
    }
}