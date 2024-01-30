//package com.example.surakshamitra
//// ShakeDetector.kt
//
//
//import android.content.Context
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import kotlin.math.sqrt
//
//class ShakeDetector(context: Context, private val listener: Listener) : SensorEventListener {
//
//    private val sensorManager: SensorManager =
//        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    private val accelerometer: Sensor? =
//        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//
//    private var lastUpdate: Long = 0
//    private var lastX = 0f
//    private var lastY = 0f
//    private var lastZ = 0f
//
//    interface Listener {
//        fun onShake()
//    }
//
//    init {
//        start()
//    }
//
//    fun start() {
//        accelerometer?.let {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
//        }
//    }
//
//    fun stop() {
//        sensorManager.unregisterListener(this)
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        // Not needed for this example
//    }
//
//    override fun onSensorChanged(event: SensorEvent) {
//        val currentTime = System.currentTimeMillis()
//        val timeDifference = currentTime - lastUpdate
//
//        if (timeDifference > SHAKE_THRESHOLD_TIME) {
//            val x = event.values[0]
//            val y = event.values[1]
//            val z = event.values[2]
//
//            val acceleration =
//                sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
//
//            if (acceleration > SHAKE_THRESHOLD) {
//                lastUpdate = currentTime
//                listener.onShake()
//            }
//
//            lastX = x
//            lastY = y
//            lastZ = z
//        }
//    }
//
//    companion object {
//        private const val SHAKE_THRESHOLD = 5f
//        private const val SHAKE_THRESHOLD_TIME = 500 // in milliseconds
//    }
//}
