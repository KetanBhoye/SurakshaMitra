package com.example.surakshamitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar

class LoginSignupScreen : AppCompatActivity() {
    private lateinit var createAccount: Button
    private lateinit var loginUser: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup_screen)
        supportActionBar?.hide()


        createAccount = findViewById(R.id.createloginBtn)
        loginUser = findViewById(R.id.loginBtn)

        createAccount.setOnClickListener {
            intent = Intent(this, RegisterUsers::class.java)
            startActivity(intent)
        }

        loginUser.setOnClickListener {

//            intent = Intent(this, LoginScreen::class.java)
//            startActivity(intent)
            intent = Intent(this, AlertSent::class.java)
            startActivity(intent)
        }




    }
}