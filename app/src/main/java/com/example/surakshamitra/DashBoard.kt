// DashBoard.kt

package com.example.surakshamitra

import HomeFragment
import PanicFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.surakshamitra.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBoard : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val panicFragment = PanicFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set listener for bottom navigation items
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.nav_panic -> {
                    loadFragment(panicFragment)
                    true
                }
                R.id.nav_profile -> {

                    loadFragment(profileFragment)
                    true
                }
                else -> false
            }
        }

        // Load the home fragment by default
        loadFragment(homeFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
