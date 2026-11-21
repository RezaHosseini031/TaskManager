package com.rezahosseini.taskmanager.view.notification
import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rezahosseini.taskmanager.R

object TimerNotification {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TimerService.CHANNEL_ID,
                "Focus Timer",
                NotificationManager.IMPORTANCE_LOW
            )

            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    fun build(context: Context, seconds: Int): Notification {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        return NotificationCompat.Builder(
            context,
            TimerService.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.baseline_clear_24)
            .setContentTitle("Focus Timer")
            .setContentText(
                "%02d:%02d remaining".format(minutes, remainingSeconds)
            )
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showFinished(context: Context) {
        val manager = NotificationManagerCompat.from(context)

        manager.notify(
            1002,
            NotificationCompat.Builder(
                context,
                TimerService.CHANNEL_ID
            )
                .setSmallIcon(R.drawable.baseline_clear_24)
                .setContentTitle("Timer finished")
                .setContentText("Your focus session has ended.")
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build()
        )
    }
}