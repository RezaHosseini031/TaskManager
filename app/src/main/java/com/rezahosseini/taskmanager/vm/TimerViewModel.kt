package com.rezahosseini.taskmanager.vm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezahosseini.taskmanager.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    enum class Mode {
        IDLE, FOCUS, BREAK
    }

    private val _mode = MutableLiveData(Mode.IDLE)
    val mode: LiveData<Mode> = _mode

    private val _remainingSeconds = MutableLiveData(0)
    val remainingSeconds: LiveData<Int> = _remainingSeconds

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private var timer: CountDownTimer? = null

    // Configured durations
    private var focusDurationSeconds = 25 * 60
    private var breakDurationSeconds = 5 * 60

    // Current session duration
    private var totalTime = 0

    fun startTimer(
        context: Context,
        focusSeconds: Int? = null,
        breakSeconds: Int? = null
    ) {
        focusSeconds?.let {
            focusDurationSeconds = it.coerceAtLeast(1)
        }

        breakSeconds?.let {
            breakDurationSeconds = it.coerceAtLeast(1)
        }

        if (focusSeconds != null) {
            reset()
        }

        val startFrom = _remainingSeconds.value
            ?.takeIf { it > 0 }
            ?: focusDurationSeconds

        _mode.value = Mode.FOCUS
        _remainingSeconds.value = startFrom
        totalTime = startFrom
        _progress.value = 0
        _isRunning.value = true

        timer?.cancel()

        timer = object : CountDownTimer(
            startFrom * 1000L,
            1000
        ) {
            override fun onTick(ms: Long) {
                val seconds = (ms / 1000).toInt()

                _remainingSeconds.value = seconds
                updateProgress(seconds)
            }

            override fun onFinish() {
                _remainingSeconds.value = 0

                playSound(context)

                NotificationHelper.showNotification(
                    context,
                    "Focus finished",
                    "Time for a break!"
                )

                startBreak(context)
            }
        }.start()
    }

    private fun startBreak(context: Context) {
        val breakSeconds = breakDurationSeconds

        _mode.value = Mode.BREAK
        _remainingSeconds.value = breakSeconds
        totalTime = breakSeconds
        _progress.value = 0
        _isRunning.value = true

        timer?.cancel()

        timer = object : CountDownTimer(
            breakSeconds * 1000L,
            1000
        ) {
            override fun onTick(ms: Long) {
                val seconds = (ms / 1000).toInt()

                _remainingSeconds.value = seconds
                updateProgress(seconds)
            }

            override fun onFinish() {
                _remainingSeconds.value = 0

                playSound(context)

                NotificationHelper.showNotification(
                    context,
                    "Break finished",
                    "Ready for next session"
                )

                reset()
            }
        }.start()
    }

    fun stopTimer() {
        _isRunning.value = false
        timer?.cancel()
    }

    fun reset() {
        timer?.cancel()
        timer = null

        _isRunning.value = false
        _mode.value = Mode.IDLE
        _remainingSeconds.value = 0
        _progress.value = 0
        totalTime = 0
    }

    fun resume(context: Context) {
        val seconds = _remainingSeconds.value ?: 0

        if (seconds <= 0 || _isRunning.value == true) {
            return
        }

        _isRunning.value = true

        timer?.cancel()

        timer = object : CountDownTimer(
            seconds * 1000L,
            1000
        ) {
            override fun onTick(ms: Long) {
                val remaining = (ms / 1000).toInt()

                _remainingSeconds.value = remaining
                updateProgress(remaining)
            }

            override fun onFinish() {
                _remainingSeconds.value = 0

                when (_mode.value) {
                    Mode.FOCUS -> {
                        playSound(context)

                        NotificationHelper.showNotification(
                            context,
                            "Focus finished",
                            "Time for a break!"
                        )

                        // Uses the configured break duration
                        startBreak(context)
                    }

                    Mode.BREAK -> {
                        playSound(context)

                        NotificationHelper.showNotification(
                            context,
                            "Break finished",
                            "Ready for next session"
                        )

                        reset()
                    }

                    else -> reset()
                }
            }
        }.start()
    }

    fun restart(context: Context) {
        when (_mode.value) {
            Mode.FOCUS -> {
                startTimer(
                    context = context,
                    focusSeconds = focusDurationSeconds,
                    breakSeconds = breakDurationSeconds
                )
            }

            Mode.BREAK -> {
                startBreak(context)
            }

            else -> {
                startTimer(
                    context = context,
                    focusSeconds = focusDurationSeconds,
                    breakSeconds = breakDurationSeconds
                )
            }
        }
    }

    private fun updateProgress(seconds: Int) {
        if (totalTime <= 0) {
            _progress.value = 0
            return
        }

        _progress.value = (
                (totalTime - seconds) * 100 / totalTime
                ).coerceIn(0, 100)
    }

    private fun playSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(
            context,
            RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_ALARM
            )
        ) ?: return

        mediaPlayer.isLooping = false
        mediaPlayer.start()

        viewModelScope.launch {
            delay(10_000)

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }

            mediaPlayer.release()
        }
    }

    override fun onCleared() {
        timer?.cancel()
        timer = null
        super.onCleared()
    }
}
