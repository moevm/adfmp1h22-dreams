package com.example.dreamsx

import android.os.Build
import androidx.annotation.RequiresApi
import junit.framework.TestCase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateHandlerTest : TestCase() {
    private val dateStatisticsHandler: DateStatisticsHandler = DateStatisticsHandler()
    private val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy - HH:mm")
    private var currentDate : LocalDate = LocalDate.parse("09 Apr, 2022 - 13:25", datePattern) //Текущая дата

    fun test_isDreamsBefore(){
        var date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        // Convert Date to Calendar
        val calendar1: Calendar = Calendar.getInstance()
        calendar1.time = date //current date
        calendar1.add(Calendar.DATE, -30) // -30 дней

        var res1 = dateStatisticsHandler.isDreamsBefore(dateChecker = calendar1.time, noteDateStr = "06 Apr, 2022 - 19:25")
        assertTrue(res1)

        var res2 = dateStatisticsHandler.isDreamsBefore(dateChecker = calendar1.time, noteDateStr = "1 Mar, 2022 - 19:25")
        assertFalse(res2)
    }

    fun test_getPeriod(){

        assertEquals("P-3D", dateStatisticsHandler.getPeriod("06 Apr, 2022 - 19:25", currentDate).toString())
        currentDate = LocalDate.parse("25 Mar, 2022 - 13:25", datePattern)

        assertEquals("P8D", dateStatisticsHandler.getPeriod("02 Apr, 2022 - 19:25", currentDate).toString())
    }


}