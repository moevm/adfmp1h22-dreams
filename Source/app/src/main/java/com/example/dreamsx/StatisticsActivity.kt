package com.example.dreamsx

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.view.size
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class StatisticsActivity() : AppCompatActivity() {

    private lateinit var countOfDreamTitle : TextView
    private lateinit var countOfPositiveDreamsTitle : TextView
    private lateinit var countOfMiddleDreamsTitle : TextView
    private lateinit var countOfNegativeDreamsTitle : TextView
    private lateinit var dreamsPeriodTitle : TextView
    private lateinit var topTagsTitle : TextView // топ тегов
    private lateinit var topTags : TextView

    private lateinit var weekBtn : Button
    private lateinit var monthBtn : Button
    private lateinit var yearBtn : Button

    private lateinit var pieChart: PieChart
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var currentDate: LocalDate
    private lateinit var datePattern: DateTimeFormatter
    private lateinit var listOfAllNotes: MutableList<Note>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Статистика"
        weekBtn = findViewById<Button>(R.id.buttonWeek)
        monthBtn = findViewById<Button>(R.id.buttonMonth)
        yearBtn = findViewById<Button>(R.id.buttonYear)
        dreamsPeriodTitle = findViewById<TextView>(R.id.textDreamsPeriodTitle)
        countOfDreamTitle = findViewById<TextView>(R.id.textDreamsTotalCount)
        countOfPositiveDreamsTitle = findViewById<TextView>(R.id.textPositiveDreamsCount)
        countOfMiddleDreamsTitle = findViewById<TextView>(R.id.textMiddleDreamsCount)
        countOfNegativeDreamsTitle = findViewById<TextView>(R.id.textNegativeDreamsCount)
        topTagsTitle = findViewById<TextView>(R.id.textTagsType)
        topTags = findViewById<TextView>(R.id.tagsList)
        pieChart = findViewById<PieChart>(R.id.pieChart)

        listOfAllNotes = intent.getSerializableExtra("listOfAllNotes") as MutableList<Note>

        // Статистика по дате -----------------------------------------------------------------------

        simpleDateFormat = SimpleDateFormat("dd MMM, yyyy - HH:mm")

        val currentDateStr:String = simpleDateFormat.format(Date())
        datePattern = DateTimeFormatter.ofPattern("dd MMM, yyyy - HH:mm")
        currentDate = LocalDate.parse(currentDateStr, datePattern) //Текущая дата

        // Convert Date to Calendar
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date() //current date
        btnWeekHandler(calendar) // по умолчанию недельный обзор

        // отбор снов по указанному периоду.
        weekBtn.setOnClickListener {
            btnWeekHandler(calendar)
        }
        monthBtn.setOnClickListener {
            btnMonthHandler(calendar)
        }
        yearBtn.setOnClickListener {
            btnYearHandler(calendar)
        }
    }

    private fun fillStatistics(notes: List<Note>){
        var listOfTags: Array<String> = getAllTags(notes.filter { it.noteTags != "" })
        val countOfDreams = notes.size
        val countOfPositiveDreams = notes.count { notes -> notes.mood == DreamMood.COOL }
        val countOfMiddleDreams = notes.count { notes -> notes.mood == DreamMood.MIDDLE }
        val countOfNegativeDreams = notes.count { notes -> notes.mood == DreamMood.BAD }
        countOfDreamTitle.text = countOfDreams.toString()
        countOfPositiveDreamsTitle.text = countOfPositiveDreams.toString()
        countOfMiddleDreamsTitle.text = countOfMiddleDreams.toString()
        countOfNegativeDreamsTitle.text = countOfNegativeDreams.toString()

        //Подготовка данных для диаграммы
        if(countOfDreams!!.toInt() > 0){
            val goodPct : Float = getPercentage(countOfPositiveDreams!!.toInt(), countOfDreams!!.toInt())
            val middlePct : Float = getPercentage(countOfMiddleDreams!!.toInt(), countOfDreams!!.toInt())
            val badPct : Float = getPercentage(countOfNegativeDreams!!.toInt(), countOfDreams!!.toInt())

            createPieChart(goodPct, middlePct, badPct)

            getTopTegs(listOfTags)
        } else{
            pieChart.isVisible = false
        }

    }

    private fun btnWeekHandler(cal: Calendar){
        dreamsPeriodTitle.text = "неделю:"
        cal.add(Calendar.DATE, -7)
        fillStatistics(notes = listOfAllNotes.filter { isDreamsBefore(dateChecker = cal.time, it.timeStamp) })
    }

    private fun btnMonthHandler(cal: Calendar){
        dreamsPeriodTitle.text = "месяц:"
        cal.add(Calendar.DATE, -30)
        fillStatistics(notes = listOfAllNotes.filter { isDreamsBefore(dateChecker = cal.time, it.timeStamp) })
    }

    private fun btnYearHandler(cal: Calendar){
        dreamsPeriodTitle.text = "год:"
        cal.add(Calendar.YEAR, -1)
        fillStatistics(notes = listOfAllNotes.filter { isDreamsBefore(dateChecker = cal.time, it.timeStamp) })
    }

    // Отбор снов по определеннмоу периоду.
    private fun isDreamsBefore(dateChecker: Date, noteDateStr: String) : Boolean {
        return dateChecker.before(simpleDateFormat.parse(noteDateStr))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPeriod(timeStamp: String) : Period{

        var noteDate = LocalDate.parse(timeStamp, datePattern)
        return Period.between(currentDate, noteDate)
    }

    private fun getAllTags(newList : List<Note>) : Array<String>{
        var tags : Array<String> = arrayOf()
        for(element in newList)
            tags += element.noteTags
        return tags
    }

    private fun getPercentage(current: Int, total: Int) : Float {
        return ((current*100) / total ).toFloat()
    }

    //Отрисовка диаграммы статистики
    private fun createPieChart(goodPct: Float, middlePct: Float, badPct: Float) {
        pieChart.isVisible = true
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
            Color.rgb(69, 243, 6), //green
            Color.rgb(234, 203, 6), //yellow
            Color.rgb(243, 6, 6) //red
        )
        dataSet.colors = customColors
        pieChart.data = PieData(dataSet)
        pieChart.size

        //Подписи к дугам
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

        val topTegs: MutableList<String> = mutableListOf()
        var key: String
        for (teg in countTegs){
            key = teg.key
            if (countTegs[key] == maxValue)
                topTegs.add(key)
        }

        var stringTopTegs: String = ""

        var newTeg: String = ""
        for (teg in topTegs){
            if(teg.startsWith("#")){
                newTeg = "$teg "
            } else{
                newTeg = "#$teg "
            }
            stringTopTegs += newTeg
        }
        this.topTags.text = stringTopTegs

    }
}