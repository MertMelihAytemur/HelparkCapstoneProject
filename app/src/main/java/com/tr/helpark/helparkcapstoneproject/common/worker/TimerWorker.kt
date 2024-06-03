package com.tr.helpark.helparkcapstoneproject.common.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tr.helpark.helparkcapstoneproject.R

class TimerWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val duration = inputData.getLong("duration", 10) // Default 10 minutes in milliseconds

        showNotification("Timer started", "Your timer will end in $duration milliseconds")

        try {
            Thread.sleep(duration)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return Result.failure()
        }

        showNotification("Timer ended", "Your timer has finished.")
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("TIMER_CHANNEL", "Timer", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "TIMER_CHANNEL")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.icon_close)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
