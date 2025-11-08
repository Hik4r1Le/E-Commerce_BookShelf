package com.example.bookstore

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.navView)

        // Lấy NavController từ NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment_activity_main
            ) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_cart,
                R.id.navigation_orders,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}