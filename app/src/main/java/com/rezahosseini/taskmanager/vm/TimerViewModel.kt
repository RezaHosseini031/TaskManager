package com.rezahosseini.taskmanager.vm

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.SystemClock
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rezahosseini.taskmanager.utils.NotificationHelper
import com.rezahosseini.taskmanager.view.notification.TimerAlarmManager
import com.rezahosseini.taskmanager.view.notification.TimerService

class TimerViewModel : ViewModel() {

    enum class Mode { IDLE, FOCUS, BREAK }

    enum class ExecutionMode {
        FOREGROUND,
        BACKGROUND,
        ALARM
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
    private var endAtElapsed: Long = 0L

    private var focusDurationSeconds = 25 * 60
    private var breakDurationSeconds = 5 * 60
    private var totalTime = 0

    private var appContext: Context? = null
    private var mediaPlayer: MediaPlayer? = null
    private val _executionMode =
        MutableLiveData(ExecutionMode.FOREGROUND)

    val executionMode: LiveData<ExecutionMode> = _executionMode

    fun setExecutionMode(mode: ExecutionMode) {
        if (_isRunning.value == true) return
        _executionMode.value = mode
    }
    fun initializeTimer(focusSeconds: Int, breakSeconds: Int) {
        if (_mode.value != Mode.IDLE) return

        focusDurationSeconds = focusSeconds.coerceAtLeast(1)
        breakDurationSeconds = breakSeconds.coerceAtLeast(1)

        _mode.value = Mode.FOCUS
        _remainingSeconds.value = focusDurationSeconds
        totalTime = focusDurationSeconds
        _progress.value = 0
        _isRunning.value = false
    }

    fun startTimer(
        context: Context,
        focusSeconds: Int? = null,
        breakSeconds: Int? = null
    ) {
        appContext = context.applicationContext

        focusSeconds?.let { focusDurationSeconds = it.coerceAtLeast(1) }
        breakSeconds?.let { breakDurationSeconds = it.coerceAtLeast(1) }

        if (focusSeconds != null) {
            reset()
            _mode.value = Mode.FOCUS
        }

        val seconds = _remainingSeconds.value
            ?.takeIf { it > 0 }
            ?: focusDurationSeconds

        startCountdown(seconds)
    }

    private fun startCountdown(seconds: Int) {
        timer?.cancel()

        totalTime = seconds
        endAtElapsed = SystemClock.elapsedRealtime() + seconds * 1000L

        _remainingSeconds.value = seconds
        _isRunning.value = true

        timer = object : CountDownTimer(seconds * 1000L, 250L) {

            override fun onTick(millisUntilFinished: Long) {
                val remaining = (
                        (endAtElapsed - SystemClock.elapsedRealtime() + 999) / 1000
                        ).toInt().coerceAtLeast(0)

                _remainingSeconds.value = remaining
                updateProgress(remaining)
            }

            override fun onFinish() {
                _remainingSeconds.value = 0
                _progress.value = 100
                _isRunning.value = false
                timer = null

                onPhaseFinished()
            }
        }.start()
    }

    private fun onPhaseFinished() {
        val context = appContext

        when (_mode.value) {
            Mode.FOCUS -> {
                context?.let {
                    playSound(it)
                    NotificationHelper.showNotification(
                        it,
                        "Focus finished",
                        "Time for a break!"
                    )
                }

                _mode.value = Mode.BREAK
                startCountdown(breakDurationSeconds)
            }

            Mode.BREAK -> {
                context?.let {
                    playSound(it)
                    NotificationHelper.showNotification(
                        it,
                        "Break finished",
                        "Ready for the next session?"
                    )
                }

                reset()
            }

            else -> reset()
        }
    }

    fun stopTimer() {
        if (_isRunning.value != true) return

        val remaining = (
                (endAtElapsed - SystemClock.elapsedRealtime() + 999) / 1000
                ).toInt().coerceAtLeast(0)

        timer?.cancel()
        timer = null

        _remainingSeconds.value = remaining
        _isRunning.value = false
        updateProgress(remaining)
    }

