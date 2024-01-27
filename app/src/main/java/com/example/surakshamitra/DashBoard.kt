package com.example.surakshamitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class DashBoard : AppCompatActivity() {

    private lateinit var logoutBtn: Button

    private lateinit var mAuth: FirebaseAuth
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
        mAuth = FirebaseAuth.getInstance()
        logoutBtn.setOnClickListener {
            mAuth.signOut()

            // Redirect to the login screen or any other screen after logout
            val intent = Intent(applicationContext, LoginScreen::class.java)
            startActivity(intent)
            finish() //

        }

    }
}