package com.iti.tiempo.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Window
import androidx.core.app.NotificationCompat
import com.iti.tiempo.R
import com.iti.tiempo.base.utils.getLocaleStringResource
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.utils.ALARM
import com.iti.tiempo.utils.ALARMS
import com.iti.tiempo.utils.NOTIFICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("LaunchActivityFromNotification", "UnspecifiedImmutableFlag")

object NotificationHandler {
    private const val CHANNEL_ID = "transactions_reminder_channel"
    private const val CHANNEL_ID_VALUE = 500
    private const val TAG = "NotificationHandler"
    fun createWeatherNotification(
        context: Context,
        alarm: Alarm,
        msg: String,
        appSharedPreference: AppSharedPreference,
    ) {
        Log.e(TAG, "createWeatherNotification: ALarm = $alarm")
        Log.e(TAG, "createWeatherNotification: ALarm type= ${alarm.type}")
        when (alarm.type) {
            NOTIFICATION -> {
                val manager =
                    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
                createNotificationChannel(context, appSharedPreference)
                val notificationBuilder: NotificationCompat.Builder =
                    NotificationCompat.Builder(context, CHANNEL_ID)
                val notification = notificationBuilder.setOngoing(true)
                    .setContentTitle(context.getLocaleStringResource(appSharedPreference,
                        R.string.weather_alarm))
                    .setContentText(msg)
                    // this is important, otherwise the notification will show the way
                    // you want i.e. it will show some default notification
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_ALARM)
                    .setOngoing(false)
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.thunder))
                    .build()
                manager.notify(CHANNEL_ID_VALUE, notification)
            }
            ALARMS -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(context, ForegroundService::class.java)
                    intent.putExtra(ALARM, msg)
                    // start the service based on the android version
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // start the service based on the android version
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    val pendingIntent = PendingIntent.getService(context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT)
                    createNotificationChannel(context, appSharedPreference)
                    val builder: NotificationCompat.Builder = NotificationCompat.Builder(context,
                        CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(context.getLocaleStringResource(appSharedPreference,
                            R.string.weather_alarm))
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent) // For launching the MainActivity
                        .setAutoCancel(true) // Remove notification when tapped
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen

                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel(
        context: Context,
        appSharedPreference: AppSharedPreference,
    ) {
        val name = context.getLocaleStringResource(appSharedPreference, R.string.weather_alarm)
        val descriptionText =
            context.getLocaleStringResource(appSharedPreference, R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(CHANNEL_ID,
                name,
                importance)
        channel.description = descriptionText
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}