    fun resume(context: Context) {
        appContext = context.applicationContext

        if (_isRunning.value == true) return

        val seconds = _remainingSeconds.value ?: 0
        if (seconds <= 0) return

        startCountdown(seconds)
    }

    fun restart(context: Context) {
        appContext = context.applicationContext

        when (_mode.value) {
            Mode.BREAK -> {
                _mode.value = Mode.BREAK
                startCountdown(breakDurationSeconds)
            }

            else -> {
                _mode.value = Mode.FOCUS
                startCountdown(focusDurationSeconds)
            }
        }
    }

    fun updateDurationsAndReset(focusSeconds: Int, breakSeconds: Int) {
        reset()

        focusDurationSeconds = focusSeconds.coerceAtLeast(1)
        breakDurationSeconds = breakSeconds.coerceAtLeast(1)

        _mode.value = Mode.FOCUS
        _remainingSeconds.value = focusDurationSeconds
        totalTime = focusDurationSeconds
        _progress.value = 0
    }

    fun reset() {
        timer?.cancel()
        timer = null

        _isRunning.value = false
        _mode.value = Mode.IDLE
        _remainingSeconds.value = 0
        _progress.value = 0

        totalTime = 0
        endAtElapsed = 0L
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
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(
            context.applicationContext,
            Settings.System.DEFAULT_ALARM_ALERT_URI
        ) ?: return

        mediaPlayer?.setOnCompletionListener { player ->
            player.release()
            if (mediaPlayer === player) mediaPlayer = null
        }

        mediaPlayer?.start()
    }

    override fun onCleared() {
        timer?.cancel()
        timer = null

        mediaPlayer?.release()
        mediaPlayer = null

        appContext = null
        super.onCleared()
    }

    fun startBackgroundTimer(context: Context, seconds: Int) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.EXTRA_SECONDS, seconds)
        }

        androidx.core.content.ContextCompat.startForegroundService(
            context,
            intent
        )
    }

    fun pauseBackgroundTimer(context: Context) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_PAUSE
        }

        context.startService(intent)
    }

    fun resetBackgroundTimer(context: Context) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_RESET
        }

        context.startService(intent)
    }
    fun startAlarmTimer(context: Context, seconds: Int) {
        TimerAlarmManager.cancel(context)
        TimerAlarmManager.schedule(context, seconds)
    }

    fun cancelAlarmTimer(context: Context) {
        TimerAlarmManager.cancel(context)
    }
    fun startSelectedTimer(context: Context) {
        val seconds = remainingSeconds.value ?: 0
        if (seconds <= 0) return

        when (_executionMode.value) {
            ExecutionMode.FOREGROUND -> {
                resume(context)
            }

            ExecutionMode.BACKGROUND -> {
                startBackgroundTimer(context, seconds)
            }

            ExecutionMode.ALARM -> {
                startAlarmTimer(context, seconds)
            }

            null -> Unit
        }
    }

    fun stopSelectedTimer() {
        when (_executionMode.value) {
            ExecutionMode.FOREGROUND -> {
                stopTimer()
            }

            ExecutionMode.BACKGROUND -> {
                appContext?.let { pauseBackgroundTimer(it) }
            }

            ExecutionMode.ALARM -> {
                appContext?.let { cancelAlarmTimer(it) }
            }

            null -> Unit
        }
    }

    fun restartSelectedTimer(context: Context) {
        appContext = context.applicationContext

        val seconds = when (_mode.value) {
            Mode.BREAK -> breakDurationSeconds
            else -> focusDurationSeconds
        }

        when (_executionMode.value) {
            ExecutionMode.FOREGROUND -> {
                restart(context)
            }

            ExecutionMode.BACKGROUND -> {
                startBackgroundTimer(context, seconds)
            }

            ExecutionMode.ALARM -> {
                startAlarmTimer(context, seconds)
            }

            null -> Unit
        }
    }
}