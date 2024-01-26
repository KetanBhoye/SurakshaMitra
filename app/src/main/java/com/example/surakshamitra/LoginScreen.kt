package com.example.surakshamitra

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar

class LoginScreen : AppCompatActivity() {
    private lateinit var loginAnimLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        val actionBar = supportActionBar
        actionBar?.hide()

        loginAnimLayout = findViewById(R.id.loginanim) // Replace R.id.loginanim with the actual ID

        // Add this code to initiate the popup animation
        val translateAnimation = TranslateAnimation(0f, 0f, loginAnimLayout.height.toFloat(), 0f)
        translateAnimation.duration = 500 // Set the duration of the animation in milliseconds
        loginAnimLayout.startAnimation(translateAnimation)

    }
}