package com.rezahosseini.taskmanager.view.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rezahosseini.taskmanager.R

class TimerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)
        playAlarm(context)
    }

    private fun showNotification(context: Context) {
        val channelId = "timer_finished_channel"

        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Timer Finished",
                NotificationManager.IMPORTANCE_HIGH
            )

            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_clear_24)
            .setContentTitle("Timer finished")
            .setContentText("Your focus session has ended.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(3001, notification)
    }

    private fun playAlarm(context: Context) {
        val ringtoneUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_ALARM
        ) ?: return

        val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
        ringtone?.play()
    }
}