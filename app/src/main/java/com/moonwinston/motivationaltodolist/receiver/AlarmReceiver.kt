package com.moonwinston.motivationaltodolist.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.moonwinston.motivationaltodolist.R

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        val taskDate = intent.extras?.getString("taskDate")?:""
        val task = intent.extras?.getString("task")?:""
        val contentText = "$taskDate $task"
        createNotificationChannel()
        deliverNotification(context, contentText)
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Stand up notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(
            notificationChannel)
    }

    private fun deliverNotification(context: Context, contentText: String) {
//        val contentIntent = Intent(context, MainActivity::class.java)
//        val contentPendingIntent = PendingIntent.getActivity(
//            context,
//            NOTIFICATION_ID,
//            contentIntent,
//            PendingIntent.FLAG_IMMUTABLE
//        )

        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(contentText)
//                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}