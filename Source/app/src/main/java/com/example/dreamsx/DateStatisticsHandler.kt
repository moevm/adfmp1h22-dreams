package com.example.dreamsx

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class DateStatisticsHandler {

    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy - HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy - HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    private val currentDate: LocalDate = LocalDate.parse(simpleDateFormat.format(Date()), datePattern) //Текущая дата

    // Отбор снов по определеннмоу периоду.
    fun isDreamsBefore(dateChecker: Date, noteDateStr: String) : Boolean {
        return dateChecker.before(simpleDateFormat.parse(noteDateStr))
    }

    //Вычисление периода между двумя датами.
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPeriod(timeStamp: String, curDate: LocalDate = currentDate) : Period {

        var noteDate = LocalDate.parse(timeStamp, datePattern)
        return Period.between(curDate, noteDate)
    }
}