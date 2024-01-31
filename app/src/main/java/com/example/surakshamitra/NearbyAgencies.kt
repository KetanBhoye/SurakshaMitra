package com.example.surakshamitra

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NearbyAgencies : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_agencies)

        val alertButton: Button = findViewById(R.id.alertButton)

        alertButton.setOnClickListener {
            // Handle button click event here
            // You can add any animation or additional actions here
            startScaleAnimation(alertButton)
        }

        val recyclerView: RecyclerView = findViewById(R.id.nearbyagencyrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = getDataList()
        val adapter = AgencylistAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun startScaleAnimation(view: View) {
        val scaleAnimation = ScaleAnimation(
            1f, 1.1f, // Start and end scale values
            1f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point of Y scaling
        )
        scaleAnimation.duration = 200 // Animation duration in milliseconds
        scaleAnimation.repeatCount = 2 // Number of times to repeat the animation
        scaleAnimation.repeatMode = Animation.REVERSE // Reverse the animation after it completes

        view.startAnimation(scaleAnimation)
    }

    private fun getDataList(): List<AgencyListDataModel> {

        return listOf(
            AgencyListDataModel(R.drawable.age1, "Text 1", R.drawable.statuson, R.drawable.alerticon),
            AgencyListDataModel(R.drawable.age3, "Text 1", R.drawable.statuson, R.drawable.alerticon),
            AgencyListDataModel(R.drawable.age2, "Text 1", R.drawable.statusoff, R.drawable.alerticon),
            AgencyListDataModel(R.drawable.age1, "Text 1", R.drawable.statuson, R.drawable.alerticon),
            AgencyListDataModel(R.drawable.age3, "Text 1", R.drawable.statusoff, R.drawable.alerticon),
            AgencyListDataModel(R.drawable.age2, "Text 1", R.drawable.statuson, R.drawable.alerticon),
            // Add more items as needed
        )
    }
}