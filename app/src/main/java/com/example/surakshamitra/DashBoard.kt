package com.example.surakshamitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.compose.runtime.produceState
import com.example.surakshamitra.data.UserRegistrationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoard : AppCompatActivity() {

    private lateinit var logoutBtn: Button

    private lateinit var mAuth: FirebaseAuth

    private lateinit var tempprofile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
//        intent = Intent(this,AgencyInfo::class.java)
//        startActivity(intent)
//        intent = Intent(this,AboutAgency::class.java)
//        startActivity(intent)
        intent = Intent(this,MapNearbyAgencies::class.java)
        startActivity(intent)

        logoutBtn = findViewById(R.id.logoutbtn)
        tempprofile = findViewById(R.id.profileimgtest)
        mAuth = FirebaseAuth.getInstance()

        val agencyType = mapOf<String,Int>(
            "National Disaster Response Force (NDRF)" to R.drawable.ndrf,
            "State Police Departments" to R.drawable.maharastrapol,
            "Fire Services" to R.drawable.firebrigade,
            "Emergency Medical Services (EMS)" to R.drawable.ems,
            "Indian Coast Guard" to R.drawable.indiancoastguard,
            "Indian Search and Rescue" to R.drawable.searchandrescue

        )

        val database = FirebaseDatabase.getInstance().reference
        val userReference = database.child("Agencies").child("ketan")

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Data retrieved successfully
                val userData = snapshot.getValue(UserRegistrationData::class.java)

                if (userData != null) {
                    agencyType[userData.agencyType]?.let { tempprofile.setImageResource(it) }
                }

                // Use the userData object to populate your UI elements
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        logoutBtn.setOnClickListener {
            mAuth.signOut()

            // Redirect to the login screen or any other screen after logout
            val intent = Intent(applicationContext, LoginScreen::class.java)
            startActivity(intent)
            finish() //

        }

    }
}