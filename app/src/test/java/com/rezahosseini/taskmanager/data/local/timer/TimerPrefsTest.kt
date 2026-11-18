package com.rezahosseini.taskmanager.data.local.timer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rezahosseini.taskmanager.model.local.TimerPrefs
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class TimerPrefsTest {

    private lateinit var context: Context
    private lateinit var timerPrefs: TimerPrefs

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Ensure each test starts with clean preferences.
        context.getSharedPreferences(
            "timer_prefs",
            Context.MODE_PRIVATE
        ).edit().clear().commit()

        timerPrefs = TimerPrefs(context)
    }

    @Test
    fun getFocusMinutes_returnsDefaultValue() {
        // Act
        val result = timerPrefs.getFocusMinutes()

        // Assert
        assertEquals(25, result)
    }

    @Test
    fun getBreakMinutes_returnsDefaultValue() {
        // Act
        val result = timerPrefs.getBreakMinutes()

        // Assert
        assertEquals(5, result)
    }

    @Test
    fun setFocusMinutes_savesValue() {
        // Arrange
        val expected = 40

        // Act
        timerPrefs.setFocusMinutes(expected)

        // Assert
        assertEquals(expected, timerPrefs.getFocusMinutes())
    }

    @Test
    fun setBreakMinutes_savesValue() {
        // Arrange
        val expected = 10

        // Act
        timerPrefs.setBreakMinutes(expected)

        // Assert
        assertEquals(expected, timerPrefs.getBreakMinutes())
    }

    @Test
    fun setFocusMinutes_updatesExistingValue() {
        // Arrange
        timerPrefs.setFocusMinutes(30)
        val expected = 45

        // Act
        timerPrefs.setFocusMinutes(expected)

        // Assert
        assertEquals(expected, timerPrefs.getFocusMinutes())
    }

    @Test
    fun setBreakMinutes_updatesExistingValue() {
        // Arrange
        timerPrefs.setBreakMinutes(5)
        val expected = 15

        // Act
        timerPrefs.setBreakMinutes(expected)

        // Assert
        assertEquals(expected, timerPrefs.getBreakMinutes())
    }
}
