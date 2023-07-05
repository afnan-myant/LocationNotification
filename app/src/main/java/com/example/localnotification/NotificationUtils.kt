import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import com.example.localnotification.NotificationReceiver
import java.util.*
import android.content.SharedPreferences

object NotificationUtils {

    private const val TAG = "NotificationUtils"
    private var mContext: Context? = null

    private const val INTERVAL_NOTIFICATION_ID = 111
    private const val SPECIFIC_TIME_NOTIFICATION_ID = 1
    private const val REPEAT_INTERVAL_NOTIFICATION_ID = 2
    private const val IMMEDIATE_NOTIFICATION_ID = 3
    private const val DAILY_REPEAT_NOTIFICATION_ID = 4
    private const val WEEK_DAY_REPEAT_NOTIFICATION_ID = 5

    private val dictionary: Map<String, Int> = mapOf(
        "INTERVAL_NOTIFICATION_ID" to 1,
        "INTERVAL_NOTIFICATION_ID" to 111,
        "SPECIFIC_TIME_NOTIFICATION_ID" to 1,
        "REPEAT_INTERVAL_NOTIFICATION_ID" to 2,
        "IMMEDIATE_NOTIFICATION_ID" to 3,
        "DAILY_REPEAT_NOTIFICATION_ID" to 4,
        "WEEK_DAY_REPEAT_NOTIFICATION_ID" to 5
    )

    fun scheduleIntervalNotification(context: Context, title: String, message: String,  timeInterval: Int  ){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On scheduleNotification 1")
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, timeInterval)
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)
        intent.putExtra(NotificationReceiver.EXTRA_NOTIFICATIONID, INTERVAL_NOTIFICATION_ID)
        intent.putExtra(NotificationReceiver.STRING_TITLE, "INTERVAL_NOTIFICATION_ID")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            INTERVAL_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("INTERVAL_NOTIFICATION_ID", INTERVAL_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelScheduledNotification(context: Context, title: String, Identifier: Int){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "cancel notification " + "" + Identifier)
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Identifier,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val sharedPref = context?.getSharedPreferences("Local_Not", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        editor?.remove(title)
        editor?.apply()
        Log.d("removed prefernceeessssss", "" + title)

        alarmManager.cancel(pendingIntent)

        Log.d("cancelled alarm manager", "" + title)
    }

    fun cancelAllScheduledNotification(context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val sharedPref = context?.getSharedPreferences("Local_Not", Context.MODE_PRIVATE)
        val allValues: Map<String, *> = sharedPref?.all as Map<String, *>
        val keyList: List<String> = allValues.keys.toList()
        val editor = sharedPref?.edit()
        val intent = Intent(context, NotificationReceiver::class.java)

        for (key in keyList) {
            Log.d("remove000dKey", "Key: $key, Value: ${sharedPref.all[key]}")

            editor?.remove(key)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                sharedPref.all[key] as Int,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Log.d("remove111dKey", "Key: $key, Value: ${sharedPref.all[key]}")
        }
        editor?.apply()
        Log.d("remov2222edKey", "changes applied")

    }

    fun getAllNotificationIdentifiers(context: Context): List<String> {

        val sharedPref = context?.getSharedPreferences("Local_Not", Context.MODE_PRIVATE)
        val allValues: Map<String, *> = sharedPref?.all as Map<String, *>
        val keyList: List<String> = allValues.keys.toList()

        return keyList

    }

    fun isNotificationScheduled(context: Context, title: String): Boolean {

        val sharedPref = context?.getSharedPreferences("Local_Not", Context.MODE_PRIVATE)
        val allValues: Map<String, *> = sharedPref?.all as Map<String, *>
        val keyList: List<String> = allValues.keys.toList()

        val isScheduled = keyList.contains(title)

        if (isScheduled) {
            return true
        } else {
            return false
        }

    }

    fun scheduleSpecificTimeNotification(context: Context, title: String, message: String, hour: Int, minute: Int  ){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On SpecificscheduleNotification 1")
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            SPECIFIC_TIME_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("SPECIFIC_TIME_NOTIFICATION_ID", SPECIFIC_TIME_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun scheduleIntervalRepeatNotification(context: Context, title: String, message: String, intervalTime: Long  ){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On schedule Interval Repeat Notification 1")
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REPEAT_INTERVAL_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("REPEAT_INTERVAL_NOTIFICATION_ID", REPEAT_INTERVAL_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis ,
            intervalTime,
            pendingIntent
        )
    }

    fun scheduleDailyRepeatNotification(context: Context, title: String, message: String, hour: Int, minute: Int ){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On schedule Daily Repeat Notification 1")
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REPEAT_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("DAILY_REPEAT_NOTIFICATION_ID", DAILY_REPEAT_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis ,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun scheduleWeekDayRepeatNotification(context: Context, title: String, message: String, hour: Int, minute: Int ){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On schedule Week day Repeat Notification")
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);  //here pass week number
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        val now = Calendar.getInstance()
        now[Calendar.SECOND] = 0
        now[Calendar.MILLISECOND] = 0

        if (calendar.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
            calendar.add(Calendar.DATE, 7);
        }
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)

        Log.d(TAG , "Calendar time "+calendar.timeInMillis)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            WEEK_DAY_REPEAT_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("WEEK_DAY_REPEAT_NOTIFICATION_ID", WEEK_DAY_REPEAT_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis ,
            7 * AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


    fun scheduleImmediateNotification(context: Context, title: String, message: String){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(TAG , "On schedule Immediate Notification 1")
        // Create an intent for the notification
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title)
        intent.putExtra(NotificationReceiver.EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            IMMEDIATE_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val editor: SharedPreferences.Editor = context.getSharedPreferences("Local_Not", MODE_PRIVATE)?.edit()!!
        editor?.putInt("IMMEDIATE_NOTIFICATION_ID", IMMEDIATE_NOTIFICATION_ID)
        editor?.apply()

        // Schedule the notification using AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis,
            pendingIntent
        )
    }


    fun getAllNotificationList(context: Context) {
        val intent = Intent(context.applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 300, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        Log.i("All notification", "getAllNotificationList")
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        if (pendingIntent != null)
//            alarmManager.cancel(pendingIntent);

        val sharedPref = context?.getSharedPreferences("Local_Not", Context.MODE_PRIVATE)
        val myValue = sharedPref?.getInt("INTERVAL_NOTIFICATION_ID", -1)

        Log.d("hi there looking for shred", "" + myValue)


    }


}
