package com.example.surakshamitra

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NearbyAgencies : AppCompatActivity() {
    private lateinit var adapter: AgencylistAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var dataList: MutableList<AgencyListDataModel>

    private val agencyTypeMap = mapOf(
        "National Disaster Response Force (NDRF)" to R.drawable.ndrf,
        "State Police Departments" to R.drawable.maharastrapol,
        "Fire Services" to R.drawable.firebrigade,
        "Emergency Medical Services (EMS)" to R.drawable.ems,
        "Indian Coast Guard" to R.drawable.indiancoastguard,
        "Indian Search and Rescue" to R.drawable.searchandrescue
    )
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_agencies)

        val alertButton: Button = findViewById(R.id.alertButton)

        alertButton.setOnClickListener {
            startScaleAnimation(alertButton)
        }

        dataList = mutableListOf()


        recyclerView = findViewById(R.id.nearbyagencyrecyclerView)
        adapter = AgencylistAdapter(mutableListOf())
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        recyclerView.adapter = adapter


        getDataListFromFirebase()


    }
    fun getItemViewType(position: Int): Int {
        // Return the layout resource ID based on the position or any other logic
        return if (position % 3 == 0) {
            R.layout.agencylistrecycler
        } else {
            R.layout.agencylistrecycler
        }
    }


    private fun startScaleAnimation(view: View) {
        val scaleAnimation = ScaleAnimation(
            1f, 1.1f, // Start and end scale values
            1f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point of Y scaling
        )
        scaleAnimation.duration = 300 // Animation duration in milliseconds
        scaleAnimation.repeatCount = 3 // Number of times to repeat the animation
//        scaleAnimation.repeatMode = Animation.REVERSE // Reverse the animation after it completes

        view.startAnimation(scaleAnimation)
    }

    private fun getDataListFromFirebase() {
        val agenciesReference = FirebaseDatabase.getInstance().getReference("Agencies")

        agenciesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear() // Clear existing data before adding new data

                for (agencySnapshot in dataSnapshot.children) {
                    val agencyName = agencySnapshot.child("agencyName").getValue(String::class.java)
                    val agencyType = agencySnapshot.child("agencyType").getValue(String::class.java)
                    val agencyStatus = agencySnapshot.child("activeStaus").getValue(Boolean::class.java)

//                    if (agencyStatus != null) {
//                        Log.d("HEL", agencyStatus.toString())
//                    }
                    var agestatus = agencyStatus.toString()
                    if (agencyName != null && agencyType != null && agestatus!=null) {
                        // Use some logic to determine the imageResource, statusIconResource, and alertIconResource
                        val imageResource = agencyTypeMap[agencyType]?: R.drawable.age1
                        var statusIconResource = R.drawable.statusoff

                        if(agestatus == "true"){
                            statusIconResource = R.drawable.statuson
                        }

                        val alertIconResource = R.drawable.alerticon

                        val agencyListDataModel = AgencyListDataModel(imageResource, agencyName, statusIconResource, alertIconResource)
                        dataList.add(agencyListDataModel)
                    }
                }

                // Update the adapter with the new data
                adapter.setDataList(dataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("getDataListFromFirebase", "Error: ${databaseError.message}")
            }
        })
    }
}