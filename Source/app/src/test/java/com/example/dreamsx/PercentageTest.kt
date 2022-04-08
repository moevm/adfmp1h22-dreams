package com.example.dreamsx

import junit.framework.TestCase



class PercentageTest : TestCase() {
    fun testPercantage(){

        assertEquals(33f, getPercentage(current = 20, total = 60))

        assertEquals(10f, getPercentage(current = 13, total = 123))

        assertEquals(68f, getPercentage(current = 65, total = 95))
    }

}

private fun getPercentage(current: Int, total: Int) : Float {
    return ((current*100) / total ).toFloat()
}