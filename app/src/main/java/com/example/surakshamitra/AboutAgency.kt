package com.example.surakshamitra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.surakshamitra.data.UserRegistrationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AboutAgency : AppCompatActivity() {

    private lateinit var agencyProfileImg: ImageView
    private lateinit var agencyName: TextView
    private lateinit var aboutagencyType: TextView
    private lateinit var agencyStrength: TextView
    private lateinit var agencyResponseTm: TextView
    private lateinit var agencyContactDetails: TextView
    private lateinit var agencyLocation: TextView
    private lateinit var agencyState: TextView
    private lateinit var email: String
    private lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_agency)


        //Initialization
        agencyProfileImg = findViewById(R.id.aboutagencyImg)
        agencyName = findViewById(R.id.aboutagencyName)
        aboutagencyType = findViewById(R.id.aboutagencyType)
        agencyStrength = findViewById(R.id.aboutagencyCap)
        agencyResponseTm = findViewById(R.id.aboutagencyResponseTime)
        agencyContactDetails = findViewById(R.id.aboutagencyContact)
        agencyLocation = findViewById(R.id.aboutagencyLocation)
        agencyState = findViewById(R.id.aboutagencyState)
        backBtn = findViewById(R.id.aboutagencyBack)

        backBtn.setOnClickListener {

            finish()
        }

        getAgencyInfo()


    }

    fun getAgencyInfo(){
   //photo mapping

        val agencyType = mapOf<String,Int>(
            "National Disaster Response Force (NDRF)" to R.drawable.ndrf,
            "State Police Departments" to R.drawable.maharastrapol,
            "Fire Services" to R.drawable.firebrigade,
            "Emergency Medical Services (EMS)" to R.drawable.ems,
            "Indian Coast Guard" to R.drawable.indiancoastguard,
            "Indian Search and Rescue" to R.drawable.searchandrescue

        )



        //get user
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            email = currentUser.email.toString()
            Log.d("TAG", "Logged in user's email: $email")
            // Use the email for any actions or display
        } else {
            Log.d("TAG", "No user is currently logged in")
        }
        val index = email.indexOf('@')
        val usernameInp = email.substring(0,index)
        val database = FirebaseDatabase.getInstance().reference
        val userReference = database.child("Agencies").child(usernameInp)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Data retrieved successfully
                val userData = snapshot.getValue(UserRegistrationData::class.java)

                if (userData != null) {
                    agencyType[userData.agencyType]?.let { agencyProfileImg.setImageResource(it) }

                      agencyName.text = userData.agencyName
                    aboutagencyType.text = userData.agencyType
                    agencyStrength.text = userData.totalMembers
                    agencyContactDetails.text = userData.phoneNumber
                    agencyLocation.text = userData.address

                }

                // Use the userData object to populate your UI elements
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }
}