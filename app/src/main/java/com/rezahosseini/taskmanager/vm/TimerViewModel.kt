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

    enum class Mode { IDLE, FOCUS, BREAK }

    private val _mode = MutableLiveData(Mode.IDLE)
    val mode: LiveData<Mode> = _mode

    private val _remainingSeconds = MutableLiveData(0)
    val remainingSeconds: LiveData<Int> = _remainingSeconds

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private var timer: CountDownTimer? = null
    private var totalTime = 0

    fun startTimer(
        context: Context,
        focusSeconds: Int? = null,
        breakSeconds: Int? = null
    ) {
        if (focusSeconds != null) reset()

        val startFrom = when {
            _remainingSeconds.value!! > 0 -> _remainingSeconds.value!!
            focusSeconds != null -> focusSeconds
            else -> return
        }

        _mode.value = Mode.FOCUS
        _remainingSeconds.value = startFrom
        totalTime = startFrom
        _progress.value = ((totalTime - startFrom) * 100 / totalTime)
        _isRunning.value = true

        timer?.cancel()

        timer = object : CountDownTimer((startFrom * 1000).toLong(), 1000) {
            override fun onTick(ms: Long) {
                val sec = (ms / 1000).toInt()
                _remainingSeconds.value = sec
                _progress.value = ((totalTime - sec) * 100 / totalTime)
            }

            override fun onFinish() {
                playSound(context)
                NotificationHelper.showNotification(context, "Focus finished", "Time for a break!")
                startBreak(context, breakSeconds ?: 5 * 60)
            }
        }.start()
    }

    private fun startBreak(context: Context, breakSeconds: Int) {
        _mode.value = Mode.BREAK
        _remainingSeconds.value = breakSeconds
        totalTime = breakSeconds
        _progress.value = 0
        _isRunning.value = true
        timer?.cancel()

        timer = object : CountDownTimer((breakSeconds * 1000).toLong(), 1000) {
            override fun onTick(ms: Long) {
                val sec = (ms / 1000).toInt()
                _remainingSeconds.value = sec
                _progress.value = ((totalTime - sec) * 100 / totalTime)
            }

            override fun onFinish() {
                playSound(context)
                NotificationHelper.showNotification(context, "Break finished", "Ready for next session")
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
        _isRunning.value = false
        _mode.value = Mode.IDLE
        _remainingSeconds.value = 0
        _progress.value = 0
    }
    fun resume(context: Context) {
        val sec = _remainingSeconds.value ?: 0
        if (sec <= 0) return

        _isRunning.value = true

        timer?.cancel()

        timer = object : CountDownTimer(sec * 1000L, 1000) {
            override fun onTick(ms: Long) {
                val s = (ms / 1000).toInt()
                _remainingSeconds.value = s
                _progress.value = ((totalTime - s) * 100 / totalTime)
            }

            override fun onFinish() {
                when (_mode.value) {
                    Mode.FOCUS -> {
                        playSound(context)
                        NotificationHelper.showNotification(context, "Focus finished", "Time for a break!")
                        startBreak(context, 5 * 60)
                    }
                    Mode.BREAK -> {
                        playSound(context)
                        NotificationHelper.showNotification(context, "Break finished", "Ready for next session")
                        reset()
                    }
                    else -> {}
                }
            }
        }.start()
    }


    private fun playSound(context: Context) {
        val mp= MediaPlayer.create(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        mp.isLooping=false
        mp.start()
        viewModelScope.launch {
            delay(10000)
            if (mp.isPlaying){
                mp.stop()
            }
            mp.release()
        }
    }
    fun restart(context: Context) {
        when (_mode.value) {
            Mode.FOCUS -> startTimer(context, totalTime)
            Mode.BREAK -> startBreak(context, totalTime)
            else -> {}
        }
    }

}
