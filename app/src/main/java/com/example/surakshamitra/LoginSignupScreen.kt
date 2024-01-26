package com.example.surakshamitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginSignupScreen : AppCompatActivity() {
    private lateinit var createAccount: Button
    private lateinit var loginUser: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar?.hide();
        }
        setContentView(R.layout.activity_login_signup_screen)

        createAccount = findViewById(R.id.createloginBtn)
        loginUser = findViewById(R.id.loginBtn)

        createAccount.setOnClickListener {
            intent = Intent(this, RegisterUsers::class.java)
            startActivity(intent)
        }




    }
}