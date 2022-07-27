package com.canbazdev.hmskitsproject1

import com.canbazdev.hmskitsproject1.presentation.home.HomeViewModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val actual = 4.0
        val expected = 2.0+2.0
        assertEquals("Addition",actual,expected,0.001)
    }
}