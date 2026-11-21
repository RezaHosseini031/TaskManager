package com.rezahosseini.taskmanager.data.local.timer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rezahosseini.taskmanager.vm.TimerViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class TimerViewModelTest {

    private val context: Context =
        ApplicationProvider.getApplicationContext()

    @Test
    fun `focus completion should start custom break duration`() {
        // Arrange
        val viewModel = TimerViewModel()

        val focusSeconds = 2
        val breakSeconds = 3

        // Act
        viewModel.startTimer(
            context = context,
            focusSeconds = focusSeconds,
            breakSeconds = breakSeconds
        )

        // Advance the Android main looper and timer
        shadowOf(android.os.Looper.getMainLooper())
            .idleFor(java.time.Duration.ofSeconds(2))

        // Assert
        assertEquals(
            TimerViewModel.Mode.BREAK,
            viewModel.mode.value
        )

        assertEquals(
            breakSeconds,
            viewModel.remainingSeconds.value
        )

        assertTrue(viewModel.isRunning.value == true)

        viewModel.reset()
    }
}
