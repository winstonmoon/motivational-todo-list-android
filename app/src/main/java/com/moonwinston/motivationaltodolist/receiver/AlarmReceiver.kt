package com.moonwinston.motivationaltodolist.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.moonwinston.motivationaltodolist.ui.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            //Todo
            val task = intent.extras?.getString("task")?:""
            val taskDate = intent.extras?.getString("taskDate")?:""
            createNotificationChannel()
            deliverNotification(context, task, taskDate)
//        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Stand up notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AlarmManager Tests"
        notificationManager.createNotificationChannel(
            notificationChannel)
    }

    private fun deliverNotification(context: Context, task: String, taskDate: String) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Motivational Todo List")
                .setContentText("${taskDate}\n${task}")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}