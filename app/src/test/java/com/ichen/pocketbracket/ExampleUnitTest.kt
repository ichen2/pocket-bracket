package com.ichen.pocketbracket

import com.ichen.pocketbracket.utils.addDays
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUnitTests {
    // TODO: Add more unit tests
    @Test
    fun dateAdditionTest() {
        val today = Date()
        assertEquals(today.addDays(1).time, today.time + 24 * 60 * 60 * 1000)
    }
    @Test
    fun dateSubtractionTest() {
        val today = Date()
        assertEquals(today.addDays(-1).time, today.time - 24 * 60 * 60 * 1000)
    }
}