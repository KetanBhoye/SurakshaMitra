// DashBoard.kt

package com.example.surakshamitra

import HomeFragment
import PanicFragment
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.surakshamitra.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoard : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val panicFragment = PanicFragment()
    private val profileFragment = ProfileFragment()

    // Shake detector
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        // Shake detector initialization
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        shakeDetector = ShakeDetector { showYesNoDialog() }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set listener for bottom navigation items
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.nav_panic -> {
                    loadFragment(panicFragment)
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(profileFragment)
                    true
                }
                else -> false
            }
        }

        // Load the home fragment by default
        loadFragment(homeFragment)
    }

    private fun showYesNoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert Sending !!")
        builder.setMessage("Do you want to proceed?")
        builder.setPositiveButton("Yes") { dialog, which ->
            sendMessage()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Handle "No" button click
            dialog.dismiss()
        }
        builder.show()
    }

   fun sendMessage() {
        // Get reference to the "Agencies" node in Firebase Realtime Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Agencies")

        // Attach a ValueEventListener to retrieve data from the "Agencies" node
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through each child node under "Agencies"
                for (agencySnapshot in dataSnapshot.children) {
                    val phoneNumber = agencySnapshot.child("phoneNumber").value.toString()
                    sendSMS(phoneNumber, "Test")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Toast.makeText(this@DashBoard, "Failed to retrieve data from Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        val sentIntent = Intent("SMS_SENT")
        val deliveredIntent = Intent("SMS_DELIVERED")

        val sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_IMMUTABLE)
        val deliveredPI = PendingIntent.getBroadcast(this, 0, deliveredIntent, PendingIntent.FLAG_IMMUTABLE)

        // Check for message length and split it into parts if necessary
        val parts = smsManager.divideMessage(message)
        val messageCount = parts.size

        // Send each part of the message
        for (i in 0 until messageCount) {
            // Send the SMS using the default SMS manager
            smsManager.sendTextMessage(phoneNumber, null, parts[i], sentPI, deliveredPI)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        sensorManager.unregisterListener(shakeDetector)
        super.onPause()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

class ShakeDetector(private val onShakeListener: () -> Unit) : SensorEventListener {

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7F
        private const val SHAKE_SLOP_TIME_MS = 500
        private const val SHAKE_COUNT_RESET_TIME_MS = 3000
    }

    private var shakeTimestamp: Long = 0
    private var shakeCount = 0

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing here
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0
                }

                shakeTimestamp = now
                shakeCount++

                if (shakeCount == 2) {
                    onShakeListener.invoke()
                }
            }
        }
    }
}
