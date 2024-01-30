//// ShakeService.kt
//package com.example.surakshamitra
//
//import android.annotation.SuppressLint
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//
//class ShakeService : Service(), ShakeDetector.Listener {
//
//    private lateinit var shakeDetector: ShakeDetector
//    private val NOTIFICATION_ID = 12345 // Choose a unique ID for your notification
//    private val CHANNEL_ID = "ShakeServiceChannel"
//
//    @SuppressLint("ForegroundServiceType")
//    override fun onCreate() {
//        super.onCreate()
//        Log.d("ShakeService", "onCreate")
//        shakeDetector = ShakeDetector(this, this)
//        shakeDetector.start()
//
//        // Call startForeground here to make the service run in the foreground
////        startForeground(NOTIFICATION_ID, createNotification())
//        startForeground(NOTIFICATION_ID, createNotification())
//
//    }
//
//    override fun onShake() {
//        // Handle shake event here
//        // You can show a dialog, start an activity, or perform any other action
//        showShakeDialog()
//    }
//
//    private fun showShakeDialog() {
//        // Display a dialog or start an activity upon shake detection
//        // You can customize this based on your requirements
//        val dialogIntent = Intent(this, DashBoard::class.java)
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(dialogIntent)
//    }
//
//    private fun createNotification(): Notification {
//        // Create a Notification for your foreground service
//        // You can customize this based on your requirements
//        createNotificationChannel()
//
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Shake Service")
//            .setContentText("Service is running in the background")
//            .setSmallIcon(R.drawable.bellicon)
//            .setPriority(NotificationCompat.PRIORITY_LOW) // Adjust priority as needed
//
//        return notificationBuilder.build()
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Shake Service Channel",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        shakeDetector.stop()
//    }
//}
