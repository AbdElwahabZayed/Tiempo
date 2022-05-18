package com.iti.tiempo.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.iti.tiempo.R
import com.iti.tiempo.utils.ALARM

const val NOTIFICATION_CHANNEL_ID = "example.permanence"

class ForegroundService : Service() {


    private lateinit var window: AlarmWindow


    override fun onCreate() {
        super.onCreate()
        // create the custom or default notification
        // based on the android version

        startMyOwnForeground()
        Log.i("TAG", "onCreate: ")
        // create an instance of Window class
        // and display the content on screen
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val rem = intent.extras!![ALARM] as String?
        window = AlarmWindow(this, rem.toString())
        window.open()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    // for android version >=O we need to create
    // custom notification stating
    private fun startMyOwnForeground() {
        val channelName = "Background Service"
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN)
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(channel)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle(applicationContext.resources.getString(R.string.weather_alarm))
            .setContentText(applicationContext.resources.getString(R.string.alarm)) // this is important, otherwise the notification will show the way
            // you want i.e. it will show some default notification
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(false)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

}