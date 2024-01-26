package com.example.surakshamitra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar?.hide();
        }
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoginSignupScreen::class.java)
        startActivity(intent)

    }
}