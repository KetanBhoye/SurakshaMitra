package com.example.surakshamitra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var donthaveac: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        mAuth = FirebaseAuth.getInstance()

        // Check if the user is already logged in
        if (mAuth.currentUser != null) {
            // User is already logged in, redirect to the dashboard
            startDashboardActivity()
            return
        }

        // Initialization
        username = findViewById(R.id.loginUserName)
        password = findViewById(R.id.loginPassword)
        loginBtn = findViewById(R.id.loginButton2)
        donthaveac = findViewById(R.id.donthaveac)

        donthaveac.setOnClickListener {
            intent = Intent(applicationContext, RegisterUsers::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
             authenticateUser(username.text.toString(), password.text.toString())
        }
    }

    private fun startDashboardActivity() {
        val intent = Intent(applicationContext, DashBoard::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from coming back to the login screen
    }

     private fun authenticateUser(email: String, password: String) {
         mAuth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(this) { task ->
                 if (task.isSuccessful) {
                     startDashboardActivity()
                 } else {

                     Toast.makeText(applicationContext,"Wrong Credentials Please, Try again!", Toast.LENGTH_SHORT).show()
                     // Handle authentication failure
                 }
             }
     }
}
