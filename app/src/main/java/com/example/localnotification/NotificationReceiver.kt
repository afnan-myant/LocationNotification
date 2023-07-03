package com.example.localnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationReceiver : BroadcastReceiver() {

    private  val CHANNEL_ID = "first-id"
    private  val CHANNEL_NAME = "My Channel"
    private  val CHANNEL_DESCRIPTION = "My Channel Description"
    private  val NOTIFICATION_ID = 100

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("On Receive" , "On Receive 1")
        val title = intent?.getStringExtra(EXTRA_TITLE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)

        if (context != null && title != null && message != null) {
//            showNotification(context, title, message)
            createNotification(context,title,message,message)
        }
    }



    fun createNotification(context: Context, title: String, message: String,
                           bigText: String) {



        val notificationManager = NotificationManagerCompat.from(context)


        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(message)
            setAutoCancel(true)
            setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val pendingIntent =
                    PendingIntent.getActivity(context, 0, intent,
                        (PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
                setContentIntent(pendingIntent)
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH,
                )
                notificationManager.createNotificationChannel(channel)
            }
            else {
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                setContentIntent(pendingIntent)
            }
        }

        val notificationID = (0..Int.MAX_VALUE).random()


        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}
