package com.rezahosseini.taskmanager.view.notification

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationManagerCompat

class TimerService : Service() {

    companion object {
        const val ACTION_START = "timer.START"
        const val ACTION_PAUSE = "timer.PAUSE"
        const val ACTION_RESET = "timer.RESET"

        const val EXTRA_SECONDS = "extra.SECONDS"
        const val CHANNEL_ID = "timer_channel"
        const val NOTIFICATION_ID = 1001
    }

    private var timer: CountDownTimer? = null
    private var endAt: Long = 0L
    private var remainingSeconds = 0

    override fun onCreate() {
        super.onCreate()
        TimerNotification.createChannel(this)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {
            ACTION_START -> {
                val seconds = intent.getIntExtra(EXTRA_SECONDS, 0)
                if (seconds > 0) startTimer(seconds)
            }

            ACTION_PAUSE -> pauseTimer()

            ACTION_RESET -> {
                timer?.cancel()
                timer = null
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun startTimer(seconds: Int) {
        timer?.cancel()

        remainingSeconds = seconds
        endAt = SystemClock.elapsedRealtime() + seconds * 1000L

        startForeground(
            NOTIFICATION_ID,
            TimerNotification.build(this, seconds)
        )

        timer = object : CountDownTimer(seconds * 1000L, 1000L) {

            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = (
                        (endAt - SystemClock.elapsedRealtime() + 999) / 1000
                        ).toInt().coerceAtLeast(0)

                NotificationManagerCompat.from(this@TimerService).notify(
                    NOTIFICATION_ID,
                    TimerNotification.build(
                        this@TimerService,
                        remainingSeconds
                    )
                )
            }

            override fun onFinish() {
                remainingSeconds = 0
                timer = null

                TimerNotification.showFinished(this@TimerService)

                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }.start()
    }

    private fun pauseTimer() {
        if (timer == null) return

        remainingSeconds = (
                (endAt - SystemClock.elapsedRealtime() + 999) / 1000
                ).toInt().coerceAtLeast(0)

        timer?.cancel()
        timer = null

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        timer?.cancel()
        timer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}