package com.example.localnotification

import android.app.*
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.localnotification.databinding.ActivityMainBinding
import android.content.Context
import androidx.core.app.NotificationCompat
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private  val CHANNEL_ID = "skiin"
    private  val CHANNEL_NAME = "skiin"
    private  val CHANNEL_DESCRIPTION = "My Channel Description"
    private val RECORD_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        setupPermissions()

        NotificationUtils.scheduleIntervalNotification(this,"Interval notifition",  "This is Interval notifition",  10)

        NotificationUtils.scheduleSpecificTimeNotification(this,"Spcific time notifition",  "This is specific time notifition", 14, 19)
        NotificationUtils.scheduleIntervalRepeatNotification(this,"Repeat notifition",  "This is Repeat Interval notifition", 10 * 1000) // after each 10 seconds
        NotificationUtils.scheduleImmediateNotification(this,"Immediate notifition",  "This is immediate notifition")
        NotificationUtils.scheduleDailyRepeatNotification(this,"Daily Repeat notifition",  "This is Daily Interval notifition", 18, 9) // after each 10 seconds
        NotificationUtils.scheduleWeekDayRepeatNotification(this,"Daily Repeat notifition",  "This is Daily Interval notifition", 17, 54) // after each 10 seconds

//        NotificationUtils.getAllNotificationList(this)
    }




    private fun setupPermissions() {

        askPermissionForPostNotification()
        askPermissionForBatteryOptimization()

         val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.POST_NOTIFICATIONS)

        val permission2 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Test", "Permission to post notification denied")
        }
    }

    private fun askPermissionForPostNotification() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            RECORD_REQUEST_CODE)

    }

    private fun askPermissionForBatteryOptimization() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS),
            102)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}