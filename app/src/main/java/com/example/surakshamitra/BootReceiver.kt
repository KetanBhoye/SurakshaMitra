package com.example.surakshamitra

// BootReceiver.kt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
//            val serviceIntent = Intent(context, ShakeService::class.java)
//            context.startService(serviceIntent)
//        }
    }
}
